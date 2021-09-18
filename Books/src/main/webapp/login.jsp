<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <c:choose>
	   <c:when test="${sessionScope.authorized_user != null}">
           <c:redirect url="index.jsp" />
       </c:when>
    </c:choose>
    <c:if test="${param.action == 'failed'}">
        <div class="alert alertAdd bg-danger text-center" id="alertFailed" role="alert">
        	<div>Username or password is not correct !</div>
		</div>
    </c:if>

    <c:import url="navbar.jsp" />
    <div class="loginForm" >
      <div class="container">
           <form id="login" method="post" action="LoginUser.do" class="box">
               <h1>Login</h1>
               <p class="text-muted"> Please enter your user name and password!</p>
               <input type="text" name="uid" placeholder="Username" value="${cookie.credentials_uid.value}"> 
               <input type="password" name="pwd" placeholder="Password" value="${cookie.credentials_pwd.value}">
               <input id="rememberMe" name="rememberMe" type="checkbox"> <label for="rememberMe" class="text-light">RememberMe</label>
               <input type="submit" value="Login">
               <input type="hidden" name="dest" value="${param.dest}"/>
           </form>
       	</div>
     </div>

  <c:import url="footer.jsp" >
    <c:param name="copyrightYear" value="${initParam.copyright}" />
    <c:param name="webLink" value="${initParam.weblink}" />
  </c:import>
  <script>
    	setTimeout(function () {document.getElementById('alertFailed').style.display='none'}, 3000)
  </script>
</body>
</html>