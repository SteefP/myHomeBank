package view.rulePane;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import view.FXML.MainController;



public class BackSearchController implements Initializable {
	
	MainController mainController;
	
	@FXML
	public ListView<String> lvKeyWords;
	public ListView<String> lvCounterAccounts;
	public ListView<String> lvCombinedRules;
	
	
	public ObservableList<String> olKeyWords = FXCollections.observableArrayList();
	public ObservableList<String> olCounterAccounts = FXCollections.observableArrayList();
	public ObservableList<String> olCombinedRules = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initKeywords();
		initCounterAccounts();
		initCombinedRules();
		
	}
	
	
	
	
	
	public void initCombinedRules() {
		
		
	}





	public void initCounterAccounts() {
		
		
	}





	public void initKeywords() {
		lvKeyWords.setItems(olKeyWords);
		
	}





	public void addMainController(MainController mainController){
		this.mainController = mainController;
	}
	
	public void back(ActionEvent e){
		mainController.setRulePanelNormal();
	}

}
