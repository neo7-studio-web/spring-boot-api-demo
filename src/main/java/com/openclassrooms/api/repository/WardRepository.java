package com.openclassrooms.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.api.model.Ward;

@Repository
public interface WardRepository extends CrudRepository<Ward, Long> {

}
