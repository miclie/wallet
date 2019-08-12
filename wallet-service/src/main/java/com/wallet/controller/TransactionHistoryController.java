package com.wallet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.dto.Deposit;
import com.wallet.entity.DepositEntity;
import com.wallet.service.DepositService;

@RestController
@RequestMapping(path = "/people", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TransactionHistoryController {

	@Autowired
	private DepositService depositService;

	@GetMapping
	public List<Deposit> getAll() {
		return depositService.listAll();
	}

	@PreAuthorize("hasAuthority('role_admin')")
	@GetMapping(path = "/{id}")
	public Deposit getOne(@PathVariable("id") Long id) {
		return null;//depositService.findById(id);
	}
	
	@GetMapping(path = "/{id}/members")
	public DepositEntity getHouseMembers(@PathVariable("id") Long houseId) {
		return depositService.listById(houseId);
	}

}