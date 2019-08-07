package com.wallet.authservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.wallet.authservice.dto.UserDto;
import com.wallet.authservice.dto.UserRegistrationDto;
import com.wallet.authservice.model.User;
import com.wallet.authservice.service.UserServiceImpl;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserServiceImpl userService;

	public UserController(UserServiceImpl userService) {
		this.userService = userService;
	}

	@GetMapping("/current")
	public Principal getUser(Principal principal) {
		return principal;
	}

	@PostMapping
	public UserDto createUser(@Valid @RequestBody UserRegistrationDto userRegistration) {
		User savedUser = userService.create(toUser(userRegistration));
		return toDto(savedUser);
	}

	private UserDto toDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId().toString());
		userDto.setUsername(user.getUsername());
		return userDto;
	}

	private User toUser(UserRegistrationDto userRegistration) {
		User user = new User();
		user.setUsername(userRegistration.getUsername());
		user.setPassword(userRegistration.getPassword());
		return user;
	}

}