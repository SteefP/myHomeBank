package model;
import java.io.Serializable;
import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;



public class Transaction implements Cloneable, Serializable, Comparable<Transaction>{
	private static final long serialVersionUID = 1L;
	
	final String account;
	final LocalDate transactionDate;
	final float amount;
	final String debetOrCredit;
	final String description;
	final String otherEndOfTransactionAccount;
	final String otherEndOfTransactionDiscription;
	
	
	Category category;
	
	// For future applications "split amount" and "set amount in right month.."
	float adjustedAmount;
	LocalDate adjustedDate;

	
	// Getters for all fields
	public LocalDate getTransactionDate() {
		return transactionDate;
	}
	public String getOtherEndOfTransactionName() {
		return otherEndOfTransactionDiscription;
	}
	public String getOtherEndOfTransactionAccount() {
		return otherEndOfTransactionAccount;
	}
	public float getAmount() {
		return amount;
	}
	public String getDebetOrCredit() {
		return debetOrCredit;
	}
	public Category getCategory() {
		return category;
	}
	public float getAdjustedAmount() {
		return adjustedAmount;
	}
	public LocalDate getAdjustedDate() {
		return adjustedDate;
	}
	public String getDescription() {
		return description;
	}
	
	
	
	//Getters for all fields for use in JavaFX, only necessary for primitives 
	public SimpleStringProperty getSimpleTransactionDate() {
		return new SimpleStringProperty(transactionDate.toString());
		}
	public SimpleStringProperty getSimpleOtherEndOfTransactionName() {
		return new SimpleStringProperty( otherEndOfTransactionDiscription);
	}
	public SimpleStringProperty getSimpleOtherEndOfTransactionAccount() {
		return new SimpleStringProperty( otherEndOfTransactionAccount);
	}
	public SimpleStringProperty getSimpleDebetOrCredit() {
		return new SimpleStringProperty( debetOrCredit);
	}
	public SimpleStringProperty getSimpleDescription() {
		return new SimpleStringProperty( description);
	}
	public SimpleFloatProperty getSimpleAmount() {
		return new SimpleFloatProperty(amount);
	}
	public SimpleFloatProperty getSimpleAdjustedAmount() {
		return new SimpleFloatProperty (adjustedAmount);
	}

	
	//Setters for non final fields 
	public void setAdjustedAmount(float adjustedAmount) {
		this.adjustedAmount = adjustedAmount;
	}
	public void setAdjustedDate(LocalDate adjustedDate) {
		this.adjustedDate = adjustedDate;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	//Setters for non final fields for JavaFX
	public void setSimpleAdjustedAmount(SimpleFloatProperty adjustedAmount) {
		this.adjustedAmount = adjustedAmount.getValue();
	}
	
	


	
	// Constructor requiring all final fields
	public Transaction(String account, LocalDate transactionDate, 
			float amount, String debetOrCredit, String description, String otherEndOfTransactionAccount, String otherEndOfTransactionDiscription) {
		this.account = account;
		this.transactionDate = transactionDate;
		
		this.amount = amount;
		this.debetOrCredit = debetOrCredit;
		this.description = description;
		this.otherEndOfTransactionDiscription = otherEndOfTransactionDiscription;
		this.otherEndOfTransactionAccount = otherEndOfTransactionAccount;
	}

	
	
	@Override
	public String toString(){
		return transactionDate + " " + debetOrCredit + " " + amount+ " "+description + " category: " + category + " "+ otherEndOfTransactionAccount + " "+ otherEndOfTransactionDiscription;
	}

	@Override
	public int compareTo(Transaction o) {
		return (int) (amount - o.amount);
	}

	
	

}
