package com.eastnetic.taskmgt.controllers;

import com.eastnetic.taskmgt.models.User;
import com.eastnetic.taskmgt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class ApplicationController {
//
//    @Autowired
//    UserRepository userRepository;
//
//    public User getApplicationUser(){
//        String currentUserName = null;
//        User user;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!(authentication instanceof AnonymousAuthenticationToken)) {
//            currentUserName = authentication.getName();
//
//        }
//
//        user = userRepository.findByUsername(currentUserName)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("User Not Found with username: ")
//                );
//        return user;
//    }
}
