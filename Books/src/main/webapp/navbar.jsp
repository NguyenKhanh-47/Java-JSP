<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="ISO-8859-1">
	<link rel="icon" href="${initParam.hostURL}${pageContext.request.contextPath}/public/image/favicon.io" type="image/x-icon">
  	<link type="text/css" rel="stylesheet" href="${initParam.hostURL}${pageContext.request.contextPath}/public/css/style.css" />
	<link type="text/css" rel="stylesheet" href="${initParam.hostURL}${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css" rel="stylesheet">
</head>
	<nav class="header navbar navbar-expand-lg navbar-dark bg-primary" style="height: 75px">
	  <a class="navbar-brand" href="${initParam.hostURL}${pageContext.request.contextPath}/index.jsp">
	  	<img src="${initParam.hostURL}${pageContext.request.contextPath}/public/image/books.png" alt="Logo" style="width:60px;">
	  </a>
	  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
	    <span class="navbar-toggler-icon"></span>
	  </button>
	  <div class="collapse navbar-collapse" id="navbarNavDropdown">
	    <ul class="navbar-nav mr-auto">
	      <li class="nav-item">
	        <a class="nav-link" class="nav-link" href="${initParam.hostURL}${pageContext.request.contextPath}/index.jsp">Home</a>
	      </li>
	      <c:if test="${sessionScope.authorized_user.authLevel eq 2}">
	           <li class="nav-item">
		        	<a class="nav-link" href="${initParam.hostURL}${pageContext.request.contextPath}/Protected/bookForm.jsp">Add Book</a>
		       </li>
		       <li class="nav-item">
		        	<a class="nav-link" href="${initParam.hostURL}${pageContext.request.contextPath}/Protected/addAuthor.jsp">Add Author</a>
		       </li>
		       <li class="nav-item">
		        	<a class="nav-link" href="${initParam.hostURL}${pageContext.request.contextPath}/Protected/addCategory.jsp">Add Category</a>
		       </li>
	      </c:if>
	    </ul>
	    <ul class="navbar-nav">
	    <%
	          if (session.getAttribute("authorized_user") != null)
	          {
	      %>
	          <li class="nav-item dropdown">
		      	<a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown">
		        	${sessionScope.authorized_user.uid}
		      	</a>
		      	<div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
		        	<a class="dropdown-item" href="${initParam.hostURL}${pageContext.request.contextPath}/signout.do">Sign out</a>
		        	<a class="dropdown-item" href="${initParam.hostURL}${pageContext.request.contextPath}/invalidatesessionandremovecookies.do">Clear All User Data</a>
		      	</div>
		      </li>
	      <%
	          }
	          else
	          {
	      %>
	          <li class="nav-item">
	        	<a class="nav-link" href="${initParam.hostURL}${pageContext.request.contextPath}/login.jsp">Login</a>
	      	  </li>
	      <%
	          }
	      %>
	    </ul>
	  </div>
	</nav>
	  <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
	  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>
	  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>
</body>
</html>




