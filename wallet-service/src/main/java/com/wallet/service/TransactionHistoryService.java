package com.wallet.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wallet.controller.TransactionHistoryController;
import com.wallet.dto.Deposit;
import com.wallet.entity.DepositEntity;
import com.wallet.exception.BadRequestException;
import com.wallet.exception.ConflictException;
import com.wallet.exception.InternalServerException;
import com.wallet.exception.ResourceNotFoundException;
import com.wallet.repository.DepositEntityRepository;
import com.wallet.repository.TransactionHistoryEntityRepository;

@Service
public class TransactionHistoryService {

	private static Logger LOGGER = LoggerFactory.getLogger(TransactionHistoryService.class);

	@Autowired
	private DepositEntityRepository depositRepository;

	@Autowired
	private TransactionHistoryEntityRepository transactionHistoryRepository;

	@Autowired
	private Validator validator;


	@Transactional(readOnly = true)
	public Deposit findById(Long id) {
		Optional<DepositEntity> entity = null;
		try {
			entity = depositRepository.findById(id);
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


	public Deposit toDto(DepositEntity entity) {
		return new Deposit(entity.getUser().getUsername(),entity.getRemaining(),entity.getCredit())
				.withLink(linkTo(methodOn(TransactionHistoryController.class).getOne((entity.getId()))).withSelfRel())
				.withLink(linkTo(methodOn(TransactionHistoryController.class).getHouseMembers(entity.getId()))
						.withRel("members"));
	}

	@Transactional(readOnly = true)
	public DepositEntity listByHouseId(Long id) {
		return depositRepository.findById(id).get();
	}

}