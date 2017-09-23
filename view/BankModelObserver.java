package view;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

import controller.BankControllerInterface;
import model.Transaction;

public interface BankModelObserver {

	
	public BankControllerInterface getController();
	public void updateData();

	
	public String toString();
	
	public void viewRevalidate();
	public void setTempSearch(ArrayList<Transaction> tempSearch);
	public void addTempSearchToJlist();
	void addSetCategoryPanel();
}
