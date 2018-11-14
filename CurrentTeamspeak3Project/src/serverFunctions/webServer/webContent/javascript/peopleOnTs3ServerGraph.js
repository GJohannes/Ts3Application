	

	function drawChart(data) {
		if(data === undefined){
			refreshLineChart(); // in case that loading takes longer
			return;	
		}
		
		for(i = 1; i < data.length; i++){
			data[i][0] = new Date(data[i][0]);// make unix time stamp into date
		}
		var time = new Date().getTime();
    	var date = new Date(time);

		data[data.length] = [date, data[data.length-1][1]]; // add current date with last number of peoples
	
  		var data = google.visualization.arrayToDataTable(data);

  		var options = {
  			title: 'People on the Teamspeak3 Server Online',
   		 //curveType: 'function',
   		 vAxis: {
    			title: "Number of people",
    			viewWindow: {
          			min: 0,
          			//max: 32
        		},
    		},
    	legend: { position: 'bottom' },
    	width:  2000,
    	height: 500
  		};
  	
  		var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));
  	
  		chart.draw(data, options);
	}

	function refreshLineChart() {
		setInterval(function(){
			getLineChartData();
		}, 1000);
	}


	function getLineChartData(){
		$.ajax({
			url : '/Update?method=getLineChartData',
			type : 'GET',
			dataType : 'json',
			success : function(data) {
				drawChart(data);
			},
			failure : function(data){
				console.log("ERROR!!! getting line chart data");
				console.log(data);
			}
		})
	}