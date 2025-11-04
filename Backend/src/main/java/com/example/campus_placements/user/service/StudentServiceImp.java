package com.example.campus_placements.user.service;

import com.example.campus_placements.user.dto.UpdateRequest;
import com.example.campus_placements.user.dto.UserResponse;
import com.example.campus_placements.user.model.Student;
import com.example.campus_placements.user.model.User;
import com.example.campus_placements.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class StudentServiceImp implements StudentService {
    private final UserRepository users;

    public StudentServiceImp(UserRepository users) { this.users = users; }

    @Override
    @Transactional
    public ArrayList<Student> getStudentDetails() {
        return users.getAll();
    }
}
