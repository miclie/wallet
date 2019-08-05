package com.account.service;

import org.springframework.stereotype.Service;

import com.account.service.client.AuthServiceFeignClient;
import com.account.service.dto.UserDto;
import com.account.service.dto.UserRegistrationDto;

@Service
public class AccountServiceImpl implements AccountService {

	private final AuthServiceFeignClient authServiceFeignClient;

	public AccountServiceImpl(AuthServiceFeignClient authServiceFeignClient) {
		this.authServiceFeignClient = authServiceFeignClient;
	}

	@Override
	public UserDto create(UserRegistrationDto user) {
		return authServiceFeignClient.createUser(user);
	}
}