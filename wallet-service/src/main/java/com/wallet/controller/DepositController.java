package com.wallet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.dto.Deposit;
import com.wallet.entity.DepositEntity;
import com.wallet.service.DepositService;
import com.wallet.service.TransactionHistoryService;

@RestController
@RequestMapping(path = "/houses", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DepositController {

	@Autowired
	private DepositService depositService;

	@Autowired
	private TransactionHistoryService transactionHistoryService;

	@GetMapping
	public List<Deposit> getAll() {
		return depositService.listAll();
	}

	@GetMapping(path = "/{id}")
	public Deposit getOne(@PathVariable("id") Long id) {
		return transactionHistoryService.findById(id);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Deposit postNew(@RequestBody(required = true) Deposit house) {
		return depositService.addNew(house);
	}

	@GetMapping(path = "/{id}/members")
	public DepositEntity getHouseMembers(@PathVariable("id") Long id) {
		return transactionHistoryService.listByHouseId(id);
	}

}