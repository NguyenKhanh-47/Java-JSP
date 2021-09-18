<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login Error</title>
</head>
<body>
  <body>
  <c:import url="navbar.jsp" />

  <div class="errorLogin">
		<h1 align="center" class="pt-5">Access Denied</h1>
	</div>

  <c:import url="footer.jsp" >
    <c:param name="copyrightYear" value="${initParam.copyright}" />
    <c:param name="webLink" value="${initParam.weblink}" />
  </c:import>
  </body>
</body>
</html>