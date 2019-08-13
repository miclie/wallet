package com.wallet.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
import com.wallet.entity.DepositEntity;
import com.wallet.entity.TransactionHistoryEntity;
import com.wallet.entity.User;
import com.wallet.exception.BadRequestException;
import com.wallet.exception.NoUserFoundException;
import com.wallet.repository.DepositEntityRepository;
import com.wallet.repository.TransactionHistoryEntityRepository;

@Service
public class DepositService {

	private static Logger LOGGER = LoggerFactory.getLogger(DepositService.class);

	@Autowired
	private DepositEntityRepository depositRepository;

	@Autowired
	private TransactionHistoryEntityRepository transactionHistoryRepository;

	@Autowired
	private Validator validator;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Transactional(readOnly = true)
	public DepositEntity listById(Long id) {
		return depositRepository.findById(id).get();
	}

	@Async
	@Transactional(readOnly = false)
	public CompletableFuture<Deposit> createNewAccount(String username, Deposit dto) throws NoUserFoundException {

		Set<ConstraintViolation<Deposit>> violations = validator.validate(dto);
		if (violations.size() > 0) {
			throw new BadRequestException();
		}
		User user = (User) userDetailsService.loadUserByUsername(username);
		if (user == null) {
			throw new NoUserFoundException();
		}
		dto.setUsername(username);

		try {
			DepositEntity account = depositRepository.save(new DepositEntity(user, dto));
			TransactionHistoryEntity transactionHistoryEntity = new TransactionHistoryEntity();

			transactionHistoryEntity.setId(dto.getTransactionId());
			transactionHistoryEntity.setDeposit(account);
			transactionHistoryEntity.setUser(user);
			transactionHistoryRepository.save(transactionHistoryEntity);

			return CompletableFuture.completedFuture(toDto(account, dto.getTransactionId()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Deposit toDto(DepositEntity entity, String transactionId) throws AccountNotFoundException {
		return new Deposit(entity.getUser().getUsername(), entity.getRemaining(), entity.getCredit(), transactionId)
				.withLink(linkTo(methodOn(DepositController.class).getAccount(entity.getId())).withRel("account"));
	}

	public Deposit toDto(DepositEntity entity) throws AccountNotFoundException {
		return new Deposit(entity.getUser().getUsername(), entity.getRemaining(), entity.getCredit(), "")
				.withLink(linkTo(methodOn(DepositController.class).getAccount(entity.getId())).withRel("account"));
	}

	@Async
	@Transactional(readOnly = true)
	public CompletableFuture<Deposit> getAccount(String username) throws AccountNotFoundException {
		User user = (User) userDetailsService.loadUserByUsername(username);
		Optional<DepositEntity> accountOptional = depositRepository.findByUser(user);
		if (accountOptional.isPresent()) {
			return CompletableFuture.completedFuture(toDto(accountOptional.get()));
		} else {
			throw new AccountNotFoundException();
		}
	}

	@Async
	@Transactional(readOnly = true)
	public CompletableFuture<Deposit> getAccountById(Long id) throws AccountNotFoundException {
		Optional<DepositEntity> depositEntityOptional = depositRepository.findById(id);
		return CompletableFuture.completedFuture(toDto(depositEntityOptional.get()));
	}
}