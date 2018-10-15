<html>
<head>
<title>Embedded Jetty: JSP Examples</title>
<link href="static/main.css" media="all" rel="stylesheet"
	type="text/css" />
</head>
<body>
	<h1>Online People</h1>
	<p>all people that are online and the ability to send server
		messages</p>
	<ul>
		<div id="wasd">
			<button onclick="sendServerMessage()">Send Message to the
				TS3Server</button>
			<input type="text" id="messageToServerString"
				value="Send stuff to display on the server">
		</div>
		<div>
			<div id="peopleOnServerDisplay">
				<h2>NO PEOPLE ON SERVER</h2>
			</div>
			<div id="containerWithPersonaleMessageButtons">
			</div>
		</div>
	</ul>
</body>
<script src="jquery-3.3.1.js"></script>
<script type="text/javascript">
	function refreshUsersOnline() {
		setInterval(function(){
			getUseresOnlineFromServer();
		}, 5000);
	}

	function getUseresOnlineFromServer(){
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
				updateButtonsForPersonalMessage(data);		
			},
			failure : function(data){
				console.log("ERROR!!!");
				console.log(data);
			}
		})
	}
	
	function updateButtonsForPersonalMessage(returnData){
		// clear old buttons 
		document.getElementById('containerWithPersonaleMessageButtons').innerHTML = "";
		
		for (i = 0; i < returnData.allClientNicknames.length; i++) {
			var nameOfCurrentUser = returnData.allClientNicknames[i];
			var button = document.createElement("BUTTON");
			var textOfButton = document.createTextNode(returnData.allClientNicknames[i]);
			button.onclick = function(){
				privateMessageButtonOnclick(this);
			}
			button.appendChild(textOfButton);
			document.getElementById('containerWithPersonaleMessageButtons').appendChild(button);
			console.log("wasd");
		}
		
	}
	
	function privateMessageButtonOnclick(button){
		console.log(button.innerHTML);
		window.open(window.location.href  + "/privateMessage?User=" + button.innerHTML);
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
		getUseresOnlineFromServer();
		refreshUsersOnline();
		
	})
</script>
</html>