package com.wallet.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.wallet.auth.entity.User;
import com.wallet.entity.TransactionHistoryEntity;

public interface TransactionHistoryEntityRepository extends JpaRepository<TransactionHistoryEntity, String> {

	@Transactional(readOnly = true)
	Optional<TransactionHistoryEntity> findById(String id);

	@Transactional(readOnly = true)
	List<TransactionHistoryEntity>  findByUser(User user);

}