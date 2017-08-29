package model;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SubCategory extends Category {
	
	private static final long serialVersionUID = 1L;
	
	Category parentCategory;
		
	
	public SubCategory(SimpleStringProperty fixedOrVariable, SimpleStringProperty name, Category parentCategory) {
		super(fixedOrVariable, name);
		this.parentCategory =parentCategory;
	}
	
	
	
	


	// Overriden behaviour (maybe not needed since extended?? i think so
	@Override
	public String toString(){
		return name;
	}
	@Override
	public int compareTo(Category o) {

		return name.compareTo(o.name);
	}
}
