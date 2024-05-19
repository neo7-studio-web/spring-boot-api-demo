package com.neo7.api.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {

	public String generateToken(Authentication authentication);
}
