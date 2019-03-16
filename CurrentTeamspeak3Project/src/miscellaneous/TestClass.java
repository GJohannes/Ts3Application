package miscellaneous;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import InputOutput.FileInputOutput;

public class TestClass {

	public void test() {
		
		
		FileInputOutput inOut = FileInputOutput.getInstance();
		Date date = new Date();
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		
		try {
			inOut.getHistoryFromLocalDateTime(localDateTime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
