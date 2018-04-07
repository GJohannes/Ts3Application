package miscellaneous;

import com.github.theholywaffle.teamspeak3.TS3Query;

public class ExtendedTS3Query extends TS3Query{
	private String hostIpAdress;

	public ExtendedTS3Query(ExtendedTS3Config config) {
		super(config);
		this.hostIpAdress = config.getHostIpAdress();
	}
	
	public String getHostIpAdress() {
		return hostIpAdress;
	}
}
