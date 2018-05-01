package serverFunctions;

import java.util.Timer;
import java.util.TimerTask;

/*
 * Destroys the given process after the given amount of time
 * Process destruction is used by using the ProcessDestoryer class
 * 
 * This is triggered even if the current musicProcess already changed
 */
public class BotTimeout extends TimerTask {
	Timer timer;
	Process process;

	public BotTimeout(Timer timer, Process process) {
		this.timer = timer;
		this.process = process;
	}

	public void run() {
		ProcessDestoryer destroyer = new ProcessDestoryer();
		destroyer.destroy(process);
		timer.cancel(); // Terminate the timer thread
	}

}
