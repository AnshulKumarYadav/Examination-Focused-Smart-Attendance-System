package com.app.SmartAttendanceSystem.Controllers;

import com.app.SmartAttendanceSystem.Models.Student;
import com.app.SmartAttendanceSystem.Repositories.AttendanceRepository;
import com.app.SmartAttendanceSystem.Repositories.StudentRepository;
import com.app.SmartAttendanceSystem.Services.AttendanceService;
import com.app.SmartAttendanceSystem.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

record PasswordUpdateRequest(String enrollmentNo, String newPassword) {}
record AttendanceRequest(String enrollmentNo, String qrCode, String tempCode) {}

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired private UserService userService;
    @Autowired private AttendanceService attendanceService;
    @Autowired private AttendanceRepository attendanceRepo;
    @Autowired private StudentRepository studentRepo;

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdateRequest request) {
        try {
            userService.updateStudentPassword(request.enrollmentNo(), request.newPassword());
            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/attendance")
    public ResponseEntity<?> markAttendance(@RequestBody AttendanceRequest request) {
        try {
            String result = attendanceService.verifyAndMarkAttendance(
                    request.enrollmentNo(),
                    request.qrCode(),
                    request.tempCode()
            );

            if (result.contains("successfully")) {
                return ResponseEntity.ok(Map.of("message", result));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", result));
            }

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/attendance/{enrollmentNo}")
    public ResponseEntity<?> getMyAttendance(@PathVariable String enrollmentNo) {
        Optional<Student> student = studentRepo.findByEnrollmentNo(enrollmentNo);
        if(student.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Student not found"));
        }

        return ResponseEntity.ok(attendanceRepo.findByStudent(student.get()));
    }
}