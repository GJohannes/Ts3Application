<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@page import="serverFunctions.webServer.DoSomeJavaScript"%>
<html>
<head>
<title>Embedded Jetty: JSP Examples</title>
</head>
<body>
	<h1>Embedded Jetty: JSP Examples</h1>
	<p>Examples of JSP within Embedded Jetty. Random Shit bbbbbbbbbbbbbbbbb</p>
	<ul>
		<li><a href="test/dump.jsp">JSP 1.2 embeddedbnbbbbbbbbbbbbbbbbbbbbbbb java</a></li>
		<li><a href="test/bean1.jsp">JSP 1.2 Bean demo</a></li>
		<li><a href="test/tag.jsp">JSP 1.2 BodyTag demo</a></li>
		<li><a href="test/tag2.jsp">JSP 2.0 SimpleTag demo</a></li>
		<li><a href="test/tagfile.jsp">JSP 2.0 Tag File demo</a></li>
		<li><a href="test/expr.jsp?A=1">JSP 2.0 Tag Expression</a></li>
		<li><a href="test/jstl.jsp">JSTL Expression</a></li>
		<li><a href="test/foo/">WORK HERE</a></li>
		<li><a href="/test/foo/foo.jsp">ALSO WORK HERE</a></li>
		<li><a href="date/">Servlet Forwarding to JSP demo</a></li>
		<li><a href="first.jsp">WORKING!!!!</a></li>
	</ul>
</body>
<script src="jquery-3.3.1.js"></script>
<script type="text/javascript">
	var randomNumber = <%=DoSomeJavaScript.getARandomNumber()%>;
	console.log(randomNumber);
	console.log("Something");	
	myFunction();
	
	function myFunction() {
	    setInterval(function(){ console.log(askServerForNewRandomNumbers()); }, 1000);
	}
	
	function askServerForNewRandomNumbers (){
			 var x = <%=DoSomeJavaScript.getARandomNumber()%>;
			 return x;
	}

	$(document).ready(function(){
		console.log("Document is now ready");
		$.ajax({
			url: 'update',
			type: 'POST',
			dataType: 'json',
			data: "testsring form Javascruipt",
			success: function(){
				console.log("success on ajax call");
			}
			
		})
		
		
	})
	
</script>
</html>