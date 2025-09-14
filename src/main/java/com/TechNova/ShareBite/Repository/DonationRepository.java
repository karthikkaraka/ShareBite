package com.TechNova.ShareBite.Repository;

import com.TechNova.ShareBite.Model.Donation;
import com.TechNova.ShareBite.Model.DonationStatus;
import com.TechNova.ShareBite.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByDonor(User donor);
    List<Donation> findByNgo(User ngo);
    List<Donation> findByStatus(DonationStatus status);
    List<Donation> findByCityAndStatus(String city, DonationStatus status);

}