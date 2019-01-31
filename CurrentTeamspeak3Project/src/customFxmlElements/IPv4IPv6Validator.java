package customFxmlElements;

import java.util.regex.Pattern;

import org.apache.commons.validator.routines.InetAddressValidator;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class IPv4IPv6Validator {

	/**
	 * checks weather the string is a acceptable IPv4 or IPv6 Ip Address 
	 */
	public boolean validateIpAdress(String s) {
		if(this.validateIPv4Adress(s) || this.validateIPv6Adress(s)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean validateIPv4Adress(String s) {
		if(Pattern.matches("((25[0-5]|2[0-4][0-9]|[0-1]?[0-9]?[0-9]).){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]?[0-9])", s)) {
			return true;
		} else if(Pattern.matches("localhost", s)) {
			return true;
		} else {
			return false;
		}
	}

	// 2a05:d014:c34:f9ef:b893:9e8b:2c77:233c // standart example
	private boolean validateIPv6Adress(String s) {
		// ask for a hex number that occurs between 0 and 4 times
		//(([0-9]|[a-fA-F]){0,4}?)
		//followed by a : and the entire package is repeated 7 times
		//:){7})
		// ask for a hex number that occurs between 0 and 4 times without the following :
		//(([0-9]|[a-fA-F]){0,4}?)
		
		//regular case that there are no :: blocks. ca. 8/65536 --> 99.98779296875% this is the relevant part. rest are edge cases with :: blocks
		//exact number is: 100 - 1.2206379344092586E-4 (exact % that IPv6 has a 0) * 100 = 99.987793620655907414% : chance that no entire block is 0
		if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){7})(([0-9]|[a-fA-F]){1,4}?)", s)) {
			return true;
		//case that :: followed by 1-7 blocks (e.g.: ::d014:c34:f9ef:b893:ab:aa:aa)
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){0})::(((([0-9]|[a-fA-F]){1,4}?):){0,6}?)(([0-9]|[a-fA-F]){1,4}?)", s)) {
			return true;
		// case that :: followed by nothing 
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){0})::(((([0-9]|[a-fA-F]){1,4}?):){0}?)(([0-9]|[a-fA-F]){1,4}?){0,1}?", s)) {
			return true;
		// case that 1block:: 0-6 blocks (e.g.: 2a4c::d014:c34:f9ef:b893:ab:aa:aa) || (e.g.: 2a4c::d014)
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){1}):(((([0-9]|[a-fA-F]){1,4}?):){0,5}?)(([0-9]|[a-fA-F]){1,4}?)", s)) {
			return true;
		// case that 1 block followed by only :: (e.g.: 2a4c::)
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){1}):(((([0-9]|[a-fA-F]){1,4}?):){0}?)(([0-9]|[a-fA-F]){1,4}?){0,1}?", s)) {
			return true;
		// case that 2 blocks followed by 
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){2}):(((([0-9]|[a-fA-F]){1,4}?):){0,4}?)(([0-9]|[a-fA-F]){1,4}?)", s)) {
			return true;
		// case that 2 blocks followed by only :: (e.g.: 2a4c:aaa::)
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){2}):(((([0-9]|[a-fA-F]){1,4}?):){0}?)(([0-9]|[a-fA-F]){1,4}?){0,1}?", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){3}):(((([0-9]|[a-fA-F]){1,4}?):){0,3}?)(([0-9]|[a-fA-F]){1,4}?)", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){3}):(((([0-9]|[a-fA-F]){1,4}?):){0}?)(([0-9]|[a-fA-F]){1,4}?){0,1}?", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){4}):(((([0-9]|[a-fA-F]){1,4}?):){0,2}?)(([0-9]|[a-fA-F]){1,4}?)", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){4}):(((([0-9]|[a-fA-F]){1,4}?):){0}?)(([0-9]|[a-fA-F]){1,4}?){0,1}?", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){5}):(((([0-9]|[a-fA-F]){1,4}?):){0,1}?)(([0-9]|[a-fA-F]){1,4}?)", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){5}):(((([0-9]|[a-fA-F]){1,4}?):){0}?)(([0-9]|[a-fA-F]){1,4}?){0,1}?", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){6}):(((([0-9]|[a-fA-F]){1,4}?):){0,0}?)(([0-9]|[a-fA-F]){1,4}?)", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){6}):(((([0-9]|[a-fA-F]){1,4}?):){0,0}?)(([0-9]|[a-fA-F]){1,4}?){0,1}?", s)) {
			return true;
		//case 7 blocks followed by only one :: possible
		} else if(Pattern.matches("(((([0-9]|[a-fA-F]){1,4}?):){7}):(((([0-9]|[a-fA-F]){1,4}?):){0,0}?)(([0-9]|[a-fA-F]){1,4}?){0}", s)) {
			return true;
		//ideally never true if self built validation method never fails
		} else if(InetAddressValidator.getInstance().isValid(s)) {
			openPopup();
			System.out.println("Error using self built IPv6 validation - Validation from library accepts the IPv6 Adress");
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * in case that the self built IPv6 Address validation returns a 
	 * false and the library returns a true then open this error popUp
	 */
	private void openPopup() {
		VBox box = new VBox();
		box.setSpacing(20);
		box.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane();
		stackPane.getChildren().add(box);
		
		Label label = new Label();
		label.setFont(new Font("Cambria", 20));
		label.setText("IPv6 validity questionable. Please save the IPv6 adress and contact an admin with it.");
		Scene secondScene = new Scene(stackPane, 800, 100);

		// New window (Stage)
		Stage newWindow = new Stage();
		newWindow.setTitle("IPv6 Validation - Inconsistency");
		box.getChildren().add(label);
		newWindow.setScene(secondScene);
		newWindow.show();
	}
	
	/*
	 * Unused helper method which was used to calculate the exact percentage in 
	 * which the basic regEx is not needed but one of the extensions
	 */
	public void calculateProbabilityThatNumberHasAZeroIfRandomlyChoosen() {
		
		double midResult = 0;
		double numeralSystem = 65536; // numeral system in which a "digit" of ipv6 is given (ffff)
		
		//number of "digits" () in an ipv6 adress has
		for(int numberOfDigits = 0; numberOfDigits < 8; numberOfDigits++) {
			midResult  = (1 / numeralSystem) - midResult * (1 / numeralSystem) + midResult;
			System.out.println(midResult);
		}
	}
}
