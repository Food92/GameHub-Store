package com.gamehubstore.user_mscv.config;

import com.gamehubstore.user_mscv.models.User;
import com.gamehubstore.user_mscv.repositories.UserRepositorio; // Reemplaza por el nombre real de tu repositorio
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private UserRepositorio userRepositorio;

    public DataLoader(UserRepositorio userRepositorio) {
        this.userRepositorio = userRepositorio;
    }

    @Override
    public void run(String... args) {
        log.info("[DataLoader-User] Iniciando siembra automática de perfiles de clientes para GameHub Store...");

        try {
            // El campo "correo" o "rut" nos servirá para verificar si ya existen en la base de datos de perfiles
            crearPerfilSiNoExiste("11.111.111-1", "Administrador Principal", "admin@gamehub.cl", "+56911111111");
            crearPerfilSiNoExiste("22.222.222-2", "Juan Vendedor Pro", "vendedor1@gamehub.cl", "+56922222222");
            crearPerfilSiNoExiste("33.333.333-3", "Gamer Cliente Uno", "cliente1@gmail.com", "+56933333333");

            log.info("[DataLoader-User] Perfiles de comunidad Gamer inicializados exitosamente.");
        } catch (Exception e) {
            log.error("[DataLoader-User] Error al inicializar perfiles: {}", e.getMessage());
        }
    }

    private void crearPerfilSiNoExiste(String rut, String nombre, String correo, String telefono) {
        // Evitamos duplicidad usando el correo único
        if (this.userRepositorio.existsByCorreo(correo)) {
            return;
        }

        // Construcción de tu entidad real User
        User userPerfil = new User();
        userPerfil.setRut(rut);
        userPerfil.setNombreCompleto(nombre);
        userPerfil.setApellidoCompleto("GHub"); // Rellenamos apellido por validación @NotBlank
        userPerfil.setCorreo(correo);
        userPerfil.setTelefono(telefono);
        userPerfil.setEstado(true);
        // Tu atributo "audit" se inicializa solo al hacer new Audit() en tu entidad

        this.userRepositorio.save(userPerfil);
        log.info("[DataLoader-User] Perfil creado en BD -> [RUN: {}, Correo: {}]", rut, correo);
    }
}