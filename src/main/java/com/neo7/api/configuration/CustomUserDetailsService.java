package com.neo7.api.configuration;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.neo7.api.model.DBUser;
import com.neo7.api.repository.DBUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  @Autowired
  private DBUserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    DBUser user = userRepository.findByUsername(username);

    return new User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user.getRole()));
  }

  private List<GrantedAuthority> getGrantedAuthorities(String role) {
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
    return authorities;
  }
}