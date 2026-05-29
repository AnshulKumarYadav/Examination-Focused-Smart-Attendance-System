package com.app.SmartAttendanceSystem.Security;

import com.app.SmartAttendanceSystem.Models.Administrator;
import com.app.SmartAttendanceSystem.Models.Student;
import com.app.SmartAttendanceSystem.Repositories.AdministratorRepository;
import com.app.SmartAttendanceSystem.Repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdministratorRepository adminRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        Optional<Administrator> admin = adminRepo.findByUsername(identifier);
        if (admin.isPresent()) {
            String cleanRole = admin.get().getRole().toUpperCase().replace("ROLE_", "");
            return new User(
                    admin.get().getUsername(),
                    admin.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + cleanRole))
            );
        }

        Optional<Student> student = studentRepo.findByEnrollmentNo(identifier);
        if (student.isPresent()) {
            return new User(
                    student.get().getEnrollmentNo(),
                    student.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT"))
            );
        }

        throw new UsernameNotFoundException("User not found with identifier: " + identifier);
    }
}