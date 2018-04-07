package miscellaneous;


import java.util.ArrayList;
import java.util.List;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;

import login.ConnectedConfigValues;



public class ExtendedTS3Api extends TS3Api{
	private List<ExtendedTS3EventAdapter> allCurrentActiveEventAdapters = new ArrayList<>();
	private ConnectedConfigValues connectedConfigValues;
	
	
	public ExtendedTS3Api(ExtendedTS3Query query) {
		super(query);
		this.connectedConfigValues = new ConnectedConfigValues(query.getHostIpAdress(), "NotInizialized", "NotInizialized", -1);
		
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
	
	public ConnectedConfigValues getConnectedConfigValues() {
		return this.connectedConfigValues;
	}
	
	
	 /*
	 * Selecting the server port is now also loaded into the connectedConfigValues -Object 
	 * 
	 * @param
	 * @return true/false based on existing server on inserted port
	 
	 * (non-Javadoc)
	 * @see com.github.theholywaffle.teamspeak3.TS3Api#selectVirtualServerByPort(int)
	 */
	public boolean selectVirtualServerByPort(int port) { 
		if(super.selectVirtualServerByPort(port)) {
			ConnectedConfigValues newConnectedConfigValues = new ConnectedConfigValues(this.connectedConfigValues.getServerIpAdress(), 
					this.connectedConfigValues.getServerQueryName(), this.connectedConfigValues.getServerQueryPassword(), port);
			this.connectedConfigValues = newConnectedConfigValues;
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Login in to a server now also stores login information in connectedConfigValues -Object
	 * 
	 * port is set to -1 since after a new login no port has been set and has to be set first using the method selectVirtualServerByPort
	 * 
	 * @param serverQueryName the name of the server query that logs onto the server
	 * @param serverQueryPassword the corresponding password to the name for login on the server
	 * 
	 * @return true/false based on success of fail while connecting to the server
	 * 
	 * (non-Javadoc)
	 * @see com.github.theholywaffle.teamspeak3.TS3Api#login(java.lang.String, java.lang.String)
	 */
	public boolean login(String serverQueryName, String serverQueryPassword) {
		if(super.login(serverQueryName, serverQueryPassword)) {
			ConnectedConfigValues newConnectedConfigValues = new ConnectedConfigValues(this.connectedConfigValues.getServerIpAdress(), 
					serverQueryName, serverQueryPassword, -1);
			this.connectedConfigValues = newConnectedConfigValues;
			return true;
		} else {
			return false;
		}	
	}
	

	
}
