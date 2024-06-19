package org.example.velogclone.config;

import org.example.velogclone.domain.Role;
import org.example.velogclone.domain.RoleName;
import org.example.velogclone.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Datainitializer {
    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (!roleRepository.findByRoleName(RoleName.ROLE_ADMIN).isPresent()) {
                Role admin = new Role();
                admin.setRoleName(RoleName.ROLE_ADMIN);
                roleRepository.save(admin);
            }

            if (!roleRepository.findByRoleName(RoleName.ROLE_USER).isPresent()) {
                Role user = new Role();
                user.setRoleName(RoleName.ROLE_USER);
                roleRepository.save(user);
            }
        };
    }
}
