package serverFunctions.riotApi.DataObjects;

import org.eclipse.jetty.server.Authentication.User;

public class UserAddedInformation {
	private boolean userWasSuccessfullyAdded;
	private boolean userWasAddedInThePast = false; // field is not necessarily filed
	
	public UserAddedInformation(boolean userWasSuccessfullyAdded, boolean userWasAddedInThePast ) {
		this.userWasSuccessfullyAdded = userWasSuccessfullyAdded;
		this.userWasAddedInThePast = userWasAddedInThePast;
	}
	
	public UserAddedInformation(boolean userWasSuccessfullyAdded) {
		this.userWasSuccessfullyAdded = userWasSuccessfullyAdded;
	}
	
	public boolean isUserWasSuccessfullyAdded() {
		return userWasSuccessfullyAdded;
	}

	public boolean isUserWasAddedInThePast() {
		return userWasAddedInThePast;
	}
	
}
