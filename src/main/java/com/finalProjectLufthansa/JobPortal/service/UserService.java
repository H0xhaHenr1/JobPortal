package com.finalProjectLufthansa.JobPortal.service;

import com.finalProjectLufthansa.JobPortal.mapper.UserMapper;
import com.finalProjectLufthansa.JobPortal.model.entity.User;
import com.finalProjectLufthansa.JobPortal.model.enums.Role;
import com.finalProjectLufthansa.JobPortal.repository.UserRepository;
import com.finalProjectLufthansa.JobPortal.resource.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Page<UserResource> getAllUsers(Role role, Pageable pageable) {
        Page<User> userPage;
        if (role != null) {
            userPage = userRepository.findAllByRole(role, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        return userPage.map(userMapper::toResource);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    //Employer methods
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
