package clientFunctions;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;

import miscellaneous.AllExistingEventAdapter;
import miscellaneous.ExtendedTS3Api;
import miscellaneous.ExtendedTS3EventAdapter;

import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;

public class LookUserInChannel {
	private static ExtendedTS3EventAdapter thisEventAdapter = new ExtendedTS3EventAdapter(AllExistingEventAdapter.CATCH_ME_IF_YOU_CAN) {};
	private static int currentChannelId = 0;

	public ExtendedTS3Api activateCatchMeIfYouCan(ExtendedTS3Api api, String userName, boolean activate) {
		if (activate) {
			api.removeTS3Listeners(AllExistingEventAdapter.CATCH_ME_IF_YOU_CAN);
			thisEventAdapter = catchMeIfYouCanEventAdapter(api, userName);
			currentChannelId = api.getClientsByName(userName).get(0).getChannelId();
			api.addTS3Listeners(thisEventAdapter);
		} else {
			api.removeTS3Listeners(AllExistingEventAdapter.CATCH_ME_IF_YOU_CAN);
		}
		return api;
	}

	private ExtendedTS3EventAdapter catchMeIfYouCanEventAdapter(ExtendedTS3Api api, String userName) {
		ExtendedTS3EventAdapter catchMeIfYouCan = new ExtendedTS3EventAdapter(AllExistingEventAdapter.CATCH_ME_IF_YOU_CAN) {
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
