package clientFunctions;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;

import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;

public class LookUserInChannel {
	private static TS3EventAdapter thisEventAdapter = new TS3EventAdapter() {
	};
	private static int currentChannelId = 0;

	public TS3Api activateCatchMeIfYouCan(TS3Api api, String userName, boolean activate) {
		if (activate) {
			api.removeTS3Listeners(thisEventAdapter);
			thisEventAdapter = catchMeIfYouCanEventAdapter(api, userName);
			currentChannelId = api.getClientsByName(userName).get(0).getChannelId();
			api.addTS3Listeners(thisEventAdapter);
		} else {
			api.removeTS3Listeners(thisEventAdapter);
		}
		return api;
	}

	private TS3EventAdapter catchMeIfYouCanEventAdapter(TS3Api api, String userName) {
		TS3EventAdapter catchMeIfYouCan = new TS3EventAdapter() {
			@Override
			public void onClientMoved(ClientMovedEvent e) {

				if (e.getClientId() == api.getClientsByName(userName).get(0).getId()) {
					api.moveClient(e.getClientId(), currentChannelId);
					api.sendPrivateMessage(e.getClientId(), "You are not allowed to leave this channel");
				}
			}
		};

		return catchMeIfYouCan;
	}

}
