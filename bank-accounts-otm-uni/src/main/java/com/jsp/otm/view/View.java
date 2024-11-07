package com.jsp.otm.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityExistsException;

import com.jsp.otm.controller.Controller;
import com.jsp.otm.model.Account;
import com.jsp.otm.model.Bank;

public class View {

	// Main class representing the console-based view
	public static void main(String[] args) {
		System.out.println("Welcome to Bank Management");
		// Scanner for user input
		Scanner myInput = new Scanner(System.in);
		// Controller instance for handling business logic
		Controller controller = new Controller();
		do {
			// Display menu options to the user
			System.out.println("Bank Management System Menu: ");
			System.out.println(
					"1.Add Bank and Accounts\n2.Add Accounts into Bank\n3.Show Bank and Accounts details\n4.Update Bank and Accounts details\n5.Delete Bank and Accounts\n6.Delete A/C from Bank\n0.Exit");
			System.out.print("Enter values of desired option : ");
			int userInput = 10;
			try {
				// Take user input for the selected option
				userInput = myInput.nextInt();
				myInput.nextLine();
			}
			catch (Exception e) {
				// Handle invalid input
				System.out.println("Invalid Input");
				myInput.nextLine();
				continue;
			}
			// Perform actions based on user input
			switch (userInput) {
			case 0:
				// Exit the program
				myInput.close();
				System.out.println("----Exited----");
				System.exit(0);
				break;
			case 1:
				// Add a new bank and associated accounts
				Bank bank = new Bank();
				String bankName;
				try {
					// Take input for bank details 
					System.out.print("Enter Bank id : ");
					bank.setId(myInput.nextInt());
					myInput.nextLine();
					System.out.print("Enter Bank name : ");
					bankName = myInput.nextLine();
					bank.setBank_name(bankName);
					System.out.print("Enter Bank Location : ");
					bank.setLocation(myInput.nextLine());
				} catch (InputMismatchException e) {
					// Handle invalid input for bank details
					System.out.println("Please enter Valid Data");
					myInput.nextLine();
					continue;
				}
				boolean verifyBank = false;
				try {
					// Add the bank to the database
					verifyBank = controller.addBank(bank);
				} catch (Exception e) {
					// Handle if bank with the same id already exists
					System.out.println("Bank with given id is already exits");
					continue;
				}
				if (verifyBank) {
					// If the bank is added, proceed to add accounts
					System.out.println("Bank Added");
					ArrayList<Account> accounts = new ArrayList<Account>();
					System.out.println("To add Bank account in " + bankName + " bank press 1 else 0");
					byte input;
					try {
						input = myInput.nextByte();
						myInput.nextLine();
					} catch (InputMismatchException e) {
						// Handle invalid input for adding accounts
						System.out.println("Please enter valid option.");
						myInput.nextLine();
						continue;
					}
					// Loop to add multiple accounts
					while (input == 1) {
						Account account = new Account();
						try {
							// Take input for account details
							System.out.println("Enter Accounts Details : ");
							System.out.print("Enter Account id : ");
							int account_id=myInput.nextInt();
							myInput.nextLine();
							int bank_id = bank.getId()*100;
							if (account_id>=1 && account_id<=9) {
								String twoDigitNumber = String.format("%02d", account_id);
								account_id=Integer.parseInt(twoDigitNumber);
								account_id=bank_id+account_id;								
							} else {
								account_id=bank_id+account_id;
							}
							account.setId(account_id);
							System.out.print("Enter Account name : ");
							account.setName(myInput.nextLine());
							System.out.print("Enter Account no : ");
							account.setAccount_no(myInput.nextLong());
							myInput.nextLine();
							System.out.print("Enter Account balance : ");
							account.setBalance(myInput.nextDouble());
							myInput.nextLine();
							System.out.print("Enter Mo. number : ");
							account.setNumber(myInput.nextLong());
							myInput.nextLine();
							accounts.add(account);
							System.out.println("Do you want to add more account in this bank press 1 else 0");
							input = myInput.nextByte();
							myInput.nextLine();
						} catch (InputMismatchException e) {
							// Handle invalid input for account details
							System.out.println("Please enter Valid Data");
							myInput.nextLine();
							continue;
						}
					}
					bank.setAccounts(accounts);
					boolean addAccountsIntoBank;
					try {
						// Add the accounts to the bank
						addAccountsIntoBank = controller.addAccountsIntoBank(bank, accounts);
					} catch (EntityExistsException e) {
						// Handle if an account with the same id already exists
						System.out.println("Account with given id is already exits");
						continue;
					}
					if (addAccountsIntoBank) {
						System.out.println("Account Added");
					} else {
						System.out.println("Account not added");
					}

				} else {
					System.out.println("Bank was not Added");
				}

				break;
			case 2:
				// Add accounts to an existing bank
				List<Bank> allBanks = controller.fetchAllBank();
				if (allBanks != null) {
					System.out.println("Select Bank to add an account : ");
					for (Bank bank2 : allBanks) {
						System.out.println("Bank id : " + bank2.getId() + "   Bank name : " + bank2.getBank_name());
					}

				} else {
					System.out.println("No banks found");
					break;
				}
				System.out.print("To add an A/C Select Bank id : ");
				int selectedBankId;
				try {
					selectedBankId = myInput.nextInt();
					myInput.nextLine();
				} catch (InputMismatchException e) {
					System.out.println("Please enter Valid Data");
					myInput.nextLine();
					continue;
				}
				Bank selectedBank = controller.findBank(selectedBankId);
				if (selectedBank != null) {
					List<Account> accounts2 = selectedBank.getAccounts();
					System.out.println("Add Bank A/C in " + selectedBank.getBank_name() + " Bank");
					byte input = 1;
					while (input == 1) {
						Account account = new Account();
						try {
							// Take input for account details
							System.out.println("Enter Accounts Details : ");
							System.out.print("Enter Account id : ");
							int account_id= myInput.nextInt();
							myInput.nextLine();
							int bank_id = selectedBank.getId()*100;
							if (account_id>=1 && account_id<=9) {
								String twoDigitNumber = String.format("%02d", account_id);
								account_id=Integer.parseInt(twoDigitNumber);
								account_id=bank_id+account_id;
							} else {
								account_id=bank_id+account_id;
							}
							account.setId(account_id);
							System.out.print("Enter Account name : ");
							account.setName(myInput.nextLine());
							System.out.print("Enter Account no : ");
							account.setAccount_no(myInput.nextLong());
							myInput.nextLine();
							System.out.print("Enter Account balance : ");
							account.setBalance(myInput.nextDouble());
							myInput.nextLine();
							System.out.print("Enter Mo. number : ");
							account.setNumber(myInput.nextLong());
							myInput.nextLine();
							accounts2.add(account);
							System.out.println("Do you want to add more account in this bank press 1 else 0");
							input = myInput.nextByte();
							myInput.nextLine();
						} catch (InputMismatchException e) {
							// Handle invalid input for account details
							System.out.println("Please enter Valid Data");
							myInput.nextLine();
							continue;
						}
					}
					selectedBank.setAccounts(accounts2);
					boolean verifyAccount;
					try {
						// Add the accounts to the bank
						verifyAccount = controller.addAccountsIntoBank(selectedBank, accounts2);
					} catch (Exception e) {
						// Handle if an account with the same id already exists
						System.out.println("Account with given id is already exits");
						break;
					}
					if (verifyAccount) {
						System.out.println("Account Added");
					} else {
						System.out.println("Account not added");
					}
				} else {
					System.out.println("No banks found");
				}
				break;
			case 3:
				// Display bank and account details
				List<Bank> allBanksForDetails = controller.fetchAllBank();
				if (allBanksForDetails != null) {
					System.out.println("Select Bank to see an account details : ");
					for (Bank bank3 : allBanksForDetails) {
						System.out.println("Bank id : " + bank3.getId() + "   Bank name : " + bank3.getBank_name());
					}
				} else {
					System.out.println("No banks found");
					break;
				}
				System.out.print("Enter Bank id : ");
				int selectedBankIdForDetails = 0;
				try {
					selectedBankIdForDetails = myInput.nextInt();
					myInput.nextLine();
				} catch (InputMismatchException e) {
					System.out.println("Please enter Valid Data");
					myInput.nextLine();
					continue;
				}
				Bank foundBank = controller.findBank(selectedBankIdForDetails);
				if (foundBank != null) {
					System.out.println(foundBank.getBank_name() + " Bank Deatils : ");
					System.out.println("Bank id : " + foundBank.getId());
					System.out.println("Bank Name : " + foundBank.getBank_name());
					System.out.println("Bank Location : " + foundBank.getLocation());
					List<Account> bankAccountsForDetails = foundBank.getAccounts();
					if (!bankAccountsForDetails.isEmpty()) {
						System.out.print(
								"do you want to see Accounts in " + foundBank.getBank_name() + " Bank press 1 else 0 : ");
						byte acInput = 0;
						try {
							acInput = myInput.nextByte();
							myInput.nextLine();
						} catch (InputMismatchException e) {
							System.out.println("Please enter Valid Data");
							myInput.nextLine();
							break;
						}
						while (acInput == 1) {
							System.out.println("Select Account id to see account details : ");
							for (Account account : bankAccountsForDetails) {
								System.out.println(
										"A/C id : " + account.getId() + "   A/C holder name : " + account.getName());
							}
							System.out.print("Enter A/C id : ");
							int accountIdForDetails;
							try {
								accountIdForDetails = myInput.nextInt();
								myInput.nextLine();
							} catch (InputMismatchException e) {
								System.out.println("Please enter Valid Data");
								myInput.nextLine();
								continue;
							}
							Account accountForDetails = controller.findAccount(accountIdForDetails);
							if (accountForDetails != null) {
								System.out.println("Accounts Details : ");
								System.out.println("Account id : " + accountForDetails.getId());
								System.out.println("Account holder name : " + accountForDetails.getName());
								System.out.println("Account no : " + accountForDetails.getAccount_no());
								System.out.println("Account mo. number : " + accountForDetails.getNumber());
								System.out.println("Account Balance : " + accountForDetails.getBalance());
							} else {
								System.out.println("Account does not exist in " + foundBank.getBank_name() + " Bank");
							}
							System.out.print("do you want to see another A/C details press 1 else 0 : ");
							try {
								acInput = myInput.nextByte();
								myInput.nextLine();
							} catch (InputMismatchException e) {
								System.out.println("Please enter Valid Data");
								myInput.nextLine();
								break;
							}
						}

					} else {
						System.out.println(foundBank.getBank_name() + " doesn't have any account");
					}
				} else {
					System.out.println("No banks found");
				}
				break;
			case 4:
				// Update bank and account details
				List<Bank> allBanksUpdate = controller.fetchAllBank();
				if (allBanksUpdate != null) {
					System.out.println("Select Bank to update details : ");
					for (Bank bankUpdate : allBanksUpdate) {
						System.out.println("Bank id : " + bankUpdate.getId() + "   Bank name : " + bankUpdate.getBank_name());
					}

				} else {
					System.out.println("No banks found");
					break;
				}
				System.out.print("Enter Bank id : ");
				int bankIdUpdate = 0;
				try {
					bankIdUpdate = myInput.nextInt();
					myInput.nextLine();
				} catch (InputMismatchException e) {
					System.out.println("Please enter Valid Data");
					myInput.nextLine();
					continue;
				}
				Bank bankUpdate = controller.findBank(bankIdUpdate);
				byte updateInput = 0;
				if (bankUpdate != null) {
					do {
						// Display options for updating bank and account details
						System.out.println("What do you want to change : ");
						System.out.println(
								"1.Change Bank name \n2.Change Bank Location\n3.Do you want to change account details of "
										+ bankUpdate.getBank_name() + " Bank\n0.Exit");
						System.out.print("Enter values of desired option : ");
						try {
							updateInput = myInput.nextByte();
							myInput.nextLine();
						} catch (InputMismatchException e) {
							System.out.println("Please enter Valid Data");
							myInput.nextLine();
							continue;
						}
						switch (updateInput) {
						case 1:
							// Update bank name
							System.out.print("Enter New Bank Name : ");
							String name = myInput.nextLine();
							if (controller.updateBank(bankUpdate, updateInput, name) != null) {
								System.out.println("Bank Name successfully Updated");
							} else {
								System.out.println("Bank Name not Updated");
							}
							break;
						case 2:
							// Update bank location
							System.out.print("Enter New Bank Location : ");
							String location = myInput.nextLine();
							if (controller.updateBank(bankUpdate, updateInput, location) != null) {
								System.out.println("Bank location successfully Updated");
							} else {
								System.out.println("Bank location not Updated");
							}
							break;
						case 3:
							// Update account details
							List<Account> accountsUpdate = bankUpdate.getAccounts();
							if (accountsUpdate != null) {
								System.out.println("Select Account id to update account details : ");
								for (Account accountUpdate : accountsUpdate) {
									System.out.println("A/C id : " + accountUpdate.getId() + "   A/C holder name : "
											+ accountUpdate.getName());
								}
								// User input for account id
								System.out.print("Enter A/C id : ");
								int acIdUpdate = 0;
								try {
									acIdUpdate = myInput.nextInt();
									myInput.nextLine();
								} catch (InputMismatchException e) {
									System.out.println("Please enter Valid Data");
									myInput.nextLine();
									continue;
								}
								// Fetch the selected account
								Account accountUpdate = controller.findAccount(acIdUpdate);
								byte acInputUpdate = 0;
								if (accountUpdate != null) {
									// Menu for updating account details
									do {
										System.out.println("What do you want to change in A/C : ");
										System.out.println(
												"1.Change A/C name \n2.Change A/C no\n3.Change A/C balance\n4.Change Mo. number\n0.Exit");
										System.out.print("Enter values of desired option : ");
										try {
											acInputUpdate = myInput.nextByte();
											myInput.nextLine();
										} catch (InputMismatchException e) {
											System.out.println("Please enter Valid Data");
											myInput.nextLine();
											continue;
										}
										switch (acInputUpdate) {
										case 1:
											// Update account name
											System.out.print("Enter New A/C Name : ");
											String acNameUpdate = myInput.nextLine();
											if (controller.updateAccount(accountUpdate, acInputUpdate, acNameUpdate) != null) {
												System.out.println("A/C Name successfully Updated");
											} else {
												System.out.println("A/C Name not Updated");
											}
											break;
										case 2:
											// Update account number
											System.out.print("Enter New A/C no. : ");
											long accountNoUpdate = 0;
											try {
												accountNoUpdate = myInput.nextLong();
												myInput.nextLine();
											} catch (InputMismatchException e) {
												System.out.println("Please enter Valid Data");
												myInput.nextLine();
												continue;
											}
											if (controller.updateAccount(accountUpdate, acInputUpdate, accountNoUpdate) != null) {
												System.out.println("A/C no. successfully Updated");
											} else {
												System.out.println("A/C no. not Updated");
											}
											break;
										case 3:
											// Update account balance
											System.out.print("Enter updated A/C Balance : ");
											double balanceUpdate = 0;
											try {
												balanceUpdate = myInput.nextDouble();
												myInput.nextLine();
											} catch (InputMismatchException e) {
												System.out.println("Please enter Valid Data");
												myInput.nextLine();
												continue;											}
											if (controller.updateAccount(accountUpdate, acInputUpdate, balanceUpdate) != null) {
												System.out.println("A/C Balance successfully Updated");
											} else {
												System.out.println("A/C Balance not Updated");
											}
											break;
										case 4:
											 // Update account mobile number
											System.out.print("Enter updated Mo. number : ");
											long numberUpdate = 0;
											try {
												numberUpdate = myInput.nextLong();
												myInput.nextLine();
											} catch (InputMismatchException e) {
												System.out.println("Please enter Valid Data");
												myInput.nextLine();
												continue;											}
											if (controller.updateAccount(accountUpdate, acInputUpdate, numberUpdate) != null) {
												System.out.println("Mo. number successfully Updated");
											} else {
												System.out.println("Mo. number not Updated");
											}
											break;

										default:
											System.out.println("Please enter Valid Option.");
											break;
										}

									} while (acInputUpdate != 0);
								} else {
									System.out.println("Account does not exist in " + bankUpdate.getBank_name() + " Bank");
								}

							} else {
								System.out.println(bankUpdate.getBank_name() + " doesn't have any account");
							}
							break;

						default:
							System.out.println("Please enter Valid Option.");
							break;
						}
					} while (updateInput != 0);
				} else {
					System.out.println("Bank with given id is does not exits");
				}
				break;
			case 5:
				// Remove Bank and Accounts
				List<Bank> allBanksRemove = controller.fetchAllBank();
				if (allBanksRemove != null) {
					System.out.println("Select Bank to delete an account from Bank : ");
					for (Bank bankRemove : allBanksRemove) {
						System.out.println("Bank id : " + bankRemove.getId() + "   Bank name : " + bankRemove.getBank_name());
					}
				} else {
					System.out.println("No banks found");
					break;
				}
				// User input for bank id
				System.out.print("Enter Bank id to Remove Bank and Accounts : ");
				int bankIdRemove = 0;
				try {
					bankIdRemove = myInput.nextInt();
					myInput.nextLine();
				} catch (InputMismatchException e) {
					System.out.println("Please enter Valid Data");
					myInput.nextLine();
					continue;
				}
				// Remove the selected bank and its accounts
				if (controller.removeBank(bankIdRemove)) {
					System.out.println("Removed Bank and Accounts");
				} else {
					System.out.println("Not Removed");
				}
				break;
			case 6:
				// Delete an Account from Bank
				List<Bank> allBanksDeleteAccount = controller.fetchAllBank();
				if (allBanksDeleteAccount != null) {
					System.out.println("Select Bank to delete an account from Bank : ");
					for (Bank bankDeleteAccount : allBanksDeleteAccount) {
						System.out.println("Bank id : " + bankDeleteAccount.getId() + "   Bank name : " + bankDeleteAccount.getBank_name());
					}
				} else {
					System.out.println("No banks found");
					break;
				}
				// User input for bank id
				System.out.print("Enter Bank id : ");
				int bankIdDeleteAccount = 0;
				try {
					bankIdDeleteAccount = myInput.nextInt();
					myInput.nextLine();
				} catch (InputMismatchException e) {
					System.out.println("Please enter Valid Data");
					myInput.nextLine();
					continue;
				}
				// Fetch the selected bank
				Bank bankDeleteAccount = controller.findBank(bankIdDeleteAccount);
				if (bankDeleteAccount != null) {
					List<Account> accountsDeleteAccount = bankDeleteAccount.getAccounts();
					if (accountsDeleteAccount != null) {
						System.out.println("List of A/C available in " + bankDeleteAccount.getBank_name() + " Bank : ");
						byte acInputDeleteAccount = 1;
						while (acInputDeleteAccount == 1) {
							for (Account account : accountsDeleteAccount) {
								System.out.println(
										"A/C id : " + account.getId() + "   A/C holder name : " + account.getName());
							}
							System.out.print("Enter A/C id : ");
							int acIdRemoveAccount = 0;
							try {
								acIdRemoveAccount = myInput.nextInt();
								myInput.nextLine();
							} catch (InputMismatchException e) {
								System.out.println("Please enter Valid Data");
								break;
							}
							Account account = controller.findAccount(acIdRemoveAccount);
							if (account != null) {
								if (controller.deletePerticularAccount(bankDeleteAccount, account)) {
									System.out.println("A/C deleted");
								} else {
									System.out.println("A/C not deleted");
								}
							} else {
								System.out.println("Account does not exist in " + bankDeleteAccount.getBank_name() + " Bank");
							}
							System.out.print("do you want to delete more A/C from Bank press 1 else 0 : ");
							try {
								acInputDeleteAccount = myInput.nextByte();
								myInput.nextLine();
							} catch (InputMismatchException e) {
								System.out.println("Please enter Valid Data");
								myInput.nextLine();
								continue;
							}
						}
					} else {
						System.out.println(bankDeleteAccount.getBank_name() + " doesn't have any account");
					}
				} else {
					System.out.println("No banks found");
				}
				break;
			default:
				System.out.println("Please enter Valid Option.");
				break;
			}
		} while (true);
	}
}
