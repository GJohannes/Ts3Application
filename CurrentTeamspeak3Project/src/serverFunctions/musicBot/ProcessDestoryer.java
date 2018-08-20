package serverFunctions.musicBot;

public class ProcessDestoryer {
	public void destroy(Process process) {
		if (process != null && process.isAlive()) {
			process.destroy();
		}
		process = null;
	}
}
