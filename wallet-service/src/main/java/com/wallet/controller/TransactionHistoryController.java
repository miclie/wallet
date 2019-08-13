package com.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.dto.Transaction;
import com.wallet.service.TransactionHistoryService;

@RestController
@RequestMapping(path = "/history", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TransactionHistoryController {

	@Autowired
	private TransactionHistoryService transactionHistoryService;


	@PreAuthorize("hasAuthority('role_admin')")
	@GetMapping(path = "/{id}")
	public Transaction getOne(@PathVariable("id") String id) {
		return transactionHistoryService.findById(id);
	}

}