package com.wallet.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
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
import com.wallet.dto.Transaction;
import com.wallet.entity.DepositEntity;
import com.wallet.entity.TransactionHistoryEntity;
import com.wallet.entity.User;
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
	private TransactionHistoryEntityRepository transactionHistoryRepository;

	@Transactional(readOnly = true)
	public List<Transaction> findByUser(User user) {

		List<Transaction> transactionDtoList = new ArrayList<Transaction>();
		List<TransactionHistoryEntity> transactionList = transactionHistoryRepository.findByUser(user);
		transactionList.forEach(p -> {
			transactionDtoList.add(toDto(p));
		});

		return transactionDtoList;

	}

	@Transactional(readOnly = true)
	public Transaction findById(String id) {

		TransactionHistoryEntity transactionHistoryEntity = transactionHistoryRepository.findById(id).get();

		return toDto(transactionHistoryEntity);

	}

	public Transaction toDto(TransactionHistoryEntity entity) {
		return new Transaction(entity)
				.withLink(linkTo(methodOn(TransactionHistoryController.class).getOne(entity.getId())).withSelfRel()
						.withRel("transaction"));
	}

}