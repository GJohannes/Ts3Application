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
import java.util.ArrayList;
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
		writer.write("\"" + audioFileName + "\"");
		writer.flush();
		writer.close();
	}

	public void wirteTs3ClientBatch(String path, String filename) throws IOException {
		FileWriter writer = new FileWriter(filename);
		writer.write("start " + path);
		writer.flush();
		writer.close();
	}

	// uId of user who left + the time on the server in seconds
	public void updateStayedOnServer(String uID, long additionalStayedOnServerTime) throws IOException {
		List<String> allLines;
		// directory
		File directory = new File("TimeOnServer");
		if (!directory.exists()) {
			directory.mkdir();
		}

		File file = new File("TimeOnServer/PeoplesTimesOnTheServer.txt");
		Path path = Paths.get(file.getAbsolutePath());

		if (file.exists()) {
			allLines = Files.readAllLines(path);
			boolean foundUID = false;
			for (int i = 0; i < allLines.size(); i++) {
				// uId already know so read old value and update
				if (allLines.get(i).startsWith(uID)) {
					String[] test = allLines.get(i).split(" ");
					long oldStayedOnServerTime = Long.parseLong(test[1]);
					long newStayedOnServerTime = oldStayedOnServerTime + additionalStayedOnServerTime;
					allLines.set(i, uID + " " + newStayedOnServerTime);
					foundUID = true;
					break;
				}
			}
			// the uID was not found therefore create a new entry and add the
			// time value
			if (foundUID == false) {
				String newUID = uID + " " + additionalStayedOnServerTime;
				allLines.add(newUID);
			}
		} else {
			allLines = new ArrayList<>();
			allLines.add(uID + " " + additionalStayedOnServerTime);
		}

		
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(fileWriter);
		for (int i = 0; i < allLines.size(); i++) {
			writer.write(allLines.get(i));
			writer.newLine();
		}

		// file does not already exist
		writer.flush();
		writer.close();
	}
	
	/**
	 * 
	 * @return time on server in seconds
	 * @throws IOException 
	 */
	public long readTimeOnServer(String uID) throws IOException{
		List<String> allLines;
		File file = new File("TimeOnServer/PeoplesTimesOnTheServer.txt");
		Path path = Paths.get(file.getAbsolutePath());
		
		if(file.exists()){
			allLines = Files.readAllLines(path);
			
			for(int i = 0; i < allLines.size(); i++){
				if (allLines.get(i).startsWith(uID)) {
					String[] test = allLines.get(i).split(" ");
					long timeOnThisServer = Long.parseLong(test[1]);
					return timeOnThisServer;
				}
			}
		}
		return -1;
	}
	
	
	/*
	 * Add json t a text file that is
	 */
	public void writeServerLog(JSONObject json) throws IOException {
		File directory = new File("log");
		if (!directory.exists()) {
			directory.mkdir();
		}

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

		for (int i = 0; i < allLines.size(); i++) {
			writer.write(allLines.get(i));
			writer.newLine();
		}

		writer.write(json.toJSONString());
		writer.flush();
		writer.close();

		// code if json date has to be read and difference calculated
		// LocalDateTime now = LocalDateTime.now();
		// System.out.println(now + " now");
		// String s = now.toString();
		// LocalDateTime later = LocalDateTime.parse(s);
		// System.out.println(later + " later");
		//
		// try {
		// Thread.sleep(3000);
		// LocalDateTime later = LocalDateTime.now();
		// System.out.println(now.until(later, ChronoUnit.SECONDS));
		// } catch (InterruptedException e) {
		//
		// e.printStackTrace();
		// }
	}
}
