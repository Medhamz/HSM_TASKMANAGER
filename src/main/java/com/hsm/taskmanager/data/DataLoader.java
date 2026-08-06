package com.hsm.taskmanager.data;

import com.hsm.taskmanager.entity.AppUser;
import com.hsm.taskmanager.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            AppUser admin = new AppUser("admin", passwordEncoder.encode("admin123"), "ADMIN");
            userRepository.save(admin);
        }
        if (userRepository.findByUsername("viewer").isEmpty()) {
            AppUser viewer = new AppUser("viewer", passwordEncoder.encode("viewer123"), "VIEWER");
            userRepository.save(viewer);
        }
    }
}