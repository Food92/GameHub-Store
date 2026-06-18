package com.GameHub_Store.usuarios_mscv.config;

import com.GameHub_Store.usuarios_mscv.models.Rol;
import com.GameHub_Store.usuarios_mscv.models.Usuarios;
import com.GameHub_Store.usuarios_mscv.repositories.RolRepository;
import com.GameHub_Store.usuarios_mscv.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Inyección limpia por constructor
    public DataLoader(RolRepository rolRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        log.info("[DataLoader] Iniciando siembra automática de roles y credenciales para GameHub Store...");

        try {
            // 1. Obtener o crear los roles reales de tu tienda de videojuegos
            Rol admin = obtenerOCrearRol("ROLE_ADMIN");
            Rol vendedor = obtenerOCrearRol("ROLE_VENDEDOR");
            Rol cliente = obtenerOCrearRol("ROLE_CLIENTE");

            // 2. Sembrar usuarios de prueba listos para usar en Postman
            crearUsuarioSiNoExiste("admin", "admin123", Set.of(admin));
            crearUsuarioSiNoExiste("vendedor1", "vendedor123", Set.of(vendedor));
            crearUsuarioSiNoExiste("cliente1", "cliente123", Set.of(cliente));

            log.info("[DataLoader] Ecosistema de autenticación inicializado exitosamente.");
        } catch (Exception e) {
            log.error("[DataLoader] Error crítico al inicializar la base de datos de usuarios: {}", e.getMessage());
        }
    }

    private Rol obtenerOCrearRol(String nombre) {
        // Se adapta a tu método real 'findByRolName' y tu constructor personalizado de Rol
        return this.rolRepository.findByRolName(nombre)
                .orElseGet(() -> {
                    Rol nuevoRol = this.rolRepository.save(new Rol(nombre));
                    log.info("[DataLoader] Rol de seguridad creado: {}", nombre);
                    return nuevoRol;
                });
    }

    private void crearUsuarioSiNoExiste(String username, String passwordPlano, Set<Rol> roles) {
        // Evita duplicar registros si la base de datos ya contiene la información
        if (this.usuarioRepository.existsByUsername(username)) {
            return;
        }

        // Se adapta a tu clase entidad real 'Usuarios'
        Usuarios usuario = new Usuarios();
        usuario.setUsername(username);
        // Cifrado obligatorio con BCrypt antes de impactar la base de datos
        usuario.setPassword(this.passwordEncoder.encode(passwordPlano));
        usuario.setRoles(roles);

        this.usuarioRepository.save(usuario);
        log.info("[DataLoader] Usuario de demostración registrado -> [Username: {}]", username);
    }
}
