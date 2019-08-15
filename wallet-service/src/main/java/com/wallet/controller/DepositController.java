package com.wallet.controller;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import com.wallet.service.AccountService;
import com.wallet.service.TokenDecryptionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/account", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DepositController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private TokenDecryptionService tokenDecryptionService;

	@ApiOperation(value = "Creates New Account", response = Deposit.class)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> createNewAccount(@RequestHeader("bearer") String token,
			@RequestBody(required = true) Deposit deposit) throws NoUserFoundException, AccountNotFoundException,
			TransactionNumberAlreadyExists, AccountAlreadyExistsException, InterruptedException, ExecutionException {

		String username = tokenDecryptionService.getNameFromToken(token);

		if (accountService.getAccount(username) != null) {
			throw new AccountAlreadyExistsException();
		}

		return accountService.createNewAccount(username, deposit);
	}

	@ApiOperation(value = "Updates Account", response = Deposit.class)
	@PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> updateAccount(@RequestHeader("bearer") String token,
			@RequestBody(required = true) Deposit deposit) throws NoUserFoundException, AccountNotFoundException,
			TransactionNumberAlreadyExists, AccountAlreadyExistsException {

		String username = tokenDecryptionService.getNameFromToken(token);

		return accountService.updateAccount(username, deposit);
	}

	@ApiOperation(value = "Adds Money to Account", response = Deposit.class)
	@PutMapping(path = "/{amount}/{transactionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> addMoney(@PathVariable("amount") BigDecimal amount,
			@PathVariable("transactionId") String transactionId, @RequestHeader("bearer") String token)
			throws NoUserFoundException, AccountNotFoundException, TransactionNumberAlreadyExists,
			AccountAlreadyExistsException {

		String username = tokenDecryptionService.getNameFromToken(token);

		return accountService.addMoneyToAccount(username, amount, transactionId);
	}

	@ApiOperation(value = "Withdraws Money from Account", response = Deposit.class)
	@PutMapping(path = "/{amount/{transactionId}}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> spendMoney(@PathVariable("amount") BigDecimal amount,
			@PathVariable("transactionId") String transactionId, @RequestHeader("bearer") String token)
			throws NoUserFoundException, AccountNotFoundException, TransactionNumberAlreadyExists,
			AccountAlreadyExistsException, AccountBalanceCannotBeLessThanZeroException {

		String username = tokenDecryptionService.getNameFromToken(token);

		return accountService.spendMoneyFromAccount(username, amount, transactionId);
	}

	// @PreAuthorize("hasAuthority('role_admin')")
	@GetMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Deposit getAccount(@RequestHeader("bearer") String token) throws AccountNotFoundException {

		String username = tokenDecryptionService.getNameFromToken(token);

		return accountService.getAccount(username);
	}

	@ApiOperation(value = "Gets Account by Id", response = Deposit.class)
	@GetMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Deposit> getAccount(@PathVariable("id") Long id) throws AccountNotFoundException {
		return accountService.getAccountById(id);
	}

}