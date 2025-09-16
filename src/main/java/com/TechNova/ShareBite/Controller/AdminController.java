package com.TechNova.ShareBite.Controller;

import com.TechNova.ShareBite.Model.NotificationType;
import com.TechNova.ShareBite.Model.User;
import com.TechNova.ShareBite.Model.Status;
import com.TechNova.ShareBite.Repository.UserRepository;
import com.TechNova.ShareBite.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ShareBite/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;
   @Autowired
    private NotificationService notificationService;
    @GetMapping("/pending-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getPendingUsers() {
        List<User> pending = userRepository.findByStatus(Status.PENDING);
        return ResponseEntity.ok(pending);
    }

    @PutMapping("/users/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveUser(@PathVariable Long id, Authentication auth) {
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        u.setStatus(Status.ACCEPTED);
        User user = userRepository.findByName(auth.getName());
        notificationService.createNotification(user.getId(), "You approved user " + u.getName(), NotificationType.SUCCESS);
        notificationService.createNotification(u.getId(), "Your account has been approved", NotificationType.SUCCESS);
        userRepository.save(u);
        return ResponseEntity.ok("User approved");
    }

    @PutMapping("/users/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectUser(@PathVariable Long id) {
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        u.setStatus(Status.REJECTED);
        userRepository.save(u);
        return ResponseEntity.ok("User rejected");
    }
}
