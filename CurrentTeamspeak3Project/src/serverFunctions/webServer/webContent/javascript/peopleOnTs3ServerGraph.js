// public variable whith the current lineChartData. read from when resizing window. 
//refreshed/written when ajax call from server is comming back
var lineChartData;

$(window).resize(function() {
	drawChart();
});

function drawChart() {
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

	var data = google.visualization.arrayToDataTable(lineChartData);

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

	var chart = new google.visualization.LineChart(document
			.getElementById('curve_chart'));

	chart.draw(data, options);
}

function refreshLineChart() {
	setInterval(function() {
		getLineChartData();
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
			for (i = 1; i < data.length; i++) {
				data[i][0] = new Date(data[i][0]);// make unix time stamp into
													// date
			}

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

			var chartData = google.visualization.arrayToDataTable(data);

			var chart = new google.visualization.LineChart(document
					.getElementById('history_chart'));

			chart.draw(chartData, options);
		},
		failure : function(data) {
			console.log("ERROR!!! getting line chart data");
			console.log(data);
		}
	})
}

$(function() {
	$("#datepicker").datepicker();
});
