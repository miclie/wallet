package com.wallet.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "transactionhistory")
public class TransactionHistoryEntity extends BaseEntity<Long> {

	private static final long serialVersionUID = 6411122249171550018L;

	@Id
	private String id;

	@ManyToOne
	private User user;

	@ManyToOne
	private DepositEntity deposit;

}