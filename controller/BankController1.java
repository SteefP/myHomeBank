package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import model.*;

public class BankController1 implements BankControllerInterface {

	BankModelInterface bankModel;

	public BankController1(BankModelInterface bankModel) {
		this.bankModel = bankModel;
	}

	@Override
	public void loadTransactionFile(File file) {
		bankModel.loadTransactionFile(file);
	}

	@Override
	public void loadCategoryFile(File file) {
		bankModel.loadCategoryFile(file);

	}
	@Override
	public void saveState() {
		bankModel.saveState();

	}
	@Override
	public void excludeTransactionFromAnalisys(Transaction t){
		bankModel.getTransactions().remove(t);
		bankModel.getExcludedTransactions().add(t);

	}

	@Override
	public void addCategory(Category category){
		bankModel.getCategories().add(category);
	}
	@Override
	public void removeCategory(Category category){
		bankModel.getCategories().remove(category);
	}

	
	@Override
	public ArrayList<Category> getCategories() {
		return bankModel.getCategories();
	}

	@Override
	public ArrayList<Transaction> getTransactions() {
		return bankModel.getTransactions();
	}

	@Override  // Data are INCLUDING the first date (day), EXLUDING the last date (day), so you can put 1-5-2016, 1-6-2016 and get one month including the first of the month 
	public double categoryTotal(String category, String debcred, LocalDate date1, LocalDate date2){
		Category tempCat = null;     // for flexibilty reasons this methods takes a String value for the category, but for checking subcategories the real category is needed
		for(Category c: bankModel.getCategories()){
			if(c.getName().equals(category)){
				tempCat= c;
			}
		}
		double total = 0;
		for(Transaction t: bankModel.getTransactions()){
			if(date1.isEqual(t.getTransactionDate()) || date1.isBefore(t.getTransactionDate()) && date2.isAfter(t.getTransactionDate()))
				if(t.getDebetOrCredit().equals(debcred)){  
					if(t.getCategory().getName().equals(category)){
						total += t.getAmount();
					}
					for(SubCategory sc : tempCat.getSubCategories()){
						if(t.getCategory().getName().equals(sc.getName())){
							total += t.getAmount();
						}
					}
				}
		}
		return total;
	}
	@Override  
	public double subCategoryTotal(String subCategory, LocalDate date1, LocalDate date2){
		double total = 0;
		for(Transaction t: bankModel.getTransactions()){
			if(date1.isEqual(t.getTransactionDate()) || date1.isBefore(t.getTransactionDate()) && date2.isAfter(t.getTransactionDate()))
				if(t.getCategory().getName().equals(subCategory)){
					total += t.getAmount();
					if(t.getCategory().getName().equals("Gas & elektriciteit")){
						System.out.println("Subcategory "+t.getCategory().getName()+ " added amount of " + t.getAmount() +" dd "+ t.getTransactionDate() );
					};
				}
		}
		return total;
	}

	@Override  
	public double generalTotal(String debcred, LocalDate date1, LocalDate date2){
		double total = 0;
		for(Transaction t: bankModel.getTransactions()){
			if(date1.isEqual(t.getTransactionDate()) || date1.isBefore(t.getTransactionDate()) && date2.isAfter(t.getTransactionDate())){
				if(t.getDebetOrCredit().equals(debcred)){
						total += t.getAmount();
				}
			}
		}
		return total;
	}

	@Override
	public ArrayList<Transaction> searchTransactions(LocalDate date1, LocalDate date2, String category, int minAmount) {
		ArrayList<Transaction> transactionsToBeCategorised = new ArrayList<Transaction>();

		// Creates a local ArrayList temp to list the transactions that match the criteria, and reverses the ordering to biggest amount on index 0 etc.
		int size = bankModel.getTransactions().size();
		for(int i = 1; i < size-1; i++){
			Transaction t = bankModel.getTransactions().get(size-i);
			LocalDate d = bankModel.getTransactions().get(size-i).getTransactionDate();


			if(date1.isEqual(d) || date1.isBefore(d) && date2.isAfter(d))
				if(t.getAmount()> minAmount)
					if(t.getCategory().getName().equals(category)) { //If criteria match transaction t is added to the list to be categorized 
						transactionsToBeCategorised.add(t);          
					}
		}
		return transactionsToBeCategorised;
	}

	@Override
	public ArrayList<Transaction> searchTransactions(LocalDate date1, LocalDate date2, String category, int minAmount, String debcred) {
		ArrayList<Transaction> transactionsToBeCategorised = new ArrayList<Transaction>();

		int size = bankModel.getTransactions().size();
		for(int i = 1; i < size-1; i++){
			Transaction t = bankModel.getTransactions().get(size-i);
			LocalDate d = bankModel.getTransactions().get(size-i).getTransactionDate();

			if(date1.isEqual(d) || date1.isBefore(d) && date2.isAfter(d)){
				if(t.getAmount()> minAmount){
					if(t.getDebetOrCredit().equals(debcred)){
						try {
							
							if(t.getCategory().getName().equals(category)) { 
								transactionsToBeCategorised.add(t);          
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		return transactionsToBeCategorised;
	}


	
	@Override
	public void addRuleToCategory(String debCred, String category, Rule rule) {

		for(Category c : bankModel.getCategories()){
			if(c.getDebetOrCredit().equals(debCred)){
				String tempname = c.getName();
				if(tempname.equalsIgnoreCase(category)){
					c.getRules().add(rule);
				}
			}
		}
		System.out.println("Made a new rule");
		checkTransactionsAgianstRules("not yet assigned");
	}
	
	// Currently not used
	@Override
	public void addKeyWordToCategory(String category, String keyword){
		for(Category c : bankModel.getCategories()){
				String tempname = c.getName();
				if(tempname.equalsIgnoreCase(category)){
					c.getKeyWords().add(keyword);
				}
		}
	}
	@Override
	public void removeKeyWordFromCategory(String category, String keyword){
		for(Category c : bankModel.getCategories()){
			String tempname = c.getName();
			if(tempname.equalsIgnoreCase(category)){
				c.getKeyWords().remove(keyword);
			}
		}
	}

	// Currently not used
	@Override
	public void addAccountToCategory(String category, String account){
		for(Category c : bankModel.getCategories()){
		
				String tempname = c.getName();
				if(tempname.equalsIgnoreCase(category)){
					c.getCounterAccounts().add(account);
				}

		}
	}
	@Override
	public void removeAccountFromCategory(String category, String account){
		for(Category c : bankModel.getCategories()){
		
				String tempname = c.getName();
				if(tempname.equalsIgnoreCase(category)){
					c.getCounterAccounts().remove(account);
				}

		}
	}

	
	

	@Override
	public 	void checkTransactionsAgainstKeyWords(String categoryName){
		//Checks against keyword and set a category
		for(Transaction t: bankModel.getTransactions()){
			if(t.getCategory().getName().equals(categoryName)){
				for(Category category : bankModel.getCategories()){
					for(String s : category.getKeyWords()){
						if(t.getDescription().contains(s)){
							t.setCategory(category);
						}
					}
					for(SubCategory sb : category.getSubCategories()){
						for(String s : sb.getKeyWords()){
							if(t.getDescription().contains(s)){
								t.setCategory(sb);
								System.out.println("Transaction :" + " added to catgory " +sb);
							}
						}
					}
				}
			}
		}
	}
	@Override
	public void checkTransactionsAgainstCounterAccounts(String categoryName){
		for(Transaction t: bankModel.getTransactions()){
			if (t.getCategory().getName().equals(categoryName)) {
				for (Category category : bankModel.getCategories()) {
					for (String a : category.getCounterAccounts()) {
						if (t.getOtherEndOfTransactionName().equals(a)) {
							t.setCategory(category);
						}
					}
					for (SubCategory sb : category.getSubCategories()) {
						for (String a : sb.getCounterAccounts()) {
							if (t.getOtherEndOfTransactionName().equals(a)) {
								t.setCategory(sb);
							}
						}
					}
				} 
			}
		}
	}
	

	
	@Override
	public void checkTransactionsAgianstRules(String categoryName){
		for(Transaction t: bankModel.getTransactions()){
			if (t.getCategory().getName().equals(categoryName)) {
				for (Category category : bankModel.getCategories()) {
					if (!category.getRules().isEmpty()) {
						for (Rule r : category.getRules()) {
							Boolean keyword = false;
							Boolean minAmount = false;
							Boolean maxAmount = false;
							Boolean startDate = false;
							Boolean endDate = false;
							Boolean counterAccount = false;
							Boolean counterAcccountDiscription = false;

							if (r.getKeyword() == null) {
								keyword = true;
							} else if (t.getDescription().contains(r.getKeyword())) {
								keyword = true;
							}
							if (r.getMinAmount() < t.getAmount()) {
								minAmount = true;
							}
							if ((r.getMaxAmount() == 0)) {
								maxAmount = true;
							} else if (r.getMaxAmount() > t.getAmount()) {
								maxAmount = true;
							}
							if (r.getStartDate() == null) {
								startDate = true;
							} else if (r.getStartDate().isBefore(t.getTransactionDate())) {
								startDate = true;
							}
							if (r.getEndDate() == null) {
								endDate = true;
							} else if (r.getEndDate().isAfter(t.getTransactionDate())) {
								endDate = true;
							}
							if (r.getCounterAccount() == null) {
								counterAccount = true;
							} else if (r.getCounterAccount().equals(t.getOtherEndOfTransactionAccount())) {
								counterAccount = true;
							}
							if (r.getCounterAcccountDiscription() == null) {
								counterAcccountDiscription = true;
							} else if (r.getCounterAcccountDiscription()
									.equalsIgnoreCase(t.getOtherEndOfTransactionName())) {
								counterAcccountDiscription = true;
							}

							if (keyword & minAmount & maxAmount & startDate & endDate & counterAccount
									& counterAcccountDiscription) {
								t.setCategory(category);
								System.out.println("transaction: " + t + "is categorized as " + category);
							}
						} // close for rule
					} // close if cat not empty
					for (SubCategory sc : category.getSubCategories()){
						if (!sc.getRules().isEmpty()){
								for (Rule r : sc.getRules()) {
								Boolean keyword = false;
								Boolean minAmount = false;
								Boolean maxAmount = false;
								Boolean startDate = false;
								Boolean endDate = false;
								Boolean counterAccount = false;
								Boolean counterAcccountDiscription = false;

								if (r.getKeyword() == null) {
									keyword = true;
								} else if (t.getDescription().contains(r.getKeyword())) {
									keyword = true;
								}
								if (r.getMinAmount() < t.getAmount()) {
									minAmount = true;
								}
								if ((r.getMaxAmount() == 0)) {
									maxAmount = true;
								} else if (r.getMaxAmount() > t.getAmount()) {
									maxAmount = true;
								}
								if (r.getStartDate() == null) {
									startDate = true;
								} else if (r.getStartDate().isBefore(t.getTransactionDate())) {
									startDate = true;
								}
								if (r.getEndDate() == null) {
									endDate = true;
								} else if (r.getEndDate().isAfter(t.getTransactionDate())) {
									endDate = true;
								}
								if (r.getCounterAccount() == null) {
									counterAccount = true;
								} else if (r.getCounterAccount().equals(t.getOtherEndOfTransactionAccount())) {
									counterAccount = true;
								}
								if (r.getCounterAcccountDiscription() == null) {
									counterAcccountDiscription = true;
								} else if (r.getCounterAcccountDiscription()
										.equalsIgnoreCase(t.getOtherEndOfTransactionName())) {
									counterAcccountDiscription = true;
								}
								if (keyword & minAmount & maxAmount & startDate & endDate & counterAccount
										& counterAcccountDiscription) {
									t.setCategory(sc);
									System.out.println("transaction: " + t + "is categorized as " + sc);
								}
							}  // close for rule
						} // close if subcategory
					} //close for sub category
				} // close for category
			} // close if category
		} //close for transaction
	} //close method


	@Override 
	// This function is misplaced shoul be in model, of al the things that dont have to be in the model should be in the controller
	public void saveCategories(File file){
		try{
			FileWriter writer = new FileWriter(file);
			for(Category c : bankModel.getCategories()){
				ArrayList<String> tempDisc = c.getKeyWords();
				System.out.println(c);
				writer.write(c.getdebetOrCredit()+ "; ");
				writer.write(c.getFixedOrVariable()+ "; ");
				writer.write(c.getName()+ "; ");
				if(!tempDisc.isEmpty()){
					for(int i = 0; i < tempDisc.size()-1; i++){
						writer.write(tempDisc.get(i) +"; ");
					}
					writer.write(tempDisc.get(tempDisc.size()-1));
				}
				writer.write("\r\n");
			}
			writer.close();
		} catch(IOException ex) {
			System.out.println("couldn't write the categoryList out");
			ex.printStackTrace();
		}
	}




	@Override
	public void clearAll() {
		System.out.println("Clear all");
		bankModel.clearTransactions();
		bankModel.clearCategories();
		
	}
	
	// Helper convenience method
		double Total(String debcred, LocalDate date1, LocalDate date2){
			double total = 0;
			for(Transaction t: bankModel.getTransactions()){
				if(date1.isEqual(t.getTransactionDate()) || date1.isBefore(t.getTransactionDate()) && date2.isAfter(t.getTransactionDate()))
					if(t.getDebetOrCredit().equals(debcred))
						total += t.getAmount();
			}
			return total;
		}

	@Override
	public String toString(){
		return "controller 1";
	}
	
	
/*
 * DEPRECATED METHODS  DEPRECATED METHODS  DEPRECATED METHODS  DEPRECATED METHODS  DEPRECATED METHODS
 */
		
	@Override
	public DefaultCategoryDataset createMontlyDataset(LocalDate date1, LocalDate date2, String debCred){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		Period p = Period.between(date1, date2);
		int maxI =  p.getMonths() + p.getYears()*12;
		System.out.println(maxI);
		for(int i = 0; i < maxI; i++){
			for(Category category : bankModel.getCategories()){
				double m1 = categoryTotal(category.getName(), debCred, date1.plusMonths(i), date1.plusMonths(1 + i));
				if(m1 > 0)
				dataset.addValue(m1, category.getName(), date1.plusMonths(i).getMonth().toString().substring(0,3));
			}
		}
		return dataset;
	}
	@Override
	public DefaultCategoryDataset createMontlyDataset(LocalDate date, String label){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(int i = 0; i < 12; i++){
			double m1 = Total("D", date.plusMonths(i), date.plusMonths(1 + i));
			dataset.addValue(m1, label, date.plusMonths(i).getMonth().toString().substring(0, 3));
		}
		return dataset;
	}
	
	

	

	@Override
	public void catogeriseTransaction(Transaction t, String category, String keyword) {
		boolean isCat = false;
		for(Category c : bankModel.getCategories()){
			if(c.getName().equalsIgnoreCase(category)){
				isCat = true;
				if(!keyword.equals("")){
					
					try {
						c.getKeyWords().add(keyword);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Excisting category " + c + " added to transaction, keyword :" +keyword +"added to the category" );
				}
				t.setCategory(c);

				System.out.println("Excisting category " + c + " added to transaction");

				if(!isCat){
					ArrayList<String> names = new ArrayList<>();
					names.add(keyword);
					Category newCat = new Category(category, names);
					bankModel.getCategories().add(newCat);
					t.setCategory(newCat);

					System.out.println("New category " + newCat + " added to transaction. With keyword " + keyword);
				}
				checkTransactionsAgainstKeyWords("not yet assigned");
			}
		}
	}
	@Override
	public void catogeriseTransactionAccount(Transaction t, String category, String account) {
		for(Category c : bankModel.getCategories()){
			if(c.getName().equalsIgnoreCase(category)){
				c.getCounterAccounts().add(account);
				checkTransactionsAgainstCounterAccounts("not yet assigned");
			}
		}
	}
	
	@Override
	public DefaultPieDataset createPieDataset(LocalDate date1, LocalDate date2, String debCred) {
		System.out.println("Creating pie dataset");
		DefaultPieDataset data = new DefaultPieDataset();
		for(Category c : bankModel.getCategories()){

			ArrayList<Transaction> temp = null;
			if(debCred.equals("B")){
				temp = searchTransactions(date1, date2, c.getName(), 0);
			} else {
				temp = searchTransactions(date1, date2, c.getName(), 0, debCred);
			}
			int tempAmount = 0;
			for (Transaction t : temp){
				tempAmount += t.getAmount();
			}
			if(tempAmount>0){
				data.setValue(c.getName(), tempAmount);
			}
		}
		return data;
	}

	
	@Override  // not used at the moment
	public void createMontlyReport(LocalDate date1, LocalDate date2, String debCred, File file){
		try{

			FileWriter writer = new FileWriter(file);
			writer.write("Report of all monthly moneyflow of type " + debCred + "from the period starting " + date1+ " \r\n");
			writer.write("------------------------------------------------------------------------\r\n");
			
			writer.write("Residence \t\t\t"  + (int)categoryTotal("Residence" ,debCred,date1, date2)/12 + " Nibud number is undefined \r\n"); 

			writer.write("Gas, Water Electricity \t\t" + (int)categoryTotal("Gas, Water Electricity" ,debCred,date1, date2)/12+ "Nibud number is 135\r\n") ;
			writer.write("Insurance \t\t\t" + (int)categoryTotal("Insurance" ,debCred,date1, date2)/12 + "Nibud number is 163\r\n")  ;
			writer.write("Subscriptions & Bubbles\t\t" + (int)categoryTotal("Subscriptions & Bubbles" ,debCred,date1, date2)/12 + "Nibud number is 103 \r\n")  ;
			
			writer.write("Education \t\t\t" + (int)categoryTotal("Education" ,debCred,date1, date2)/12 + "Nibud number is\r\n")  ;
			writer.write("Transport \t\t\t" + (int)categoryTotal("Transport" ,debCred,date1, date2)/12 + "Nibud number is 165 \r\n")   ;
			writer.write("Clothing & Shoes \t\t" + (int)categoryTotal("Clothing & Shoes" ,debCred,date1, date2)/12 + "Nibud number is 56\r\n")   ;
			writer.write("Inventory, home & garden \t" + (int)categoryTotal("Inventory, home & garden" ,debCred,date1, date2)/12 + "Nibud number is 98\r\n") ;
			writer.write("Non-reimbursed healthcare \t" + (int)categoryTotal("Non-reimbursed healthcare costs" ,debCred,date1, date2)/12 + "Nibud number is 44\r\n") ;
			writer.write("Leisure expenses \t\t" + (int)categoryTotal("Leisure expenses" ,debCred,date1, date2)/12 + "Nibud number is 88\r\n") ;
			writer.write("Household expenses \t\t" + (int)categoryTotal("Household expenses" ,debCred,date1, date2)/12 + "Nibud number is 271 \r\n") ;
			writer.write("Other fixed charges \t\t" + (int)categoryTotal("Other fixed charges" ,debCred,date1, date2)/12+ "Nibud number is\r\n\r\n")   ;
			writer.write("Total \t\t\t\t"+ (int)Total(debCred ,date1, date2)/12);
			
			
			writer.close();

		} catch(IOException ex) {
			System.out.println("couldn't write the report out");
			ex.printStackTrace();
		}

	}
	
	@Override
	public float generalTotal(String debCred){
		float temp = 0;
		for(Transaction t : bankModel.getTransactions()){
			if(t.getDebetOrCredit().equals(debCred)){
				temp += t.getAmount();
			}
		}
		return temp;
	}
	
	
	@Override
	public float categoryTotal(String categoryName, String debCred){
		float temp = 0;
		for(Transaction t : bankModel.getTransactions()){
			if(t.getCategory().getName().equals(categoryName)){
				if(t.getDebetOrCredit().equals(debCred)){
				temp += t.getAmount();
				}
			}
		}
		
		return temp;
	}
	
	
	
	
	
	
	
	
	
}
