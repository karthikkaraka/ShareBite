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
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;
    @ManyToOne
    @JoinColumn(name = "ngo_id")
    private User ngo;
    private String foodDetails;
    private int quantity;
    private String street;
    private String city;
    private String state;
    private String pinCode;
    @Enumerated(EnumType.STRING)
    private DonationStatus status;
    private LocalDateTime createdAt;
    private double estimatedValue;
    private LocalDateTime updatedAt;
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if(status == null){
            status = DonationStatus.PENDING; // Default status
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}