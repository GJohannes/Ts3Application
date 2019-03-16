package serverFunctions.riotApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import InputOutput.FileInputOutput;
import InputOutput.RiotApiIO;
import miscellaneous.ExtendedTS3Api;

public class RiotApiPersistentData {
	private RiotApiIO riotApiIO;
	private ExtendedTS3Api api;

	public RiotApiPersistentData(ExtendedTS3Api api) {
		this.riotApiIO = riotApiIO.getInstance();
		this.api = api;
	}

	public void initializeApiCheckUsers(RiotApiNotification apiNotification, int initializerId) {
		ArrayList<RiotApiPersitentObject> readRiotApiPersitance = riotApiIO.readRiotApiPersitance();
		for (RiotApiPersitentObject riotApiPersitentObject : readRiotApiPersitance) {
			try {
				if(riotApiPersitentObject.isPartOfRepeatedApiCheck()) {
					apiNotification.addUser(riotApiPersitentObject.getCaseCorrectNickName());
					api.sendPrivateMessage(initializerId, "Added: " + riotApiPersitentObject.getCaseCorrectNickName() + " to Riot api Check");					
				} else {
					api.sendPrivateMessage(initializerId, "NOT Added: " + riotApiPersitentObject.getCaseCorrectNickName() + " because he is set as not part of checkup");
				}
			} catch (IOException | ParseException e) {
				api.logToCommandline("Error adding the following user to Riot api check:" + riotApiPersitentObject.getCaseCorrectNickName());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Stores the new average kda on harddrive
	 * 
	 * @param user      which corosponds to the data
	 * @param kdaHolder holds the newest kda
	 * @return returns the average kda (includs the new kda)
	 */
	public double updatePersistentAverageKda(RiotApiUser user, WinKdaMostDamageHolder kdaHolder) {
		RiotApiPersitentObject userOnHardDrive = this.getThePersistentUser(user);
		double averageKDAonHDD = userOnHardDrive.getAverageKDA();
		long numberOfGamesonHDD = userOnHardDrive.getNumberOfGamesPlayed();
		double newKDA = kdaHolder.getKda();
		long numberOfNewGames = 1;
		double newAverageKDA = ((averageKDAonHDD * numberOfGamesonHDD) + (newKDA * numberOfNewGames)) / (numberOfGamesonHDD + numberOfNewGames);

		RiotApiPersitentObject newPersistenceOnHDD = new RiotApiPersitentObject(user.getCaseCorrectNickName(), newAverageKDA, numberOfGamesonHDD + numberOfNewGames,
				userOnHardDrive.isPartOfRepeatedApiCheck());
		riotApiIO.updateRiotApiPersistance(newPersistenceOnHDD);

		return newAverageKDA;
	}

	public void updatePersistentIsAddedToRepeatingCheckup(RiotApiUser user, boolean isAddedToRepeatingCheckup) {
		RiotApiPersitentObject thePersistentUser = this.getThePersistentUser(user);
		thePersistentUser.setPartOfRepeatedApiCheck(isAddedToRepeatingCheckup);
		riotApiIO.updateRiotApiPersistance(thePersistentUser);
	}

	private RiotApiPersitentObject getThePersistentUser(RiotApiUser user) {
		RiotApiPersitentObject userPersistentOnHardDrive = null;
		ArrayList<RiotApiPersitentObject> readRiotApiPersitance = riotApiIO.readRiotApiPersitance();
		for (RiotApiPersitentObject riotApiPersitentObject : readRiotApiPersitance) {
			if (riotApiPersitentObject.getCaseCorrectNickName().equals(user.getCaseCorrectNickName())) {
				userPersistentOnHardDrive = riotApiPersitentObject;
			}
		}
		if (userPersistentOnHardDrive == null) {
			return new RiotApiPersitentObject(user.getCaseCorrectNickName(), 0.0, 0, true);
		}
		return userPersistentOnHardDrive;
	}
}
