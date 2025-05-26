package com.microservices.auth.controller;

import com.microservices.auth.dto.UserDTO;
import com.microservices.auth.model.User;
import com.microservices.auth.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"}, allowCredentials = "true", allowedHeaders = {"*"})
@RestController
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/api/auth/register")
  public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
    UserDTO savedUser = userService.registerUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
  }

  @PostMapping("/api/auth/login")
  public ResponseEntity<UserDTO> loginUser(@RequestBody User user, HttpSession session) {
    UserDTO existUser = userService.loginUser(user);

    session.setAttribute("user", existUser);
    session.setMaxInactiveInterval(604800);
    return ResponseEntity.status(HttpStatus.OK).body(existUser);
  }

  @GetMapping("/api/auth/check-auth")
  public ResponseEntity<UserDTO> isLoggedIn(HttpSession session) {
    System.out.println(session.getAttribute("user") + " Session");

    UserDTO loggedInUser = (UserDTO) session.getAttribute("user");

    if (loggedInUser != null) {
      return ResponseEntity.status(HttpStatus.OK).body(loggedInUser);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // or handle with custom error message
    }
  }

  @GetMapping("/api/auth/logout")
  public ResponseEntity<Boolean> logout(HttpSession session){
    session.invalidate();
    return ResponseEntity.ok(true);
  }

  @GetMapping("/api/users")
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    List<UserDTO> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }
}
