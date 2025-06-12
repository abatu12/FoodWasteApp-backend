package com.example.foodwasteapp.controller;

import com.example.foodwasteapp.dbmodel.User;
import com.example.foodwasteapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepo;

    @GetMapping("/all")
    public List<User> getAll() {
        return userRepo.findAll();
    }
}
