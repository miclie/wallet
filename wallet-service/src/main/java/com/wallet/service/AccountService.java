package com.wallet.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wallet.auth.service.CustomUserDetailsService;
import com.wallet.controller.DepositController;
import com.wallet.dto.Deposit;
import com.wallet.dto.Transaction;
import com.wallet.entity.AccountEntity;
import com.wallet.entity.User;
import com.wallet.exception.AccountBalanceCannotBeLessThanZeroException;
import com.wallet.exception.BadRequestException;
import com.wallet.exception.NoUserFoundException;
import com.wallet.exception.TransactionNumberAlreadyExists;
import com.wallet.repository.AccountEntityRepository;

@Service
public class AccountService {

	private static Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

	private AccountEntityRepository depositRepository;

	private TransactionHistoryService transactionHistoryService;

	private Validator validator;

	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	public AccountService(AccountEntityRepository depositRepository,TransactionHistoryService transactionHistoryService,Validator validator,CustomUserDetailsService userDetailsService) {
		this.depositRepository=depositRepository;
		this.transactionHistoryService=transactionHistoryService;
		this.validator=validator;
		this.userDetailsService=userDetailsService;
	}

	@Transactional(readOnly = true)
	public AccountEntity listById(Long id) {
		return depositRepository.findById(id).get();
	}

	@Async
	@Transactional(readOnly = false)
	public CompletableFuture<Deposit> createNewAccount(String username, Deposit dto)
			throws NoUserFoundException, AccountNotFoundException, TransactionNumberAlreadyExists {

		User user = (User) userDetailsService.loadUserByUsername(username);

		checkConstraints(dto);
		checkConstraints(dto.getTransactionId(), username, user);

		AccountEntity accountEntity = depositRepository.save(new AccountEntity(user, dto));
		transactionHistoryService.saveTransactionHistory(accountEntity, dto, user);
		return CompletableFuture.completedFuture(toDto(accountEntity, dto.getTransactionId()));

	}

	public Deposit toDto(AccountEntity entity, String transactionId) throws AccountNotFoundException {
		return new Deposit(entity.getUser().getUsername(), entity.getRemaining(), entity.getCredit(), transactionId,
				entity.getCreatedOn(), entity.getUpdatedOn()).withLink(
						linkTo(methodOn(DepositController.class).getAccount(entity.getId())).withRel("account"));
	}

	public Deposit toDto(AccountEntity entity) throws AccountNotFoundException {
		return new Deposit(entity.getUser().getUsername(), entity.getRemaining(), entity.getCredit(), "",
				entity.getCreatedOn(), entity.getUpdatedOn()).withLink(
						linkTo(methodOn(DepositController.class).getAccount(entity.getId())).withRel("account"));
	}

	@Transactional(readOnly = true)
	public Deposit getAccount(String username) throws AccountNotFoundException {
		User user = (User) userDetailsService.loadUserByUsername(username);
		Optional<AccountEntity> accountOptional = depositRepository.findByUser(user);
		if (accountOptional.isPresent()) {
			return toDto(accountOptional.get());
		}
		return null;
	}

	@Async
	@Transactional(readOnly = true)
	public CompletableFuture<Deposit> getAccountById(Long id) throws AccountNotFoundException {
		Optional<AccountEntity> depositEntityOptional = depositRepository.findById(id);
		return CompletableFuture.completedFuture(toDto(depositEntityOptional.get()));
	}

	@Async
	@Transactional(readOnly = false)
	public CompletableFuture<Deposit> updateAccount(String username, Deposit deposit)
			throws NoUserFoundException, TransactionNumberAlreadyExists, AccountNotFoundException {

		User user = (User) userDetailsService.loadUserByUsername(username);

		checkConstraints(deposit);
		checkConstraints(deposit.getTransactionId(), username, user);

		Optional<AccountEntity> accountOptional = depositRepository.findByUser(user);
		if (!accountOptional.isPresent()) {
			throw new AccountNotFoundException();
		}
		AccountEntity accountUpdated = accountOptional.get();
		accountUpdated.setCredit(deposit.getCredit());
		accountUpdated.setRemaining(deposit.getRemaining());
		depositRepository.save(accountUpdated);
		return CompletableFuture.completedFuture(toDto(accountUpdated, deposit.getTransactionId()));
	}

	public void checkConstraints(Deposit dto) {
		Set<ConstraintViolation<Deposit>> violations = validator.validate(dto);
		if (violations.size() > 0) {
			throw new BadRequestException();
		}

	}

	public void checkConstraints(String transactionId, String username, User user)
			throws NoUserFoundException, TransactionNumberAlreadyExists {

		if (user == null) {
			throw new NoUserFoundException();
		}

		Transaction transaction = transactionHistoryService.findById(transactionId);
		if (transaction != null) {
			throw new TransactionNumberAlreadyExists();
		}
	}

	@Async
	@Transactional(readOnly = false)
	public CompletableFuture<Deposit> addMoneyToAccount(String username, BigDecimal amount, String transactionId)
			throws NoUserFoundException, TransactionNumberAlreadyExists, AccountNotFoundException {
		User user = (User) userDetailsService.loadUserByUsername(username);
		checkConstraints(transactionId, username, user);
		Optional<AccountEntity> accountOptional = depositRepository.findByUser(user);
		if (!accountOptional.isPresent()) {
			throw new AccountNotFoundException();
		}
		AccountEntity accountUpdated = accountOptional.get();
		accountUpdated.setRemaining(accountUpdated.getRemaining().add(amount));
		depositRepository.save(accountUpdated);
		return CompletableFuture.completedFuture(toDto(accountUpdated, transactionId));
	}

	@Async
	@Transactional(readOnly = false)
	public CompletableFuture<Deposit> spendMoneyFromAccount(String username, BigDecimal amount, String transactionId)
			throws NoUserFoundException, TransactionNumberAlreadyExists, AccountBalanceCannotBeLessThanZeroException,
			AccountNotFoundException {
		User user = (User) userDetailsService.loadUserByUsername(username);

		checkConstraints(transactionId, username, user);

		Optional<AccountEntity> accountOptional = depositRepository.findByUser(user);
		if (!accountOptional.isPresent()) {
			throw new AccountNotFoundException();
		}
		AccountEntity accountUpdated = accountOptional.get();
		accountUpdated.setRemaining(accountUpdated.getRemaining().subtract(amount));
		if (accountUpdated.getRemaining().compareTo(BigDecimal.ZERO) == -1) {
			throw new AccountBalanceCannotBeLessThanZeroException();
		}
		depositRepository.save(accountUpdated);
		return CompletableFuture.completedFuture(toDto(accountUpdated, transactionId));

	}
}
