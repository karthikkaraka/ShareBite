package com.TechNova.ShareBite.Service;

import com.TechNova.ShareBite.Model.*;
import com.TechNova.ShareBite.Repository.DonationRepository;
import com.TechNova.ShareBite.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepo;

    @Autowired
    private UserRepository userRepo;

    // Donor creates donation
    public Donation createDonation(Long donorId, Donation donation) {
        User donor = userRepo.findById(donorId).orElseThrow(() -> new RuntimeException("Donor not found"));
        donation.setDonor(donor);
        donation.setStatus(DonationStatus.PENDING);
        return donationRepo.save(donation);
    }

    // NGO accepts donation
    public Donation acceptDonation(Long donationId, Long ngoId) {

        Donation donation = donationRepo.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        if (!donation.getStatus().equals("PENDING")) {
            throw new RuntimeException("Only PENDING donations can be accepted");
        }
        User ngo = userRepo.findById(ngoId).orElseThrow(() -> new RuntimeException("NGO not found"));

        donation.setNgo(ngo);
        donation.setStatus(DonationStatus.ACCEPTED);
        return donationRepo.save(donation);
    }

    public List<Donation> getDonationsByDonor(Long donorId) {
        User donor = userRepo.findById(donorId).orElseThrow(() -> new RuntimeException("Donor not found"));
        return donationRepo.findByDonor(donor);
    }

    public List<Donation> getDonationsByNgo(Long ngoId) {
        User ngo = userRepo.findById(ngoId).orElseThrow(() -> new RuntimeException("NGO not found"));
        return donationRepo.findByNgo(ngo);
    }
}