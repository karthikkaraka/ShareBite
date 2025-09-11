package com.TechNova.ShareBite.Controller;

import com.TechNova.ShareBite.DTO.RegisterResponce;
import com.TechNova.ShareBite.Model.Status;
import com.TechNova.ShareBite.Model.User;
import com.TechNova.ShareBite.Repository.UserRepository;
import com.TechNova.ShareBite.Service.JwtService;
import com.TechNova.ShareBite.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ShareBite")
public class UserController {
    @Autowired
    UserService userser;
    @Autowired
    AuthenticationManager authmanager;
    @Autowired
    UserRepository userrepo;
    @Autowired
    JwtService jwtservice;
    @PostMapping("/register/user")
    public ResponseEntity<RegisterResponce> register(@RequestBody User user)
    {
        System.out.println("karthik");
        RegisterResponce regres = userser.register(user);
        return new ResponseEntity<>(regres, HttpStatus.CREATED);
    }
    @PostMapping("/login/user")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            Authentication auth = authmanager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getName(), loginRequest.getPassword())
            );

            User realUser = userrepo.findByName(loginRequest.getName());
            if (realUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            if (realUser.getStatus() != Status.ACCEPTED) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Account status: " + realUser.getStatus() + ". Waiting for admin approval.");
            }

            String token = jwtservice.generatejwttoken(realUser, realUser.getName());
            return ResponseEntity.ok(token);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
    }

