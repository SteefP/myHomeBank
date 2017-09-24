package model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import view.BankModelObserver;

public class BankModel implements BankModelInterface , Serializable{
	private static final long serialVersionUID = 1L;
	
	ArrayList<String> accounts= new ArrayList<>();
	
	

	ArrayList<String> rawInput;
	ArrayList<Transaction> excludedTransactions = new ArrayList<Transaction>();
	
	
	ArrayList<Category> categories = new ArrayList<Category>();
	ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	
	
	@Override
	public ArrayList<Transaction> getExcludedTransactions() {
		return excludedTransactions;
	}
	
	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}

	public ArrayList<Category> getCategories() {
		return categories;
	}

	public void loadTransactionFile(File file){
		rawInput = new ArrayList<String> ();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file)); 
			String line = null;
			
			while ((line = reader.readLine()) !=null) {              
				rawInput.add(line);
			}
			reader.close();
		} catch (Exception ex) {
			System.out.println("Could'nt read the file");
			ex.printStackTrace();
		}
		convertRawInputToTransaction();
	}

	void convertRawInputToTransaction(){
		for(String s: rawInput){
			String[] result = s.split(",");
			String account = result[0];
			int year = Integer.parseInt(result[2].substring(1, 5));
			int month = Integer.parseInt(result[2].substring(5, 7));
			int day = Integer.parseInt(result[2].substring(7, 9));
			LocalDate date = LocalDate.of(year,month,day);
			String debetOrCredit = result[3].replaceAll("\"","");
			float amount = Float.parseFloat(result[4].replaceAll("\"",""));
			String otherEndOfTransactionAccount = result[5].replaceAll("\"","");
			String otherEndOfTransactionDiscription = result[6].replaceAll("\"","");
			String description = result[10].replaceAll("\"","");
			description = description.concat(result[11].replaceAll("\"",""));
			description= description.concat(result[12].replaceAll("\"",""));
			description = description.concat(result[13].replaceAll("\"",""));

			Transaction transaction = new Transaction(account, date, amount, debetOrCredit, description, otherEndOfTransactionAccount, otherEndOfTransactionDiscription);
			transactions.add(transaction);

		}
		
		initialCategoryAssignment();
	} 

	void initialCategoryAssignment(){
		
		Category categoryC = new Category("C", "V","not yet assigned", new ArrayList<String>());
		Category categoryD= new Category("D", "V","not yet assigned", new ArrayList<String>());
		categories.add(categoryC);
		categories.add(categoryD);
		for(Transaction t: transactions){
			if(t.getDebetOrCredit().equals("C")){
				t.setCategory(categoryC);
				System.out.println(t.getCategory());
			} else {
				t.setCategory(categoryD);
				System.out.println(t.getCategory());
			}
			
			for(Category category : categories){
				for(String s : category.keyWords){
					if(t.getDescription().contains(s)){
						t.setCategory(category);
						System.out.println(t.getCategory());
					}
				}
			}
		}
	}

	public void loadCategoryFile(File file){
		/*The file must define categories in the following way, per line a new category is defined, 
		 * the words in one line have the following meaning:
		 * debitOrCredit; fixedOrVariable; name; subcategory1; subcategory2; subcategory3; etc til end of line 
		*/
		
		//The lines of the file are loaded and added (per line)to a arraylist called temp, then these
		ArrayList<String> temp = new ArrayList<String> ();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file)); 
			String line = null;
			while ((line = reader.readLine()) !=null) {              
				temp.add(line);
			}
			reader.close();
		} catch (Exception ex) {
			System.out.println("Could'nt read the file");
			ex.printStackTrace();
		}

		for(String s: temp){
			String[] lineSeperated = s.split(";");
			
			ArrayList<SubCategory> subcategories = new ArrayList<SubCategory>();
			
			for(int i = 3; i < lineSeperated.length; i++){  //categories start at 3;
				subcategories.add(new SubCategory(lineSeperated[i].trim()));
				System.out.println("Subcategory" +lineSeperated[i].trim());
			}
			System.out.println(lineSeperated[0]+ lineSeperated[1]+ lineSeperated[2]);
			Category tempCategory = new Category(lineSeperated[0], lineSeperated[1], lineSeperated[2]);
			tempCategory.setSubCategories(subcategories);
			
			try {
				System.out.println("Category created "+tempCategory );
				System.out.println("Subcategories added" + subcategories);
			} catch (Exception e) {
					e.printStackTrace();
			}
			
			categories.add(tempCategory);
		}
	
	}

	
	

	@Override
	public void saveState() {
		try {
			FileOutputStream fs = new FileOutputStream("HBcurrentstate.ser");
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(this);
			os.close();
		} catch(Exception e)
		{e.printStackTrace();
		}
		
	}

	@Override
	public void clearTransactions() {
		transactions.clear(); 
		
		
	}

	@Override
	public void clearCategories() {
		categories.clear(); 
		
	}

	
		
	

	@Override
	public void registerObserver(BankModelObserver o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeObserver(BankModelObserver o) {
		// TODO Auto-generated method stub
		
	}
}

/* Deprecated
 * public void loadCategoryFile(File file){
		
		
		//The lines of the file are loaded and added (per line)to a arraylist called temp
		ArrayList<String> temp = new ArrayList<String> ();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file)); 
			String line = null;
			while ((line = reader.readLine()) !=null) {              
				temp.add(line);
			}
			reader.close();
		} catch (Exception ex) {
			System.out.println("Could'nt read the file");
			ex.printStackTrace();
		}

		for(String s: temp){
			String[] lineSeperated = s.split(";");
			ArrayList<String> keywords = new ArrayList<String>();
			for(int i = 3; i < lineSeperated.length; i++){  //categories start at 3;
				keywords.add(lineSeperated[i].trim());
			}
			categories.add(new Category(lineSeperated[0], lineSeperated[1], lineSeperated[2], keywords));
		}
		System.out.println("loadCategoryFile, on bankmodel completed, first category: " + categories.get(0));
	}
 * 
 * 
 * 
 * 
 * 
 */
