package serverFunctions.riotApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.parser.ParseException;

import InputOutput.RiotApiIO;
import miscellaneous.ExtendedTS3Api;
import serverFunctions.riotApi.DataObjects.RiotApiUser;
import serverFunctions.riotApi.DataObjects.WinKdaMostDamageHolder;

public class RiotApiPersistentDataLogic {
	private RiotApiIO riotApiIO;

	public RiotApiPersistentDataLogic() {
		this.riotApiIO = riotApiIO.getInstance();
	}

	public boolean userAlreadyStoredOnHDD(String encryptedAccountID) {
		ArrayList<RiotApiUser> readRiotApiPersitance = riotApiIO.readAllRiotApiPersitantUsers();
		for (RiotApiUser riotApiPersitentUserInformation : readRiotApiPersitance) {
			if (riotApiPersitentUserInformation.getEncryptedAccountId().equals(encryptedAccountID)) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<RiotApiUser> getAllRepeatedApiCheckAddedUsersFromHDD() {
		ArrayList<RiotApiUser> readRiotApiPersitance = riotApiIO.readAllRiotApiPersitantUsers();
		
		Iterator<RiotApiUser> it = readRiotApiPersitance.iterator();
		while(it.hasNext()) {
			RiotApiUser user = it.next();
			if(!user.isPartOfRepeatedApiCheck()) {
				it.remove();
			}
		}
		return readRiotApiPersitance;
	}

	/**
	 * Stores the new average kda on harddrive
	 * 
	 * @param user      which corosponds to the data
	 * @param kdaHolder holds the newest kda
	 * @return returns the average kda (includs the new kda)
	 */
	public double updateAverageKdaAndGamesPlayedOnHDD(RiotApiUser user, WinKdaMostDamageHolder kdaHolder) {
		double averageKDAonHDD = user.getAverageKDA();
		long numberOfGamesonHDD = user.getNumberOfGamesPlayed();
		double newKDA = kdaHolder.getKda();
		long numberOfNewGames = 1;
		double newAverageKDA = ((averageKDAonHDD * numberOfGamesonHDD) + (newKDA * numberOfNewGames)) / (numberOfGamesonHDD + numberOfNewGames);
		long newNumberOfGames = numberOfGamesonHDD + numberOfNewGames;
		
		user.setAverageKDA(newAverageKDA);
		user.incrementNumberOfGamesPlayed();
		
		riotApiIO.updateUserPersistance(user);
		return newAverageKDA;
	}
	
	public void updateUserPersistantInformationOnHDD(RiotApiUser user) {
		riotApiIO.updateUserPersistance(user);
	}

	/**
	 * get the user from hdd. if no one is existing a new user is created with the given data
	 * 
	 * @param encryptedAccountId used to find a mathc of existing users
	 * @param caseCorrectNickName used to 
	 * @return existing/new user
	 */
	public RiotApiUser getRiotApiUserFromHDD(String encryptedAccountId, String caseCorrectNickName) {
		ArrayList<RiotApiUser> readAllRiotApiPersitantUsers = riotApiIO.readAllRiotApiPersitantUsers();
		for (RiotApiUser riotApiUser : readAllRiotApiPersitantUsers) {
			if(riotApiUser.getEncryptedAccountId().equals(encryptedAccountId)) {
				return riotApiUser;
			}
		}
		
		//case that user was not on hdd stored 
		return new RiotApiUser(encryptedAccountId, caseCorrectNickName, -1, 0.0, 0, true,System.currentTimeMillis());
	}
}
