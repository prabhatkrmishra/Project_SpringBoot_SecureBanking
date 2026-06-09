package com.security.bank.config;

import com.security.bank.entity.Role;
import com.security.bank.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initializes default application roles (ROLE_ADMIN, ROLE_CUSTOMER)
 * if they are not already present in the database.
 * Runs at application startup via {@link CommandLineRunner}.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Checks for the existence of the required roles and creates them
     * if missing. This guarantees the security configuration can
     * assign proper roles after startup.
     *
     * @param args command line arguments (unused)
     */
    @Override
    public void run(String... args) {
        // Create ROLE_ADMIN if not present
        if (roleRepository.findByRoleName("ROLE_ADMIN").isEmpty()) {
            Role role = new Role();
            role.setRoleName("ROLE_ADMIN");
            roleRepository.save(role);
        }

        // Create ROLE_CUSTOMER if not present
        if (roleRepository.findByRoleName("ROLE_CUSTOMER").isEmpty()) {
            Role role = new Role();
            role.setRoleName("ROLE_CUSTOMER");
            roleRepository.save(role);
        }
    }
}