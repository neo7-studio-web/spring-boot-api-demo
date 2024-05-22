package com.neo7.api.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
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

import lombok.Getter;
import lombok.Setter;

@Configuration
@EnableWebSecurity
@ConfigurationProperties(prefix = "neo7.security")
public class SpringSecurityConfiguration {

  @Autowired
  private UserDetailsService customUserDetailsService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(auth -> {
      auth.requestMatchers("/admin").hasRole("ADMIN");
      auth.requestMatchers("/user").hasRole("USER");
      auth.requestMatchers("/h2-console/**").permitAll(); // Access to H2 console
      auth.requestMatchers("/swagger-ui/**").permitAll(); // Access to Swagger UI
      auth.anyRequest().authenticated();
    })
        // .formLogin(Customizer.withDefaults()) // Comment for an api (no login page)
        .headers(header -> {
          header.frameOptions(FrameOptionsConfig::sameOrigin);
        }) // For H2 console iFrames
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

  /**
   * Value is coming from application.properties and automatically injected
   * by Spring because of @ConfigurationProperties above and prefix
   */
  @Setter
  @Getter
  private String jwtKey;

  @Bean
  public JwtDecoder jwtDecoder() {
    SecretKeySpec secretKey = new SecretKeySpec(this.getJwtKey().getBytes(), 0, this.getJwtKey().getBytes().length,
        "RSA");
    return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    return new NimbusJwtEncoder(new ImmutableSecret<>(this.getJwtKey().getBytes()));
  }

}
