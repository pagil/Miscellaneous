<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- Enable IE8 Standards mode -->
<meta http-equiv="X-UA-Compatible" content="IE=8" >
<style>
    table, th, td {
    	border: 1px solid black;
    }
</style>
<title>Test Redirect</title>
<script>
	function incorrectRedirect(url) {
		// similar behavior as clicking on a link
		window.location.href = url;
	}

	function correctRedirect(url) {
		var link = document.createElement('a');

		if (link.click) {
			// IE8 and lower
			link.href = url;
			link.style.display = "none";
			document.body.appendChild(link);
			alert("About to click " + url);
			link.click();
		} else {
		    alert("BAD :(");
			// All other browsers
			window.location.replace(url);
		}
	}

	function jQueryRedirect(url) {
		 $(location).attr('href',"http://stackoverflow.com");
	}
</script>
</head>
<body onload="alert(navigator.userAgent);">
<p>
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
</p>
<p>
	<h2>List of HTTP Request Headers</h2>
	<table>
	  <thead><tr><th>Header Name</th><th>Header Value</th></tr></thead>
	  <c:forEach items="${header}" var="httpHeader">
	    <tr><td><c:out value="${httpHeader.key}"/></td>
	    <td><c:out value="${httpHeader.value}"/></td></tr>
	  </c:forEach>
	</table>
</p>
<hr>
<p>
	<h2>List of HTTP Session Attributes</h2>
	<table>
	  <thead><tr><th>Attribute Name</th><th>Attribute Value</tr></thead>
	  <tr><td>Session ID</td><td><c:out value="${pageContext.session.id}"/></td></tr>
	</table>
<p>	
</body>
</html>