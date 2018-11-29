// public variable whith the current lineChartData. read from when resizing window. 
//refreshed/written when ajax call from server is comming back
var lineChartData;
var historyChartData;
var lastCheckedHistoryDate;

$(window).resize(function() {
	drawChart();
	if(historyChartData !== undefined){
		drawHistoryChart();		
	}
	
});

function drawChart() {
	// case that the current date should be displayed
	if($('#datepicker').val() === currentDate()){
		if (lineChartData === undefined) {
			return;
		}
		
		for (i = 1; i < lineChartData.length; i++) {
			lineChartData[i][0] = new Date(lineChartData[i][0]);// make unix time
			// stamp into date
		}
		var time = new Date().getTime();
		var date = new Date(time);

		lineChartData[lineChartData.length] = [ date,
				lineChartData[lineChartData.length - 1][1] ]; // add current date
		// with last number
		// of peoples

		var chartData = google.visualization.arrayToDataTable(lineChartData);
		
		var options = {
				title : '24h history of the number of people online',
			// curveType: 'function',
			vAxis : {
				title : "Number of people",
				viewWindow : {
					min : 0,
				// max: 32
				},
			},
			legend : {
				position : 'bottom'
			},
		};
		
	// case that an older/newer date should be displayed
	} else {
		for (i = 1; i < historyChartData.length; i++) {
			historyChartData[i][0] = new Date(historyChartData[i][0]);// make unix time stamp into date
		}

		// in case the server did not sent any date for the requested date
		if(historyChartData.length < 2){
			var list = document.getElementById("curve_chart");
			if (list.hasChildNodes()) {
			    list.removeChild(list.childNodes[0]);
			}
			document.getElementById("curve_chart").classList.remove("chart");
			var img = document.createElement("IMG");
			img.src = "images/noDataFound.gif";
			document.getElementById("curve_chart").appendChild(img);
			return;
		}
		
		
		var options = {
			title : 'All events at the given date',
			// curveType: 'function',
			vAxis : {
				title : "Number of people",
				viewWindow : {
					min : 0,
				// max: 32
				},
			},
			legend : {
				position : 'bottom'
			},
		};

		var chartData = google.visualization.arrayToDataTable(historyChartData);
	}
	
	var chart = new google.visualization.LineChart(document
			.getElementById('curve_chart'));

	document.getElementById("curve_chart").classList.add('chart'); // set class so that div is set according to the google library
	
	chart.draw(chartData, options);
}

function refreshLineChart() {
	setInterval(function() {
		if($('#datepicker').val() === currentDate()){
			// selecting a older date after selecting the current day graph 
			lastCheckedHistoryDate = "JUST SOME STRING";
			getLineChartData();			
		} else {
			if(lastCheckedHistoryDate === $('#datepicker').val()){
				// do nothing since history data is already there 
			// get new data since new date is selected
			} else {
				lastCheckedHistoryDate = $('#datepicker').val();
				getHistoryData();				
			}
		}
	}, 1000);
}

function getLineChartData() {
	$.ajax({
		url : '/Update?method=getLineChartData',
		type : 'GET',
		dataType : 'json',
		success : function(data) {
			lineChartData = data;
			drawChart();
		},
		failure : function(data) {
			console.log("ERROR!!! getting line chart data");
			console.log(data);
		}
	})
}

function getHistoryData() {
	$.ajax({
		url : '/historyData',
		type : 'POST',
		dataType : 'json',
		data : {
			dateInMilis : new Date(document.getElementById('datepicker').value)
					.getTime(),
		},
		success : function(data) {
			historyChartData = data;
			drawChart();
		},
		failure : function(data) {
			console.log("ERROR!!! getting line chart data");
			console.log(data);
		}
	})
}
