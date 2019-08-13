package com.wallet.dto;

import java.io.Serializable;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wallet.entity.DepositEntity;
import com.wallet.entity.TransactionHistoryEntity;
import com.wallet.entity.User;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Represents a a family house within the Game of Thrones fantasy world")
@JsonInclude(Include.NON_NULL)
public class Transaction extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private UserDto user;

	private Deposit deposit;

	@JsonCreator
	public Transaction(TransactionHistoryEntity entity) {
		super();
		this.id = entity.getId();
		this.user = new UserDto(entity.getUser());
		this.deposit.setCredit(entity.getDeposit().getCredit());
		this.deposit.setRemaining(entity.getDeposit().getRemaining());

	}

	public Transaction withLink(Link link) {
		this.add(link);
		return this;
	}

	public Transaction withLink(String href, String rel) {
		add(new Link(href, rel));
		return this;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public Deposit getDeposit() {
		return deposit;
	}

	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
	}
}