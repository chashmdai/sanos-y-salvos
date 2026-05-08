package cl.smid.auth.config;

import cl.smid.auth.entity.Role;
import cl.smid.auth.entity.User;
import cl.smid.auth.repository.RoleRepository;
import cl.smid.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        
        // 1. Inyectar Roles del contexto "Sanos y Salvos"
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            log.info("Creando rol ROLE_ADMIN...");
            return roleRepository.save(Role.builder()
                    .name("ROLE_ADMIN")
                    .description("Administrador de la plataforma Sanos y Salvos")
                    .build());
        });

        Role duenoRole = roleRepository.findByName("ROLE_DUEÑO").orElseGet(() -> {
            log.info("Creando rol ROLE_DUEÑO...");
            return roleRepository.save(Role.builder()
                    .name("ROLE_DUEÑO")
                    .description("Ciudadano que reporta mascota perdida o encontrada")
                    .build());
        });

        Role vetRole = roleRepository.findByName("ROLE_VETERINARIA").orElseGet(() -> {
            log.info("Creando rol ROLE_VETERINARIA...");
            return roleRepository.save(Role.builder()
                    .name("ROLE_VETERINARIA")
                    .description("Clínica veterinaria colaboradora")
                    .build());
        });

        @SuppressWarnings("unused")
        Role refugioRole = roleRepository.findByName("ROLE_REFUGIO").orElseGet(() -> {
            log.info("Creando rol ROLE_REFUGIO...");
            return roleRepository.save(Role.builder()
                    .name("ROLE_REFUGIO")
                    .description("Refugio de animales o Municipalidad")
                    .build());
        });

        // Hash común para la clave "1234"
        String commonPasswordHash = "$2b$12$tgTdJ6AgbfmkNlMdobRos.tWD8GmWKEu7GBEryTCzt6.PigTxAIVi";

        // 2. Inyectar Usuario Administrador
        String adminEmail = "admin@sanosysalvos.cl";
        if (!userRepository.existsByEmail(adminEmail)) {
            log.info("Inyectando usuario administrador...");
            userRepository.save(User.builder()
                    .email(adminEmail)
                    .password(commonPasswordHash)
                    .firstName("Admin")
                    .lastName("Sistema")
                    .enabled(true)
                    .roles(new HashSet<>(List.of(adminRole)))
                    .build());
            log.info("Usuario administrador inyectado. Email: {} | Pass: 1234", adminEmail);
        }

        // 3. Inyectar Usuario Ciudadano / Dueño
        String duenoEmail = "juan.perez@gmail.com";
        if (!userRepository.existsByEmail(duenoEmail)) {
            log.info("Inyectando usuario dueño...");
            userRepository.save(User.builder()
                    .email(duenoEmail)
                    .password(commonPasswordHash)
                    .firstName("Juan")
                    .lastName("Pérez")
                    .enabled(true)
                    .roles(new HashSet<>(List.of(duenoRole)))
                    .build());
            log.info("Usuario dueño inyectado. Email: {} | Pass: 1234", duenoEmail);
        }

        // 4. Inyectar Usuario Veterinaria
        String vetEmail = "contacto@vetdelsol.cl";
        if (!userRepository.existsByEmail(vetEmail)) {
            log.info("Inyectando usuario veterinaria...");
            userRepository.save(User.builder()
                    .email(vetEmail)
                    .password(commonPasswordHash)
                    .firstName("Clínica")
                    .lastName("Del Sol")
                    .enabled(true)
                    .roles(new HashSet<>(List.of(vetRole)))
                    .build());
            log.info("Usuario veterinaria inyectado. Email: {} | Pass: 1234", vetEmail);
        }
    }
}