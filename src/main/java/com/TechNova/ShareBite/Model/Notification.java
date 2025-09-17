package com.TechNova.ShareBite.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  // Many notifications can belong to one user
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;  // INFO, SUCCESS, ERROR, WARNING

    private LocalDateTime createdAt;

    @Column(name = "is_read")
    private boolean isRead;  // to track if the user saw it


}
