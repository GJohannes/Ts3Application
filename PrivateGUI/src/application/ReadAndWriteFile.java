package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


public class ReadAndWriteFile {

	public List<String> readInputFile(String filePath) {
		File file = new File(filePath);
		List<String> allLineInputs = new ArrayList<String>();

		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				allLineInputs.add(line);
			}
			fileReader.close();
			bufferedReader.close();
		} catch (Exception e) {
			
		}
		return allLineInputs;
	}

	
	
	public void writeTheFile(String path, List<String> allLines) {
		
		try {
			FileWriter writer = new FileWriter(path);
			for(int i = 0; i < allLines.size() ; i++) {
				writer.write(allLines.get(i));
				writer.write(System.lineSeparator());
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
}
