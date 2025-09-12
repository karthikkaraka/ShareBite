package com.TechNova.ShareBite.Repository;

import com.TechNova.ShareBite.Model.VolunteerAssignment;
import com.TechNova.ShareBite.Model.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerAssignmentRepository extends JpaRepository<VolunteerAssignment, Long> {
    List<VolunteerAssignment> findByVolunteerId(Long volunteerId);
    List<VolunteerAssignment> findByStatus(AssignmentStatus status);
}