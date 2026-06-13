package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Register;
public interface RegisterRepo extends JpaRepository<Register,Integer> {

	@Query("select r from Register r where r.username = :username")
	Register findByUsername(@Param("username") String username);
}
