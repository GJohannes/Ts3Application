<html>
<head>
<title>Embedded Jetty: JSP Examples</title>
</head>
<body>
	<h1>TODO implement private messages</h1>
</body>
<script src="/../jquery-3.3.1.js"></script>
<script type="text/javascript">

$(document).ready(function() {
	var url_string = window.location.href; //window.location.href
	var url = new URL(url_string);
	var c = url.searchParams.get("User");
	console.log(c);
})

</script>