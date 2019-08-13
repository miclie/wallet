package com.wallet.controller;

import java.util.concurrent.CompletableFuture;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wallet.dto.Deposit;
import com.wallet.exception.NoUserFoundException;
import com.wallet.service.DepositService;

@RestController
@RequestMapping(path = "/account", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DepositController {

	@Autowired
	private DepositService depositService;

	//@PreAuthorize("hasAuthority('role_admin')")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> createNewAccount(@RequestHeader("bearer") String token,
			@RequestBody(required = true) Deposit deposit) throws NoUserFoundException {

		Algorithm algorithmHS = Algorithm.HMAC256("password");
		JWTVerifier verifier = JWT.require(algorithmHS).build(); // Reusable verifier instance
		DecodedJWT jwt = verifier.verify(token);

		Claim usernameClaim = jwt.getClaim("email");
		String username = usernameClaim.asString();

		return depositService.createNewAccount(username, deposit);
	}

//	@PreAuthorize("hasAuthority('role_admin')")
	@GetMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> getAccount(@RequestHeader("bearer") String token,
			@RequestBody(required = true) Deposit deposit) throws AccountNotFoundException {

		Algorithm algorithmHS = Algorithm.HMAC256("password");
		JWTVerifier verifier = JWT.require(algorithmHS).build(); // Reusable verifier instance
		DecodedJWT jwt = verifier.verify(token);

		Claim usernameClaim = jwt.getClaim("email");
		String username = usernameClaim.asString();

		return depositService.getAccount(username);
	}

//	@PreAuthorize("hasAuthority('role_admin')")
	@GetMapping(path = "/{id}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> getAccount(@PathVariable("id") Long id) throws AccountNotFoundException {
		return depositService.getAccountById(id);
	}

}