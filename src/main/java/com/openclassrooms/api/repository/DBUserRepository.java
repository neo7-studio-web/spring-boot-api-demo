package com.openclassrooms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openclassrooms.api.model.DBUser;

public interface DBUserRepository extends JpaRepository<DBUser, Integer> {
  public DBUser findByUsername(String username);
}