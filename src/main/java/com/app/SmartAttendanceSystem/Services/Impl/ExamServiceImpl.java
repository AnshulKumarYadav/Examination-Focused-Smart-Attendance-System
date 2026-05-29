package com.app.SmartAttendanceSystem.Services.Impl;

import com.app.SmartAttendanceSystem.Models.ExamSession;
import com.app.SmartAttendanceSystem.Repositories.ExamSessionRepository;
import com.app.SmartAttendanceSystem.Services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    private ExamSessionRepository sessionRepo;

    @Override
    public ExamSession createExamSession(ExamSession session) {
        session.setQrCode(UUID.randomUUID().toString() + "-" + System.currentTimeMillis());
        session.setTempCode(String.format("%06d", new Random().nextInt(999999)));

        if (session.getExpiryTime() == null) {
            session.setExpiryTime(LocalDateTime.now().plusMinutes(30));
        }
        return sessionRepo.save(session);
    }
}
