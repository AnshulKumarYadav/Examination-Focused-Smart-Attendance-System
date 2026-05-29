package com.app.SmartAttendanceSystem.Repositories;

import com.app.SmartAttendanceSystem.Models.Administrator;
import com.app.SmartAttendanceSystem.Models.ExamSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamSessionRepository extends JpaRepository<ExamSession,Long> {
    Optional<ExamSession> findByQrCode(String qrCode);

    List<ExamSession> findByAdminOrderByExamDateDescExamTimeDesc(Administrator admin);

    List<ExamSession> findAllByOrderByExamDateDescExamTimeDesc();
}