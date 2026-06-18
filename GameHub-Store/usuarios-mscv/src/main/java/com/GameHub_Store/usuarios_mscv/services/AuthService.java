package com.GameHub_Store.usuarios_mscv.services;

import com.GameHub_Store.usuarios_mscv.dtos.AuthenRequest;
import com.GameHub_Store.usuarios_mscv.dtos.LoginRequest;
import com.GameHub_Store.usuarios_mscv.dtos.RegisterRequest;
import com.GameHub_Store.usuarios_mscv.models.Rol;
import com.GameHub_Store.usuarios_mscv.models.Usuarios;
import com.GameHub_Store.usuarios_mscv.repositories.RolRepository;
import com.GameHub_Store.usuarios_mscv.repositories.UsuarioRepository;
import com.GameHub_Store.usuarios_mscv.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Inyección por constructor: Desacoplamiento y cumplimiento estricto de buenas prácticas
    public AuthService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // REGISTRO DE CUENTA GAMER
    @Transactional
    public AuthenRequest register(RegisterRequest request) {
        // 1) Control de idempotencia: No permitir usernames duplicados en la tienda
        if (this.usuarioRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya está registrado en GameHub Store");
        }

        // 2) Regla de negocio: Si el usuario se registra vía web sin especificar rol, se le asigna ROLE_CLIENTE por defecto
        Set<String> nombresRoles = (request.getRoles() == null || request.getRoles().isEmpty())
                ? Set.of("ROLE_CLIENTE")
                : request.getRoles();

        // 3) Buscar cada rol en la BD. Si piden un rol inexistente, se rechaza semánticamente (400 Bad Request)
        Set<Rol> roles = nombresRoles.stream()
                .map(nombre -> this.rolRepository.findByRolName(nombre).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El Rol solicitado no existe en la plataforma: " + nombre)))
                .collect(Collectors.toCollection(HashSet::new));

        // 4) Mapear y persistir la entidad. La contraseña se procesa con hashing irreversible (BCrypt)
        Usuarios usuario = new Usuarios();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(this.passwordEncoder.encode(request.getPassword()));
        usuario.setRoles(roles);
        this.usuarioRepository.save(usuario);

        // 5) Retornar respuesta con el token JWT recién firmado (queda autenticado de inmediato)
        return construirRespuesta(usuario);
    }

    // INICIO DE SESIÓN (LOGIN)
    @Transactional(readOnly = true)
    public AuthenRequest login(LoginRequest request) {
        // Ofuscación de errores por seguridad: Se responde exactamente el mismo mensaje 410/401
        // para no revelar si lo que falló fue el username o la clave.
        Usuarios usuario = this.usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        // El método matches() toma la clave en texto plano, la procesa bajo el mismo algoritmo y compara los hashes
        if (!this.passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        return construirRespuesta(usuario);
    }

    // Encapsulación de respuesta DTO limpia (Excluye la contraseña por seguridad)
    private AuthenRequest construirRespuesta(Usuarios usuario) {
        String token = this.jwtService.generarToken(usuario);
        Set<String> roles = usuario.getRoles().stream()
                .map(Rol::getRolName)
                .collect(Collectors.toSet());

        return new AuthenRequest(token, "Bearer", usuario.getUsername(), roles);
    }
}
