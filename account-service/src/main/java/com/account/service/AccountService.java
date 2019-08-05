package com.account.service;

import com.account.service.dto.UserDto;
import com.account.service.dto.UserRegistrationDto;

public interface AccountService {

    UserDto create(UserRegistrationDto user);
}