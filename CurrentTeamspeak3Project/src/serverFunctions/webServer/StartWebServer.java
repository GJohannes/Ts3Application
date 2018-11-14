package serverFunctions.webServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.json.simple.JSONArray;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import miscellaneous.AllExistingEventAdapter;
import miscellaneous.ExtendedTS3Api;
import miscellaneous.ExtendedTS3EventAdapter;

public class StartWebServer implements Runnable {
	private static final String WEBROOT_INDEX = "webContent"; // !! different navigation inside IDE (eclipse) and
																// exported runnable .jar
	// this.getClass().getResource("xyz"); is the problem for navigating inside .jar
	// files
	// only sub-directories seem to work
	private ExtendedTS3Api api;
	private int port;
	private int sslPort;
	private Server server;
	private HashMap<String, ArrayList<String>> allMessages = new HashMap<>();

	public StartWebServer(ExtendedTS3Api api, int port, int sslPort) {
		this.api = api;
		this.port = port;
		this.sslPort = sslPort;
	}

	/**
	 * only public for debugging usage. Start as a threat that will call this method
	 * @throws Exception
	 */
	public void startWebServer() throws Exception {
//		otherServer();
//		
//
//		if (true) {
//			return;
//		}

		int port = this.port;
		this.server = new Server();

		this.addPortAndSSLPort();
//		// Add annotation scanning (for WebAppContexts)
//      Configuration.ClassList classlist = Configuration.ClassList.setServerDefault( server );
//      classlist.addBefore(
//              "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
//              "org.eclipse.jetty.annotations.AnnotationConfiguration" );

		// Base URI for servlet context
		URI baseUri = getWebRootResourceUri();
//		System.out.println("Base URI: " + baseUri);

		// Create Servlet context
		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.setContextPath("/");
		// servletContextHandler.setResourceBase(baseUri.toASCIIString());
		// String resourceBase = StartWebServer.class.getResource("").toExternalForm();

		servletContextHandler.setResourceBase(baseUri.toASCIIString());

		System.out.println(servletContextHandler.getBaseResource());
		System.out.println(servletContextHandler.getResourceBase());

		// Since this is a ServletContextHandler we must manually configure JSP support.
		enableEmbeddedJspSupport(servletContextHandler);

		// Create Example of mapping jsp to path spec
		ServletHolder holderAltMapping = new ServletHolder();
		holderAltMapping.setName("peopleOnTs3Server.html");
		holderAltMapping.setForcedPath("/peopleOnTs3Server.html");
		servletContextHandler.addServlet(holderAltMapping, "/OnlinePeople");

		ServletHolder privateMessageHolder = new ServletHolder();
		privateMessageHolder.setName("privateMessageDialog.html");
		privateMessageHolder.setForcedPath("/privateMessageDialog.html");
		servletContextHandler.addServlet(privateMessageHolder, "/OnlinePeople/privateMessage");

		// Class default as servlet
		servletContextHandler.addServlet(PeopleOnTs3Server.class, "/UpdateAsDefaultServlet");

		// Instance of a class as servlet
		PeopleOnTs3Server update = new PeopleOnTs3Server(api);
		ServletHolder updateHolder = new ServletHolder();
		updateHolder.setServlet(update);
		servletContextHandler.addServlet(updateHolder, "/Update");

		PrivateMessageChatServlet privateMessageChatServlet = new PrivateMessageChatServlet(api, allMessages);
		ServletHolder privateMessageServletHolder = new ServletHolder();
		privateMessageServletHolder.setServlet(privateMessageChatServlet);
		servletContextHandler.addServlet(privateMessageServletHolder, "/privateMessage");

		UpdatePrivateChatBoxesServlet updatePrivateChatBoxesServlet = new UpdatePrivateChatBoxesServlet(allMessages);
		ServletHolder updatePrivateChatBoxesServletHolder = new ServletHolder();
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
		// URL indexUri = this.getClass().getClassLoader().getResource(WEBROOT_INDEX);
		// System.out.println(this.getClass().getClassLoader().getResource("index.jsp"));
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
	
	/*
	 * https://dzone.com/articles/adding-ssl-support-embedded
	 */
	private void addPortAndSSLPort() {
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(this.port);
		HttpConfiguration https = new HttpConfiguration();
		https.addCustomizer(new SecureRequestCustomizer());
		SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStorePath(StartWebServer.class.getResource("/keystore.jks").toExternalForm());
		sslContextFactory.setKeyStorePassword("wasdwasd");
		sslContextFactory.setKeyManagerPassword("wasdwasd");
		ServerConnector sslConnector = new ServerConnector(server,
				new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https));
		sslConnector.setPort(this.sslPort);
		server.setConnectors(new Connector[] { connector, sslConnector });
	}

	public void stop() throws Exception {
		server.stop();
		api.removeTS3Listeners(AllExistingEventAdapter.WEB_SERVER_CHAT);
		api.removeTS3Listeners(AllExistingEventAdapter.LOG_NUMBER_OF_PEOPLE_FOR_WEBPAGE);
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

	
	/**
	 * Event adapter for chatting of WebServer and TS3Server   
	 */
	private ExtendedTS3EventAdapter getWebServerChat(ExtendedTS3Api api) {
		ExtendedTS3EventAdapter webServerChat = new ExtendedTS3EventAdapter(AllExistingEventAdapter.WEB_SERVER_CHAT) {
			@Override
			public void onTextMessage(TextMessageEvent messageToBotEvent) {
				// do not send to server since it is a bot command if the message starts with !
				// or ?
				if (!(messageToBotEvent.getMessage().startsWith("!")
						|| messageToBotEvent.getMessage().startsWith("?"))) {
					synchronized (allMessages) {
						ArrayList<String> messagesOfThisPerson;
						if (allMessages.keySet().contains(messageToBotEvent.getInvokerName())) {
							messagesOfThisPerson = allMessages.get(messageToBotEvent.getInvokerName());
						} else {
							messagesOfThisPerson = new ArrayList<>();
							allMessages.put(messageToBotEvent.getInvokerName(), messagesOfThisPerson);
						}
						messagesOfThisPerson.add("Message From " + messageToBotEvent.getInvokerName() + ": "
								+ messageToBotEvent.getMessage());

					}
				}
			}

		};
		return webServerChat;
	}
	
	
	/**
	 * WebServer which is using the WebContent folder for the HTML
	 * files instead of servlets for JSP HTML files
	 */
	private ArrayList<String> test;
	private void otherServer() throws Exception {
		Server server = new Server(7000);

//		URI url = getWebRootResourceUri();
//
//		URI webRootUri = url;

		ResourceHandler context = new ResourceHandler();
//		    context.setContextPath("/");
//		    context.setBaseResource(Resource.newResource(webRootUri));
//		    context.setWelcomeFiles(new String[] { "index.html" });

		ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
		holderPwd.setInitParameter("dirAllowed", "true");
		context.setResourceBase("WebContent/");
		context.setWelcomeFiles(new String[] { "index.html" });
		ServletContextHandler servletContextHandler = new ServletContextHandler();
		servletContextHandler.setResourceBase("WebContent/");

		// Create Example of mapping jsp to path spec
//			ServletHolder holderAltMapping = new ServletHolder();
//			holderAltMapping.setName("peopleOnTs3Server.html");
//			holderAltMapping.setForcedPath("/peopleOnTs3Server.html");
//			context.addServlet(holderAltMapping);

//			ServletHolder privateMessageHolder = new ServletHolder();
//			privateMessageHolder.setName("privateMessageDialog.html");
//			privateMessageHolder.setForcedPath("/privateMessageDialog.html");
//			context.addServlet(privateMessageHolder);
//
		// Class default as servlet
		servletContextHandler.addServlet(PeopleOnTs3Server.class, "/UpdateAsDefaultServlet");

		// Instance of a class as servlet
		PeopleOnTs3Server update = new PeopleOnTs3Server(api);
		ServletHolder updateHolder = new ServletHolder();
		updateHolder.setServlet(update);
		servletContextHandler.addServlet(updateHolder, "/Update");

		PrivateMessageChatServlet privateMessageChatServlet = new PrivateMessageChatServlet(api, allMessages);
		ServletHolder privateMessageServletHolder = new ServletHolder();
		privateMessageServletHolder.setServlet(privateMessageChatServlet);
		servletContextHandler.addServlet(privateMessageServletHolder, "/privateMessage");

		UpdatePrivateChatBoxesServlet updatePrivateChatBoxesServlet = new UpdatePrivateChatBoxesServlet(allMessages);
		ServletHolder updatePrivateChatBoxesServletHolder = new ServletHolder();
		updatePrivateChatBoxesServletHolder.setServlet(updatePrivateChatBoxesServlet);
		servletContextHandler.addServlet(updatePrivateChatBoxesServletHolder, "/updatePrivateChatBoxes");

		servletContextHandler.insertHandler(context);
		server.setHandler(servletContextHandler);

		try {
			server.start();
			// server.dump(System.err);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
