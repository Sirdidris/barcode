package application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Barcode {
	
	// what define barcode
	private final SimpleStringProperty name;
	private final SimpleDoubleProperty amount;

	
	// constructor for barcode
	public Barcode (String newName, double newAmount){
		this.name = new SimpleStringProperty(newName);
		this.amount = new SimpleDoubleProperty(newAmount);
	}
	
	//getter
	public String getName (){
		return name.get();
	}
	
	//setter
	public void setName(String numbers) {
		name.set(numbers);
	}
	
	//getter
	public double getAmount(){
		return amount.get();
	}
	
	//setter
	public void setAmount(double numbs){
		amount.set(numbs);
	}

}
