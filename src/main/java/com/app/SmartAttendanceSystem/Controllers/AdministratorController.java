package com.app.SmartAttendanceSystem.Controllers;

import com.app.SmartAttendanceSystem.Models.Administrator;
import com.app.SmartAttendanceSystem.Models.ExamSession;
import com.app.SmartAttendanceSystem.Models.Student;
import com.app.SmartAttendanceSystem.Repositories.AdministratorRepository;
import com.app.SmartAttendanceSystem.Repositories.ExamSessionRepository;
import com.app.SmartAttendanceSystem.Services.ExamService;
import com.app.SmartAttendanceSystem.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdministratorController {

    @Autowired private UserService userService;
    @Autowired private ExamService examService;
    @Autowired private AdministratorRepository adminRepo;
    @Autowired private ExamSessionRepository sessionRepo;

    @PostMapping("/users/student")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.provisionStudent(student));
    }

    @PostMapping("/users/invigilator")
    public ResponseEntity<Administrator> createInvigilator(@RequestBody Administrator invigilator) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.provisionInvigilator(invigilator));
    }

    // CREATE SESSION (Assigns to the logged-in user)
    @PostMapping("/sessions")
    public ResponseEntity<?> createSession(@RequestBody ExamSession session, Principal principal) {
        Optional<Administrator> currentAdmin = adminRepo.findByUsername(principal.getName());
        if (currentAdmin.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");

        session.setAdmin(currentAdmin.get());
        ExamSession createdSession = examService.createExamSession(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
    }

    // FETCH SESSIONS (Categorizes into Active and Past, checks Role)
    @GetMapping("/sessions")
    public ResponseEntity<?> getSessions(Principal principal) {
        Optional<Administrator> currentAdmin = adminRepo.findByUsername(principal.getName());
        if (currentAdmin.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<ExamSession> sessions;
        // Admins see all sessions, Invigilators see only their own
        if (currentAdmin.get().getRole().equals("SUPERADMIN") || currentAdmin.get().getRole().equals("ADMIN")) {
            sessions = sessionRepo.findAllByOrderByExamDateDescExamTimeDesc();
        } else {
            sessions = sessionRepo.findByAdminOrderByExamDateDescExamTimeDesc(currentAdmin.get());
        }

        LocalDateTime now = LocalDateTime.now();
        List<ExamSession> active = sessions.stream().filter(s -> s.getExpiryTime().isAfter(now)).toList();
        List<ExamSession> past = sessions.stream().filter(s -> s.getExpiryTime().isBefore(now) || s.getExpiryTime().isEqual(now)).toList();

        return ResponseEntity.ok(Map.of("active", active, "past", past));
    }
}