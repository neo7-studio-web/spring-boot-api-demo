package com.openclassrooms.api.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {

  @Autowired
  private UserDetailsService customUserDetailsService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(auth -> {
      auth.requestMatchers("/admin").hasRole("ADMIN");
      auth.requestMatchers("/user").hasRole("USER");
      auth.anyRequest().authenticated();
    })
        // .formLogin(Customizer.withDefaults()) // Comment for an api (no login page)
        .httpBasic(Customizer.withDefaults()) // Comment if login form. Used for /login route
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Api = stateless
        .csrf(csrf -> csrf.disable()) // Uncomment for an api to allow POST, PUT...
        .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())) // JWT
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder)
      throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http
        .getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    return authenticationManagerBuilder.build();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private String jwtKey = "D7tIq+rkB7G2+EZ8hOlJ4YKpVq9DlO3K1wqjufp3JlrN58G3G/KkM+HumRqGiXW8a6ZB1TNhGbyhLq8zAW4ZsvpobwH4G6/P7L4vGWK27awt45cKc3JEGirFlXpSI5Zqds17GL1FIv5+pOeybTpfGTTv3iKwkKe6JembjZBqDdJs6inY3t7P8qmzvA9RwF2JpSYNeL/No5R/h04yuQ7c6Ny7+bAU2fgMH4iOI8Qn0vVoIuAWHLa1FM0DrERkoZoTQy/kLGiiQDCmxRiglnhSn5pjou5iMB/d5oRzkArqUqV3JTwcei9yVCXErKFyh4PjRWi3wtZSPTk5Bo6LF/9NdEcWyvltN2kUWDeCGkNxc3o=";

  @Bean
  public JwtDecoder jwtDecoder() {
    SecretKeySpec secretKey = new SecretKeySpec(this.jwtKey.getBytes(), 0, this.jwtKey.getBytes().length, "RSA");
    return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtKey.getBytes()));
  }

}
