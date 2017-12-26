package miscellaneous;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.GregorianCalendar;
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
		StringBuilder fileName = new StringBuilder();
		GregorianCalendar cal = new GregorianCalendar();
		List<String> allLines;
		
		fileName.append("log/");
		fileName.append(cal.get(GregorianCalendar.YEAR));
		fileName.append("-");
		fileName.append(cal.get(GregorianCalendar.MONTH)+1); //+1 since months seems to be counted with 0 at the beginning
		fileName.append("-");
		fileName.append(cal.get(GregorianCalendar.DAY_OF_MONTH));
		fileName.append(".txt");
		
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
	}
}
