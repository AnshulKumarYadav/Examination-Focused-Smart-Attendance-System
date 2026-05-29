package com.app.SmartAttendanceSystem.Services;

public interface AttendanceService {
    String verifyAndMarkAttendance(String enrollmentNo, String qrCode, String tempCode);
}
