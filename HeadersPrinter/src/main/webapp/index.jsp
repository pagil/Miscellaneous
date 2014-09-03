<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<header>
  <style>
    table, th, td {
    	border: 1px solid black;
    }
  </style>
</header>
<body>
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
	  <c:forEach items="${sessionScope}" var="sessionAttribute">
	    <tr><td><c:out value="${sessionAttribute.blah}"/></td>
	    <td><c:out value="${sessionAttribute.blah}"/></td></tr>
	  </c:forEach>
	</table>
<p>
</body>
</html>
