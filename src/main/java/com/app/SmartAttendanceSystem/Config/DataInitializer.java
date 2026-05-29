package com.app.SmartAttendanceSystem.Config;

import com.app.SmartAttendanceSystem.Models.Administrator;
import com.app.SmartAttendanceSystem.Repositories.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdministratorRepository adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if the system already has a Super Admin to prevent duplicates on restart
        if (adminRepo.findByUsername("superadmin").isEmpty()) {

            Administrator superAdmin = new Administrator();
            superAdmin.setName("System Super Admin");
            superAdmin.setUsername("superadmin");
            // Encrypt the default password
            superAdmin.setPassword(passwordEncoder.encode("Admin@123"));
            superAdmin.setRole("SUPERADMIN");

            adminRepo.save(superAdmin);

            System.out.println("=================================================");
            System.out.println("INTERNAL SUPER ADMIN CREATED:");
            System.out.println("Username: superadmin");
            System.out.println("Password: Admin@123");
            System.out.println("Please change this password in production.");
            System.out.println("=================================================");
        }
    }
}
