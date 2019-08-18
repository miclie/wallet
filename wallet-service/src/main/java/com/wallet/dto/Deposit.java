package com.wallet.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@ApiModel(description = "Represents a person within the Game of Thrones fantasy world")
@JsonInclude(Include.NON_NULL)
public class Deposit extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;

	private String transactionId;

	private BigDecimal remaining;

	private BigDecimal credit;

	protected LocalDateTime createdOn;

	protected LocalDateTime updatedOn;

	@JsonCreator
	public Deposit(@JsonProperty("username") String username, @JsonProperty("remaining") BigDecimal remaining,
			@JsonProperty("credit") BigDecimal credit, @JsonProperty("transactionId") String transactionId,
			@JsonProperty("createdOn") LocalDateTime createdOn, @JsonProperty("updatedOn") LocalDateTime updatedOn) {
		super();
		this.username = username;
		this.remaining = remaining;
		this.credit = credit;
		this.transactionId = transactionId;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
	}

	public Deposit withLink(Link link) {
		this.add(link);
		return this;
	}

	public Deposit withLink(String href, String rel) {
		this.add(new Link(href, rel));
		return this;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public BigDecimal getRemaining() {
		return remaining;
	}

	public void setRemaining(BigDecimal remaining) {
		this.remaining = remaining;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}