package model;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class Category implements Serializable, Comparable<Category> {
	
	private static final long serialVersionUID = 1L;


	
	//Fields
	String debetOrCredit;
	String fixedOrVariable;
	String name;
	ArrayList<String> keyWords;
	ArrayList<String> counterAccounts;
	ArrayList<Rule> rules;
	ArrayList<SubCategory> subCategories;






	// getters and setters
	public String getdebetOrCredit() {
		return debetOrCredit;
	}
	public String getFixedOrVariable() {
		return fixedOrVariable;
	}
	public String getName() {
		return name;
	}
	public ArrayList<String> getKeyWords() {
		return keyWords;
	}
	public ArrayList<Rule> getRules() {
		return rules;
	}
	public ArrayList<SubCategory> getSubCategories() {
		return subCategories;
	}
	
	public void setdebetOrCredit(String debetOrCredit) {
		this.fixedOrVariable = debetOrCredit;
	}
	public void setFixedOrVariable(String fixedOrVariable) {
		this.fixedOrVariable = fixedOrVariable;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setKeyWords(ArrayList<String> descriptions) {
		this.keyWords = descriptions;
	}
	public void setRules(ArrayList<Rule> rules) {
		this.rules = rules;
	}
	public void setSubCategories(ArrayList<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}

	// Getters and setters for FXML
	public SimpleStringProperty getSimpleDebetOrCredit() {
		return new SimpleStringProperty(debetOrCredit);
	}
	public SimpleStringProperty getSimpleFixedOrVariable() {
		return new SimpleStringProperty(fixedOrVariable);
	}
	public SimpleStringProperty getSimpleName() {
		return new SimpleStringProperty(name);
	}
	public void setSimpleDebetOrCredit(SimpleStringProperty debetOrCredit) {
		this.fixedOrVariable = debetOrCredit.getValue();
	}
	public void setSimpleFixedOrVariable(SimpleStringProperty fixedOrVariable) {
		this.fixedOrVariable = fixedOrVariable.getValue();
	}
	public void setSimpleName(SimpleStringProperty name) {
		this.name = name.getValue();
	}


	// Constructors
	public Category(String fixedOrVariable, String name) {
		this.fixedOrVariable = fixedOrVariable;
		this.name = name;
		this.rules= new ArrayList<Rule>();
		this.counterAccounts = new ArrayList<String>();
		this.keyWords = new ArrayList<String>();
		this.subCategories = new ArrayList<SubCategory>();
	}
	public Category(String name) {
	
		this.name = name;
		this.rules= new ArrayList<Rule>();
		this.debetOrCredit = "NA";
		this.fixedOrVariable = "Na";		
		this.counterAccounts = new ArrayList<String>();
		this.keyWords = new ArrayList<String>();
		this.subCategories = new ArrayList<SubCategory>();
	}
	public Category(String name, ArrayList<String> descriptions) {
		this.name = name;
		this.keyWords = descriptions;
		this.rules= new ArrayList<Rule>();
		this.debetOrCredit = "NA";
		this.fixedOrVariable = "NA";
		this.counterAccounts = new ArrayList<String>();
		this.subCategories = new ArrayList<SubCategory>();
		
	}
	
	public Category(String debetOrCredit, String fixedOrVariable, String name, ArrayList<String> descriptions) {
		this.debetOrCredit = debetOrCredit;
		this.fixedOrVariable = fixedOrVariable;		
		this.name = name;
		this.keyWords = descriptions;
		this.rules= new ArrayList<Rule>();
		this.counterAccounts = new ArrayList<String>();
		this.subCategories = new ArrayList<SubCategory>();
	}
	public Category(String debetOrCredit, String fixedOrVariable, String name) {
		this.debetOrCredit = debetOrCredit;
		this.fixedOrVariable = fixedOrVariable;		
		this.name = name;
		this.rules= new ArrayList<Rule>();
		this.counterAccounts = new ArrayList<String>();
		this.keyWords = new ArrayList<String>();
		this.subCategories = new ArrayList<SubCategory>();
	}
	public Category(String fixedOrVariable, String name, ArrayList<String> descriptions) {
		this.fixedOrVariable = fixedOrVariable;
		this.name = name;
		this.keyWords = descriptions;
		this.rules= new ArrayList<Rule>();
		this.counterAccounts = new ArrayList<String>();
		this.subCategories = new ArrayList<SubCategory>();
	}
	// Constructors FXML
	public Category(SimpleStringProperty fixedOrVariable, SimpleStringProperty name) {
		this.fixedOrVariable = fixedOrVariable.getValue();
		this.name = name.getValue();
		this.rules= new ArrayList<Rule>();
		this.counterAccounts = new ArrayList<String>();
		this.keyWords = new ArrayList<String>();
		this.subCategories = new ArrayList<SubCategory>();
	}
	
	

	// Overridden behavior
	@Override
	public String toString(){
		return name;
	}
	public String getDebetOrCredit() {
		return debetOrCredit;
	}
	public ArrayList<String> getCounterAccounts() {
		return counterAccounts;
	}
	public void setDebetOrCredit(String debetOrCredit) {
		this.debetOrCredit = debetOrCredit;
	}
	public void setCounterAccounts(ArrayList<String> counterAccounts) {
		this.counterAccounts = counterAccounts;
	}
	@Override
	public int compareTo(Category o) {

		return name.compareTo(o.name);
	}
}
