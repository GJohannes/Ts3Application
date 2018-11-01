<html>
<head>
<title>Embedded Jetty: JSP Examples</title>
</head>
<body>
	<h1 id ="headLine"></h1>
	<div>
		<input rows="4" cols="50" id="messageToServer" >Type your chat text here</input>
	</div>
	<div>
		<textarea rows="40" cols="50" readonly id="chatBox">Chat Messages:</textarea>
	</div>
	
</body>
<script src="/../jquery-3.3.1.js"></script>
<script type="text/javascript">

$(document).ready(function() {
	
	var url_string = window.location.href; //window.location.href
	var url = new URL(url_string);
	var c = url.searchParams.get("User");
	document.getElementById("headLine").innerHTML = "Chatroom with " + c;
	updateChatBox();
	console.log(c + " is the chat partner");
	refreshMessages();
})

$(function () {
    $("#messageToServer").keypress(function (e) {
        var code = e.keyCode;
        if (code == 13) {
        	sendMessageToServer();
        	document.getElementById("messageToServer").value = "";
            return true;
        }
    });
});

function getURLparam(){
	var url_string = window.location.href; //window.location.href
	var url = new URL(url_string);
	var c = url.searchParams.get("User");
	console.log(c);
	return c;
}

function refreshMessages() {
	setInterval(function(){
		updateChatBox();
	}, 1000);
}

/*
 * update method similar to sendMessageAndUpdate 
 */
function updateChatBox(){
	$.ajax({
		url : '/updatePrivateChatBoxes',
		type : 'POST',
		dataType : 'json',
		data : JSON.stringify ({
			"teamspeakUser" : getURLparam(),
			}),
		success : function(data) {
			if(data.personExisting === "false"){
				return;
			}
			var textOfChatBox = $("textarea#chatBox");
			document.getElementById("chatBox").value = "Chat Messages:";
			var messagesToDisplay = JSON.parse(data.chatContent);
			console.log(messagesToDisplay.length)
			console.log(messagesToDisplay);				
			
			for(i = 0; i < messagesToDisplay.length; i++){
				textOfChatBox.val(textOfChatBox.val() + "\n" + messagesToDisplay[i]);
			}
			
			
			
			console.log(textOfChatBox);
			console.log("success on ajax call message to server");
		},
		failure : function(data){
			console.log("ERROR!!!");
			console.log(data);
		}
	})
}

function sendMessageToServer(){
		$.ajax({
			url : '/privateMessage',
			type : 'POST',
			dataType : 'json',
			data : JSON.stringify ({
				"message" : document.getElementById("messageToServer").value,
				"teamspeakUser" : getURLparam(),
				}),
			success : function(data) {
				var textOfChatBox = $("textarea#chatBox");
				console.log(data.personExisting);
				if(data.personExisting === "false"){
					textOfChatBox.val(textOfChatBox.val() + "\n " + getURLparam() + " is not currently on the teamspeak Server or has gone offline");
					return;
				}
				
				
				document.getElementById("chatBox").value = "Chat Messages:";
				var messagesToDisplay = JSON.parse(data.chatContent);
				console.log(messagesToDisplay.length)
				console.log(messagesToDisplay);				
				
				for(i = 0; i < messagesToDisplay.length; i++){
					textOfChatBox.val(textOfChatBox.val() + "\n" + messagesToDisplay[i]);
				}
				
				
				
				console.log(textOfChatBox);
				console.log("success on ajax call message to server");
			},
			failure : function(data){
				console.log("ERROR!!!");
				console.log(data);
			}
		})
	}

</script>