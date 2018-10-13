package serverFunctions.webServer;

import javax.servlet.http.HttpServlet;

public class DoSomeJavaScript extends HttpServlet{
	private static final long serialVersionUID = 100L;
	
	/*
	 * STATIC METHOD FOR JAVASCRIPT BINDING !!!
	 */
	public static double  getARandomNumber() {
		System.out.println("random number called");
		return Math.random();
	}
	
	/*
	 * STATIC METHOD FOR JAVASCRIPT BINDING !!!
	 */
	public static String getTheSameString() {
		return "this is the same string every time";
	}
	
	protected void doGet() {
		
	}
	
}
