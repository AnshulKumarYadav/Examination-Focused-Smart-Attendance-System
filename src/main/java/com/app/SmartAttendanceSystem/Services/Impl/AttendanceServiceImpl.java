package com.app.SmartAttendanceSystem.Services.Impl;

import com.app.SmartAttendanceSystem.Models.Attendance;
import com.app.SmartAttendanceSystem.Models.ExamSession;
import com.app.SmartAttendanceSystem.Models.Student;
import com.app.SmartAttendanceSystem.Repositories.AttendanceRepository;
import com.app.SmartAttendanceSystem.Repositories.ExamSessionRepository;
import com.app.SmartAttendanceSystem.Repositories.StudentRepository;
import com.app.SmartAttendanceSystem.Services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private ExamSessionRepository sessionRepo;

    @Autowired
    private AttendanceRepository attendanceRepo;

    @Override
    public String verifyAndMarkAttendance(String enrollmentNo, String qrCode, String tempCode) {
        Student student = studentRepo.findByEnrollmentNo(enrollmentNo)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        ExamSession session = sessionRepo.findByQrCode(qrCode)
                .orElseThrow(() -> new RuntimeException("Invalid QR Code"));

        if (LocalDateTime.now().isAfter(session.getExpiryTime())) {
            return "QR Code has expired.";
        }

        if (!session.getTempCode().equals(tempCode)) {
            return "Invalid temporary verification code.";
        }

        if (attendanceRepo.existsByStudentAndExamSession(student, session)) {
            return "Attendance already marked for this session.";
        }

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setExamSession(session);
        attendance.setStatus("PRESENT");

        attendanceRepo.save(attendance);
        return "Attendance successfully recorded!";
    }

}
