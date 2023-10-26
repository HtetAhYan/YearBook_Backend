package com.backend.yearbook.controller;

import com.backend.yearbook.entity.User;
import com.backend.yearbook.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:3000")
public class GeneralController {
    @Autowired
    private UserRepo userRepo;
    @PostMapping("/{email}")
    public Optional<User> hello(@PathVariable String email){
        return
            userRepo.findByEmail(email)
            ;
    }
}
