package com.wallet.accountservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wallet.accountservice.dto.UserDto;
import com.wallet.accountservice.dto.UserRegistrationDto;

@FeignClient(name = "auth-service")
public interface AuthServiceFeignClient {

    @PostMapping
    UserDto createUser(@RequestBody UserRegistrationDto user);

}
