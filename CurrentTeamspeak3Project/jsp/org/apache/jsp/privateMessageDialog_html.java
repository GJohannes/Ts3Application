/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: jetty/9.4.12.v20180830
 * Generated at: 2018-12-28 22:55:55 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class privateMessageDialog_html extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = null;
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    final java.lang.String _jspx_method = request.getMethod();
    if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET POST or HEAD");
      return;
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<meta charset=\"utf-8\" />\r\n");
      out.write("<title>Private Message Dialog</title>\r\n");
      out.write("<link rel=\"shortcut icon\" href=\"/../images/tabIcon.ico\">\r\n");
      out.write("<style>\r\n");
      out.write(".bg {\r\n");
      out.write("\t/* The image used */\r\n");
      out.write("\tbackground-image: url(\"/../images/MessageBackground.jpg\");\r\n");
      out.write("\t/* Full height */\r\n");
      out.write("\theight: 100%;\r\n");
      out.write("\t/* Center and scale the image nicely */\r\n");
      out.write("\tbackground-position: center;\r\n");
      out.write("\tbackground-repeat: no-repeat;\r\n");
      out.write("\tbackground-size: cover;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".divMainMessage {\r\n");
      out.write("\tposition: relative;\r\n");
      out.write("\ttop: 20%;\r\n");
      out.write("\tmargin: auto;\r\n");
      out.write("\twidth: 50%;\r\n");
      out.write("\theight: 500px;\r\n");
      out.write("\ttext-align: center;\r\n");
      out.write("\tcolor: white;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".divHeadLine {\r\n");
      out.write("\tfont-size: 1.5vw;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("body {\r\n");
      out.write("\tmargin: 0;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("div {\r\n");
      out.write("\tfont-size: 1.5vw;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("input {\r\n");
      out.write("\tfont-size: 2vw;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("@media screen and (max-width: 1200px) {\r\n");
      out.write("\t.divMainMessage {\r\n");
      out.write("\t\twidth: 100%;\r\n");
      out.write("\t}\r\n");
      out.write("\tinput {\r\n");
      out.write("\t\tfont-size: 5vw;\r\n");
      out.write("\t}\r\n");
      out.write("\tdiv {\r\n");
      out.write("\t\tfont-size: 4vw;\r\n");
      out.write("\t}\r\n");
      out.write("\th1 {\r\n");
      out.write("\t\tfont-size: 6vw;\r\n");
      out.write("\t}\r\n");
      out.write("}\r\n");
      out.write("</style>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("\t<div class=\"divBackgroundGrid\">\r\n");
      out.write("\t\t<div class=\"divMainMessage\">\r\n");
      out.write("\t\t\t<div class=\"divHeadLine\">\r\n");
      out.write("\t\t\t\t<h1 id=\"headLine\"></h1>\r\n");
      out.write("\t\t\t\t<div>\r\n");
      out.write("\t\t\t\t\t<div>Type your chat text here</div>\r\n");
      out.write("\t\t\t\t\t<div>\r\n");
      out.write("\t\t\t\t\t\t<input id=\"messageToServer\">\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t\t<div>\r\n");
      out.write("\t\t\t\t<textarea style=\"width: 100%; height: 100%; resize: none; font-size: 1.5vw; border-radius: 25px;\" rows=\"4\" readonly id=\"chatBox\">Chat Messages:</textarea>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t\t<div class=\"divCenter\">\r\n");
      out.write("\t\t\t\t<a href=\"/../impressum.html\"> Impressum </a>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("</body>\r\n");
      out.write("<script src=\"/../../javascript/jquery-3.3.1.js\"></script>\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"/../css/overallCSS.css\">\r\n");
      out.write("<script type=\"text/javascript\">\r\n");
      out.write("\tvar lastMessagesToDisplay;\r\n");
      out.write("\tvar userOfflineDisplayMessage = \"\\n \" + getURLparam(\"User\") + \" is offline\";\r\n");
      out.write("\r\n");
      out.write("\t$(document)\r\n");
      out.write("\t\t\t.ready(\r\n");
      out.write("\t\t\t\t\tfunction() {\r\n");
      out.write("\t\t\t\t\t\tdocument.getElementById(\"headLine\").innerHTML = \"Chatroom with \"\r\n");
      out.write("\t\t\t\t\t\t\t\t+ getURLparam(\"User\");\r\n");
      out.write("\t\t\t\t\t\tupdateChatBox();\r\n");
      out.write("\t\t\t\t\t\tconsole.log(getURLparam(\"User\")\r\n");
      out.write("\t\t\t\t\t\t\t\t+ \" is the chat partner\");\r\n");
      out.write("\t\t\t\t\t\trefreshMessages();\r\n");
      out.write("\t\t\t\t\t})\r\n");
      out.write("\r\n");
      out.write("\t$(function() {\r\n");
      out.write("\t\t$(\"#messageToServer\").keypress(function(e) {\r\n");
      out.write("\t\t\tvar code = e.keyCode;\r\n");
      out.write("\t\t\tif (code == 13) {\r\n");
      out.write("\t\t\t\tsendMessageToServer();\r\n");
      out.write("\t\t\t\tdocument.getElementById(\"messageToServer\").value = \"\";\r\n");
      out.write("\t\t\t\treturn true;\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t});\r\n");
      out.write("\t});\r\n");
      out.write("\r\n");
      out.write("\tfunction getURLparam(parameterName) {\r\n");
      out.write("\t\tvar url_string = window.location.href; //window.location.href\r\n");
      out.write("\t\tvar url = new URL(url_string);\r\n");
      out.write("\t\tvar c = url.searchParams.get(parameterName);\r\n");
      out.write("\t\treturn c;\r\n");
      out.write("\t}\r\n");
      out.write("\r\n");
      out.write("\tfunction refreshMessages() {\r\n");
      out.write("\t\tsetInterval(function() {\r\n");
      out.write("\t\t\tupdateChatBox();\r\n");
      out.write("\t\t}, 1000);\r\n");
      out.write("\t}\r\n");
      out.write("\r\n");
      out.write("\tfunction onTextUpdate(data) {\r\n");
      out.write("\t\tvar textOfChatBox = $(\"textarea#chatBox\");\r\n");
      out.write("\t\tif (data.personOnline == false) {\r\n");
      out.write("\t\t\tif (!textOfChatBox.val().toString().includes(userOfflineDisplayMessage)) {\r\n");
      out.write("\t\t\t\ttextOfChatBox.val(textOfChatBox.val() + userOfflineDisplayMessage);\r\n");
      out.write("\t\t\t\tvar textarea = document.getElementById('chatBox');\r\n");
      out.write("\t\t\t\ttextarea.scrollTop = textarea.scrollHeight;\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t\treturn;\r\n");
      out.write("\t\t} else {\r\n");
      out.write("\t\t\tif (textOfChatBox.val().toString().includes(userOfflineDisplayMessage)) {\r\n");
      out.write("\t\t\t\tconsole.log(\"to replace\");\r\n");
      out.write("\t\t\t\ttextOfChatBox.val().replace(userOfflineDisplayMessage, \" \");\r\n");
      out.write("\t\t\t\tvar textarea = document.getElementById('chatBox');\r\n");
      out.write("\t\t\t\ttextarea.scrollTop = textarea.scrollHeight;\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t}\r\n");
      out.write("\r\n");
      out.write("\t\tvar messagesToDisplay = JSON.parse(data.chatContent);\r\n");
      out.write("\r\n");
      out.write("\t\t//if nothing changed do not redraw content\r\n");
      out.write("\t\tif (JSON.stringify(messagesToDisplay) == JSON\r\n");
      out.write("\t\t\t\t.stringify(lastMessagesToDisplay)) {\r\n");
      out.write("\t\t\treturn;\r\n");
      out.write("\t\t}\r\n");
      out.write("\r\n");
      out.write("\t\t// refill the textarea\r\n");
      out.write("\t\tdocument.getElementById(\"chatBox\").value = \"Chat Messages:\";\r\n");
      out.write("\t\tfor (i = 0; i < messagesToDisplay.length; i++) {\r\n");
      out.write("\t\t\ttextOfChatBox\r\n");
      out.write("\t\t\t\t\t.val(textOfChatBox.val() + \"\\n\" + messagesToDisplay[i]);\r\n");
      out.write("\t\t}\r\n");
      out.write("\r\n");
      out.write("\t\t//sroll to bottom\r\n");
      out.write("\t\tvar textarea = document.getElementById('chatBox');\r\n");
      out.write("\t\ttextarea.scrollTop = textarea.scrollHeight;\r\n");
      out.write("\r\n");
      out.write("\t\t//update the last messages that where displayed\r\n");
      out.write("\t\tlastMessagesToDisplay = messagesToDisplay;\r\n");
      out.write("\t}\r\n");
      out.write("\r\n");
      out.write("\t/*\r\n");
      out.write("\t * update method similar to sendMessageAndUpdate \r\n");
      out.write("\t */\r\n");
      out.write("\tfunction updateChatBox() {\r\n");
      out.write("\t\t$.ajax({\r\n");
      out.write("\t\t\turl : '/updatePrivateChatBoxes',\r\n");
      out.write("\t\t\ttype : 'POST',\r\n");
      out.write("\t\t\tdataType : 'json',\r\n");
      out.write("\t\t\tdata : JSON.stringify({\r\n");
      out.write("\t\t\t\t\"teamspeakUser\" : getURLparam(\"User\"),\r\n");
      out.write("\t\t\t}),\r\n");
      out.write("\t\t\tsuccess : function(data) {\r\n");
      out.write("\t\t\t\tonTextUpdate(data);\r\n");
      out.write("\t\t\t},\r\n");
      out.write("\t\t\tfailure : function(data) {\r\n");
      out.write("\t\t\t\tconsole.log(\"ERROR!!!\");\r\n");
      out.write("\t\t\t\tconsole.log(data);\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t})\r\n");
      out.write("\t}\r\n");
      out.write("\r\n");
      out.write("\tfunction sendMessageToServer() {\r\n");
      out.write("\t\t$.ajax({\r\n");
      out.write("\t\t\turl : '/privateMessage',\r\n");
      out.write("\t\t\ttype : 'POST',\r\n");
      out.write("\t\t\tdataType : 'json',\r\n");
      out.write("\t\t\tdata : JSON.stringify({\r\n");
      out.write("\t\t\t\t\"message\" : document.getElementById(\"messageToServer\").value,\r\n");
      out.write("\t\t\t\t\"teamspeakUser\" : getURLparam(\"User\"),\r\n");
      out.write("\t\t\t\t\"webPageUserName\" : getURLparam(\"userName\"),\r\n");
      out.write("\t\t\t}),\r\n");
      out.write("\t\t\tsuccess : function(data) {\r\n");
      out.write("\t\t\t\tonTextUpdate(data);\r\n");
      out.write("\t\t\t},\r\n");
      out.write("\t\t\tfailure : function(data) {\r\n");
      out.write("\t\t\t\tconsole.log(\"ERROR!!!\");\r\n");
      out.write("\t\t\t\tconsole.log(data);\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t})\r\n");
      out.write("\t}\r\n");
      out.write("</script>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
