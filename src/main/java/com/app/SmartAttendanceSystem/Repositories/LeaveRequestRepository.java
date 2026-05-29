package com.app.SmartAttendanceSystem.Repositories;

import com.app.SmartAttendanceSystem.Models.LeaveRequest;
import com.app.SmartAttendanceSystem.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Long> {

    List<LeaveRequest> findByExamSession(ExamSessionRepository examSession);

    List<LeaveRequest> findByStudent(Student student);
}
