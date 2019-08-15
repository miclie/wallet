package com.wallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.wallet.entity.AccountEntity;
import com.wallet.entity.User;

public interface AccountEntityRepository extends JpaRepository<AccountEntity, Long> {

	@Transactional(readOnly = true)
	Optional<AccountEntity> findById(Long id);

	@Transactional(readOnly = true)
	Optional<AccountEntity> findByUser(User user);

}