package com.wallet.repository;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.wallet.entity.DepositEntity;
import com.wallet.entity.TransactionHistoryEntity;

public interface DepositEntityRepository extends JpaRepository<DepositEntity, Long> {

	@Transactional(readOnly = true)
	@Query("select e from HouseEntity e")
	Stream<DepositEntity> streamAll();

	@Transactional(readOnly = true)
	boolean existsByName(String name);
	
	@Transactional(readOnly = true)
	List<DepositEntity> findByHouseId(Long houseId);

}