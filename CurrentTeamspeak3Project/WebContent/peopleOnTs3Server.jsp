<html>
<head>
<title>Embedded Jetty: JSP Examples</title>
<link href="static/main.css" media="all" rel="stylesheet"
	type="text/css" />
</head>
<body>
	<h1>Online People</h1>
	<p>all people that are online and the ability to send server messages</p>
	<ul>
		<div id="wasd">
			<button onclick="sendServerMessage()">Send Message to the TS3Server</button>
			<input type="text" id="messageToServerString" value="Send stuff to display on the server">
		</div>
		<div>
			<div id="peopleOnServerDisplay">
				<h2>NO PEOPLE ON SERVER</h2>
			</div>
		</div>
	</ul>
</body>
<script src="javascript/jquery-3.3.1.js"></script>
<script type="text/javascript">
	function refreshUsersOnline() {
		setInterval(function() {
			$.ajax({
				url : '/Update?method=refreshUsers',
				type : 'POST',
				dataType : 'json',
				data : {
					someName : "testName",
				},
				
				success : function(data) {
					console.log("success on ajax call");
					$("#peopleOnServerDisplay").html(data.allClientNicknames);
				},
				failure : function(data){
					console.log("ERROR!!!");
					console.log(data);
				}
			})
		}, 5000);
	}

	function sendServerMessage(){
		$.ajax({
			url : '/Update?method=sendServerMessage',
			type : 'POST',
			dataType : 'json',
			data : {serverMessage : document.getElementById('messageToServerString').value},
			success : function(data) {
				console.log("success on ajax call");
				$("#peopleOnServerDisplay").html(data.allClientNicknames);
			}
		});
	}
	
	function getUsersFromServer(){
		
	}
	
	$(document).ready(function() {
		console.log("Document is now ready");
		refreshUsersOnline();
		
	})
</script>
</html>