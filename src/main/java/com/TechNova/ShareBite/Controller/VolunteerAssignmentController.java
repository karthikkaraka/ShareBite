package com.TechNova.ShareBite.Controller;

import com.TechNova.ShareBite.Model.Donation;
import com.TechNova.ShareBite.Model.User;
import com.TechNova.ShareBite.Model.VolunteerAssignment;
import com.TechNova.ShareBite.Repository.DonationRepository;
import com.TechNova.ShareBite.Service.VolunteerAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ShareBite/volunteer")
public class VolunteerAssignmentController {

    @Autowired
    private VolunteerAssignmentService assignmentService;
    @Autowired
    private DonationRepository donationRepo;
    private VolunteerAssignmentService volunteerService;
    // Assign volunteer (Admin or NGO)
    @PostMapping("/assign/{donationId}/{volunteerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('NGO')")
    public ResponseEntity<VolunteerAssignment> assignVolunteer(
            @PathVariable Long donationId,
            @PathVariable Long volunteerId) {
        return ResponseEntity.ok(assignmentService.assignVolunteer(donationId, volunteerId));
    }

    // Volunteer marks picked up
    @PutMapping("/pickup/{assignmentId}")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseEntity<VolunteerAssignment> pickupDonation(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentService.markPickedUp(assignmentId));
    }

    // Volunteer marks delivered
    @PutMapping("/deliver/{assignmentId}")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseEntity<VolunteerAssignment> deliverDonation(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentService.markDelivered(assignmentId));
    }

    // List assignments for volunteer
    @GetMapping("/assignments/{volunteerId}")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseEntity<List<VolunteerAssignment>> getAssignments(@PathVariable Long volunteerId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByVolunteer(volunteerId));
    }
    @GetMapping("/volunteers/nearby/{donationId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('NGO')")
    public ResponseEntity<List<User>> getNearbyVolunteers(@PathVariable Long donationId) {
        Donation donation = donationRepo.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        List<User> nearbyVolunteers = volunteerService.getNearbyVolunteers(donation.getCity());
        return ResponseEntity.ok(nearbyVolunteers);
    }
}