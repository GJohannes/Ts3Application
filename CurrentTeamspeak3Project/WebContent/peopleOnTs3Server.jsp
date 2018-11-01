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
		 <div id="curve_chart" style="width: 900px; height: 500px">https://developers.google.com/chart/interactive/docs/gallery/linechart</div>
	</ul>
</body>
<script src="jquery-3.3.1.js"></script>
<script src="loader.js"></script>
<script type="text/javascript">

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
  var data = google.visualization.arrayToDataTable([
    ['Year', 'Sales', 'Expenses'],
    ['2004',  1000,      400],
    ['2005',  1170,      460],
    ['2006',  660,       1120],
    ['2007',  1030,      540]
  ]);

  var options = {
    title: 'Company Performance',
    curveType: 'function',
    legend: { position: 'bottom' }
  };

  var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

  chart.draw(data, options);
}


$(document).ready(function() {
	document.getElementById('curve_chart').style.visibility='hidden' 
	
})

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
			var button = document.createElement("BUTTON");
			var textOfButton = document.createTextNode("Message to : " + returnData.allClientNicknames[i]);
			button.value = returnData.allClientNicknames[i];
			button.onclick = function(){
				privateMessageButtonOnclick(this);
			}
			button.appendChild(textOfButton);
			document.getElementById('containerWithPersonaleMessageButtons').appendChild(button);
			console.log("wasd");
		}
		
	}
	
	function privateMessageButtonOnclick(button){
		window.open(window.location.href  + "/privateMessage?User=" + button.value);
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