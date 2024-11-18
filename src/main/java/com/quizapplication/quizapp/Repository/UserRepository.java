package com.quizapplication.quizapp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizapplication.quizapp.Entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
    
    public User findByEmail(String email);
    List<User> findAllByOrderByRanksAsc(); // Sorts users by rank in ascending order
	public List<User> findAllByOrderByScoreDesc();


}
