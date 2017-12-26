package login;

import miscellaneous.FileInputOutput;

public class Ts3Client {
	public void startTs3Client(String ts3Path) throws Exception {
		if(ts3Path == null || ts3Path.equals("")){
			return;
		}
		String filename = "Ts3Starter.bat";
		//Modify path so that blanks can be read by the system		
		String[] parts = ts3Path.split(":");
		parts[0] = parts[0] +":\""; 
		parts[1] = parts[1] + "\"";
		String execString = parts[0] + parts[1];
		FileInputOutput writer = new FileInputOutput();
		
		writer.wirteTs3ClientBatch(execString,filename);
		
		Runtime.getRuntime().exec("Ts3Starter.bat");
	}
}
