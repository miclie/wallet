package com.wallet.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Version
	protected Long version;

	@Column(name = "created_on")
	@CreationTimestamp
	protected LocalDateTime createdOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	protected LocalDateTime updatedOn;
	


}