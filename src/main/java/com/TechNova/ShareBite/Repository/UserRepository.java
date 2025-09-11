package com.TechNova.ShareBite.Repository;

import com.TechNova.ShareBite.Model.Roles;
import com.TechNova.ShareBite.Model.Status;
import com.TechNova.ShareBite.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findByName(String name);
    User findByEmail(String email);
    List<User> findByStatus(Status status);
    List<User> findByRoleAndStatus(Roles role, Status status);
}
