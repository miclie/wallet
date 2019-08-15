package com.wallet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wallet.dto.Transaction;
import com.wallet.service.TokenDecryptionService;
import com.wallet.service.TransactionHistoryService;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping(path = "/history", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TransactionHistoryController {

	@Autowired
	private TransactionHistoryService transactionHistoryService;

	@Autowired
	private TokenDecryptionService tokenDecryptionService;

	@GetMapping(path = "/{id}")
	public Transaction getOne(@PathVariable("id") String id) {
		return transactionHistoryService.findById(id);
	}

	@GetMapping
	public List<Transaction> getList(@RequestHeader("bearer") String token) {

		String username = tokenDecryptionService.getNameFromToken(token);

		return transactionHistoryService.findByUser(username);
	}

}