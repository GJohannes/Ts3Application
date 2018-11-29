	function refreshUsersOnline() {
		setInterval(function(){
			getUsersOnlineFromServer();
		}, 5000);
	}

	/*
	* GET type and data result in http parameters of the data
	*/
	function getUsersOnlineFromServer(){
		$.ajax({
			url : '/Update?method=refreshUsers',
			type : 'GET',
			dataType : 'json',
			data : {
				someName : "testName",
			},
			success : function(data) {
				displayAllPeopleOnline(data);
				
				updateButtonsForPersonalMessage(data);	
			},
			failure : function(data){
				console.log("ERROR!!!");
				console.log(data);
			}
		})
	}
	
	function displayAllPeopleOnline(returnData){
		$("#peopleOnServerDisplay").html("");
		var element = document.getElementById("peopleOnServerDisplay");
		
		for(i = 0; i < returnData.allClientNicknames.length; i++){			
			var span = document.createElement("span");
			var node = document.createTextNode(returnData.allClientNicknames[i]);
			span.appendChild(node);
			span.className = "dot";
			element.appendChild(span);
		}
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
			button.className = "buttonGreenStandart";
			document.getElementById('containerWithPersonaleMessageButtons').appendChild(button);
		}
		
	}
	
	function privateMessageButtonOnclick(button){
		window.open(window.location.href  + "/privateMessage?User=" + button.value);
	}