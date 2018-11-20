package serverFunctions.riotApi;

public class WinKdaMostDamageHolder {
	private boolean win;
	private double kda;
	private boolean highestDamageDealer = false;
	
	public WinKdaMostDamageHolder(boolean win, double kda) {
		this.win = win;
		this.kda = kda;
	}

	public boolean isHighestDamageDealer() {
		return highestDamageDealer;
	}

	public void setHighestDamageDealer(boolean highestDamageDealer) {
		this.highestDamageDealer = highestDamageDealer;
	}

	public boolean isWin() {
		return win;
	}

	public double getKda() {
		return kda;
	}
	
}
