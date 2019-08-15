package com.wallet.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
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
	@Access(AccessType.PROPERTY)
	@Column(name = "transaction_id", unique = true, columnDefinition = "VARCHAR(20)")
	private String id;

	@ManyToOne
	private User user;

	@ManyToOne
	private DepositEntity deposit;

}