package customFxmlElements;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class IpAdressTextField extends TextField {
	private boolean isTextFieldInputValid = false;
	private Text text;
	
	public IpAdressTextField() {
		// do after every key release
		this.setOnKeyReleased(event -> {
			this.checkValidity();
		});
	}
	
	//should be done after setText but no possibiliy found yet
	public void checkValidity(){
		if(this.isValidIpAdress(this.getText())){
			this.nowValid();
		} else {
			this.nowInValid();
		}
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
	
	private boolean isValidIpAdress(String input){
		String[] array = input.split("\\.");
		if (array.length != 4) {
			return false;
		} else {
			//each array field has to represent a number between 0 - 255
			for (int i = 0; i < array.length; i++) {
				try {
					int temp = Integer.parseInt(array[i]);
					if(temp > 255 || temp < 0){
						return false;
					}
				//could not cast seperated part to integer therefore not all numbers
				} catch (Exception e) {
					return false;
				}
			}
			// nothing killed it therefore it mus be a valid ip adress
			return true;
		}
	}
}
