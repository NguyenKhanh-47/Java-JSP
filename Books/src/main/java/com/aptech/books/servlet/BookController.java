package com.aptech.books.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.aptech.books.dbmodels.DBManager;
import com.aptech.books.helpers.DBBooksQueries;
import com.aptech.books.models.Book;

@WebServlet("/bookcontroller.do")
@MultipartConfig(
        fileSizeThreshold = 1024*1024*1,    // 1MB
        maxFileSize = 1024 * 1024 * 10,     // 10MB
        maxRequestSize = 1024 * 1024 * 100 // 100MB
)
public class BookController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String theCommand = request.getParameter("command");
		System.out.println("thecommand: "+ theCommand);

        if (theCommand == null) {
            theCommand = "LIST";
        }

        switch (theCommand) {
            case "DELETE":
                deleteBook(request, response);
                break;
            case "LOAD":
                loadBook(request, response);
                break;
            default:
                getBookData(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String theCommand = request.getParameter("command");
    	System.out.println("command: " + theCommand);

        if (theCommand == null) {
            theCommand = "LIST";
        }

        switch (theCommand) {
            case "ADD":
                addBook(request,response);
                break;
            case "UPDATE":
                updateBook(request, response);
                break;
        }
    }

    private void getBookData(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
                
                String query = DBBooksQueries.getBook();
                ArrayList<Book> allBooks = new ArrayList<Book>();
                ResultSet rs = dbm.ExecuteResultSet(query);
                while (rs.next())
                {
                    Book b = new Book();
                    b.setBookID(rs.getInt("bookID"));
                    b.setTitle(rs.getString("title"));
                    b.setContent(rs.getString("content"));
                    b.setPrice(rs.getInt("price"));
                    b.setAuthor(rs.getString("author"));
                    b.setReleaseDate(rs.getString("releaseDate"));
                    b.setImage((rs.getString("image") == null || rs.getString("image").equals("")) ?
                            "books.png" : rs.getString("image"));
                    b.setCategory(rs.getString("category"));

                    System.out.println(b);
                    allBooks.add(b);
                }
                s.setAttribute("bookData", allBooks);
                System.out.println("finish getting bookData");

            }
            catch (Exception ex)
            {
                throw new IOException("Query could not be executed to get all books");
            }
            response.sendRedirect(getServletContext().getInitParameter("hostURL")
                    + getServletContext().getContextPath() +"/Protected/listBooks.jsp");
        }
        else
        {
            response.sendRedirect(getServletContext().getInitParameter("hostURL")
                    + getServletContext().getContextPath() + "login.jsp");
        }
    }
    
    private void loadBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession s = request.getSession();
        String bookID = request.getParameter("bookID");

        if (getServletConfig().getServletContext().getAttribute("BooksDBManager") != null)
        {
            DBManager dbm = (DBManager)getServletConfig().getServletContext().getAttribute("BooksDBManager");

            try {
                if (!dbm.isConnected())
                {
                    if (!dbm.openConnection())
                        throw new IOException("Could not connect to database and open connection");
                }

                String query = DBBooksQueries.loadBook(bookID);

                Book theBook = new Book();

                ResultSet rs = dbm.ExecuteResultSet(query);

                while (rs.next())
                {
                	theBook.setBookID(rs.getInt("bookID"));
                	theBook.setTitle(rs.getString("title"));
                	theBook.setContent(rs.getString("content"));
                	theBook.setPrice(rs.getInt("price"));
                	theBook.setAuthor(rs.getString("author"));
                	theBook.setReleaseDate(rs.getString("releaseDate"));
                	theBook.setImage((rs.getString("image") == null || rs.getString("image").equals("")) ?
                            "books.png" : rs.getString("image"));
                	theBook.setCategory(rs.getString("category"));
                }
                s.setAttribute("theBook", theBook);
            }
            catch (Exception ex)
            {
                throw new IOException("Query could not be executed to get the book with given bookID");
            }
            response.sendRedirect(getServletContext().getInitParameter("hostURL")
                    + getServletContext().getContextPath() +"/Protected/bookForm.jsp");
        }
        else
        {
            response.sendRedirect(getServletContext().getInitParameter("hostURL")
                    + getServletContext().getContextPath() + "login.jsp");
        }
    }

    private void addBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	Part filePart = request.getPart("image");
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
		
		if (!fileName.equals("")) {
			InputStream inputStream =  filePart.getInputStream();
			String uploadPath = getServletContext().getInitParameter("uploadPath");
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
			            uploadDir.mkdir();
			    }
			FileOutputStream outputStream = new FileOutputStream(uploadPath + 
			File.separator + fileName);
	        int read = 0;
	        final byte[] bytes = new byte[1024];
	        while ((read = inputStream.read(bytes)) != -1) {
	            outputStream.write(bytes, 0, read);
	        }
		}
    	
    	String title = request.getParameter("title");
        String content = request.getParameter("content");
        String price = request.getParameter("price");
        String author = request.getParameter("author");
        String releaseDate = request.getParameter("releaseDate");
        String category = request.getParameter("category");


        if (title == null || title.equals("")
            || content == null || content.equals("")
            || price == null || price.equals("")
            || author == null || author.equals("")
            || releaseDate == null || releaseDate.equals("")
            || category == null || category.equals(""))
        {
        	response.sendRedirect(getServletContext().getInitParameter("hostURL")
                    + getServletContext().getContextPath() +"/Protected/bookForm.jsp?action=failed");
        	return;
        }

        try {
            Book b = new Book();
            b.setTitle(title);
            b.setContent(content);
            b.setPrice(Integer.parseInt(price));
            b.setAuthor(author);
            b.setReleaseDate(releaseDate);
            b.setImage(fileName.equals("") || fileName.equals(null) ? "books.png" : fileName);
            b.setCategory(category);
            System.out.println(b);

            if (getServletConfig().getServletContext().getAttribute("BooksDBManager") != null)
            {
                DBManager dbm = (DBManager)getServletConfig().getServletContext().getAttribute("BooksDBManager");

                try {
                    if (!dbm.isConnected())
                    {
                        if (!dbm.openConnection())
                            throw new IOException("Could not connect to database and open connection");
                    }

                    String query = DBBooksQueries.insertBook(b);
                    System.out.print(query);

                    dbm.ExecuteNonQuery(query);
                }
                catch (Exception ex)
                {
                    throw new IOException("Query could not be executed to insert a new book");
                }

                HttpSession s = request.getSession();
                s.setAttribute("bookData", null);

                response.sendRedirect(getServletContext().getContextPath() + "/Protected/bookForm.jsp?action=success");
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
    
    private void updateBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Part filePart = request.getPart("image");
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
		
		if (!fileName.equals("")) {
			InputStream inputStream =  filePart.getInputStream();
			String uploadPath = getServletContext().getInitParameter("uploadPath");
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
			            uploadDir.mkdir();
			    }
			FileOutputStream outputStream = new FileOutputStream(uploadPath + 
			File.separator + fileName);
	        int read = 0;
	        final byte[] bytes = new byte[1024];
	        while ((read = inputStream.read(bytes)) != -1) {
	            outputStream.write(bytes, 0, read);
	        }
		}
    	
    	String title = request.getParameter("title");
        String content = request.getParameter("content");
        String price = request.getParameter("price");
        String author = request.getParameter("author");
        String releaseDate = request.getParameter("releaseDate");
        String category = request.getParameter("category");

        if (title == null || title.equals("")
                || content == null || content.equals("")
                || price == null || price.equals("")
                || author == null || author.equals("")
                || releaseDate == null || releaseDate.equals("")
                || category == null || category.equals(""))
        {
            response.sendRedirect(getServletContext().getInitParameter("hostURL")
                    + getServletContext().getContextPath() + "/bookcontroller.do?command=LOAD&bookID="
                    + request.getParameter("bookID"));
            return;
        }

        try {
        	HttpSession s = request.getSession();
            Book tempBook = (Book) s.getAttribute("theBook");    ;       
            int bookID = Integer.parseInt(request.getParameter("bookID"));
            System.out.println("bookID " + bookID);

            Book b = new Book();
            
            b.setBookID(bookID);
            b.setTitle(title);
            b.setContent(content);
            b.setPrice(Integer.parseInt(price));
            b.setAuthor(author);
            b.setReleaseDate(releaseDate);
            b.setImage(fileName.equals("") || fileName.equals(null) ? tempBook.getImage() : fileName);
            b.setCategory(category);
            
            System.out.println("newBook update " + b);

            if (getServletConfig().getServletContext().getAttribute("BooksDBManager") != null)
            {
                DBManager dbm = (DBManager)getServletConfig().getServletContext().getAttribute("BooksDBManager");

                try {
                    if (!dbm.isConnected())
                    {
                        if (!dbm.openConnection())
                            throw new IOException("Could not connect to database and open connection");
                    }

                    String query = DBBooksQueries.updateBook(b);
                    System.out.println(query);

                    dbm.ExecuteNonQuery(query);
                }
                catch (Exception ex)
                {
                    throw new IOException("Query could not be executed to update this book");
                }

                s.setAttribute("bookData", null);
                s.setAttribute("theBook", null);

                response.sendRedirect(getServletContext().getInitParameter("hostURL") +
                        getServletContext().getContextPath() + "/Protected/listBooks.jsp");
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
    
    private void deleteBook(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            String bookID = request.getParameter("bookID");
            System.out.println(bookID);

            if (getServletConfig().getServletContext().getAttribute("BooksDBManager") != null)
            {
                DBManager dbm = (DBManager)getServletConfig().getServletContext().getAttribute("BooksDBManager");

                try {
                    if (!dbm.isConnected())
                    {
                        if (!dbm.openConnection())
                            throw new IOException("Could not connect to database and open connection");
                    }

                    String query = DBBooksQueries.deleteBook(bookID);

                    dbm.ExecuteNonQuery(query);
                }
                catch (Exception ex)
                {
                    throw new IOException("Query could not be executed to delete this book");
                }

                HttpSession s = request.getSession();
                s.setAttribute("bookData", null);

                response.sendRedirect(getServletContext().getInitParameter("hostURL") +
                        getServletContext().getContextPath() + "/Protected/listBooks.jsp");
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