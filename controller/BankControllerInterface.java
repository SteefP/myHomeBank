package controller;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import model.Category;
import model.Rule;
import model.Transaction;

public interface BankControllerInterface {

	ArrayList<Category> getCategories();
	ArrayList<Transaction> getTransactions();
	
	void saveCategories(File file);
	void loadCategoryFile(File file);
	void loadTransactionFile(File file);
	void saveState();
	void clearAll();
	void excludeTransactionFromAnalisys(Transaction t);
	
	void addCategory(Category category);
	void removeCategory(Category category);
	
	
	
	

	
	ArrayList<Transaction> searchTransactions(LocalDate date1, LocalDate date2, String category, int minAmount,
			String debcred);
	
	double categoryTotal(String category, String debcred, LocalDate date1, LocalDate date2);
	double generalTotal(String debcred, LocalDate date1, LocalDate date2);
	
	
	@Override
	String toString();
	;
	
	/*
	 * Below methods are not used anymore still in place to let the old java swing GUI function 
	 */
	
	
	// To enable JFreechart data sets, not used in JavaFX GUI
		DefaultPieDataset createPieDataset(LocalDate date1, LocalDate date2, String debCred);
		DefaultCategoryDataset createMontlyDataset(LocalDate date, String label);
		DefaultCategoryDataset createMontlyDataset(LocalDate date1, LocalDate date2, String debCred);
		void createMontlyReport(LocalDate date1, LocalDate date2, String debCred, File file);
		
		// Used in old model, now rule making and assigning category are separated, not removed so the swing model still works
		void catogeriseTransaction(Transaction t, String category, String keyword);
		void catogeriseTransactionAccount(Transaction t, String category, String account);
		
		// Used in old model
		float generalTotal(String debCred);
		float categoryTotal(String categoryName, String debCred);
	
		ArrayList<Transaction> searchTransactions(LocalDate date1, LocalDate date2, String Category, int minAmount);
		void checkTransactionsAgainstKeyWords(String categoryName);
		void checkTransactionsAgainstCounterAccounts(String categoryName);
		void checkTransactionsAgianstRules(String categoryName);
		double subCategoryTotal(String subCategory, LocalDate date1, LocalDate date2);
		void addRuleToCategory(String debCred, String category, Rule rule);
		

		void addKeyWordToCategory(String category, String keyword);
		void removeKeyWordFromCategory(String category, String keyword);
		void addAccountToCategory(String category, String account);
		void removeAccountFromCategory(String category, String account);
	
}