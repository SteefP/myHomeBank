package view.rulePane;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.Rule;
import view.FXML.MainController;



public class BackSearchController implements Initializable {

	MainController mainController;

	@FXML
	public ListView<String> lvKeyWords;
	public ListView<String> lvCounterAccounts;
	public ListView<String> lvCombinedRules;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public void addMainController(MainController mainController){
		this.mainController = mainController;
	}

	public void back(ActionEvent e){
		mainController.setRulePanelNormal();
	}


	public void deleteKeyword(ActionEvent e){ // delete keyword form category, the to put the applicable transactions back to unaasigned the last 3 steps are taken
		mainController.getSelectedCategory().getKeyWords().remove(getSelectedKeyword());
		mainController.controller.addKeyWordToCategory("not yet assigned", getSelectedKeyword());
		mainController.controller.checkTransactionsAgainstKeyWords(mainController.getSelectedCategory().getName());
		mainController.controller.removeKeyWordFromCategory("not yet assigned", getSelectedKeyword());
		mainController.controller.checkTransactionsAgainstKeyWords("not yet assigned");
		mainController.refreshList();
	}
	public void deleteCounterAccount(ActionEvent e){
		System.out.println("Deleting coutneraccount: " +getSelectedCounterAccount());
		mainController.getSelectedCategory().getCounterAccounts().remove(getSelectedCounterAccount());
		mainController.controller.addAccountToCategory("not yet assigned", getSelectedCounterAccount());
		mainController.controller.checkTransactionsAgainstCounterAccounts(mainController.getSelectedCategory().getName());
		mainController.controller.removeAccountFromCategory("not yet assigned", getSelectedCounterAccount());
		mainController.controller.checkTransactionsAgainstCounterAccounts("not yet assigned");
		mainController.refreshList();
	}
	public void deleteCombinedRule(ActionEvent e){
		

	}
	
	
	
	
	//Helper functions
	public String getSelectedKeyword(){ 
		
		return lvKeyWords.getSelectionModel().getSelectedItem();
	}
	
	public String getSelectedCounterAccount(){
		return lvCounterAccounts.getSelectionModel().getSelectedItem();
	}
	
	public Rule getSelectedRule(){
		return mainController.searchedRule;
	}










	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * Probably obsolete
	public ObservableList<String> olKeyWords = FXCollections.observableArrayList();
	public ObservableList<String> olCounterAccounts = FXCollections.observableArrayList();
	public ObservableList<String> olCombinedRules = FXCollections.observableArrayList();
	
	public void initCombinedRules() {
		
		
	}





	public void initCounterAccounts() {
		
		
	}





	public void initKeywords() {
		lvKeyWords.  setItems(olKeyWords);
		
	}


*/

}
