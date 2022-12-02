package com.security.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.api.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	Optional<Role>findByName(String name);
}
