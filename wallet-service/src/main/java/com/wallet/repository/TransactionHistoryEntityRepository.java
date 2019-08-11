package com.wallet.repository;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.wallet.entity.TransactionHistoryEntity;

public interface TransactionHistoryEntityRepository extends JpaRepository<TransactionHistoryEntity, Long> {

	@Transactional(readOnly = true)
	List<TransactionHistoryEntity> findByHouseId(Long houseId);

	@Transactional(readOnly = true)
	@Query("select e from PersonEntity e")
	Stream<TransactionHistoryEntity> streamAll();

	@Transactional(readOnly = true)
	boolean existsByName(String name);

}