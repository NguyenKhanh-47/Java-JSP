<%@ page contentType="text/html;charset=UTF-8" language="java"
    import="com.aptech.books.dbmodels.*
            , com.aptech.books.helpers.*
            , com.aptech.books.models.WebUser
            , java.sql.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>List Books</title>
</head>
<body>
    <c:import url="../navbar.jsp" />

    <c:choose>
		<c:when test="${sessionScope.authorized_user == null}">
           <c:redirect url="../login.jsp?dest=Protected/listBooks" />
       </c:when>
       <c:when test="${sessionScope.authorized_user.authLevel < 1 }">
           <c:redirect url="../login.jsp?dest=Protected/listBooks" />
       </c:when>
       <c:when test="${sessionScope.authorized_user.uid == null }">
           <c:redirect url="../login.jsp?dest=Protected/listBooks" />
       </c:when>
        <c:when test="${sessionScope.bookData == null}" >
            <c:redirect url="${initParam.hostURL}${pageContext.request.contextPath}/bookcontroller.do" />
        </c:when>
    </c:choose>

    <div class="container-fluid listBookContainer mb-5">
    	<h4><c:out value="List Books" /></h4>
	    <table style="width: 100%">
	        <tr>
	            <td style="width:100%;height:80%;">
	                <table border="1" class="listBooks">
	                    <tr>
	                        <th>Order</th>
	                        <th>Title</th>
	                        <th>Content</th>
	                        <th>Price</th>
	                        <th>Author</th>
	                        <th style="width: 120px">Release Date</th>
	                        <th>Image</th>
	                        <th>Category</th>
	                        <c:if test="${sessionScope.authorized_user.authLevel eq 2}">
                                <th>Action</th>
                            </c:if>	                        
	                    </tr>
	                    <c:forEach var="tempBook" items="${sessionScope.bookData}" varStatus="iterationCount">
	                        <c:url var="updateLink" value="${initParam.hostURL}${pageContext.request.contextPath}/bookcontroller.do">
                            	<c:param name="command" value="LOAD" />
                            	<c:param name="bookID" value="${tempBook.bookID}" />
                        	</c:url>
                        	<c:url var="deleteLink" value="${initParam.hostURL}${pageContext.request.contextPath}/bookcontroller.do">
                            	<c:param name="command" value="DELETE" />
                            	<c:param name="bookID" value="${tempBook.bookID}" />
                        	</c:url>
	                        <tr>
	                            <td align="center">${iterationCount.count}</td>
	                            <td align="center">${tempBook.title}</td>
	                            <td>${tempBook.content}</td>
	                            <td align="center">${tempBook.price}</td>
	                            <td align="center">${tempBook.author}</td>
	                            <td align="center">${tempBook.releaseDate}</td>
	                            <td><img width="130" height="180" src="${initParam.hostURL}${pageContext.request.contextPath}/FileDisplayServlet/${tempBook.image}"></td>
	                            <td align="center">${tempBook.category}</td>
	                            <c:if test="${sessionScope.authorized_user.authLevel == 2}">
	                                <td align="center">
	                            		<a href='${updateLink}'>
	                            			<button style="width: 80px" type="button" class="btn btn-primary">Update</button>
	                            		</a>
	                            		<button data-toggle="modal" data-target="#exampleModal${tempBook.bookID}" style="width: 80px; margin-top: 8px" type="button" class="btn btn-danger">Delete</button>
	                            		<div class="modal fade" id="exampleModal${tempBook.bookID}" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel${tempBook.bookID}" aria-hidden="true">
										  <div class="modal-dialog" role="document">
										    <div class="modal-content">
										      <div class="modal-header">
										        <h5 class="modal-title" id="exampleModalLabel${tempBook.bookID}">Delete Book</h5>
										        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
										          <span aria-hidden="true">&times;</span>
										        </button>
										      </div>
										      <div class="modal-body">
										        Do you want to delete this book?. Your action cannot be undone.
										      </div>
										      <div class="modal-footer">
										        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
										        <a href='${deleteLink}'>
										        	<button type="button" class="btn btn-danger">OK</button>
										        </a>
										      </div>
										    </div>
										  </div>
										</div>
	                            	</td>
	                            </c:if>
	                        </tr>
	                    </c:forEach>
	                </table>
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
</body>
</html>