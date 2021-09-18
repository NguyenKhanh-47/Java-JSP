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
import com.aptech.books.models.Category;

@WebServlet("/categorycontroller.do")
public class CategoryController extends HttpServlet {
	private static final long serialVersionUID = 1L;


	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getCategory(request, response);
    }
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        addCategory(request,response);
    }
	
    private void getCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
                
                String query = DBBooksQueries.getCategory();

                ArrayList<String> allCategory = new ArrayList<String>();

                ResultSet rs = dbm.ExecuteResultSet(query);

                while (rs.next())
                {
                    String n = rs.getString("name");
                    allCategory.add(n);
                }

                s.setAttribute("allCategory", allCategory);

            }
            catch (Exception ex)
            {
                throw new IOException("Query could not be executed to get all category");
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
    
    private void addCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String categoryName = request.getParameter("categoryName");

    	if (categoryName == null || categoryName.equals(""))
        {
        	response.sendRedirect(getServletContext().getInitParameter("hostURL")
                    + getServletContext().getContextPath() +"/Protected/addCategory.jsp?action=failed");
        	return;
        }

        try {
        	Category c = new Category();
            c.setName(categoryName);

            if (getServletConfig().getServletContext().getAttribute("BooksDBManager") != null)
            {
                DBManager dbm = (DBManager)getServletConfig().getServletContext().getAttribute("BooksDBManager");

                try {
                    if (!dbm.isConnected())
                    {
                        if (!dbm.openConnection())
                            throw new IOException("Could not connect to database and open connection");
                    }
                    
                    String queryGet = DBBooksQueries.getOneCategory(c);

                    Category theCategory = new Category();

                    ResultSet rs = dbm.ExecuteResultSet(queryGet);
                    
                    while (rs.next())
                    {
                        String n = rs.getString("name");
                        theCategory.setName(n);
                    }
                    
                    if (theCategory.getName() == null || theCategory.getName().equals("")) {
                    	String query = DBBooksQueries.insertCategory(c);
                    	
                    	dbm.ExecuteNonQuery(query);
                    	
                    	HttpSession s = request.getSession();
                        s.setAttribute("allCategory", null);

                        response.sendRedirect(getServletContext().getContextPath() + "/Protected/addCategory.jsp?action=success");
                    } else {
                    	response.sendRedirect(getServletContext().getContextPath() + "/Protected/addCategory.jsp?action=existed");
                    }
                }
                catch (Exception ex)
                {
                    throw new IOException("Query could not be executed to insert a new cagegory");
                }
            }
            else
            {
                response.sendRedirect(getServletContext().getInitParameter("hostURL")
                        + getServletContext().getContextPath() + "login.jsp");
                return;
            }
        } catch (Exception ex)
        {
            response.sendRedirect(getServletContext().getInitParameter("hostURL")
                    + getServletContext().getContextPath() + "/errorHandler.jsp");
        }
    }
}