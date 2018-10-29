<html>
<head>
<title>Embedded Jetty: JSP Examples</title>
</head>
<body>
	<h1>TODO implement private messages !!!</h1>
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
	console.log(c);
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
				console.log(textOfChatBox);
				textOfChatBox.val( textOfChatBox.val() + "\n" + data.chatContent);
				console.log("success on ajax call message to server");
				console.log(data.chatContent);
			},
			failure : function(data){
				console.log("ERROR!!!");
				console.log(data);
			}
		})
	}

</script>