package model;
import java.io.File;
import java.util.ArrayList;

import view.BankModelObserver;

/* BASIC OUTLINE OF THE MODEL
 * 
 * The 2 data sets that determine the state of the model are
 * ArrayList<Transaction> transactions
 * ArrayList<Category> categories
 * Observers (view, controller), get notifications if one of these changes
 * 
 * The functionalities the controller has with regard to the model are
 * 	-ask the inputHanler to load a transactionfile
 * 	-add a category to a transaction
 * 	-load a textfile with categories (as a "startingpoint"), and add them to the list
 * 	-add a new category to the list provided by the view
 * 	-add new keywords or rules to a category, input provided by the view
 * 
 * Functionalities the controller has with regard to the view:
 * 	- Get input to make changes to the model, as the last 2 bullets above
 * 	- Perform a "sql query function" for the view to show specific information 
 * as requested by the human user, i.e. overview of monthly costs from date 1 to 
 * date 2, summed per subcategory.
 *  
 * For handling different kinds of input format from different banks, 
 * the strategy pattern is used: the model is composed with and delegates 
 * the task of input handling to classes that implement the InputHandler interface
 */

public interface BankModelInterface {

	
	public void loadTransactionFile(File file);
	public void loadCategoryFile(File file);
	public void saveState();
	public ArrayList<Category> getCategories();
	public ArrayList<Transaction> getTransactions();
	ArrayList<Transaction> getExcludedTransactions();
	public void clearTransactions();
	public void clearCategories();
	
	
	
	//Data 
		
		
		//Observer Methods
		void registerObserver(BankModelObserver o);
		void removeObserver(BankModelObserver o);
		
		
		
}
