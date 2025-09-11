package com.TechNova.ShareBite.Controller;

import com.TechNova.ShareBite.DTO.RegisterResponce;
import com.TechNova.ShareBite.Model.User;
import com.TechNova.ShareBite.Repository.UserRepository;
import com.TechNova.ShareBite.Service.JwtService;
import com.TechNova.ShareBite.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
    public ResponseEntity<String> login(@RequestBody User user){
        System.out.println("karthik karaka");
        Authentication auth = authmanager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(),user.getPassword()));
        User realuser = userrepo.findByName(user.getName());
        if(auth.isAuthenticated())
        {
            String token =  jwtservice.generatejwttoken(realuser,user.getName());
            return new ResponseEntity<>(token,HttpStatus.FOUND);
        }
        else{
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
}
