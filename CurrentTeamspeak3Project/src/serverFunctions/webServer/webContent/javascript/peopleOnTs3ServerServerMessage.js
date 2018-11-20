	function sendServerMessage(){
		$.ajax({
			url : '/Update?method=sendServerMessage',
			type : 'POST',
			dataType : 'json',
			data : {serverMessage : document.getElementById('messageToServerString').value},
			success : function(data) {
				$("#peopleOnServerDisplay").html(data.allClientNicknames);
			}
		});
	}
	
	