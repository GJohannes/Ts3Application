
	$("#messageToServerString").focus(function() {
		console.log("triggered textarea focus")
		if (this.value === this.defaultValue) {
			this.value = '';
		}
	}).blur(function() {
		if (this.value === '') {
			this.value = this.defaultValue;
		}
	});
	
	function sendServerMessage(){
		$.ajax({
			url : '/Update?method=sendServerMessage',
			type : 'POST',
			dataType : 'json',
			data : {serverMessage : document.getElementById('messageToServerString').value,
					password: document.getElementById('passwordForMessageToServerString').value },
			success : function(data) {
				var passwordCorrect = data.passwordCorrect;
				console.log(passwordCorrect);
				if(passwordCorrect === 'true'){
					alert("Succesfully sent Server message");
				} else {
					alert("Password wrong");
				}
				//$("#peopleOnServerDisplay").html(data.allClientNicknames);
				$("#messageToServerString").value='';
			}
		});
		document.getElementById('messageToServerString').value = "";
	}
	

	
	