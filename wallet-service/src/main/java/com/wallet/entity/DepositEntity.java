package com.wallet.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.wallet.auth.entity.User;
import com.wallet.dto.Deposit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "deposit")
public class DepositEntity extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@Access(AccessType.PROPERTY)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private User user;

	private BigDecimal remaining;
	
	private BigDecimal credit;

	
	public DepositEntity(Deposit dto) {
		super();
		//this.name = dto.getName();
	}

}