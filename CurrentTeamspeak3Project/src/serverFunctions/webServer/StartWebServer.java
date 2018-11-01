package serverFunctions.webServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.eclipse.jetty.jsp.JettyJspServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.Configuration;
import org.json.simple.JSONArray;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import miscellaneous.AllExistingEventAdapter;
import miscellaneous.ExtendedTS3Api;
import miscellaneous.ExtendedTS3EventAdapter;

public class StartWebServer implements Runnable {
	private static final String WEBROOT_INDEX = ""; //!! different navigation inside IDE (eclipse) and exported runnable .jar  
													// this.getClass().getResource("xyz"); is the prblem for navigating inside .jar files
	private ExtendedTS3Api api;
	private int port;
	private Server server;
	private HashMap<String, ArrayList<String>> allMessages = new HashMap<>();

	public StartWebServer(ExtendedTS3Api api, int port) {
		this.api = api;
		this.port = port;
	}

	public void startWebServer() throws Exception {
		int port = this.port;
		this.server = new Server();

		// Define ServerConnector
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(port);
		server.addConnector(connector);

//		// Add annotation scanning (for WebAppContexts)
//      Configuration.ClassList classlist = Configuration.ClassList.setServerDefault( server );
//      classlist.addBefore(
//              "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
//              "org.eclipse.jetty.annotations.AnnotationConfiguration" );

		// Base URI for servlet context
		URI baseUri = getWebRootResourceUri();
//		System.out.println("Base URI: " + baseUri);
		
		
		
		
//		
		
		// Create Servlet context
		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.setContextPath("/");
		//servletContextHandler.setResourceBase(baseUri.toASCIIString());
		//String resourceBase = StartWebServer.class.getResource("").toExternalForm();
	
		
		servletContextHandler.setResourceBase(baseUri.toASCIIString());
		
		 System.out.println(servletContextHandler.getBaseResource());
	       System.out.println(servletContextHandler.getResourceBase());
		
		// Since this is a ServletContextHandler we must manually configure JSP support.
		enableEmbeddedJspSupport(servletContextHandler);

		// Create Example of mapping jsp to path spec
		ServletHolder holderAltMapping = new ServletHolder();
		holderAltMapping.setName("peopleOnTs3Server.jsp");
		holderAltMapping.setForcedPath("/peopleOnTs3Server.jsp");
		servletContextHandler.addServlet(holderAltMapping, "/OnlinePeople");

		ServletHolder privateMessageHolder = new ServletHolder();
		privateMessageHolder.setName("privateMessageDialog.jsp");
		privateMessageHolder.setForcedPath("/privateMessageDialog.jsp");
		servletContextHandler.addServlet(privateMessageHolder, "/OnlinePeople/privateMessage");

		// Class default as servlet
		servletContextHandler.addServlet(PeopleOnTs3Server.class, "/UpdateAsDefaultServlet");

		// Instance of a class as servlet
		PeopleOnTs3Server update = new PeopleOnTs3Server("some random test string", api);
		ServletHolder updateHolder = new ServletHolder();
		updateHolder.setServlet(update);
		servletContextHandler.addServlet(updateHolder, "/Update");

		PrivateMessageChatServlet privateMessageChatServlet = new PrivateMessageChatServlet(api, allMessages);
		ServletHolder privateMessageServletHolder = new ServletHolder();
		privateMessageServletHolder.setServlet(privateMessageChatServlet);
		servletContextHandler.addServlet(privateMessageServletHolder, "/privateMessage");
		
		UpdatePrivateChatBoxesServlet updatePrivateChatBoxesServlet = new UpdatePrivateChatBoxesServlet(allMessages);
		ServletHolder updatePrivateChatBoxesServletHolder  = new ServletHolder();
		updatePrivateChatBoxesServletHolder.setServlet(updatePrivateChatBoxesServlet);
		servletContextHandler.addServlet(updatePrivateChatBoxesServletHolder, "/updatePrivateChatBoxes");

		// Default Servlet (always last, always named "default")
		ServletHolder holderDefault = new ServletHolder("default", DefaultServlet.class);
		holderDefault.setInitParameter("resourceBase", baseUri.toASCIIString());
		holderDefault.setInitParameter("dirAllowed", "true");
		servletContextHandler.addServlet(holderDefault, "/");
		server.setHandler(servletContextHandler);

		// Start Server
		server.start();
	}

	private URI getWebRootResourceUri() throws FileNotFoundException, URISyntaxException {
		URL indexUri = this.getClass().getResource(WEBROOT_INDEX);
		 System.out.println(indexUri);
		if (indexUri == null) {
			throw new FileNotFoundException("Unable to find resource ");
		}
		// Points to wherever /webroot/ (the resource) is
		return indexUri.toURI();
	}

	/**
	 * Setup JSP Support for ServletContextHandlers.
	 * <p>
	 * NOTE: This is not required or appropriate if using a WebAppContext.
	 * </p>
	 *
	 * @param servletContextHandler the ServletContextHandler to configure
	 * @throws IOException if unable to configure
	 */
	private void enableEmbeddedJspSupport(ServletContextHandler servletContextHandler) throws IOException {
		// Establish Scratch directory for the servlet context (used by JSP compilation)
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");

		if (!scratchDir.exists()) {
			if (!scratchDir.mkdirs()) {
				throw new IOException("Unable to create scratch directory: " + scratchDir);
			}
		}
		servletContextHandler.setAttribute("javax.servlet.context.tempdir", scratchDir);

		// Set Classloader of Context to be sane (needed for JSTL)
		// JSP requires a non-System classloader, this simply wraps the
		// embedded System classloader in a way that makes it suitable
		// for JSP to use
		ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
		servletContextHandler.setClassLoader(jspClassLoader);

		// Manually call JettyJasperInitializer on context startup
		servletContextHandler.addBean(new JspStarter(servletContextHandler));

		// Create / Register JSP Servlet (must be named "jsp" per spec)
		ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
		holderJsp.setInitOrder(0);
		holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
		holderJsp.setInitParameter("fork", "false");
		holderJsp.setInitParameter("xpoweredBy", "false");
		holderJsp.setInitParameter("compilerTargetVM", "1.8");
		holderJsp.setInitParameter("compilerSourceVM", "1.8");
		holderJsp.setInitParameter("keepgenerated", "true");
		servletContextHandler.addServlet(holderJsp, "*.jsp");

	}

	public void stop() throws Exception {
		server.stop();
		api.removeTS3Listeners(AllExistingEventAdapter.WEB_SERVER_CHAT);
	}

	@Override
	public void run() {
		try {
			this.api.addTS3Listeners(this.getWebServerChat(api));
			this.startWebServer();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ArrayList<String> test;

	private ExtendedTS3EventAdapter getWebServerChat(ExtendedTS3Api api) {
		ExtendedTS3EventAdapter webServerChat = new ExtendedTS3EventAdapter(AllExistingEventAdapter.WEB_SERVER_CHAT) {
			@Override
			public void onTextMessage(TextMessageEvent messageToBotEvent) {
				// do not send to server since it is a bot command if the message starts with !
				// or ?
				if (!(messageToBotEvent.getMessage().startsWith("!") || messageToBotEvent.getMessage().startsWith("?"))) {
					synchronized (allMessages) {
						ArrayList<String> messagesOfThisPerson;
						if (allMessages.keySet().contains(messageToBotEvent.getInvokerName())) {
							messagesOfThisPerson = allMessages.get(messageToBotEvent.getInvokerName());
						} else {
							messagesOfThisPerson = new ArrayList<>();
							allMessages.put(messageToBotEvent.getInvokerName(), messagesOfThisPerson);
						}
						messagesOfThisPerson.add("Message From " + messageToBotEvent.getInvokerName() + ": " + messageToBotEvent.getMessage());
						
						
					}
				}
			}

		};
		return webServerChat;
	}

}
