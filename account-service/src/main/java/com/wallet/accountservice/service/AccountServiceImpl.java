package com.wallet.accountservice.service;

import com.wallet.accountservice.client.AuthServiceFeignClient;
import com.wallet.accountservice.dto.UserDto;
import com.wallet.accountservice.dto.UserRegistrationDto;

import org.springframework.stereotype.Service;

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
