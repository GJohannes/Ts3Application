package serverFunctions.riotApi;

public class WinAndKdaHolder {
	private boolean win;
	private double kda;
	
	public WinAndKdaHolder(boolean win, double kda) {
		this.win = win;
		this.kda = kda;
	}

	public boolean isWin() {
		return win;
	}

	public double getKda() {
		return kda;
	}
	
}
