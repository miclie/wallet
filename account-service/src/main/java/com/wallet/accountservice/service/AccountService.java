package com.wallet.accountservice.service;

import com.wallet.accountservice.dto.UserDto;
import com.wallet.accountservice.dto.UserRegistrationDto;

public interface AccountService {

    /**
     * Invokes Auth Service user creation
     *
     * @param user
     * @return created account
     */
    UserDto create(UserRegistrationDto user);
}
