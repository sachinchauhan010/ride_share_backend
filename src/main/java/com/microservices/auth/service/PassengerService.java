package com.microservices.auth.service;

import com.microservices.auth.dto.PassengerDTO;
import com.microservices.auth.exception.ValidationException;
import com.microservices.auth.model.Passenger;
import com.microservices.auth.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PassengerService {

  private final PassengerRepository passengerRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public PassengerService(PassengerRepository passengerRepository, PasswordEncoder passwordEncoder) {
    this.passengerRepository = passengerRepository;
    this.passwordEncoder = passwordEncoder;
  }

  private PassengerDTO convertToDTO(Passenger passenger) {
    return new PassengerDTO(
        passenger.getId(),
        passenger.getName(),
        passenger.getEmail(),
        passenger.getRole(),
        passenger.getPhone(),
        passenger.getCreatedAt()
    );
  }

  public PassengerDTO registerPassenger(Passenger passenger) {

    if (passengerRepository.existsByEmail(passenger.getEmail())) {
      throw new ValidationException("Email already exists");
    }

    if (!passenger.getPhone().matches("\\d{10}")) {
      throw new ValidationException("Invalid phone number. Must be 10 digits.");
    }

    passenger.setPassword(passwordEncoder.encode(passenger.getPassword()));
    Passenger savedPassenger = passengerRepository.save(passenger);
    return convertToDTO(savedPassenger);
  }

  public PassengerDTO loginPassenger(Passenger passenger) {

    Optional<Passenger> optionalPassenger = passengerRepository.findByEmail(passenger.getEmail());

    if (optionalPassenger.isEmpty()) {
      throw new ValidationException("Email is not registered");
    }

    Passenger existingPassenger = optionalPassenger.get();

    boolean passwordMatches = passwordEncoder.matches(passenger.getPassword(), existingPassenger.getPassword());

    if (!passwordMatches) {
      throw new ValidationException("Password is incorrect");
    }

    return convertToDTO(existingPassenger);
  }

  public List<PassengerDTO> getAllPassengers() {
    return passengerRepository.findAll()
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }
}
