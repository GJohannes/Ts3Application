package InputOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import serverFunctions.riotApi.DataObjects.RiotApiPersitentUserInformation;
import serverFunctions.riotApi.DataObjects.RiotApiUser;

/**
 * This class is used for all Hard Drive access of the application. Each methdo
 * that can be accessd by multiple threads is synchronized to prevent lost
 * updates from happening
 * 
 * 
 * @author Johannes
 *
 */
public class FileInputOutput {

	private static FileInputOutput instance = null;

	private FileInputOutput() {

	}

	public static FileInputOutput getInstance() {
		if (instance == null) {
			instance = new FileInputOutput();
		}
		return instance;
	}

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
	public synchronized void updateStayedOnServer(String uID, long additionalStayedOnServerTime) throws IOException {
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
	public synchronized long readTimeOnServer(String uID) throws IOException {
		List<String> allLines;
		File file = new File("TimeOnServer/PeoplesTimesOnTheServer.txt");
		Path path = Paths.get(file.getAbsolutePath());

		if (file.exists()) {
			allLines = Files.readAllLines(path);

			for (int i = 0; i < allLines.size(); i++) {
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
	 * This method can be invoked from multiple threads and therefore has to be
	 * synchronized since a lost update for the log files is possible and is
	 * suspected for the past (29.11.2018)
	 */
	public synchronized void writeServerLog(JSONObject json) throws IOException {
		File directory = new File(DefinedStringsTSServerLog.logFolderName.getValue());
		if (!directory.exists()) {
			directory.mkdir();
		}

		List<String> allLines;
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String fileName = now.format(formatter);
		fileName = DefinedStringsTSServerLog.logFolderName.getValue() + "/" + fileName + ".txt";

		File file = new File(fileName.toString());
		if (!file.exists()) {
			file.createNewFile();
		}
		Path path = Paths.get(file.getAbsolutePath());
		allLines = Files.readAllLines(path, StandardCharsets.UTF_8);

		BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);

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

	public synchronized ArrayList<JSONObject> getHistoryFromLocalDateTime(LocalDateTime localDateTime) throws IOException {
		String fileName = localDateTime.toString().split("T")[0];
		fileName = fileName + ".txt";

		String folderName = DefinedStringsTSServerLog.logFolderName.getValue();
		File file = new File(folderName + "/" + fileName);
		Path path = Paths.get(file.getAbsolutePath());
		ArrayList<JSONObject> allEventsAsJSON = new ArrayList<>(100);
		JSONParser parser = new JSONParser();

		try {
			List<String> allEvents = Files.readAllLines(path, StandardCharsets.UTF_8);
			for (int i = 0; i < allEvents.size(); i++) {
				try {
					allEventsAsJSON.add((JSONObject) parser.parse(allEvents.get(i).trim()));
				} catch (ParseException e) {
					System.out.println("ERROR - parsing written jsons. log file has to be manipulated or corrupted");
				}
			}
		} catch (NoSuchFileException e) {
			// System.out.println("Selected a date from which no data was found");
			return allEventsAsJSON;
		}

		return allEventsAsJSON;
	}

	
}
