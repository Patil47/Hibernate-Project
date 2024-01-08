package com.jsp.otm.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.jsp.otm.model.Account;
import com.jsp.otm.model.Bank;

public class Controller {
	static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pgsql");
	static EntityManager entityManager = entityManagerFactory.createEntityManager();
	static EntityTransaction entityTransaction = entityManager.getTransaction();

	public boolean addAccountsIntoBank(Bank bank, List<Account> accounts) {
		if (bank != null && accounts != null) {
			entityTransaction.begin();
			for (Account account : accounts) {
				entityManager.persist(account);
			}
			entityTransaction.commit();
			return true;
		}
		return false;
	}

	public boolean addBank(Bank bank) {
		if (bank != null) {
			entityTransaction.begin();
			entityManager.persist(bank);
			entityTransaction.commit();
			return true;
		}
		return false;
	}

	public Bank findBank(int bankId) {
		return entityManager.find(Bank.class, bankId);
	}

	public Account findAccount(int accountId) {
		return entityManager.find(Account.class, accountId);
	}

	public List<Bank> fetchAllBank() {
		String query = "SELECT b FROM Bank b";
		TypedQuery<Bank> typedQuery = entityManager.createQuery(query, Bank.class);
		List<Bank> banks = typedQuery.getResultList();
		return banks;
	}

	public Bank updateBank(Bank bank, byte index, Object updatedData) {
		switch (index) {
		case 1:
			String newBankName = (String) updatedData;
			bank.setBank_name(newBankName);
			break;
		case 2:
			String newlocation = (String) updatedData;
			bank.setLocation(newlocation);
			break;
		default:
			break;
		}
		entityTransaction.begin();
		Bank updatedBank = entityManager.merge(bank);
		entityTransaction.commit();
		return updatedBank;
	}
	
	public Account updateAccount(Account account,byte index,Object updatedData) {
		switch (index) {
		case 1:
			String acName = (String) updatedData;
			account.setName(acName);
			break;
		case 2:
			long account_no = (Long) updatedData;
			account.setAccount_no(account_no);
			break;
		case 3:
			double balance = (Double) updatedData;
			account.setBalance(balance);
			break;
		case 4:
			long number = (Long) updatedData;
			account.setNumber(number);
			break;
		default:
			break;
		}
		entityTransaction.begin();
		Account updatedAccount = entityManager.merge(account);
		entityTransaction.commit();
		return updatedAccount;
	}

	public boolean removeBank(int bankId) {
		Bank bank = findBank(bankId);
		if (bank != null) {
			List<Account> accounts = bank.getAccounts();
			entityTransaction.begin();
			entityManager.remove(bank);
			for (Account account : accounts) {
				entityManager.remove(account);
			}
			entityTransaction.commit();
			return true;
		}
		return false;
	}

	public boolean deletePerticularAccount(Bank bank, Account account) {
		if (account!=null) {
//			Account acToRemove = null;
			List<Account> accounts = bank.getAccounts();
			accounts.remove(account);
			bank.setAccounts(accounts);
			entityTransaction.begin();
			entityManager.remove(account);
			entityTransaction.commit();
			return true;
		} else {
			return false;
		}
		
	}
}
