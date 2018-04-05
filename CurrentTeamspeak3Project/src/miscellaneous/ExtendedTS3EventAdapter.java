package miscellaneous;

import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;

public class ExtendedTS3EventAdapter extends TS3EventAdapter{
	private Enum<AllExistingEventAdapter> name;
	
	
	public ExtendedTS3EventAdapter(Enum<AllExistingEventAdapter> name) {
		this.name = name;
	}

	public Enum<AllExistingEventAdapter> getName() {
		return name;
	}
		
}
