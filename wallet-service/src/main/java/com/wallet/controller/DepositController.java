package com.wallet.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.auth.service.CustomUserDetailsService;
import com.wallet.dto.Deposit;
import com.wallet.dto.UserDto;
import com.wallet.entity.User;
import com.wallet.service.DepositService;

@RestController
@RequestMapping(path = "/account", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DepositController {

	@Autowired
	private DepositService depositService;

	
	@Autowired
	private JwtAccessTokenConverter jwtAccessTokenConverter;

	
	@GetMapping
	public Deposit getOne(@RequestHeader("bearer") String token) {
		Map<String, String> tokenMap = new HashMap<String, String>();
		jwtAccessTokenConverter.extractAccessToken(token, tokenMap);
		String username = tokenMap.get("user_name");
		return depositService.findByUserName(username);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> postNew(@RequestHeader("bearer") String token,@RequestBody(required = true) Deposit deposit) {
		Map<String, String> tokenMap = new HashMap<String, String>();
		jwtAccessTokenConverter.extractAccessToken(token, tokenMap);
		String username = tokenMap.get("user_name");
		
		return depositService.addNew(username, deposit);
	}

}