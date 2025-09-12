package com.TechNova.ShareBite.Controller;

import com.TechNova.ShareBite.Model.Donation;
import com.TechNova.ShareBite.Service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ShareBite/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;

    // Donor creates donation
    @PostMapping("/create/{donorId}")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<Donation> createDonation(@PathVariable Long donorId , @RequestBody Donation donation) {
        return ResponseEntity.ok(donationService.createDonation(donorId, donation));
    }

    // NGO accepts donation
    @PutMapping("/{donationId}/accept/{ngoId}")
    @PreAuthorize("hasRole('NGO')")
    public ResponseEntity<Donation> acceptDonation(@PathVariable Long donationId, @PathVariable Long ngoId) {
        return ResponseEntity.ok(donationService.acceptDonation(donationId, ngoId));
    }

    // Get donations by donor
    @GetMapping("/donor/{donorId}")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<List<Donation>> getDonationsByDonor(@PathVariable Long donorId) {
        return ResponseEntity.ok(donationService.getDonationsByDonor(donorId));
    }

    // Get donations by NGO
    @GetMapping("/ngo/{ngoId}")
    @PreAuthorize("hasRole('NGO')")
    public ResponseEntity<List<Donation>> getDonationsByNgo(@PathVariable Long ngoId) {
        return ResponseEntity.ok(donationService.getDonationsByNgo(ngoId));
    }
}