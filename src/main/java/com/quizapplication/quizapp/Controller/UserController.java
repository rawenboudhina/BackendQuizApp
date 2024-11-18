package com.quizapplication.quizapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.quizapplication.quizapp.Entity.User;
import com.quizapplication.quizapp.Repository.UserRepository;
import com.quizapplication.quizapp.Request.LoginRequest;
import com.quizapplication.quizapp.Response.AuthResponse;

@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("user")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + email);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("updateUser")
    public ResponseEntity<User> updateUserHandler(@RequestBody User user, @RequestParam String email) {
        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + email);
        }

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User updatedUser = userRepository.save(existingUser);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) {
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            return new ResponseEntity<>(new AuthResponse("User exists with email: " + user.getEmail(), false), HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setScore(0L);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(new AuthResponse("Registration Successful!", true));
    }

    @PostMapping("signin")
    public ResponseEntity<AuthResponse> signInHandler(@RequestBody LoginRequest loginRequests) {
        User user = userRepository.findByEmail(loginRequests.getEmail());
        if (user == null || !passwordEncoder.matches(loginRequests.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(new AuthResponse("Invalid credentials!", false), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(new AuthResponse("Login Successful!", true), HttpStatus.OK);
    }
}
