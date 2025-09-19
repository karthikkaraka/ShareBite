package com.TechNova.ShareBite.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Volunteer reference
    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    private User volunteer;

    // Donation reference
    @ManyToOne
    @JoinColumn(name = "donation_id", nullable = false)
    private Donation donation;

    // Assignment status
    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;  // ASSIGNED, PICKED_UP, DELIVERED

    private LocalDateTime assignedAt;
    private LocalDateTime pickedUpAt;
    private LocalDateTime deliveredAt;
    private double volunteerPayment;
    private boolean paymentDone = false;



}
