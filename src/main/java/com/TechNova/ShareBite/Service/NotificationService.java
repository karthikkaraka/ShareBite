package com.TechNova.ShareBite.Service;

import com.TechNova.ShareBite.Model.Notification;
import com.TechNova.ShareBite.Model.NotificationType;
import com.TechNova.ShareBite.Model.User;
import com.TechNova.ShareBite.Repository.NotificationRepository;
import com.TechNova.ShareBite.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepo;
    @Autowired
    UserRepository userrepo;

    public void createNotification(Long userId, String message, NotificationType type) {
        Notification n = new Notification();
        User user = userrepo.findById(userId).orElseThrow(()->  new RuntimeException("user not found"));
        n.setUser(user);
        n.setMessage(message);
        n.setType(type);
        n.setCreatedAt(LocalDateTime.now());
        n.setRead(false);
        notificationRepo.save(n);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
