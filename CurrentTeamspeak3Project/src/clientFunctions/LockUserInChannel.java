package clientFunctions;

import miscellaneous.AllExistingEventAdapter;
import miscellaneous.ExtendedTS3Api;
import miscellaneous.ExtendedTS3EventAdapter;

import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;

public class LockUserInChannel {
	public void activateCatchMeIfYouCan(ExtendedTS3Api api, String userName) {
		api.addTS3Listeners(catchMeIfYouCanEventAdapter(api, userName, api.getClientsByName(userName).get(0).getChannelId()));
	}

	public void deactivateCatchMeIfYouCan(ExtendedTS3Api api) {
		api.removeTS3Listeners(AllExistingEventAdapter.CATCH_ME_IF_YOU_CAN);
	}
	
	private ExtendedTS3EventAdapter catchMeIfYouCanEventAdapter(ExtendedTS3Api api, String userName, int currentChannelId) {
		ExtendedTS3EventAdapter catchMeIfYouCan = new ExtendedTS3EventAdapter(AllExistingEventAdapter.CATCH_ME_IF_YOU_CAN) {
			@Override
			public void onClientMoved(ClientMovedEvent e) {
				if (e.getClientId() == api.getClientsByName(userName).get(0).getId() && e.getTargetChannelId() != currentChannelId) {
						api.moveClient(e.getClientId(), currentChannelId);
						api.sendPrivateMessage(e.getClientId(), "You are not allowed to leave this channel");
						System.out.println("sent message");	
				}
			}
		};
		return catchMeIfYouCan;
	}
}
