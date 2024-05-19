package com.neo7.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.neo7.api.model.Ward;

@Repository
public interface WardRepository extends CrudRepository<Ward, Long> {

}
