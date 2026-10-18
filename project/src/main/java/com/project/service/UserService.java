package com.project.service;

import com.project.dto.UserRequest;
import com.project.dto.UserResponse;
import com.project.entity.User;
import com.project.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Convert User entity to UserResponse DTO
    private UserResponse convertToResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    // Create User
    public UserResponse saveUser(UserRequest userRequest) {

        User user = new User();

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(
                passwordEncoder.encode(userRequest.getPassword())
        );
        user.setRole(userRequest.getRole());
        User savedUser = userRepository.save(user);

        return convertToResponse(savedUser);
    }

    // Get All Users
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Get User By ID
    public Optional<UserResponse> getUserById(Long id) {

        return userRepository.findById(id)
                .map(this::convertToResponse);
    }

    // Get User By Email
    public Optional<UserResponse> getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .map(this::convertToResponse);
    }

    // Update User
    public Optional<UserResponse> updateUser(
            Long id,
            UserRequest userRequest) {

        return userRepository.findById(id)
                .map(user -> {

                    user.setName(userRequest.getName());
                    user.setEmail(userRequest.getEmail());
                    user.setPassword(
                            passwordEncoder.encode(userRequest.getPassword())
                    );
                    user.setRole(userRequest.getRole());

                    User updatedUser = userRepository.save(user);

                    return convertToResponse(updatedUser);
                });
    }

    // Delete User
    public boolean deleteUser(Long id) {

        if (userRepository.existsById(id)) {

            userRepository.deleteById(id);
            return true;
        }

        return false;
    }
}