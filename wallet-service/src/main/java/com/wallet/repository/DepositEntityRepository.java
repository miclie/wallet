package com.wallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.wallet.entity.DepositEntity;
import com.wallet.entity.User;

public interface DepositEntityRepository extends JpaRepository<DepositEntity, Long> {

	
	@Transactional(readOnly = true)
	Optional<DepositEntity> findById(Long id);

	@Transactional(readOnly = true)
	Optional<DepositEntity> findByUser(User user);

}