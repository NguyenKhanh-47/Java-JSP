<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Add Author</title>
</head>
<body>
    <body>
    <c:import url="../navbar.jsp" />
    <c:choose>
    	<c:when test="${sessionScope.authorized_user == null }">
           <c:redirect url="../login.jsp?dest=Protected/addAuthor" />
       </c:when>
       <c:when test="${sessionScope.authorized_user.uid == null }">
           <c:redirect url="../login.jsp?dest=Protected/addAuthor" />
       </c:when>
       <c:when test="${sessionScope.authorized_user.authLevel < 2 }">
           <c:redirect url="../index.jsp" />
       </c:when>
    </c:choose>
    
    <c:if test="${param.action == 'success'}">
        <div class="alert bg-success" id="alertSuccess" role="alert">
        	<div>Add Author successfully !</div>
		</div>
    </c:if>
    <c:if test="${param.action == 'failed'}">
        <div class="alert alertAdd bg-danger text-center" id="alertFailed" role="alert">
        	<div>Please enter Author name !</div>
		</div>
    </c:if>
    <c:if test="${param.action == 'existed'}">
        <div class="alert alertAdd bg-danger text-center" id="alertFailed" role="alert">
        	<div>Author name existed !</div>
		</div>
    </c:if>

    <div class="container-fluid addAuthor">
	    <table style="width: 100%">
	        <tr>
	            <td style="width:75%;height:80%;">
	                <form method="post" action="${initParam.hostURL}${pageContext.request.contextPath}/authorcontroller.do">
	
	                    <table class="addBook">
	                        <tr>
	                            <td>Author name <span class="requiredField">*</span></td>
	                            <td><input type="text" class="form-control" name="authorName" style="width: 100%;"></td>
	                        </tr>
	                        <tr>
	                            <td colspan="2" align="right">
	                            	<button type="submit" class="btn btn-primary">Add Author</button>
	                            </td>
	                        </tr>
	                    </table>
	                </form>
	            </td>
	        </tr>
	    </table>
	</div>

    <c:import url="../footer.jsp" >
        <c:param name="copyrightYear" value="${initParam.copyright}" />
        <c:param name="webLink" value="${initParam.weblink}" />
    </c:import>
    
    <%
    	session.removeAttribute("theBook");
    %>
	<script>
    	setTimeout(function () {document.getElementById('alertSuccess').style.display='none'}, 3000)
    	setTimeout(function () {document.getElementById('alertFailed').style.display='none'}, 3000)
    </script>
    </body>
</body>
</html>