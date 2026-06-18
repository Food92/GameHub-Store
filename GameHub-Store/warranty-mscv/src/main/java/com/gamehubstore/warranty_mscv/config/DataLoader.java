package com.gamehubstore.warranty_mscv.config;

import com.gamehubstore.warranty_mscv.models.Warranty;
import com.gamehubstore.warranty_mscv.repositories.WarrantyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final WarrantyRepository warrantyRepository;

    // Inyección por constructor del repositorio
    public DataLoader(WarrantyRepository warrantyRepository) {
        this.warrantyRepository = warrantyRepository;
    }

    @Override
    public void run(String... args) {
        log.info("[DataLoader-Warranty] Iniciando siembra automática de tickets de soporte...");

        try {
            // Controlamos que la tabla no tenga datos para evitar duplicados al reiniciar
            if (this.warrantyRepository.count() == 0) {

                Warranty ticket1 = new Warranty();
                // Seteamos los campos reales de tu entidad Warranty:
                ticket1.setUserId(1L);
                ticket1.setProductId(100L);
                ticket1.setMotivo("Código de descarga digital inválido o ya canjeado.");
                ticket1.setEstado("PENDIENTE_REVISION");
                ticket1.setFechaSolicitud(LocalDateTime.now());
                ticket1.setResolucion("En espera de revisión técnica por parte del staff.");

                // Guardamos en la base de datos autónoma de garantías
                this.warrantyRepository.save(ticket1);

                log.info("[DataLoader-Warranty] Ticket de prueba ('Warranty') insertado con éxito.");
            } else {
                log.info("[DataLoader-Warranty] La base de datos ya contiene registros. Omitiendo siembra.");
            }
        } catch (Exception e) {
            log.error("[DataLoader-Warranty] Error crítico al sembrar datos de garantías: {}", e.getMessage());
        }
    }
}
