package com.security.api.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.api.dto.AuthResponseDTO;
import com.security.api.dto.LoginDto;
import com.security.api.dto.RegisterDto;
import com.security.api.models.Role;
import com.security.api.models.UserEntity;
import com.security.api.repository.RoleRepository;
import com.security.api.repository.UserRepository;
import com.security.api.security.JWTGenerator;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private JWTGenerator jwtGenerator;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
			RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtGenerator = jwtGenerator;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDto loginDto) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getCpf(), loginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtGenerator.generateToken(authentication);
		return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
	}

	// alterar codigo para selecionar o tipo de "ROLE"
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {

		if (userRepository.existsByCpf(registerDto.getCpf())) {
			return new ResponseEntity<>("Usuário já cadastrado", HttpStatus.BAD_REQUEST);
		}
		UserEntity user = new UserEntity();
		user.setCpf(registerDto.getCpf());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		Role roles = roleRepository.findByName("USER").get();
		user.setRoles(Collections.singletonList(roles));

		userRepository.save(user);

		return new ResponseEntity<>("Usuário registrado com sucesso!", HttpStatus.OK);
	}
}
