package com.example.campus_placements.user.repository;

import com.example.campus_placements.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    public boolean existsByEmail(String email);
}
