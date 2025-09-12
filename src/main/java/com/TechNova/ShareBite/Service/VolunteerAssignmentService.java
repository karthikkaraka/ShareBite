package com.TechNova.ShareBite.Service;

import com.TechNova.ShareBite.Model.*;
import com.TechNova.ShareBite.Repository.DonationRepository;
import com.TechNova.ShareBite.Repository.UserRepository;
import com.TechNova.ShareBite.Repository.VolunteerAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Assign volunteer to donation
    public VolunteerAssignment assignVolunteer(Long donationId, Long volunteerId) {
        Donation donation = donationRepo.findById(donationId).orElseThrow(() -> new RuntimeException("Donation not found"));
        User volunteer = userRepo.findById(volunteerId).orElseThrow(() -> new RuntimeException("Volunteer not found"));

        // Update donation status
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
        VolunteerAssignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setStatus(AssignmentStatus.PICKED_UP);
        assignment.setAssignedAt(LocalDateTime.now()); // optional: update time
        return assignmentRepo.save(assignment);
    }

    // Volunteer marks donation delivered
    public VolunteerAssignment markDelivered(Long assignmentId) {
        VolunteerAssignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setStatus(AssignmentStatus.DELIVERED);
        assignment.setDeliveredAt(LocalDateTime.now());

        // Also update donation status
        Donation donation = assignment.getDonation();
        donation.setStatus(DonationStatus.DELIVERED);
        donationRepo.save(donation);

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
}