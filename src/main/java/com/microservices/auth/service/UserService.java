package com.microservices.auth.service;

import com.microservices.auth.dto.UserDTO;
import com.microservices.auth.exception.ValidationException;
import com.microservices.auth.model.User;
import com.microservices.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  private UserDTO convertToDTO(User user) {
    return new UserDTO(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getRole(),
        user.getPhone(),
        user.getCreatedAt()
    );
  }

  public UserDTO registerUser(User user) {

    if (userRepository.existsByEmail(user.getEmail())) {
      throw new ValidationException("Email already exists");
    }

    if (!user.getPhone().matches("\\d{10}")) {
      throw new ValidationException("Invalid phone number. Must be 10 digits.");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    User savedUser = userRepository.save(user);
    return convertToDTO(savedUser);
  }

  public UserDTO loginUser(User user) {

    Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

    if (optionalUser.isEmpty()) {
      throw new ValidationException("Email is not registered");
    }

    User existingUser = optionalUser.get();

    boolean passwordMatches = passwordEncoder.matches(user.getPassword(), existingUser.getPassword());

    if (!passwordMatches) {
      throw new ValidationException("Password is incorrect");
    }

    return convertToDTO(existingUser);
  }

  public List<UserDTO> getAllUsers() {
    return userRepository.findAll()
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }
}
