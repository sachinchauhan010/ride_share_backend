package com.microservices.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {
  private Long id;
  private String name;
  private String email;
  private String role;
  private String phone;
  private LocalDateTime createdAt;
}
