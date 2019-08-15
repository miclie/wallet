package com.wallet.controller;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.dto.Deposit;
import com.wallet.exception.AccountAlreadyExistsException;
import com.wallet.exception.AccountBalanceCannotBeLessThanZeroException;
import com.wallet.exception.NoUserFoundException;
import com.wallet.exception.TransactionNumberAlreadyExists;
import com.wallet.service.DepositService;
import com.wallet.service.TokenDecryptionService;

@RestController
@RequestMapping(path = "/account", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DepositController {

	@Autowired
	private DepositService depositService;

	@Autowired
	private TokenDecryptionService tokenDecryptionService;

	@PreAuthorize("hasAuthority('role_admin')")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> createNewAccount(@RequestHeader("bearer") String token,
			@RequestBody(required = true) Deposit deposit) throws NoUserFoundException, AccountNotFoundException,
			TransactionNumberAlreadyExists, AccountAlreadyExistsException {

		String username = tokenDecryptionService.getNameFromToken(token);

		if (depositService.getAccount(username) != null) {
			throw new AccountAlreadyExistsException();
		}

		return depositService.createNewAccount(username, deposit);
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> updateAccount(@RequestHeader("bearer") String token,
			@RequestBody(required = true) Deposit deposit) throws NoUserFoundException, AccountNotFoundException,
			TransactionNumberAlreadyExists, AccountAlreadyExistsException {

		String username = tokenDecryptionService.getNameFromToken(token);

		return depositService.updateAccount(username, deposit);
	}

	@PutMapping(path = "/{amount}/{transactionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> addMoney(@PathVariable("amount") BigDecimal amount,
			@PathVariable("transactionId") String transactionId, @RequestHeader("bearer") String token)
			throws NoUserFoundException, AccountNotFoundException, TransactionNumberAlreadyExists,
			AccountAlreadyExistsException {

		String username = tokenDecryptionService.getNameFromToken(token);

		return depositService.addMoneyToAccount(username, amount, transactionId);
	}

	@PutMapping(path = "/{amount/{transactionId}}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> spendMoney(@PathVariable("amount") BigDecimal amount,
			@PathVariable("transactionId") String transactionId, @RequestHeader("bearer") String token)
			throws NoUserFoundException, AccountNotFoundException, TransactionNumberAlreadyExists,
			AccountAlreadyExistsException, AccountBalanceCannotBeLessThanZeroException {

		String username = tokenDecryptionService.getNameFromToken(token);

		return depositService.spendMoneyFromAccount(username, amount, transactionId);
	}

	@PreAuthorize("hasAuthority('role_admin')")
	@GetMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> getAccount(@RequestHeader("bearer") String token,
			@RequestBody(required = true) Deposit deposit) throws AccountNotFoundException {

		String username = tokenDecryptionService.getNameFromToken(token);

		return depositService.getAccount(username);
	}

	@PreAuthorize("hasAuthority('role_admin')")
	@GetMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> getAccount(@PathVariable("id") Long id) throws AccountNotFoundException {
		return depositService.getAccountById(id);
	}

}