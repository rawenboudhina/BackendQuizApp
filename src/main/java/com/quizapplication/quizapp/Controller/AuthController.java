package com.quizapplication.quizapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.quizapplication.quizapp.Entity.User;
import com.quizapplication.quizapp.Repository.UserRepository;
import com.quizapplication.quizapp.Request.LoginRequest;
import com.quizapplication.quizapp.Response.AuthResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/auth/")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) {

        // Vérifie si l'email existe déjà
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            return new ResponseEntity<>(new AuthResponse("User exists with email: " + user.getEmail(), false), HttpStatus.BAD_REQUEST);
        }

        User createdUser = new User();
        createdUser.setEmail(user.getEmail());
        createdUser.setFirstName(user.getFirstName());
        createdUser.setLastName(user.getLastName());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));  // Encodage du mot de passe
        createdUser.setScore(0L);

        User savedUser = userRepository.save(createdUser);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMsg("Registration Successful!");
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("signin")
    public ResponseEntity<AuthResponse> signInHandler(@RequestBody LoginRequest loginRequests) {

        User user = userRepository.findByEmail(loginRequests.getEmail());

        // Vérifie si l'utilisateur existe et si le mot de passe correspond
        if (user == null || !passwordEncoder.matches(loginRequests.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(new AuthResponse("Invalid credentials!", false), HttpStatus.UNAUTHORIZED);
        }

        AuthResponse response = new AuthResponse();
        response.setMsg("Login Successful!");
        response.setStatus(true);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
