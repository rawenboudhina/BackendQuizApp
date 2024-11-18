package com.quizapplication.quizapp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizapplication.quizapp.Config.JwtProvider;
import com.quizapplication.quizapp.Entity.User;
import com.quizapplication.quizapp.Repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        user.setRanks(0L); // Initialise le rang
        user.setScore(0L); // Initialise le score
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, Long userId) {
        User existingUser = findUserById(userId);
        if (existingUser != null) {
            if (user.getFirstName() != null) {
                existingUser.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null) {
                existingUser.setLastName(user.getLastName());
            }
            return userRepository.save(existingUser);
        }
        throw new IllegalArgumentException("User not found with id: " + userId);
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> 
            new IllegalArgumentException("User not found with id: " + userId)
        );
    }

    @Override
    public String deleteUser(Long userId) {
        User user = findUserById(userId);
        userRepository.delete(user);
        return "User deleted successfully!";
    }

    @Override
    public User findUserByToken(String jwt) {
        String email = JwtProvider.getEmailFromJwtToken(jwt);
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByScoreDesc();
    }

    @Override
    public List<User> setRankUser() {
        List<User> users = getAllUsers();
        if (users.isEmpty()) return users;

        long rank = 1;
        long prevScore = users.get(0).getScore();
        users.get(0).setRanks(rank);

        for (int i = 1; i < users.size(); i++) {
            if (users.get(i).getScore() < prevScore) {
                rank++;
            }
            prevScore = users.get(i).getScore();
            users.get(i).setRanks(rank);
        }
        return userRepository.saveAll(users); // Sauvegarde en batch
    }

    @Override
    public List<User> getAllUsersByRankUsers() {
        return userRepository.findAllByOrderByRanksAsc();
    }
}