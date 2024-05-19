package com.neo7.api.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "employees")
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "First name is required") // Note : sufficient for null, missing and empty value
  @Size(min = 3, max = 20, message = "First name should be between 3 and 20 characters")
  @Column(name = "first_name")
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Size(min = 3, max = 20, message = "Last name should be between 3 and 20 characters")
  @Column(name = "last_name")
  private String lastName;

  @NotBlank(message = "Email is required")
  @Email(message = "Email is invalid")
  private String mail;

  @NotBlank(message = "Password is required")
  private String password;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private Set<Ward> wards = new HashSet<>();

  public void addWard(Ward ward) {
    ward.setEmployee(this);
    this.wards.add(ward);
  }

  public void removeWard(Ward ward) {
    ward.setEmployee(null);
    this.wards.remove(ward);
  }
}
