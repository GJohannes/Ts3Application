package customFxmlElements;

import java.util.regex.Pattern;

public class IPv4IPv6Validator {

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
		//(([0-9]|[a-f]){0,4}?)
		//followed by a : and the entire package is repeated 7 times
		//:){7})
		// ask for a hex number that occurs between 0 and 4 times without the following :
		//(([0-9]|[a-f]){0,4}?)
		
		//regular case that there are no :: blocks 99.9% this is the relevant part. rest are edge cases with :: blocks
		if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){7})(([0-9]|[a-f]){1,4}?)", s)) {
			return true;
		//case that :: followed by 1-7 blocks (e.g.: ::d014:c34:f9ef:b893:ab:aa:aa)
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){0})::(((([0-9]|[a-f]){1,4}?):){0,6}?)(([0-9]|[a-f]){1,4}?)", s)) {
			return true;
		// case that :: followed by nothing 
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){0})::(((([0-9]|[a-f]){1,4}?):){0}?)(([0-9]|[a-f]){1,4}?){0,1}?", s)) {
			return true;
		// case that 1block:: 0-6 blocks (e.g.: 2a4c::d014:c34:f9ef:b893:ab:aa:aa) || (e.g.: 2a4c::d014)
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){1}):(((([0-9]|[a-f]){1,4}?):){0,5}?)(([0-9]|[a-f]){1,4}?)", s)) {
			System.out.println("Executed");
			return true;
		// case that 1 block followed by only :: (e.g.: 2a4c::)
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){1}):(((([0-9]|[a-f]){1,4}?):){0}?)(([0-9]|[a-f]){1,4}?){0,1}?", s)) {
			return true;
		// case that 2 blocks followed by 
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){2}):(((([0-9]|[a-f]){1,4}?):){0,4}?)(([0-9]|[a-f]){1,4}?)", s)) {
			return true;
		// case that 2 blocks followed by only :: (e.g.: 2a4c:aaa::)
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){2}):(((([0-9]|[a-f]){1,4}?):){0}?)(([0-9]|[a-f]){1,4}?){0,1}?", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){3}):(((([0-9]|[a-f]){1,4}?):){0,3}?)(([0-9]|[a-f]){1,4}?)", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){3}):(((([0-9]|[a-f]){1,4}?):){0}?)(([0-9]|[a-f]){1,4}?){0,1}?", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){4}):(((([0-9]|[a-f]){1,4}?):){0,2}?)(([0-9]|[a-f]){1,4}?)", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){4}):(((([0-9]|[a-f]){1,4}?):){0}?)(([0-9]|[a-f]){1,4}?){0,1}?", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){5}):(((([0-9]|[a-f]){1,4}?):){0,1}?)(([0-9]|[a-f]){1,4}?)", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){5}):(((([0-9]|[a-f]){1,4}?):){0}?)(([0-9]|[a-f]){1,4}?){0,1}?", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){6}):(((([0-9]|[a-f]){1,4}?):){0,0}?)(([0-9]|[a-f]){1,4}?)", s)) {
			return true;
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){6}):(((([0-9]|[a-f]){1,4}?):){0,0}?)(([0-9]|[a-f]){1,4}?){0,1}?", s)) {
			return true;
		//case 7 blocks followed by only one :: possible
		} else if(Pattern.matches("(((([0-9]|[a-f]){1,4}?):){7}):(((([0-9]|[a-f]){1,4}?):){0,0}?)(([0-9]|[a-f]){1,4}?){0}", s)) {
			return true;
		} else {
			return false;
		}
	}

}
