<html>
<head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- Enable IE8 Standards mode -->
<meta http-equiv="X-UA-Compatible" content="IE=8" >
<title>Test Redirect</title>
<script>
	function incorrectRedirect(url) {
		// similar behavior as clicking on a link
		window.location.href = url;
	}

	function correctRedirect(url) {
		var ua = navigator.userAgent.toLowerCase(), 
		    isIE = ua.indexOf('msie') !== -1,
		    version = parseInt(ua.substr(4, 2), 10),
		    link;

		if (isIE && version < 9) {
			// IE8 and lower
			link = document.createElement('a');
			link.href = url;
			document.body.appendChild(link);
			link.click();
		} else {
			// All other browsers
			window.location.replace(url);
		}
	}

	function jQueryRedirect(url) {
		 $(location).attr('href',"http://stackoverflow.com");
	}
</script>
</head>
<body>
	<h1>This page contains code to test Redirect in IE8 and below</h1>
	<p>
		<button onclick="incorrectRedirect('http://vmjdevpc:9080/HeadersPrinter/index.jsp')">Incorrect Redirect</button>
	</p>
	<p>
		<button onclick="correctRedirect('http://vmjdevpc:9080/HeadersPrinter/index.jsp')">Correct Redirect</button>
	</p>
	<p>
		<button onclick="correctRedirect('http://vmjdevpc:9080/HeadersPrinter/index.jsp')">jQuery Redirect</button>
	</p>
</body>
</html>