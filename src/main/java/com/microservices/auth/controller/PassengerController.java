package com.microservices.auth.controller;

import com.microservices.auth.dto.PassengerDTO;
import com.microservices.auth.model.Passenger;
import com.microservices.auth.service.PassengerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"}, allowCredentials = "true", allowedHeaders = {"*"})
@RestController
public class PassengerController {

  @Autowired
  private PassengerService passengerService;

  @PostMapping("/api/auth/register-passenger")
  public ResponseEntity<PassengerDTO> registerPassenger(@RequestBody Passenger passenger) {
    PassengerDTO savedPassenger = passengerService.registerPassenger(passenger);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedPassenger);
  }

  @PostMapping("/api/auth/login-passenger")
  public ResponseEntity<PassengerDTO> loginPassenger(@RequestBody Passenger passenger, HttpSession session) {
    PassengerDTO existPassenger = passengerService.loginPassenger(passenger);

    session.setAttribute("passenger", existPassenger);
    session.setMaxInactiveInterval(604800);
    return ResponseEntity.status(HttpStatus.OK).body(existPassenger);
  }

  @GetMapping("/api/auth/check-auth")
  public ResponseEntity<PassengerDTO> isLoggedIn(HttpSession session) {
    System.out.println(session.getAttribute("passenger") + " Session");

    PassengerDTO loggedInPassenger = (PassengerDTO) session.getAttribute("passenger");

    if (loggedInPassenger != null) {
      return ResponseEntity.status(HttpStatus.OK).body(loggedInPassenger);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // or handle with custom error message
    }
  }

  @GetMapping("/api/auth/logout")
  public ResponseEntity<Boolean> logout(HttpSession session){
    session.invalidate();
    return ResponseEntity.ok(true);
  }

  @GetMapping("/api/passengers")
  public ResponseEntity<List<PassengerDTO>> getAllPassengers() {
    List<PassengerDTO> passengers = passengerService.getAllPassengers();
    return ResponseEntity.ok(passengers);
  }
}
