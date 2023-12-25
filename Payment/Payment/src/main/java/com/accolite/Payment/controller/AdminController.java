package com.accolite.Payment.controller;

import com.accolite.Payment.model.User;
import com.accolite.Payment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    /*@Autowired
    private UserService userService;

    @PostMapping("/approve-registration/{userId}")
    public ResponseEntity<String> approveRegistration(@PathVariable String userId) {
        userService.approveRegistration(userId);
        return ResponseEntity.ok("User approved successfully.");
    }*/

}
