package com.TechNova.ShareBite.Repository;

import com.TechNova.ShareBite.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findByName(String name);
}
