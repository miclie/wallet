package com.wallet.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wallet.auth.entity.User;
import com.wallet.controller.DepositController;
import com.wallet.controller.TransactionHistoryController;
import com.wallet.dto.Deposit;
import com.wallet.entity.DepositEntity;
import com.wallet.exception.BadRequestException;
import com.wallet.exception.ConflictException;
import com.wallet.exception.InternalServerException;
import com.wallet.exception.ResourceNotFoundException;
import com.wallet.repository.DepositEntityRepository;
import com.wallet.repository.TransactionHistoryEntityRepository;

import rocks.spiffy.spring.hateoas.utils.uri.resolver.ControllerUriResolver;

@Service
public class DepositService {

	private static Logger LOGGER = LoggerFactory.getLogger(DepositService.class);

	@Autowired
	private DepositEntityRepository depositRepository;

	@Autowired
	private TransactionHistoryEntityRepository transactionHistoryRepository;

	@Autowired
	private Validator validator;

	@Transactional(readOnly = true)
	public List<Deposit> listAll() {
		try (Stream<DepositEntity> stream = depositRepository.streamAll()) {
			return stream.map(this::toDto).collect(Collectors.toList());
		} catch (Exception ex) {
			LOGGER.error("Unable to get all persons", ex);
			throw new InternalServerException();
		}
	}

	@Transactional(readOnly = true)
	public DepositEntity listById(Long id) {
		return depositRepository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public Deposit findByUser(UserDetails user) {
		Optional<DepositEntity> entity = null;
		try {
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
	public CompletableFuture<Deposit> addNew(Deposit dto) {

		Set<ConstraintViolation<Deposit>> violations = validator.validate(dto);
		if (violations.size() > 0) {
			throw new BadRequestException();
		}

		try {
			DepositEntity person = depositRepository.save(new DepositEntity(dto));
			return CompletableFuture.completedFuture(toDto(person));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Deposit toDto(DepositEntity entity) {
		return new Deposit(entity.getUser().getUsername())
				.withLink(linkTo(methodOn(DepositController.class).getOne((entity.toString()))).withSelfRel())
				.withLink(linkTo(methodOn(TransactionHistoryController.class).getOne(entity.getId())).withRel("house"));
	}
}