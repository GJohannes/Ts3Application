package customFxmlElements;

import javafx.scene.control.TextField;

public class PortTextField extends TextField {
private boolean isTextFieldInputValid = false;
	
	public PortTextField() {
		// do after every key release
		this.setOnKeyReleased(event -> {
			this.checkValidity();
		});	
	}
	
	//should be done after setText but no possibiliy found yet
	public void checkValidity(){
		if(this.isValidPort(this.getText())){
			this.nowValid();
		} else {
			this.nowInValid();
		}
	}
	
	private boolean isValidPort(String input){
		try{
			int temp = Integer.parseInt(input);
			if(temp > 65535 || temp < 0 ) {
				return false;
			}
		} catch (Exception e){
			return false;
		}
		return true;
	}
	
	private void nowValid() {
		this.isTextFieldInputValid = true;
		// #7FE817 color hummingbird green
		this.setStyle("-fx-background-color: #7FE817;");
	}

	private void nowInValid() {
		this.isTextFieldInputValid = false;
		// #F75D59 color light red
		this.setStyle("-fx-background-color: #F75D59;");
	}

	public boolean isValid() {
		return this.isTextFieldInputValid;
	}
	
	
}
