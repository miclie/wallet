package com.wallet.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wallet.auth.entity.User;
import com.wallet.auth.service.CustomUserDetailsService;
import com.wallet.controller.TransactionHistoryController;
import com.wallet.dto.Deposit;
import com.wallet.dto.Transaction;
import com.wallet.entity.AccountEntity;
import com.wallet.entity.TransactionHistoryEntity;
import com.wallet.repository.TransactionHistoryEntityRepository;

@Service
public class TransactionHistoryService {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	private static Logger LOGGER = LoggerFactory.getLogger(TransactionHistoryService.class);

	@Autowired
	private TransactionHistoryEntityRepository transactionHistoryRepository;

	@Transactional(readOnly = true)
	public List<Transaction> findByUser(String username) {

		User user = (User) userDetailsService.loadUserByUsername(username);

		List<Transaction> transactionDtoList = new ArrayList<Transaction>();
		List<TransactionHistoryEntity> transactionList = transactionHistoryRepository.findByUser(user);
		transactionList.forEach(p -> {
			transactionDtoList.add(toDto(p));
		});

		return transactionDtoList;

	}

	@Transactional(readOnly = true)
	public Transaction findById(String id) {

		Optional<TransactionHistoryEntity> transactionOptional = transactionHistoryRepository.findById(id);
		if (transactionOptional.isPresent()) {
			return toDto(transactionOptional.get());
		}

		return null;

	}

	public TransactionHistoryEntity saveTransactionHistory(AccountEntity account, Deposit dto, User user) {

		TransactionHistoryEntity transactionHistoryEntity = new TransactionHistoryEntity();
		transactionHistoryEntity.setId(dto.getTransactionId());
		transactionHistoryEntity.setAccount(account);
		transactionHistoryEntity.setUser(user);
		return transactionHistoryRepository.save(transactionHistoryEntity);
	}

	public Transaction toDto(TransactionHistoryEntity entity) {
		return new Transaction(entity)
				.withLink(linkTo(methodOn(TransactionHistoryController.class).getOne(entity.getId())).withSelfRel()
						.withRel("transaction"));
	}

}