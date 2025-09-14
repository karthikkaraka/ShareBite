package com.TechNova.ShareBite.Controller;

import com.TechNova.ShareBite.Model.Notification;
import com.TechNova.ShareBite.Repository.UserRepository;
import com.TechNova.ShareBite.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ShareBite/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    UserRepository userrepo;
    @GetMapping("/my")
    public ResponseEntity<List<Notification>> getMyNotifications(Authentication auth) {
       String username = auth.getName();
       long userId = userrepo.findByName(username).getId();
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }
}
