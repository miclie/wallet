package com.wallet.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import com.wallet.auth.entity.User;
import com.wallet.entity.DepositEntity;
import com.wallet.entity.TransactionHistoryEntity;

public interface DepositEntityRepository extends JpaRepository<DepositEntity, Long> {

	@Transactional(readOnly = true)
	@Query("select e from DepositEntity e")
	Stream<DepositEntity> streamAll();

	@Transactional(readOnly = true)
	boolean existsByName(String name);
	
	@Transactional(readOnly = true)
	Optional<DepositEntity> findById(Long houseId);

	@Transactional(readOnly = true)
	Optional<DepositEntity> findByUser(UserDetails user);

}