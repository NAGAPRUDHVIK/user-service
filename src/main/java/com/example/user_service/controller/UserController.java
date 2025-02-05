package com.example.user_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.UserRoleDto;
import com.example.user_service.dto.UserRoleTokenDto;
import com.example.user_service.repository.entity.UserEntity;
import com.example.user_service.service.CustomUserDetails;
import com.example.user_service.service.JwtService;
import com.example.user_service.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/register")
	public UserRoleDto register(@RequestBody UserRoleDto user) {
		return userService.register(user);
	}

	@GetMapping
	public List<UserEntity> getAllUsers() {
		return userService.getAllUsers();
	}
	
	@GetMapping("/{userId}")
	public Optional<UserEntity> getAuserById(@PathVariable int userId){
		return userService.getAuserById(userId);
	}
	
	@GetMapping("/{userEmail}")
	public Optional<UserEntity> getAUserByEmail(@PathVariable String userEmail){
		return userService.getUserByEmail(userEmail);
	}

	@GetMapping("/validate/token")
	public boolean validateToken(@RequestParam String token) {
		return userService.verifyToken(token);
	}

	@PostMapping("/validate/user")
	public UserRoleTokenDto getToken(@RequestBody UserEntity user) {
		Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUserEmail(), user.getUserPassword()));
		if (authenticate.isAuthenticated()) {
			System.out.println("isAuthenticated?" + authenticate.isAuthenticated());
			System.out.println("user authorities: " + authenticate.getAuthorities());
			List<String> allRoles = authenticate.getAuthorities().stream().map((role)->role.getAuthority()).toList();
			return (new UserRoleTokenDto(user.getUserEmail(), allRoles, userService.generateToken(user.getUserEmail(),allRoles)));
		}
		return null;
	}
	
	@PutMapping
	public UserEntity updateUser(UserEntity editUser) {
		return userService.updateUser(editUser);
	}
	
	@DeleteMapping
	public void deleteUser(int uId) {
		userService.deleteUser(uId);
	}
}
