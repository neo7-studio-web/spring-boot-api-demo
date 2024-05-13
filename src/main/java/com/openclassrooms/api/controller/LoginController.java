package com.openclassrooms.api.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.api.service.JwtService;

@RestController
public class LoginController {

  private JwtService jwtService;

  public LoginController(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  public String getToken(Authentication authentication) {
    String token = jwtService.generateToken(authentication);
    return token;
  }

  @GetMapping("/user")
  public String getUser() {
    return "Welcome, User";
  }

  @GetMapping("/admin")
  public String getAdmin() {
    return "Welcome, Admin";
  }

}
