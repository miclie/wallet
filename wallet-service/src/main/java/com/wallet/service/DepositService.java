package com.wallet.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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
import com.wallet.controller.TransactionHistoryController;
import com.wallet.dto.Deposit;
import com.wallet.entity.DepositEntity;
import com.wallet.entity.User;
import com.wallet.exception.BadRequestException;
import com.wallet.exception.InternalServerException;
import com.wallet.exception.ResourceNotFoundException;
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

	@Transactional(readOnly = true)
	public Deposit findByUserName(String userName) {
		Optional<DepositEntity> entity = null;
		try {
			User user = (User) userDetailsService.loadUserByUsername(userName);
			entity = depositRepository.findByUser(user);
		} catch (Exception ex) {
			LOGGER.error("Unable to get house by id", ex);
			throw new InternalServerException();
		}
		if (entity.isPresent()) {
			return toDto(entity.get());
		} else {
			throw new ResourceNotFoundException();
		}
	}

	@Async
	@Transactional(readOnly = false)
	public CompletableFuture<Deposit> addNew(String username, Deposit dto) {

		Set<ConstraintViolation<Deposit>> violations = validator.validate(dto);
		if (violations.size() > 0) {
			throw new BadRequestException();
		}
		User user = (User) userDetailsService.loadUserByUsername(username);
		dto.setUsername(username);
		try {
			DepositEntity person = depositRepository.save(new DepositEntity(user, dto));
			return CompletableFuture.completedFuture(toDto(person));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Deposit toDto(DepositEntity entity) {
		return new Deposit(entity.getUser().getUsername(), entity.getRemaining(), entity.getCredit())
				.withLink(linkTo(methodOn(DepositController.class).getOne((entity.toString()))).withSelfRel())
				.withLink(linkTo(methodOn(TransactionHistoryController.class).getOne(entity.getId())).withRel("house"));
	}
}