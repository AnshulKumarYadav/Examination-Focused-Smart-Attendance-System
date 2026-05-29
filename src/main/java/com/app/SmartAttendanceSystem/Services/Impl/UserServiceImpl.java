package com.app.SmartAttendanceSystem.Services.Impl;

import com.app.SmartAttendanceSystem.Models.Administrator;
import com.app.SmartAttendanceSystem.Models.Student;
import com.app.SmartAttendanceSystem.Repositories.AdministratorRepository;
import com.app.SmartAttendanceSystem.Repositories.StudentRepository;
import com.app.SmartAttendanceSystem.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private StudentRepository studentRepo;
    @Autowired private AdministratorRepository adminRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    @Override
    public Student provisionStudent(Student student) {

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepo.save(student);
    }

    @Override
    public Administrator provisionInvigilator(Administrator invigilator) {
        invigilator.setRole("INVIGILATOR");

        invigilator.setPassword(passwordEncoder.encode(invigilator.getPassword()));
        return adminRepo.save(invigilator);
    }

    @Override
    public void updateStudentPassword(String enrollmentNo, String newPassword) {
        Student student = studentRepo.findByEnrollmentNo(enrollmentNo)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setPassword(passwordEncoder.encode(newPassword));
        studentRepo.save(student);
    }

}
