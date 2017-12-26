package miscellaneous;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileInputOutput {

	public JSONObject readFile(String filename) throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		InputStreamReader read = new FileReader(filename);
		Object o = parser.parse(read);
		JSONObject json = (JSONObject) o;
		read.close();
		return json;
	}

	public void writeFile(JSONObject o, String fileName) throws IOException {
		FileWriter writer = new FileWriter(fileName);
		writer.write(o.toJSONString());
		writer.flush();
		writer.close();
	}

	public void writeAudioBatch(String audioFileName, String filename) throws IOException {
		FileWriter writer = new FileWriter(filename);
		writer.write("\"" +audioFileName +"\""); 
		writer.flush();
		writer.close();
	}

	public void wirteTs3ClientBatch(String path,String filename) throws IOException {
		FileWriter writer = new FileWriter(filename);
		writer.write("start " + path);
		writer.flush();
		writer.close();
	}
	
	/*
	 * Add json t a text file that is 
	 */
	public void writeServerLog(JSONObject json) throws IOException {
		List<String> allLines;
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String fileName = now.format(formatter);
		fileName = "log/" + fileName + ".txt";
		
		File file = new File(fileName.toString());
		if (!file.exists()) {
		     file.createNewFile();
		}
		Path path = Paths.get(file.getAbsolutePath());
		allLines = Files.readAllLines(path);
		
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(fileWriter);
		
		for(int i = 0; i < allLines.size(); i++){
			writer.write(allLines.get(i));
			writer.newLine();
		}
		
		writer.write(json.toJSONString());
		writer.flush();
		writer.close();
		
		//code if json date has to be read and difference calculated
//		LocalDateTime now = LocalDateTime.now();
//		System.out.println(now + " now");
//		String s = now.toString();
//		LocalDateTime later = LocalDateTime.parse(s);
//		System.out.println(later + " later");
//		
//		try {
//			Thread.sleep(3000);
//			LocalDateTime later = LocalDateTime.now();
//			System.out.println(now.until(later, ChronoUnit.SECONDS));
//		} catch (InterruptedException e) {
//			
//			e.printStackTrace();
//		}
	}
}
