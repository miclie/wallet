package com.wallet.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wallet.entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Represents a person within the Game of Thrones fantasy world")
@JsonInclude(Include.NON_NULL)
public class Deposit extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;

	private BigDecimal remaining;

	private BigDecimal credit;
	
	@JsonCreator
	public Deposit(@JsonProperty("username") String username,@JsonProperty("remaining") BigDecimal remaining, @JsonProperty("credit") BigDecimal credit) {
		super();
		this.username=username;
		this.remaining=remaining;
		this.credit=credit;
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
	
	
	
}