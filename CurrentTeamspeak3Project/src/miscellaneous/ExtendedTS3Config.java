package miscellaneous;

import com.github.theholywaffle.teamspeak3.TS3Config;

public class ExtendedTS3Config extends TS3Config {
	private String hostIpAdress;
	
	/*
	 * Constructor needs host ip adress so that the value is never null
	 */
	public ExtendedTS3Config(String hostIpAdress) {
		this.setHost(hostIpAdress);
	}
	
	/*
	 * Same functionality as before with the addition that the ip adress is now also stored so that it can be read later on 
	 * 
	 * (non-Javadoc)
	 * @see com.github.theholywaffle.teamspeak3.TS3Config#setHost(java.lang.String)
	 */
	public ExtendedTS3Config setHost(String hostIpAdress) {
		super.setHost(hostIpAdress);
		this.hostIpAdress = hostIpAdress;
		return this;
	}
	
	public String getHostIpAdress() {
		return hostIpAdress;
	}
}
