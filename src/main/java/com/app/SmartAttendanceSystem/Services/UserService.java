package com.app.SmartAttendanceSystem.Services;

import com.app.SmartAttendanceSystem.Models.Administrator;
import com.app.SmartAttendanceSystem.Models.Student;

public interface UserService {
    Student provisionStudent(Student student);
    Administrator provisionInvigilator(Administrator invigilator);
    void updateStudentPassword(String enrollmentNo, String newPassword);
}
