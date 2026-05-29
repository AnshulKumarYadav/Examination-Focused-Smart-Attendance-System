package com.app.SmartAttendanceSystem.Controllers;
import com.app.SmartAttendanceSystem.Models.Administrator;
import com.app.SmartAttendanceSystem.Models.Student;
import com.app.SmartAttendanceSystem.Repositories.AdministratorRepository;
import com.app.SmartAttendanceSystem.Repositories.StudentRepository;
import com.app.SmartAttendanceSystem.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

// Java 21 Records for clean incoming request mapping
record StudentLoginRequest(String enrollmentNo, String password) {}
record AdminLoginRequest(String username, String password) {}

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private JwtUtil jwtUtil;
    @Autowired private StudentRepository studentRepo;
    @Autowired private AdministratorRepository adminRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/student/login")
    public ResponseEntity<?> studentLogin(@RequestBody StudentLoginRequest request) {
        Optional<Student> student = studentRepo.findByEnrollmentNo(request.enrollmentNo());

        if (student.isPresent() && passwordEncoder.matches(request.password(), student.get().getPassword())) {
            String token = jwtUtil.generateToken(student.get().getEnrollmentNo(), "STUDENT");
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", "STUDENT",
                    "name", student.get().getName()
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid enrollment number or password");
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody AdminLoginRequest request) {
        Optional<Administrator> admin = adminRepo.findByUsername(request.username());

        if (admin.isPresent() && passwordEncoder.matches(request.password(), admin.get().getPassword())) {
            String token = jwtUtil.generateToken(admin.get().getUsername(), admin.get().getRole());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", admin.get().getRole(),
                    "name", admin.get().getName()
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
}
