package com.app.SmartAttendanceSystem.Models;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "examsessions")
public class ExamSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examSessionId;

    // NEW FIELD
    @Column(nullable = false, length = 150)
    private String examName;

    @Column(nullable = false)
    private LocalDate examDate;

    @Column(nullable = false)
    private LocalTime examTime;

    @Column(unique = true, columnDefinition = "TEXT")
    private String qrCode;

    @Column(unique = true, length = 6)
    private String tempCode;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Administrator admin;
}