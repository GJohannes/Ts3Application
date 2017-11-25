package application;

import java.util.List;

public class Translator {
	
	/*
	 * translation based on how hardware works:
	 * date (example 21.01.1970) to integer 
	 * --> 21011970
	 * --> to hex number 1409E02
	 * --> split hex number get last 4 entries (9E02)
	 * --> to decial 40450 and save in word67
	 * --> remaining first numbers (140)
	 * --> to decimal 320 and save in word68
	 */
	public List<String> translateAndInsertDate(List<String> allLines, int dateTextFieldInput){
		//int i = 21011970;
		
		// to upper case since letters have to be to upper case
		String hex = Integer.toHexString(dateTextFieldInput).toUpperCase();
		String word67asHex = "";
		String word68asHex = "";
		int word67asIntFinalValue = 0;
		int word68asIntFinalValue = 0;

		char[] stringAsArray = hex.toCharArray();
		// string is longer than 4 hex letters meaning we need both word 68 and 67
		if (stringAsArray.length > 4) {
			for (int j = 0; j < 4; j++) {
				word67asHex = word67asHex + stringAsArray[stringAsArray.length - 4 + j];
			}
			for (int j = 0; j < stringAsArray.length - 4; j++) {
				word68asHex = word68asHex + stringAsArray[j];
			}
			word67asIntFinalValue = hex2decimal(word67asHex);
			word68asIntFinalValue = hex2decimal(word68asHex);
		} else {
			//else should theoretically never been reached since 
			//the given date semantic always produces a value between 5 and 8 digits
			System.out.println("Else block should never be reached");
			for (int j = 0; j < stringAsArray.length; j++) {
				word67asHex = word67asHex + stringAsArray[stringAsArray.length - 4 + j];
			}
			word67asIntFinalValue = hex2decimal(word67asHex);
		}
		
		allLines = insertDate(allLines, Integer.toString(word67asIntFinalValue), Integer.toString(word68asIntFinalValue));
		return allLines;
	}
	
	/*
	 * translation based on how hardware requires the data:
	 * Serial Number always 4 characters long (number or letter)
	 * 1) translate each letter to its specific asci value
	 * example : X-> 88, D->68, 9->57, 9 ->57 
	 * 2) translate asci to hex
	 * example : 88->58, 68->44,57->39, 57-> 39
	 * (SWAP !!)
	 * 3) word92 consits of second+first hex value combined (5844)
	 * 4) word93 consits of fourth+third hex value combined (3939)
	 * 5) translate hex value to dezimal
	 * examle word92(5844) --> 22596, word93(3939)--> 14649
	 */
	public List<String> translateAndInsertSerialNumber (List<String> allLines, String serialNumberTextFieldInput){
		//String serialNumber = "XD99";
		serialNumberTextFieldInput = serialNumberTextFieldInput.toUpperCase();
		
		String serialNumber = serialNumberTextFieldInput;
		char[] initialWordAsCharArray = serialNumber.toCharArray();
		String[] singleHexString = new String[initialWordAsCharArray.length];
		String[] wordAsHex = new String[singleHexString.length/2 + (singleHexString.length % 2)];
		
		// -------------------------------------------------------
		//Each entry to asci and the to hexstring
		for(int i = 0; i < initialWordAsCharArray.length; i++) {
			singleHexString[i] = Integer.toHexString(initialWordAsCharArray[i]);
		}
		System.out.println("Single Hex letters");
		System.out.println(singleHexString[0]);
		System.out.println(singleHexString[1]);
		System.out.println(singleHexString[2]);
		System.out.println(singleHexString[3]);
		
//		String hexStringFirstCharacter = Integer.toHexString(initialWordAsCharArray[0]);
//		String hexStringSecondCharacter= Integer.toHexString(initialWordAsCharArray[1]);
//		String hexStringThirdCharacter = Integer.toHexString(initialWordAsCharArray[2]);
//		String hexStringFourthCharacter =Integer.toHexString(initialWordAsCharArray[3]);
		
		
		//--------------------------------------------------------
		//Build pairs of two together //IMPORTANT Last one first (hardware)
		for(int i = 0; i < singleHexString.length/2; i++) {
			wordAsHex[i] = singleHexString[i*2+1] + singleHexString[i*2]; 
		}
		
		System.out.println("added hex letters");
		System.out.println(wordAsHex[0]);
		System.out.println(wordAsHex[1]);
		
		//case uneven length take last entry and insert in previous last entry
		if(singleHexString.length % 2 == 1) {
			wordAsHex[wordAsHex.length-1] = singleHexString[singleHexString.length-1];
		}
//		String word92AsHex = hexStringFirstCharacter + hexStringSecondCharacter;
//		String word93AsHex = hexStringThirdCharacter + hexStringFourthCharacter;

		//--------------------------------------------------------
		int[] word92Plus_i_AsIntFinalValue = new int [5];
		for(int i = 0; i < word92Plus_i_AsIntFinalValue.length; i++) {
			word92Plus_i_AsIntFinalValue[i]=0;
		}
			
		
		for(int i = 0; i < wordAsHex.length; i++) {
			word92Plus_i_AsIntFinalValue[i] = hex2decimal(wordAsHex[i]);
		}
		
		System.out.println("Words 92,93,...");
		System.out.println(word92Plus_i_AsIntFinalValue[0]);
		System.out.println(word92Plus_i_AsIntFinalValue[1]);
		
//		int word92AsIntFinalValue = hex2decimal(word92AsHex);
//		int word93AsIntFinalValue = hex2decimal(word93AsHex);
	
		allLines = insertSerialNumber(allLines, word92Plus_i_AsIntFinalValue);
	
		return allLines;
	}
	
	
	private int hex2decimal(String s) {
		String digits = "0123456789ABCDEF";
		s = s.toUpperCase();
		int val = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = digits.indexOf(c);
			val = 16 * val + d;
		}
		return val;
	}
	
	private List<String> insertDate(List<String> allLines, String word67, String word68){
		for(int i = 0; i < allLines.size(); i++) {
			if(allLines.get(i).equals("[OEMData1]")) {
				//40 chosen because the relevant words 67, 68 , 93, 94 are in the next 40 lines
				for(int j = 0; j < 40; j++) {
					if(allLines.get(i+j).startsWith("Word67=")) {
						allLines.set(i+j, "Word67=" + word67);
					}
					
					if(allLines.get(i+j).startsWith("Word68=")) {
						allLines.set(i+j, "Word68=" + word68);
					}
				}
			}
		}
		return allLines;
	}
	
	private List<String> insertSerialNumber(List<String> allLines, int[] word92Plus_i_AsIntFinalValue){
		for(int i = 0; i < allLines.size(); i++) {
			if(allLines.get(i).equals("[OEMData1]")) {
				//40 chosen because the relevant words 67, 68 , 93, 94 are in the next 40 lines
				for(int j = 0; j < 40; j++) {
					if(allLines.get(i+j).startsWith("Word92=")) {
						allLines.set(i+j, "Word92=" + word92Plus_i_AsIntFinalValue[0]);
					}
					
					if(allLines.get(i+j).startsWith("Word93=")) {
						allLines.set(i+j, "Word93=" + word92Plus_i_AsIntFinalValue[1]);
					}
					
					if(allLines.get(i+j).startsWith("Word94=")) {
						allLines.set(i+j, "Word94=" + word92Plus_i_AsIntFinalValue[2]);
					}
					
					if(allLines.get(i+j).startsWith("Word95=")) {
						allLines.set(i+j, "Word95=" + word92Plus_i_AsIntFinalValue[3]);
					}
					
					if(allLines.get(i+j).startsWith("Word96=")) {
						allLines.set(i+j, "Word96=" + word92Plus_i_AsIntFinalValue[4]);
					}
					
				}
			}
		}
		return allLines;
	}
}
