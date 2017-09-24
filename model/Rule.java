package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Rule implements Serializable{
	
	String keyword;
	float minAmount;
	float maxAmount;
	LocalDate startDate;
	LocalDate endDate;
	String counterAccount;
	String counterAcccountDiscription;
	String debetOrCredit;
	
	
	public String getKeyword() {
		return keyword;
	}
	public float getMinAmount() {
		return minAmount;
	}
	public float getMaxAmount() {
		return maxAmount;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}

	public String getCounterAcccountDiscription() {
		return counterAcccountDiscription;
	}
	public String getDebetOrCredit() {
		return debetOrCredit;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public void setMinAmount(float minAmount) {
		this.minAmount = minAmount;
	}
	public void setMaxAmount(float maxAmount) {
		this.maxAmount = maxAmount;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public String getCounterAccount() {
		return counterAccount;
	}
	public void setCounterAccount(String counterAccount) {
		this.counterAccount = counterAccount;
	}
	public void setCounterAcccountDiscription(String counterAcccountDiscription) {
		this.counterAcccountDiscription = counterAcccountDiscription;
	}
	public void setDebetOrCredit(String debetOrCredit) {
		this.debetOrCredit = debetOrCredit;
	}
	
	@Override
	public String toString(){
		return keyword+ "; min "+minAmount + " max "+maxAmount+ "; "+startDate+" "+endDate+"; "+counterAcccountDiscription;
	}
	

	
}
