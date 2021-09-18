<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Book Form</title>
</head>
<body>
    <c:import url="../navbar.jsp" />
    <c:choose>
    	<c:when test="${sessionScope.authorized_user == null }">
           <c:redirect url="../login.jsp?dest=Protected/bookForm" />
       </c:when>
       <c:when test="${sessionScope.authorized_user.uid == null }">
           <c:redirect url="../login.jsp?dest=Protected/bookForm" />
       </c:when>
       <c:when test="${sessionScope.authorized_user.authLevel < 2 }">
           <c:redirect url="../index.jsp" />
       </c:when>

        <c:when test="${sessionScope.allAuthors == null}" >
            <c:redirect url="${initParam.hostURL}${pageContext.request.contextPath}/authorcontroller.do" />
        </c:when>
        <c:when test="${sessionScope.allCategory == null}" >
            <c:redirect url="${initParam.hostURL}${pageContext.request.contextPath}/categorycontroller.do" />
        </c:when>
    </c:choose>
    
    <c:if test="${param.action == 'success'}">
        <div class="alert alertAdd bg-success" id="alertSuccess" role="alert">
        	<div>Add Book successfully !</div>
		</div>
    </c:if>
    <c:if test="${param.action == 'failed'}">
        <div class="alert alertAdd bg-danger text-center" id="alertFailed" role="alert">
        	<div>Please enter book informations</div>
		 	<div>in required field !</div>
		</div>
    </c:if>

    <div class="container-fluid">
	    <table style="width: 100%">
	        <tr>
	            <td style="width:75%;height:80%;">
	                <form method="post" action="${initParam.hostURL}${pageContext.request.contextPath}/bookcontroller.do" enctype="multipart/form-data">
						<c:if test="${theBook != null}">
							<input type="hidden" name="command" value="UPDATE"/>
							<input type="hidden" name="bookID" value="${ theBook.bookID}" />
						</c:if>
						<c:if test="${theBook == null}">
							<input type="hidden" name="command" value="ADD"/>
						</c:if>
	                    <table class="addBook">
	                        <tr>
	                            <td>Title <span class="requiredField">*</span></td>
	                            <td><input type="text" value="${theBook.title}" class="form-control" name="title" style="width: 100%;"></td>
	                        </tr>
	                        <tr>
	                            <td>Content <span class="requiredField">*</span></td>
	                            <td>
	                                <textarea name="content" class="form-control" style="width: 100%; height: 100">${theBook.content}</textarea>
	                            </td>
	                        </tr>
	                        <tr>
	                            <td>Price <span class="requiredField">*</span></td>
	                            <td><input type="number" value="${theBook.price}" class="form-control" name="price" style="width: 100%;"></td>
	                        </tr>
	                        <tr>
	                            <td>Author <span class="requiredField">*</span></td>
	                            <td>
	                                <select name="author" class="custom-select" style="width: 100%;" >
	                                    <option ${theBook.author eq '' ? 'selected="selected"' : '' } disabled selected>--Choose Author--</option>
	                                    <c:forEach items="${sessionScope.allAuthors}" var="authorName">
	                                        <option ${theBook.author eq authorName ? 'selected="selected"' : ''}>${authorName}</option>
	                                    </c:forEach>
	                                </select>
	                            </td>
	                        </tr>
	                        <tr>
	                            <td>Release Date <span class="requiredField">*</span></td>
	                            <td><input type="date" value="${theBook.releaseDate}" class="form-control" name="releaseDate" style="width: 100%;"></td>
	                        </tr>
							<tr id="imagePreview" class=${theBook eq null ? "d-none" : "hi"}>
	                            <td></td>
	                            <td align="center"><div><img width="200" height="250" id="demo" src="${initParam.hostURL}${pageContext.request.contextPath}/FileDisplayServlet/${theBook eq null ? 'books.png' : theBook.image}"/></div></td>
	                        </tr>
	                        <tr>
	                            <td>Image</td>
	                            <td><input id="myFile" onchange="previewFile()" type="file" class="form-control" accept="image/png, image/jpeg" name="image" style="width: 100%;"></td>
	                        </tr>
	                        <tr>
	                            <td>Category <span class="requiredField">*</span></td>
	                            <td>
	                                <select name="category" class="custom-select" style="width: 100%;" >
	                                    <option ${theBook.category eq '' ? 'selected="selected"' : ''} disabled selected>--Choose Category--</option>
	                                    <c:forEach items="${sessionScope.allCategory}" var="categoryName">
	                                        <option ${theBook.category eq categoryName ? 'selected="selected"' : ''}>${categoryName}</option>
	                                    </c:forEach>
	                                </select>
	                            </td>
	                        </tr>
	                        <tr>
	                            <td colspan="2" align="right">
	                            	<button type="submit" class="btn btn-primary" >${theBook == null ? "Add" : "Update"} Book</button>
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
    
    <script>
    	setTimeout(function () {document.getElementById('alertFailed').style.display='none'}, 3000)
    	setTimeout(function () {document.getElementById('alertSuccess').style.display='none'}, 3000)
		function previewFile() {
	  	  const show = document.getElementById("imagePreview");
	  	  const preview = document.getElementById("demo");
	  	  const file = document.querySelector('input[type=file]').files[0];
	  	  const reader = new FileReader();
	
	  	  reader.addEventListener("load", function () {
	  	    preview.src = reader.result;
	  	  }, false);
	
	  	  if (file) {
	  		show.setAttribute("class", "inline-block")
	  	    reader.readAsDataURL(file);
	  	  }
	  	}
    </script>
</body>
</html>