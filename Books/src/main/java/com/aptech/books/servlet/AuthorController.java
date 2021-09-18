package com.aptech.books.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.aptech.books.dbmodels.DBManager;
import com.aptech.books.helpers.DBBooksQueries;
import com.aptech.books.models.Author;

@WebServlet("/authorcontroller.do")
public class AuthorController extends HttpServlet {
	private static final long serialVersionUID = 1L;


	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getAuthor(request, response);
    }
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        addAuthor(request,response);
    }
	
    private void getAuthor(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession s = request.getSession();

        if (getServletConfig().getServletContext().getAttribute("BooksDBManager") != null)
        {
            DBManager dbm = (DBManager)getServletConfig().getServletContext().getAttribute("BooksDBManager");

            try {
                if (!dbm.isConnected())
                {
                    if (!dbm.openConnection())
                        throw new IOException("Could not connect to database and open connection");
                }
                
                String query = DBBooksQueries.getAuthor();

                ArrayList<String> allAuthors = new ArrayList<String>();

                ResultSet rs = dbm.ExecuteResultSet(query);

                while (rs.next())
                {
                    String n = rs.getString("name");
                    allAuthors.add(n);
                }

                s.setAttribute("allAuthors", allAuthors);

            }
            catch (Exception ex)
            {
                throw new IOException("Query could not be executed to get all authors");
            }
            response.sendRedirect(getServletContext().getInitParameter("hostURL")
                    + getServletContext().getContextPath() + "/Protected/bookForm.jsp");
        }
        else
        {
            response.sendRedirect(getServletContext().getInitParameter("hostURL")
                    + getServletContext().getContextPath() + "login.jsp");
        }
    }
    
    private void addAuthor(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String authorName = request.getParameter("authorName");

    	if (authorName == null || authorName.equals(""))
        {
        	response.sendRedirect(getServletContext().getInitParameter("hostURL")
                    + getServletContext().getContextPath() +"/Protected/addAuthor.jsp?action=failed");
        	return;
        }

        try {
        	Author a = new Author();
            a.setName(authorName);

            if (getServletConfig().getServletContext().getAttribute("BooksDBManager") != null)
            {
                DBManager dbm = (DBManager)getServletConfig().getServletContext().getAttribute("BooksDBManager");

                try {
                    if (!dbm.isConnected())
                    {
                        if (!dbm.openConnection())
                            throw new IOException("Could not connect to database and open connection");
                    }
                     
                    String queryGet = DBBooksQueries.getOneAuthor(a);

                    Author theAuthor = new Author();

                    ResultSet rs = dbm.ExecuteResultSet(queryGet);
                    
                    while (rs.next())
                    {
                        String n = rs.getString("name");
                        theAuthor.setName(n);
                    }
                    
                    if (theAuthor.getName() == null || theAuthor.getName().equals("")) {
                    	String query = DBBooksQueries.insertAuthor(a);
                    	
                    	dbm.ExecuteNonQuery(query);
                    	
                    	HttpSession s = request.getSession();
                        s.setAttribute("allAuthors", null);

                        response.sendRedirect(getServletContext().getContextPath() + "/Protected/addAuthor.jsp?action=success");
                    } else {
                    	response.sendRedirect(getServletContext().getContextPath() + "/Protected/addAuthor.jsp?action=existed");
                    }
                    
                }
                catch (Exception ex)
                {
                    throw new IOException("Query could not be executed to insert a new author");
                }
            }
            else
            {
                response.sendRedirect(getServletContext().getInitParameter("hostURL")
                        + getServletContext().getContextPath() + "login.jsp");
            }
        } catch (Exception ex)
        {
            response.sendRedirect(getServletContext().getInitParameter("hostURL")
                    + getServletContext().getContextPath() + "/errorHandler.jsp");
        }
    }
}