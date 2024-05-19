package com.neo7.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neo7.api.model.DBUser;

public interface DBUserRepository extends JpaRepository<DBUser, Integer> {
  public DBUser findByUsername(String username);
}