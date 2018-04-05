package miscellaneous;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import serverFunctions.LoggedServerEvents;
import serverFunctions.UserLoggedInEntity;


public class ExtendedTS3Api extends TS3Api{
	private static List<ExtendedTS3EventAdapter> allCurrentActiveEventAdapters = new ArrayList<>();
	
	public ExtendedTS3Api(TS3Query query) {
		super(query);
	}
 
	@Deprecated
	public void addTS3Listeners(TS3EventAdapter adapter) throws Exception {
		System.out.println("DO NOT USE THIS METHOD");
		throw new UsingDeprecatedMethodException();
	}
	
	@Deprecated
	public void removeTS3Listeners(TS3EventAdapter adapter) throws Exception {
		System.out.println("DO NOT USE THIS METHOD");
		throw new UsingDeprecatedMethodException();
	}
	
	public void addTS3Listeners(ExtendedTS3EventAdapter adapter) {
		for(int i = 0; i < allCurrentActiveEventAdapters.size(); i++) {
			if(allCurrentActiveEventAdapters.get(i).getName().equals(adapter.getName())) {
				System.out.println("Could not add adapter since duplicates are not allowed");
				return;
			}
		}
		// loop did not return therefore possible to add since it is not a duplicate
		allCurrentActiveEventAdapters.add(adapter);
		super.addTS3Listeners(adapter);
	}	
	
	public void removeTS3Listeners(Enum<AllExistingEventAdapter> name) {
		for(int i = 0; i < allCurrentActiveEventAdapters.size(); i++) {
			if(allCurrentActiveEventAdapters.get(i).getName().equals(name)){
				super.removeTS3Listeners(allCurrentActiveEventAdapters.get(i));
				allCurrentActiveEventAdapters.remove(i);
			}
		}
	}
	
	public List<ExtendedTS3EventAdapter> getAllTS3Listeners() {
		return allCurrentActiveEventAdapters;
	}
}
