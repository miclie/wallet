package com.wallet.test;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.wallet.auth.service.CustomUserDetailsService;
import com.wallet.dto.Deposit;
import com.wallet.entity.AccountEntity;
import com.wallet.entity.User;
import com.wallet.exception.AccountBalanceCannotBeLessThanZeroException;
import com.wallet.exception.NoUserFoundException;
import com.wallet.exception.TransactionNumberAlreadyExists;
import com.wallet.repository.AccountEntityRepository;
import com.wallet.repository.TransactionHistoryEntityRepository;
import com.wallet.repository.UserRepository;
import com.wallet.service.AccountService;
import com.wallet.service.TransactionHistoryService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApplicationTests {

	@Mock
	private AccountEntityRepository accountEntityRepository;

	@Mock
	private TransactionHistoryEntityRepository transactionHistoryEntityRepository;

	@Mock
	private Validator validator;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CustomUserDetailsService userDetailsService;

	@InjectMocks
	private TransactionHistoryService transactionHistoryService;

	private AccountService accountService;

	String username = "test";
	BigDecimal amount = new BigDecimal(10);
	String transactionId = "1";

	private Deposit deposit;

	@Before
	public void setValues() {
		User user = createUser();
		when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

		deposit = createDeposit();

		AccountEntity account = createAccount(user);
		when(accountEntityRepository.findByUser(Matchers.any(User.class))).thenReturn(Optional.of(account));
		accountService = new AccountService(accountEntityRepository, transactionHistoryService, validator,
				userDetailsService);
	}

	@Test
	public void spendMoneyFromAccountSuccessfully()
			throws AccountNotFoundException, NoUserFoundException, TransactionNumberAlreadyExists,
			AccountBalanceCannotBeLessThanZeroException, InterruptedException, ExecutionException {

		Deposit updatedDeposit = accountService.spendMoneyFromAccount(username, amount, transactionId).get();
		Assert.assertEquals(updatedDeposit.getRemaining().compareTo(BigDecimal.ZERO), 0);
	}
	
	@Test
	public void addMoneyToAccountSuccessfully()
			throws AccountNotFoundException, NoUserFoundException, TransactionNumberAlreadyExists,
			AccountBalanceCannotBeLessThanZeroException, InterruptedException, ExecutionException {

		Deposit updatedDeposit = accountService.addMoneyToAccount(username, amount, transactionId).get();
		Assert.assertEquals(updatedDeposit.getRemaining().compareTo(new BigDecimal(20)), 0);
	}

	public User createUser() {
		User user = new User();
		user.setId(1L);
		user.setEmail("test@test.com");
		user.setEnabled(true);
		user.setAccountNonExpired(false);
		user.setAccountNonLocked(false);
		user.setCredentialsNonExpired(false);
		return user;

	}

	public AccountEntity createAccount(User user) {
		AccountEntity account = new AccountEntity();
		account.setId(5L);
		account.setUser(user);
		account.setRemaining(amount);
		return account;
	}

	public Deposit createDeposit() {
		Deposit deposit = new Deposit();
		deposit.setCredit(amount);
		deposit.setTransactionId(transactionId);
		deposit.setRemaining(amount);
		deposit.setUsername(username);
		return deposit;
	}

}
