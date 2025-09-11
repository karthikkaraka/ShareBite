package com.TechNova.ShareBite.Service;

import com.TechNova.ShareBite.Model.User;

import com.TechNova.ShareBite.Model.UserPrinciple;
import com.TechNova.ShareBite.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    UserRepository repo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByName(username) ;
        if(user==null)
        {
            throw new UsernameNotFoundException("user not foumd!!!");
        }
        else{
            return new UserPrinciple(user);
        }
    }
}
