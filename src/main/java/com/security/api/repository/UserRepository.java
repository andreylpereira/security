package com.security.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.security.api.models.UserEntity;

public interface UserRepository extends JpaRepository <UserEntity, Integer> {
	
	Optional<UserEntity> findByCpf(String cpf);
	Boolean existsByCpf(String cpf);

}
