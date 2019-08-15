package com.wallet.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.wallet.dto.Deposit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "account")
public class AccountEntity extends BaseEntity<Long> {

	private static final long serialVersionUID = 7731294694456088735L;

	@Id
	@Access(AccessType.PROPERTY)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private User user;

	private BigDecimal remaining;

	private BigDecimal credit;

	public AccountEntity(User user,Deposit dto) {
		super();
		this.user = user;
		this.remaining = dto.getRemaining();
		this.credit = dto.getCredit();
	}

}