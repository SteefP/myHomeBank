package view.FXML;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;

import controller.*;
import model.*;
import view.outputPane.OutputController;
import view.rulePane.BackSearchController;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;


public class MainController implements Initializable {

	//Reference to the controller of the model, via this controller all data from the model can be obtained
	public BankControllerInterface controller;
	BackSearchController backSearchController;
	
	//Replaceable pane
	@FXML AnchorPane rulePane;
	@FXML SplitPane ruleCat;
	@FXML BorderPane outputPane;
	@FXML Tab outputTab;
	

	
	// State flags
	public String debetOrCredit = "D";
	
	private enum State {NORMAL,	BACKSEARCH, ASSIGNFEEDBACK;}
	State state = State.NORMAL;
		
	Category tempFeedbackCategory;
	String categoryNameForTransactionTable = "not yet assigned";
	
	
	// stupid help variables, to get rid of
	public ArrayList<String> searchedKeyWord;
	public int searchedKeywordRow;
	public String searchedAccount;
	public int searchedAccountRow;
	public Rule searchedRule;
	public int searchedRuleRow;

	//MenuBar Elements
	@FXML public MenuItem close;
	@FXML public MenuItem save;

	//TransactionTable elements
	@FXML public TableView<Transaction> transactionTable;
	@FXML public TableColumn<Transaction, String> date;
	@FXML public TableColumn<Transaction, Number> amount;
	@FXML public TableColumn<Transaction, String> discription;
	@FXML public TableColumn<Transaction, String> otherAccount;
	@FXML public ToggleButton credit;
	@FXML public ToggleButton debet;
	ObservableList<Transaction> list = FXCollections.observableArrayList();
	public ToggleGroup tgDebCred;
	
	//Rule panel elements
	@FXML public TextArea txtKeyword;
	@FXML public TextField txtMinAmount;
	@FXML public TextField txtMaxAmount;
	@FXML public DatePicker fromDate;
	@FXML public DatePicker untillDate;
	@FXML public TextField txtCounterAccount;
	@FXML public Button btnApplyIncludingRules;
	@FXML public Button btnApplyOnlyCategory;
	@FXML public CheckBox chcKeyWord;
	@FXML public CheckBox chcAmount;
	@FXML public CheckBox chcDate;
	@FXML public CheckBox chcAccount;

	@FXML public Button btnUndoRule;
	@FXML public Button btnBack;
	@FXML public Button btnShowRule;
	@FXML public Button btnDeleteRule;
	
	@FXML public TableView<Transaction> transactionTable2;
	@FXML public TableColumn<Transaction, Number> amount2;
	@FXML public TableColumn<Transaction, String> discription2;
	@FXML public TableColumn<Transaction, String> otherAccount2;
	ObservableList<Transaction> list2 = FXCollections.observableArrayList();
	

	//CategoryTable elements
	@FXML public TreeTableView<Category> categoryTable;
	@FXML public TreeTableColumn<Category,String> collumn1;
	@FXML public TreeTableColumn<Category,Number> collumn2;
	TreeItem<Category> root;
	
	@FXML public Button btnExclude;
	@FXML public ProgressBar progressBar;
	@FXML public Label lblProgressPercentage;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//Initialization of the complete non-GUI part of the application that are in the packages model and controler
		initModelAndController();
		
		//Initialization of the elements of the GUI separated by panel
		try {
			initSearchpanel();
			initRulePanel();
			initCategoryPanel();
			initOutputTab();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void initModelAndController(){
		/* The initiation of the GUI in view.FXML/Main starts up the Main FXML file which start this 
		 * class and automatically calls the initialize method. In this method the initiation
		 * of the rest of the model is handled,
		 * The BankModel and Controller as defined in packages model, controller are initiated and coupled below
		 */
		BankModelInterface model = new BankModel();
		File file = new File("HBcurrentstate.ser");
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
			model = (BankModel) is.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		controller = new BankController1(model);
	}

	private void initSearchpanel(){
		date.setCellValueFactory(  // The date is parsed into a string object, since its only function is text display/to not make things complicated
				(TableColumn.CellDataFeatures<Transaction, String> param)   -> param.getValue().getSimpleTransactionDate()
				);
		amount.setCellValueFactory(
				(TableColumn.CellDataFeatures<Transaction, Number> param)   -> param.getValue().getSimpleAmount()
				);
		discription.setCellValueFactory(
				(TableColumn.CellDataFeatures<Transaction, String> param)   -> param.getValue().getSimpleDescription()
				);
		otherAccount.setCellValueFactory(
				(TableColumn.CellDataFeatures<Transaction, String> param)   -> param.getValue().getSimpleOtherEndOfTransactionName()
				);
		tgDebCred = new ToggleGroup();
		tgDebCred.getToggles().add(credit);
		tgDebCred.getToggles().add(debet);
		refreshList();
	}
	
	private void initRulePanel(){
		amount2.setCellValueFactory(
				(TableColumn.CellDataFeatures<Transaction, Number> param)   -> param.getValue().getSimpleAmount()
				);
		discription2.setCellValueFactory(
				(TableColumn.CellDataFeatures<Transaction, String> param)   -> param.getValue().getSimpleDescription()
				);
		otherAccount2.setCellValueFactory(
				(TableColumn.CellDataFeatures<Transaction, String> param)   -> param.getValue().getSimpleOtherEndOfTransactionName()
				);
		setUndoElements(false);
	}
	
	private void initCategoryPanel(){
		collumn1.setCellValueFactory(   
				(TreeTableColumn.CellDataFeatures<Category, String> param)   -> param.getValue().getValue().getSimpleName());
		refreshProgressBar();
		refreshCategoryTable();
	}
	
	private void initOutputTab() {
		initDateValues();
	}
	
	private void refreshCategoryTable() {
		if(debetOrCredit=="D"){
			root= new TreeItem<>(new Category("Uitgaven"));
		} else {
			root= new TreeItem<>(new Category("Inkomsten"));
		}

		for(Category c : controller.getCategories()){
			if(c.getdebetOrCredit().equals(debetOrCredit)){
				TreeItem<Category> tempTreeItem = new TreeItem<>(c);
			
					for(SubCategory sc : c.getSubCategories()){
						tempTreeItem.getChildren().add(new TreeItem<>(sc));
					}
				root.getChildren().add(tempTreeItem);
			}
		}
		categoryTable.setRoot(root);
		root.setExpanded(true);
	}

	public void refreshList(){
		list.clear();
		if(state == state.NORMAL || state == state.BACKSEARCH  ){
			for (Transaction t: controller.searchTransactions(LocalDate.MIN, LocalDate.now(), categoryNameForTransactionTable, 0, debetOrCredit)  ){
				list.add(t);
				transactionTable.setItems(list);
			}
		} else {
			for (Transaction t: controller.searchTransactions(LocalDate.MIN, LocalDate.now(), tempFeedbackCategory.getName(), 0, debetOrCredit)  ){
				list.add(t);
				transactionTable.setItems(list);
			}
		}
		transactionTable.getSortOrder().add(amount);
	}
	
	public void refreshList2(){
		list2.clear();
		if(state == State.ASSIGNFEEDBACK)
		for (Transaction t: controller.searchTransactions(LocalDate.MIN, LocalDate.now(), tempFeedbackCategory.getName(), 0, debetOrCredit)  ){
			list2.add(t);
			transactionTable2.setItems(list2);
		}
	}
	

	public void refreshProgressBar(){
		float tempTotalUnassignedAmount = controller.categoryTotal("not yet assigned", debetOrCredit);
		float tempTotal = controller.generalTotal(debetOrCredit);
		double progress = (double) 1-tempTotalUnassignedAmount/tempTotal;
		progressBar.setProgress(progress);
		int prog = (int) (progress*100);
		String progS = Integer.toString(prog);
		lblProgressPercentage.setText(progS+ " %");
	}



	


	public void transactionSelected(){
		if(state == State.NORMAL ){
			
		try {
			txtKeyword.setText(transactionTable.getSelectionModel().getSelectedItem().getDescription());
			txtCounterAccount.setText(transactionTable.getSelectionModel().getSelectedItem().getOtherEndOfTransactionName());
		} catch (NullPointerException e) {
			System.out.println("The teansactions keyword or counteracooutn is empty");
		}
		
		chcKeyWord.setSelected(false);
		chcAccount.setSelected(false);
		chcAmount.setSelected(false);
		chcDate.setSelected(false);
		}
		if(state == State.BACKSEARCH){
			showRule();			
		}
		
	}

	public void setTemporaryCategory(){
		/* There are 4 fields that can be used as a rule (keyword, counter account, date, min/max) in all combinations,
		 * to automatically give a category to a transaction. However to dat and min max are almost never used, and using rules 
		 * makes te programm slow really fast. Therefore to to most commen "rules" can also be added stand alone, so that 
		 * searching can be done much faster. 
		 * Rules are of the "and" type all requirements have to be met, where to standalone requirements are of the "or" type
		 * if in one case both keyword and account are checked then it is assumed to be that the user wnats an "and" 
		 * requirement, and so it becomes a rule. 
		 * This is programmed below 
		 */
		if(chcAmount.isSelected()||chcDate.isSelected() || (chcKeyWord.isSelected()&& chcAccount.isSelected())){
			System.out.println("In de rule area");
			Rule r = new Rule();
			if(chcKeyWord.isSelected()){
				r.setKeyword(txtKeyword.getText().trim());
			}
			if(chcAccount.isSelected()){
				r.setCounterAcccountDiscription(txtCounterAccount.getText().trim());
			}
			if(chcDate.isSelected()){
				r.setStartDate(fromDate.getValue());
				r.setEndDate(untillDate.getValue());
			}
			if(chcAmount.isSelected()){
				r.setMinAmount(Float.parseFloat(txtMinAmount.getText()));
				r.setMaxAmount(Float.parseFloat(txtMaxAmount.getText()));
			}
			
			tempFeedbackCategory = new Category("tempR");
			tempFeedbackCategory.getRules().add(r);
			controller.addCategory(tempFeedbackCategory);
			System.out.println("Rule added to temCategory: "+r);
			controller.checkTransactionsAgianstRules("not yet assigned");
		} else 
			if(chcKeyWord.isSelected()&& !chcAccount.isSelected()){
			tempFeedbackCategory = new Category("tempK");
			tempFeedbackCategory.getKeyWords().add(txtKeyword.getText());
			controller.addCategory(tempFeedbackCategory);
			controller.checkTransactionsAgainstKeyWords("not yet assigned");
		} else 
			if(!chcKeyWord.isSelected()&& chcAccount.isSelected()){
			tempFeedbackCategory = new Category("tempA");
			tempFeedbackCategory.getCounterAccounts().add(txtCounterAccount.getText());
			controller.addCategory(tempFeedbackCategory);
			System.out.println("tem cat made with account " +tempFeedbackCategory.getCounterAccounts() );
			System.out.println("tem cat made with name " +tempFeedbackCategory.getName() );
			System.out.println("tem cat made with sub cat " +tempFeedbackCategory.getSubCategories() );
			
		
			controller.checkTransactionsAgainstCounterAccounts("not yet assigned");
		}
		refreshList();
		refreshList2();
		
	
		controller.removeCategory(tempFeedbackCategory);
		for (Transaction t :transactionTable.getItems()){
			t.setCategory(new Category("not yet assigned"));
			System.out.println(t.getAmount()+ " is now cat as unassigned");
		}


	}

	public void applyRule(ActionEvent event){
		//System.out.println("The temporary cat is: " +tempFeedbackCategory);

		if(!tempFeedbackCategory.getRules().isEmpty()){
			getSelectedCategory().getRules().add(tempFeedbackCategory.getRules().get(0));
			controller.checkTransactionsAgianstRules("not yet assigned");
		}
		if(!tempFeedbackCategory.getKeyWords().isEmpty()){

			//System.out.println("The selected category is: "+getSelectedCategory() );
			//adds the keyword to the selected category
			getSelectedCategory().getKeyWords().add(tempFeedbackCategory.getKeyWords().get(0));
			//System.out.println("Category :"+ getSelectedCategory()+ "has the keywords"+getSelectedCategory().getKeyWords()  );
			//checks for all transactions if for all the keywords, en set a category
			controller.checkTransactionsAgainstKeyWords("not yet assigned");
		}
		if(!tempFeedbackCategory.getCounterAccounts().isEmpty()){
			//System.out.println("The selected category is: "+getSelectedCategory() );
			getSelectedCategory().getCounterAccounts().add(tempFeedbackCategory.getCounterAccounts().get(0));
			controller.checkTransactionsAgainstCounterAccounts("not yet assigned");
		}

		state = state.NORMAL;
		refreshList();
		refreshList2();
		refreshProgressBar();
		chcAccount.setSelected(false);
		chcKeyWord.setSelected(false);
		chcDate.setSelected(false);
		chcAmount.setSelected(false);

	}
	
	// probably obsolete
	public void applyRuleOverwrite(ActionEvent event){
		if(chcKeyWord.isSelected()){
			controller.addKeyWordToCategory(getSelectedCategory().getName(), txtKeyword.getText());
			controller.checkTransactionsAgainstKeyWords(getSelectedTransaction().getCategory().getName());
		}
		if(chcAccount.isSelected()){
			controller.addAccountToCategory(getSelectedCategory().getName(), txtCounterAccount.getText());
			controller.checkTransactionsAgainstCounterAccounts(getSelectedTransaction().getCategory().getName());
		}
		
		refreshList();
		refreshList2();
		refreshProgressBar();
		chcAccount.setSelected(false);
		chcKeyWord.setSelected(false);
		chcDate.setSelected(false);
		chcAmount.setSelected(false);

	}


	public void anyChkBoxChecked(ActionEvent event){
		if(chcAccount.isSelected() || chcAmount.isSelected() || chcKeyWord.isSelected() || chcDate.isSelected()){
			if(state != State.BACKSEARCH){
				state = state.ASSIGNFEEDBACK;	
				System.out.println("Any checkbox checked");
				setTemporaryCategory();

			} 
		} else {
			state = state.NORMAL;
			controller.removeCategory(tempFeedbackCategory);
			refreshList();
		}

	}



	public void applyOnlyCategory(ActionEvent event){
		System.out.println("Aplly only rule reached");
		getSelectedTransaction().setCategory(getSelectedCategory());
		refreshList();
		refreshProgressBar();
	}


	public void setCategoryNameForTransactionTable() throws IOException{
		state = state.BACKSEARCH;
		categoryNameForTransactionTable = getSelectedCategory().getName();
		setUndoElements(true);
		txtKeyword.setText("");
		txtCounterAccount.setText("");
	
		refreshList();
		
		loadBackSearchPane();
	}
	
	private void loadBackSearchPane() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/view/rulePane/BackSearch.fxml"));
		BorderPane backSerach = loader.load();
		ruleCat.getItems().remove(0);
		ruleCat.getItems().add(0,backSerach);
		
		backSearchController = loader.<BackSearchController>getController();
		backSearchController.addMainController(this);
		
	}
	
	/*
	public void loadOutputPane() throws IOException {
		System.out.println("Loading output pane");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/view/outputPane/Output.fxml"));
		outputPane.
		outputPane = loader.load();
		OutputController outputController = loader.<OutputController>getController();
		outputController.addBankController(controller);
		
	}
*/
	
	
	public void showRule(){
		System.out.println("in show rule");
		txtKeyword.setText("");
		chcKeyWord.setSelected(false);
		txtCounterAccount.setText("");
		chcAccount.setSelected(false);
		searchedKeyWord= null;
		searchedKeywordRow =0;
		searchedAccount =null;
		searchedAccountRow = 0;
		searchedRule = null;
		searchedRuleRow = 0;
		backSearchController.lvKeyWords.getItems().clear();
		backSearchController.lvCounterAccounts.getItems().clear();	
		backSearchController.lvCombinedRules.getItems().clear();
		if(findAppliccableKeyword()){
			
			for(String s : searchedKeyWord){
				backSearchController.lvKeyWords.getItems().add(s);
			}
			backSearchController.lvKeyWords.getSelectionModel().select(0);
		}
		if(findAppliccableAccount()){
			
			backSearchController.lvCounterAccounts.getItems().add(searchedAccount);
			backSearchController.lvCounterAccounts.getSelectionModel().select(0);
		}
		if(findAppliccableRule()){
			
			backSearchController.lvCombinedRules.getItems().add(searchedRule.toString());
			System.out.println("found applicable rule: " +searchedRule.toString());
		}
	}
	public void deleteRule(){
		if(findAppliccableRule()){
			getSelectedCategory().getRules().remove(0);
			
		}
		if(chcAccount.isSelected()){
			System.out.println(getSelectedCategory().getCounterAccounts());
			getSelectedCategory().getCounterAccounts().remove(txtCounterAccount.getText());
			System.out.println(getSelectedCategory().getCounterAccounts());
		}
		if(findAppliccableKeyword()){
		
			System.out.println("the category has the following keyword array: "+getSelectedCategory().getKeyWords());
			getSelectedCategory().getKeyWords().remove(searchedKeywordRow);
			System.out.println("Keyword should be removed row nr " +searchedKeywordRow);
			System.out.println(categoryTable.getSelectionModel().getSelectedItem().getValue().getKeyWords());
		}
		if(findAppliccableAccount()){
			getSelectedCategory().getCounterAccounts().remove(searchedAccountRow);
			
		}
		
	}
	


	public boolean findAppliccableKeyword(){
		boolean temp = false;
		searchedKeyWord = new ArrayList<String>();
		for(Category c : controller.getCategories()){
			for(String s : c.getKeyWords()){
				if(getSelectedTransaction().getDescription().contains(s)){
					System.out.println("Found apllicable keyword in category " +c);
					searchedKeyWord.add(s);
					searchedKeywordRow = c.getKeyWords().indexOf(s);
					temp = true;
				}
			}
			for(SubCategory sc: c.getSubCategories()){
				System.out.println("The subcategory"+ sc+ "has the following keyword array"+ sc.getKeyWords());
				for(String s : sc.getKeyWords()){
					if(transactionTable.getSelectionModel().getSelectedItem().getDescription().contains(s)){
						System.out.println("Found apllicable keyword in subcategory "+ sc);
						searchedKeyWord.add(s);
						System.out.println("Searched keyword is: " + s);
						searchedKeywordRow = sc.getKeyWords().indexOf(s);
						System.out.println("Searched keyword row is: " + searchedKeywordRow);
						System.out.println("The keyword array of the subcatagory is: "+getSelectedCategory().getKeyWords());
						temp = true;
					}
				}
			}
		}
		return temp;
	}

	public boolean findAppliccableAccount(){
		boolean temp = false;
		for(Category c : controller.getCategories()){
			for(String s : c.getCounterAccounts()){
				if(getSelectedTransaction().getOtherEndOfTransactionName().contains(s)){
					System.out.println("Found apllicable account");
					searchedAccount = s;
					temp = true;
				} 
			}
			for(SubCategory sc: c.getSubCategories()){
				for(String s : sc.getCounterAccounts()){
					if(getSelectedTransaction().getOtherEndOfTransactionName().contains(s)){
						System.out.println("Found apllicable account");
						searchedAccount = s;
						temp = true;
					} 
				}
			}
		}
		return temp;
	}
	public boolean findAppliccableRule(){  // THis one is not finished yet i hope deleting a complex rule is a 0.01% event
		boolean temp = false;
		for(Category c : controller.getCategories()){
			try {
				if(c.getName().equals(getSelectedCategory().getName())){

					
						searchedRule = c.getRules().get(0);
						temp = true;
					

					System.out.println(c.getRules().get(0).getKeyword());
					System.out.println(c.getRules().get(0).getMaxAmount());
					System.out.println(c.getRules().get(0).getStartDate());
					System.out.println(c.getRules().toString());
					System.out.println("Advanced rule in this category");
				} //close if cat 
				for(SubCategory sc: c.getSubCategories()){
					if(sc.getName().equals(getSelectedCategory().getName())){

					
							searchedRule = sc.getRules().get(0);
							temp = true;
					

						System.out.println("Advanced rule in this subcategory");
					} //close if subcat
				}
			}catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("error in find app rule");
				e.printStackTrace();
			} // close try
		} // close for category
		return temp;
	}

	public void setRulePanelNormal(){
		state = state.NORMAL;
		categoryNameForTransactionTable = "not yet assigned";
		setUndoElements(false);
		refreshList();
		ruleCat.getItems().remove(0);
		ruleCat.getItems().add(0,rulePane);
		
	}
	public void excludeFromAnalisys(ActionEvent event){
		controller.excludeTransactionFromAnalisys(getSelectedTransaction());
		refreshList();
		refreshProgressBar();
	}
	public void close(ActionEvent event){

		controller.saveState();
		Platform.exit();
	}
	public void save(ActionEvent event){
		System.out.println("Saved");
		controller.saveState();
	}
	

	public void loadCategories(){
		FileChooser fc = new FileChooser();
		File selectedFile = fc.showOpenDialog(null);

	
		try {
			if (selectedFile != null){
				controller.loadCategoryFile(selectedFile);

			} else {
				System.out.println("File is not valid");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refreshCategoryTable();
		
	}
	public void loadTransactions(){
		FileChooser fc = new FileChooser();
		File selectedFile = fc.showOpenDialog(null);

		try {
			if (selectedFile != null){
				controller.loadTransactionFile(selectedFile);

			} else {
				System.out.println("File is not valid");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refreshList();
		

	}
	
	public void saveCategories(ActionEvent event){
		FileChooser fc = new FileChooser();
		File selectedFile = fc.showSaveDialog(null);

		try {
			if (selectedFile != null){
				controller.saveCategories(selectedFile);

			} else {
				System.out.println("File is not valid");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void clearall(){
		controller.clearAll();
	}

	public void toggleDebet(){
		if(debetOrCredit.equals("D")){
		} else{
			debetOrCredit= "D";
			refreshList();
			refreshProgressBar();
			refreshCategoryTable();
		}
	}
	public void toggleCredit(){
		if(debetOrCredit.equals("C")){
		} else{
			debetOrCredit= "C";
			refreshList();
			refreshProgressBar();
			refreshCategoryTable();
		}
	}
	
	public void setUndoElements(boolean value ) {
		btnUndoRule.setVisible(value);
		btnBack.setVisible(value);
		btnShowRule.setVisible(value);
		btnDeleteRule.setVisible(value);
	}
	public Category getSelectedCategory(){
		return categoryTable.getSelectionModel().getSelectedItem().getValue();
	}
	public Transaction getSelectedTransaction(){
		return transactionTable.getSelectionModel().getSelectedItem();
	}
	

	////////////////////////////////////////////////////////////
	//OUTPUT TAB COMES BELOW
	////////////////////////////////////////////////////////////

	
	
	@FXML DatePicker dpChartStart;
	@FXML DatePicker dpChartEnd;
	
	

	public void initDateValues(){
		dpChartStart.setValue(LocalDate.of(2016, 6, 1));
		dpChartEnd.setValue(LocalDate.of(2017, 6, 1));
	}
	
	@FXML StackedBarChart sBChart;

	public void initchart(){

		LocalDate chartStart = dpChartStart.getValue();
		LocalDate chartEnd = dpChartEnd.getValue();

		sBChart.getData().clear();
		ObservableList<XYChart.Series<String, Number>> dataNumber = FXCollections.observableArrayList();

		for (Category c : controller.getCategories()){
			if(c.getDebetOrCredit().equals(debetOrCredit)){
				XYChart.Series<String, Number> tempSeries = new XYChart.Series<String, Number>();
				tempSeries.setName(c.getName());


				for (LocalDate i = chartStart; i.isBefore(chartEnd); i=i.plusMonths(1)){

					tempSeries.getData().add(
							new XYChart.Data<String, Number>
							(i.toString(),controller.categoryTotal
									(c.getName(), debetOrCredit, 
											LocalDate.of(i.getYear(), i.getMonth() , 1), 
											LocalDate.of(i.plusMonths(1).getYear(), i.getMonth().plus(1) , 1))));

				}
				dataNumber.add(tempSeries);
			}
		}

		sBChart.getData().addAll(dataNumber);

		initchartnormal();
		initOutputTable();
	}
	
	@FXML BarChart bChart;

	public void initchartnormal(){  // to be adjusted 
		LocalDate chartStart = dpChartStart.getValue();
		LocalDate chartEnd = dpChartEnd.getValue();

		bChart.getData().clear();

		ObservableList<XYChart.Series<String, Number>> dataNumber = FXCollections.observableArrayList();

		XYChart.Series<String, Number> tempSeries1 = new XYChart.Series<String, Number>();
		XYChart.Series<String, Number> tempSeries2 = new XYChart.Series<String, Number>();
		tempSeries1.setName("Credit"); 
		tempSeries2.setName("Debet");

		for (LocalDate i = chartStart; i.isBefore(chartEnd); i=i.plusMonths(1)){
			System.out.println("C start date: "+LocalDate.of(i.getYear(), i.getMonth() , 1)+ "end date: "+LocalDate.of(i.plusMonths(1).getYear(), i.getMonth().plus(1) , 1));
			tempSeries1.getData().add(
					new XYChart.Data<String, Number>
					(i.toString(),controller.generalTotal("C", 
							LocalDate.of(i.getYear(), i.getMonth() , 1), 
							LocalDate.of(i.plusMonths(1).getYear(), i.getMonth().plus(1) , 1))));
			
			
		}
		dataNumber.add(tempSeries1);
		for (LocalDate i = chartStart; i.isBefore(chartEnd); i=i.plusMonths(1)){
			System.out.println("D start date: "+LocalDate.of(i.getYear(), i.getMonth() , 1)+ "end date: "+LocalDate.of(i.plusMonths(1).getYear(), i.getMonth().plus(1) , 1));
			tempSeries2.getData().add(
					new XYChart.Data<String, Number>
					(i.toString(), controller.generalTotal("D", 
							LocalDate.of(i.getYear(), i.getMonth() , 1), 
							LocalDate.of(i.plusMonths(1).getYear(), i.getMonth().plus(1) , 1))));
		}
		dataNumber.add(tempSeries2);


		bChart.getData().addAll(dataNumber);
	}

	public void about(){

	}

	public void ruleCheckboxChecked(){    // nog afmaken met vervolg
		if(state != state.BACKSEARCH){
			state = state.ASSIGNFEEDBACK;
			refreshList();
		}
	}
	
	
	@FXML public TreeTableView<MockCategory> outputTable;
	@FXML public TreeTableColumn<MockCategory,String> outputCol1;
	@FXML public TreeTableColumn<MockCategory,Number> outputCol2;
	TreeItem<MockCategory> outputRoot;
	
	
	public void initOutputTable(){
		outputCol1.setCellValueFactory(   
				(TreeTableColumn.CellDataFeatures<MockCategory, String> param)   -> param.getValue().getValue().getName());
		outputCol2.setCellValueFactory(   
				(TreeTableColumn.CellDataFeatures<MockCategory, Number> param)   -> param.getValue().getValue().getAmount());
		
		
		refreshOutputTable();
		initOutputTableC();
	}

	private void refreshOutputTable() {
		LocalDate tableStart = dpChartStart.getValue();
		LocalDate tableEnd = dpChartEnd.getValue();
		Period p = Period.between(tableStart, tableEnd);
		int months = p.getMonths()+ p.getYears()*12;
		int tempGenTotal = (int )controller.generalTotal("C", tableStart, tableEnd)/months;


		outputRoot= new TreeItem<>(new MockCategory( tempGenTotal,"Inkomsten"));


		for(Category c : controller.getCategories()){

			if(c.getdebetOrCredit().equals("C")){
				int tempCAmount = (int) controller.categoryTotal(c.getName(), "C", tableStart, tableEnd)/months;
				TreeItem<MockCategory> tempCTreeItem= new TreeItem<MockCategory>(new MockCategory(tempCAmount, c.getName()));
				for(SubCategory sc : c.getSubCategories()){
					int tempScAmount = (int) controller.subCategoryTotal(sc.getName(), tableStart, tableEnd)/months;




					tempCTreeItem.getChildren().add(new TreeItem<MockCategory>(new MockCategory(tempScAmount, sc.getName())));
				}

				outputRoot.getChildren().add(tempCTreeItem );
			}

		}
		outputTable.setRoot(outputRoot);
		outputRoot.setExpanded(true);
	}
	
	@FXML public TreeTableView<MockCategory> outputTableC;
	@FXML public TreeTableColumn<MockCategory,String> outputColC1;
	@FXML public TreeTableColumn<MockCategory,Number> outputColC2;
	TreeItem<MockCategory> outputRootC;
	
	
	public void initOutputTableC(){
		outputColC1.setCellValueFactory(   
				(TreeTableColumn.CellDataFeatures<MockCategory, String> param)   -> param.getValue().getValue().getName());
		outputColC2.setCellValueFactory(   
				(TreeTableColumn.CellDataFeatures<MockCategory, Number> param)   -> param.getValue().getValue().getAmount());
		
		
		refreshOutputTableC();
	}
	
	private void refreshOutputTableC() {
		LocalDate tableStart = dpChartStart.getValue();
		LocalDate tableEnd = dpChartEnd.getValue();
		Period p = Period.between(tableStart, tableEnd);
		int months = p.getMonths()+ p.getYears()*12;
		int tempGenTotal = (int )controller.generalTotal("D", tableStart, tableEnd)/months;
			
		
			outputRoot= new TreeItem<>(new MockCategory( tempGenTotal,"Uitgaven"));
		
		
		for(Category c : controller.getCategories()){
		
				if(c.getdebetOrCredit().equals("D")){
					int tempCAmount = (int) controller.categoryTotal(c.getName(), "D", tableStart, tableEnd)/months;
					
					
					
					TreeItem<MockCategory> tempCTreeItem= new TreeItem<MockCategory>(new MockCategory(tempCAmount, c.getName()));
					
					
					for(SubCategory sc : c.getSubCategories()){
						int tempScAmount = (int) controller.subCategoryTotal(sc.getName(), tableStart, tableEnd)/months;
						
						
						
						
						tempCTreeItem.getChildren().add(new TreeItem<MockCategory>(new MockCategory(tempScAmount, sc.getName())));
					}
					
					outputRoot.getChildren().add(tempCTreeItem );
				}
			
		}
		outputTableC.setRoot(outputRoot);
		outputRoot.setExpanded(true);
		initchart3();
	}
	
	@FXML SplitPane OutputsSplit;


	private boolean family = true;
	@FXML RadioButton btnSingle;
	@FXML RadioButton btnCouple;
	@FXML RadioButton btnFamily;
	
	public ToggleGroup nibudChoice;
	
	@FXML StackedBarChart NChart;
	
	public void initchart3(){
		nibudChoice = new ToggleGroup();
		btnSingle.setToggleGroup(nibudChoice);
		btnCouple.setToggleGroup(nibudChoice);
		btnFamily.setToggleGroup(nibudChoice);
		
		LocalDate chartStart = dpChartStart.getValue();
		LocalDate chartEnd = dpChartEnd.getValue();
		Period p = Period.between(chartStart, chartEnd);
		int months = p.getMonths()+ p.getYears()*12;

		NChart.getData().clear();
		int [] nibud = {660,156,163,103,0,0,165,56,98,44,88,271};
		
		
		int woning =0; 
		int Energie=156; 
		int Verzekeringen=163; 
		int Abonnementen=103; 
		int Onderwijs=0; 
		int Overige_vaste_lasten=0; 
		int Vervoer=165; 
		int Kleding=56; 
		int Inventaris=98; 
		int Niet_vergoede_ziektekosten=44; 
		int Vrijetijdsuitgaven=88; 
		int Huishoudelijk_uitgaven=271; 
		
		if(family){
			nibud[0] =800; 
			nibud[1]=197; 
			nibud[2]=315; 
			nibud[3]=158; 
			nibud[4]=4; 
			nibud[5]=0; 
			nibud[6]=164; 
			nibud[7]=152; 
			nibud[8]=132; 
			nibud[9]=99; 
			nibud[10]=126; 
			nibud[11]=539; 
		}
		
		
		ObservableList<XYChart.Series<String, Number>> dataNumber = FXCollections.observableArrayList();

		for (Category c : controller.getCategories()){
			if(c.getDebetOrCredit().equals("D")){
				XYChart.Series<String, Number> tempSeries = new XYChart.Series<String, Number>();
				tempSeries.setName(c.getName());
				for (int i=0; i<26; i++){
					
					if(i>0 && i<13){

						if(c.getName().equals(controller.getCategories().get(i+6).getName())){

							tempSeries.getData().add(
									new XYChart.Data<String, Number>
									(c.getName(), (controller.categoryTotal(c.getName(), "D", chartStart,chartEnd)/months)-nibud[i-1]) 
									);
						}
					}
				}
				dataNumber.add(tempSeries);
			}
		}
		NChart.getData().addAll(dataNumber);
		initchart4();
		
	}
@FXML StackedBarChart NChart2;
	
	public void initchart4(){
		
		LocalDate chartStart = dpChartStart.getValue();
		LocalDate chartEnd = dpChartEnd.getValue();
		Period p = Period.between(chartStart, chartEnd);
		int months = p.getMonths()+ p.getYears()*12;

		NChart2.getData().clear();
		
		
		int [] nibud = {600,156,163,103,0,0,165,56,98,44,88,271};
		if(family){
			nibud[0] =800; 
			nibud[1]=197; 
			nibud[2]=315; 
			nibud[3]=158; 
			nibud[4]=4; 
			nibud[5]=0; 
			nibud[6]=164; 
			nibud[7]=152; 
			nibud[8]=132; 
			nibud[9]=99; 
			nibud[10]=126; 
			nibud[11]=539; 
		}


		ObservableList<XYChart.Series<String, Number>> dataNumber = FXCollections.observableArrayList();
		int i = 0;
		for (Category c : controller.getCategories()){
			if(c.getDebetOrCredit().equals("D")){
				XYChart.Series<String, Number> tempSeries = new XYChart.Series<String, Number>();
				XYChart.Series<String, Number> tempSeries2 = new XYChart.Series<String, Number>();
				if (i<12) {
					tempSeries.getData().add(
							new XYChart.Data<String, Number>
							("Nibud", nibud[i]));
					System.out.println(nibud[i]);
					tempSeries.getData().add(
							new XYChart.Data<String, Number>
							("You", controller.categoryTotal(c.getName(), "D", chartStart,chartEnd)/months) 
							);
					
					dataNumber.add(tempSeries);
				}
				
				i++;
			}
		}
		NChart2.getData().addAll(dataNumber);

	}
	
	public void radioToggled(ActionEvent e){
		if(nibudChoice.getSelectedToggle()== btnFamily){
			family = true;
		} else {
			family = false;
		}
		initchart3();
			
	}



}


/*
 * for(Category c : controller.getCategories()){
		if(c.getdebetOrCredit().equals(debetOrCredit)){
			TreeItem<Category> tempTreeItem = new TreeItem<>(c);

				for(SubCategory sc : c.getSubCategories()){
					tempTreeItem.getChildren().add(new TreeItem<>(sc));
				}

			root.getChildren().add(tempTreeItem);
		}
	}
		 * 
		 */


















