package com.TechNova.ShareBite.Service;

import com.TechNova.ShareBite.Model.*;
import com.TechNova.ShareBite.Repository.DonationRepository;
import com.TechNova.ShareBite.Repository.NotificationRepository;
import com.TechNova.ShareBite.Repository.UserRepository;
import com.TechNova.ShareBite.Repository.VolunteerAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VolunteerAssignmentService {

    @Autowired
    private VolunteerAssignmentRepository assignmentRepo;

    @Autowired
    private DonationRepository donationRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private NotificationService notificationService;
    // Volunteer marks donation picked up
    public VolunteerAssignment markPickedUp(Long assignmentId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        VolunteerAssignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        if (!assignment.getVolunteer().getEmail().equals(currentUser)) {
            throw new RuntimeException("You are not assigned to this delivery");
        }

        assignment.setStatus(AssignmentStatus.PICKED_UP);
        assignment.setAssignedAt(LocalDateTime.now()); // optional: update time
        return assignmentRepo.save(assignment);
    }

    // Volunteer marks donation delivered
    public VolunteerAssignment markDelivered(Long assignmentId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        VolunteerAssignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        User user = assignment.getVolunteer();
        if (!assignment.getVolunteer().getEmail().equals(currentUser)) {
            throw new RuntimeException("You are not assigned to this delivery");
        }
        assignment.setStatus(AssignmentStatus.DELIVERED);
        assignment.setDeliveredAt(LocalDateTime.now());

        // Also update donation status
        Donation donation = assignment.getDonation();
        donation.setStatus(DonationStatus.DELIVERED);
        donationRepo.save(donation);
        notificationService.createNotification(user.getId(), "You delivered donation #" + donation.getId(), NotificationType.SUCCESS);
        return assignmentRepo.save(assignment);
    }

    // List assignments by volunteer
    public List<VolunteerAssignment> getAssignmentsByVolunteer(Long volunteerId) {
        return assignmentRepo.findByVolunteerId(volunteerId);
    }

    // List all pending assignments
    public List<VolunteerAssignment> getPendingAssignments() {
        return assignmentRepo.findByStatus(AssignmentStatus.ASSIGNED);
    }
    public List<User> getNearbyVolunteers(String city) {
        return userRepo.findByRoleAndStatusAndCity(Roles.VOLUNTEER, Status.ACCEPTED, city);
    }

    public VolunteerAssignment volunteerClaimDonation(Long donationId, Long volunteerId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User volunteer = userRepo.findByEmail(currentUserEmail);
        if(volunteer == null){
            throw new RuntimeException("Volunteer not found..");
        }
        Donation donation = donationRepo.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        if (!donation.getCity().equalsIgnoreCase(volunteer.getCity())) {
            throw new RuntimeException("You can only claim donations from your city");
        }

        if (donation.getStatus() != DonationStatus.PENDING) {
            throw new RuntimeException("Donation is already claimed or delivered");
        }

        // Update donation status
        donation.setStatus(DonationStatus.ASSIGNED);
        donationRepo.save(donation);

        // Create assignment
        VolunteerAssignment assignment = new VolunteerAssignment();
        assignment.setDonation(donation);
        // Calculate payment (25% of food cost)
        double payment = donation.getEstimatedValue() * 0.25;
        assignment.setVolunteerPayment(payment);
        assignment.setPaymentDone(false);
        assignment.setVolunteer(volunteer);
        assignment.setStatus(AssignmentStatus.ASSIGNED);
        assignment.setAssignedAt(LocalDateTime.now());

        return assignmentRepo.save(assignment);
    }
    public VolunteerAssignment markPaymentDone(Long assignmentId) {
        VolunteerAssignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setPaymentDone(true);
        return assignmentRepo.save(assignment);
    }

}