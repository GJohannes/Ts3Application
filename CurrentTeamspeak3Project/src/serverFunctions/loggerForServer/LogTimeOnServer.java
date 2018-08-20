package serverFunctions.loggerForServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import miscellaneous.FileInputOutput;

public class LogTimeOnServer {

	
	public void updateAbsoluteTimeOnServer(LocalDateTime timeUserJoinedServer, String uID){
		FileInputOutput inOut = new FileInputOutput();
		
		long stayedOnServer = calculateTimeDifference(timeUserJoinedServer, LocalDateTime.now());
		
		try {
			inOut.updateStayedOnServer(uID, stayedOnServer);
		} catch (IOException e) {
			System.out.println("could not update stayed on server because of thrown IO exception");
			e.printStackTrace();
		}
	}
	
	// calculates differences in seconds between two local date times 
	private long calculateTimeDifference(LocalDateTime fromDateTime, LocalDateTime toDateTime){
//		long years = fromDateTime.until( toDateTime, ChronoUnit.YEARS);
//		fromDateTime = fromDateTime.plusYears( years );
//
//		long months = fromDateTime.until( toDateTime, ChronoUnit.MONTHS);
//		fromDateTime = fromDateTime.plusMonths( months );
//
//		long days = fromDateTime.until( toDateTime, ChronoUnit.DAYS);
//		fromDateTime = fromDateTime.plusDays( days );
//
//
//		long hours = fromDateTime.until( toDateTime, ChronoUnit.HOURS);
//		fromDateTime = fromDateTime.plusHours( hours );
//
//		long minutes = fromDateTime.until( toDateTime, ChronoUnit.MINUTES);
//		fromDateTime = fromDateTime.plusMinutes( minutes );

		long seconds = fromDateTime.until( toDateTime, ChronoUnit.SECONDS);

	return seconds;
	}
	
}
