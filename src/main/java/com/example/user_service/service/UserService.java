package com.example.user_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.user_service.client.RoleClient;
import com.example.user_service.dto.UserRoleCreateDto;
import com.example.user_service.dto.UserRoleDto;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.repository.entity.UserEntity;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userInfoRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private RoleClient roleClient;
    
    
    public UserRoleDto register(UserRoleDto user) {
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        UserEntity newUser = userInfoRepository.saveAndFlush(
            new UserEntity(0, user.getUserEmail(), user.getUserPassword(), user.getUserFirstName(), user.getUserLastName(), user.getUserPhoneNo())
        );
        if (user.getAllRolesId() == null || user.getAllRolesId().isEmpty()) {
            user.setAllRolesId(List.of(1));
        }
        for (int roleId : user.getAllRolesId()) {
            roleClient.createUserRole(new UserRoleCreateDto(0, newUser.getUserId(), roleId));
        }
        return user;
    }

    public Optional<UserEntity> getAuserById(int userId){
    	return userInfoRepository.findById(userId);
    }
	
	public List<UserEntity> getAllUsers(){
		return userInfoRepository.findAll();
	}
	
	public String generateToken(String name, List<String> allRoles) {
		return jwtService.generateToken(name, allRoles);
	}

	public boolean verifyToken(String token) {
		jwtService.validateToken(token);
		return true;
	}
	
	public Optional<UserEntity> getUserByEmail(String email) {
        return userInfoRepository.findByUserEmail(email);
    }
	
	public UserEntity updateUser(UserEntity editUser) {
		editUser.setUserPassword(passwordEncoder.encode(editUser.getUserPassword()));
		return userInfoRepository.save(editUser);
	}	
	
	public void deleteUser(int uId) {
		userInfoRepository.deleteById(uId);
	}
		
}
