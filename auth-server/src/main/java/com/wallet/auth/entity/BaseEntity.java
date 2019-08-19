package com.wallet.auth.entity;

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
@MappedSuperclass
@NoArgsConstructor
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -2464094025616066125L;

	@Version
	protected Long version;

	@Column(name = "created_on")
	@CreationTimestamp
	protected LocalDateTime createdOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	protected LocalDateTime updatedOn;

}
