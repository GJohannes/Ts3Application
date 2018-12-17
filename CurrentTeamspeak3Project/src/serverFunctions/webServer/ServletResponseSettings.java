package serverFunctions.webServer;

import javax.servlet.http.HttpServletResponse;

/*
 * All servlet json responses are set in this calss to reduce code duplicates within the code
 */
public class ServletResponseSettings {
	public static void setServletResponseSettings(HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		// content type is useful + firefox reuqiures it to not display constant error messages upon calling a servlet
		response.setContentType("application/json");
		//return response;
	}	
}
