package serverFunctions.riotApi;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import InputOutput.RiotApiIO;
import miscellaneous.ExtendedTS3Api;
import serverFunctions.riotApi.DataObjects.RiotApiPersitentUserInformation;
import serverFunctions.riotApi.DataObjects.RiotApiUser;
import serverFunctions.riotApi.DataObjects.WinKdaMostDamageHolder;

public class RiotApiPersistentDataLogic {
	private RiotApiIO riotApiIO;
	private ExtendedTS3Api api;

	public RiotApiPersistentDataLogic(ExtendedTS3Api api) {
		this.riotApiIO = riotApiIO.getInstance();
		this.api = api;
	}

	public void initializeApiCheckUsers(RiotApiNotification apiNotification, int initializerId) {
		ArrayList<RiotApiPersitentUserInformation> readRiotApiPersitance = riotApiIO.readRiotApiPersitance();
		for (RiotApiPersitentUserInformation riotApiPersitentObject : readRiotApiPersitance) {
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
		RiotApiPersitentUserInformation userOnHardDrive = this.getThePersistentUser(user);
		double averageKDAonHDD = userOnHardDrive.getAverageKDA();
		long numberOfGamesonHDD = userOnHardDrive.getNumberOfGamesPlayed();
		double newKDA = kdaHolder.getKda();
		long numberOfNewGames = 1;
		double newAverageKDA = ((averageKDAonHDD * numberOfGamesonHDD) + (newKDA * numberOfNewGames)) / (numberOfGamesonHDD + numberOfNewGames);
		long newNumberOfGames =  numberOfGamesonHDD + numberOfNewGames;
		
		RiotApiPersitentUserInformation newPersistenceOnHDD = 
				new RiotApiPersitentUserInformation(user.getCaseCorrectNickName(), newAverageKDA,newNumberOfGames,userOnHardDrive.isPartOfRepeatedApiCheck(), user.getEncryptedAccountId());
		riotApiIO.updateRiotApiPersistance(newPersistenceOnHDD);

		return newAverageKDA;
	}

	public void updatePersistentIsAddedToRepeatingCheckup(RiotApiUser user, boolean isAddedToRepeatingCheckup) {
		RiotApiPersitentUserInformation thePersistentUser = this.getThePersistentUser(user);
		thePersistentUser.setPartOfRepeatedApiCheck(isAddedToRepeatingCheckup);
		riotApiIO.updateRiotApiPersistance(thePersistentUser);
	}

	private RiotApiPersitentUserInformation getThePersistentUser(RiotApiUser user) {
		RiotApiPersitentUserInformation userPersistentOnHardDrive = null;
		ArrayList<RiotApiPersitentUserInformation> readRiotApiPersitance = riotApiIO.readRiotApiPersitance();
		for (RiotApiPersitentUserInformation riotApiPersitentObject : readRiotApiPersitance) {
			if (riotApiPersitentObject.getEncryptedAccountId().equals(user.getEncryptedAccountId())) {
				userPersistentOnHardDrive = riotApiPersitentObject;
			}
		}
		if (userPersistentOnHardDrive == null) {
			return new RiotApiPersitentUserInformation(user.getCaseCorrectNickName(), 0.0, 0, true,user.getEncryptedAccountId());
		}
		return userPersistentOnHardDrive;
	}
}