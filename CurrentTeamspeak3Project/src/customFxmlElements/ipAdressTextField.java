package customFxmlElements;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class ipAdressTextField extends TextField{
	private boolean isTextFieldInputValid = false;
	
	public void ipAdressTextField(){
		this.setOnKeyPressed(event -> System.out.println("fdasd"));
	}
	
	public void nowValid() {
		this.isTextFieldInputValid = true;
		// #7FE817 color hummingbird green 
		this.setStyle("-fx-background-color: #7FE817;");
	}

	public void nowInValid() {
		this.isTextFieldInputValid = false;
		// #F75D59 color light red
		this.setStyle("-fx-background-color: #F75D59;");
	}
	
	public boolean isValid() {
		return this.isTextFieldInputValid;
	}
}
