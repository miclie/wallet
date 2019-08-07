package com.wallet.authservice.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wallet.authservice.model.User;
import com.wallet.authservice.repository.UserRepository;

@Service
public class UserServiceImpl {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}

	public User create(User user) {
		throwIfUsernameExists(user.getUsername());

		String hash = passwordEncoder.encode(user.getPassword());
		user.setPassword(hash);
		// user.set.setEnabled(Boolean.TRUE);
		// user.setRoles((Collections.singletonList("admin")));

		return userRepository.save(user);
	}

	private void throwIfUsernameExists(String username) {
		Optional<User> existingUser = userRepository.findByUsername(username);
		existingUser.ifPresent((user) -> {
			throw new IllegalArgumentException("User not available");
		});
	}

}