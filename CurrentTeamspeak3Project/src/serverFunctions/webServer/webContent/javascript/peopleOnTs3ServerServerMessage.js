
function sendServerMessage() {
	$
			.ajax({
				url : '/Update?method=sendServerMessage',
				type : 'POST',
				dataType : 'json',
				data : {
					serverMessage : document
							.getElementById('messageToServerString').value,
					password : document
							.getElementById('passwordForMessageToServerString').value
				},
				success : function(data) {
					var passwordCorrect = data.passwordCorrect;
					if (passwordCorrect == true) {
						alert("Succesfully sent Server message");
					} else {
						alert("Password wrong");
					}
					// $("#peopleOnServerDisplay").html(data.allClientNicknames);
					$("#messageToServerString").value = '';
				}
			});
	document.getElementById('messageToServerString').value = "";
}

$("#messageToServerString").focus(function() {
	if (this.value === this.defaultValue) {
		this.value = '';
	}
}).blur(function() {
	if (this.value === '') {
		this.value = this.defaultValue;
	}
});

/*
 * only accesible throught the console. enter correct characters to get the current password for server messages
 */
function calculatePassword(aString) {
	$
			.ajax({
				url : '/Update?method=getPassword',
				type : 'POST',
				dataType : 'json',
				data : {
					passwordToConvert : aString
				},
				success : function(data) {
					console.log(data);
				}
			});
}