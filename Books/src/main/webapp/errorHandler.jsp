<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Error Handle</title>
</head>
<body>
	<%@include file="navbar.jsp" %>
	<div class="errorPage">
		<h1 align="center" class="pt-5">An Error has occurred!</h1>
		${pageContext.exception.message}
		${param.ex}
	</div>
	
	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>