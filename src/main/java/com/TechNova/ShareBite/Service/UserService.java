package com.TechNova.ShareBite.Service;

import com.TechNova.ShareBite.DTO.RegisterResponce;
import com.TechNova.ShareBite.Model.User;
import com.TechNova.ShareBite.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userepo;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    public RegisterResponce register(User user) {
        RegisterResponce regres = new RegisterResponce();
        user.setPassword(encoder.encode(user.getPassword()));
        userepo.save(user);
        User userr = userepo.findByName(user.getName());
        regres.setUserid(userr.getId());
        regres.setUsername(userr.getName());
        regres.setRole(userr.getRole());
        return regres;
    }
}
