package com.TechNova.ShareBite.Service;

import com.TechNova.ShareBite.Model.*;
import com.TechNova.ShareBite.Repository.DonationRepository;
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

    // Volunteer claims donation
    public VolunteerAssignment volunteerClaimDonation(Long donationId, Long volunteerId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User volunteer = userRepo.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        Donation donation = donationRepo.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        System.out.println("Current user: " + currentUserEmail);
        System.out.println("Volunteer email: " + volunteer.getEmail());
        System.out.println("Donation status: " + donation.getStatus());
        System.out.println("Volunteer city: " + volunteer.getCity());
        System.out.println("Donation city: " + donation.getCity());

        if (!volunteer.getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("You cannot claim donations as another volunteer");
        }



        if (!donation.getCity().equalsIgnoreCase(volunteer.getCity())) {
            throw new RuntimeException("You can only claim donations from your city");
        }

        if (donation.getStatus() != DonationStatus.PENDING) {
            throw new RuntimeException("Donation is already claimed or delivered");
        }

        donation.setStatus(DonationStatus.ASSIGNED);
        donationRepo.save(donation);

        VolunteerAssignment assignment = new VolunteerAssignment();
        assignment.setDonation(donation);
        assignment.setVolunteer(volunteer);
        assignment.setStatus(AssignmentStatus.ASSIGNED);
        assignment.setAssignedAt(LocalDateTime.now());

        return assignmentRepo.save(assignment);
    }

    // Volunteer marks donation picked up
    public VolunteerAssignment markPickedUp(Long assignmentId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        VolunteerAssignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getVolunteer().getEmail().equals(currentUser)) {
            throw new RuntimeException("You are not assigned to this delivery");
        }

        assignment.setStatus(AssignmentStatus.PICKED_UP);
        assignment.setPickedUpAt(LocalDateTime.now());
        return assignmentRepo.save(assignment);
    }

    // Volunteer marks donation delivered
    public VolunteerAssignment markDelivered(Long assignmentId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        VolunteerAssignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getVolunteer().getEmail().equals(currentUser)) {
            throw new RuntimeException("You are not assigned to this delivery");
        }

        assignment.setStatus(AssignmentStatus.DELIVERED);
        assignment.setDeliveredAt(LocalDateTime.now());

        Donation donation = assignment.getDonation();
        donation.setStatus(DonationStatus.DELIVERED);
        donationRepo.save(donation);

        notificationService.createNotification(
                assignment.getVolunteer().getId(),
                "You delivered donation #" + donation.getId(),
                NotificationType.SUCCESS
        );

        return assignmentRepo.save(assignment);
    }

    // List assignments by volunteer
    public List<VolunteerAssignment> getAssignmentsByVolunteer(Long volunteerId) {
        return assignmentRepo.findByVolunteerId(volunteerId);
    }

    public List<User> getNearbyVolunteers(String city) {
        return userRepo.findByRoleAndStatusAndCity(Roles.VOLUNTEER, Status.ACCEPTED, city);
    }

    public VolunteerAssignment markPaymentDone(Long assignmentId) {
        VolunteerAssignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setPaymentDone(true);
        return assignmentRepo.save(assignment);
    }
}
