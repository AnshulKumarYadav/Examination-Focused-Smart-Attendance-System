package com.app.SmartAttendanceSystem.Repositories;

import com.app.SmartAttendanceSystem.Models.Attendance;
import com.app.SmartAttendanceSystem.Models.ExamSession;
import com.app.SmartAttendanceSystem.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    boolean existsByStudentAndExamSession(Student student, ExamSession examSession);
    List<Attendance> findByStudent(Student student);
}