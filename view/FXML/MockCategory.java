package view.FXML;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MockCategory {

	private final SimpleIntegerProperty amount;
	private final SimpleStringProperty name;
	
	
	
	
	public MockCategory(Integer amount, String name) {
		this.amount = new SimpleIntegerProperty(amount);
		this.name = new SimpleStringProperty(name);
		
	}



	public SimpleIntegerProperty getAmount() {
		return amount;
	}



	public SimpleStringProperty getName() {
		return name;
	}


}
