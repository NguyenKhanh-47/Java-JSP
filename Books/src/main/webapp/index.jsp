<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Book Manager</title>
</head>
<body>
	<c:import url="navbar.jsp" />
	<c:choose>
		<c:when test="${sessionScope.authorized_user != null}">
	          <c:redirect url="${initParam.hostURL}${pageContext.request.contextPath}/Protected/listBooks.jsp" />
	     </c:when>
	     <c:otherwise>
	         <div class="welcome" align="center">
	          	 <div class="welcome-content">
	          	 	<h2>Welcome to Books manager</h2>
	             	<h6 class="mt-3">You are not logged in. Click <a href="login.jsp">here</a> to login</h6>
	          	 </div>
	         </div>
	     </c:otherwise>
	</c:choose>

    <c:import url="footer.jsp" >
        <c:param name="copyrightYear" value="${initParam.copyright}" />
        <c:param name="webLink" value="${initParam.weblink}" />
    </c:import>
    
    <%
    	session.removeAttribute("theBook");
    %>
</body>
</html>