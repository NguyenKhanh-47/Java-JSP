package com.aptech.books.servlet;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.aptech.books.dbmodels.DBManager;
import com.aptech.books.helpers.DBBooksQueries;
import com.aptech.books.models.WebUser;

@WebServlet("/LoginUser.do")
public class LoginUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String getHashPwd(String passwordToHash) {
		String generatedPassword = null;
	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-512");
	        md.update(passwordToHash.getBytes());
	        byte[] bytes = md.digest();
	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i< bytes.length ;i++){
	            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        generatedPassword = sb.toString();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return generatedPassword;
	}

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession s = request.getSession();
        WebUser wu = (WebUser) s.getAttribute("authorized_user");

        if (wu == null || wu.getUid().equals("") || wu.getUid() == null || wu.getAuthLevel() < 1) {
            String uid = "";
            String pwd = "";

            if (request.getParameter("uid") != null)
                uid = request.getParameter("uid");

            if (request.getParameter("pwd") != null)
                pwd = request.getParameter("pwd");

            if ((wu == null || wu.getUid().equals("")  || wu.getAuthLevel() < 1 || wu.getUid() == null)
                    && (uid != "" && pwd != "")) {

                if (getServletConfig().getServletContext().getAttribute("BooksDBManager") != null)
                {
                    DBManager dbm = (DBManager)getServletConfig().getServletContext().getAttribute("BooksDBManager");

                    try {
                        if (!dbm.isConnected())
                        {
                            if (!dbm.openConnection())
                                throw new IOException("Could not connect to database and open connection");
                        }
                        
                        String pwdHash = getHashPwd(pwd);

                        String query = DBBooksQueries.getWebUserByUsernameAndPassword(uid, pwdHash);
                        ResultSet rs = dbm.ExecuteResultSet(query);

                        while (rs.next())
                        {
                            wu = new WebUser();

                            wu.setUid(rs.getString("uid"));
                            wu.setPwd(rs.getString("password"));
                            wu.setAuthLevel(rs.getInt("authLevel"));

                            s.setAttribute("authorized_user", wu);

                            if (request.getParameter("rememberMe") != null)
                            {
                                String rememberMe =  request.getParameter("rememberMe");
                                if (rememberMe.equalsIgnoreCase("ON"))
                                {
                                    int CookieLife = 3600*24*7;

                                    Cookie uidCookie = new Cookie("credentials_uid", uid);
                                    Cookie pwdCookie = new Cookie("credentials_pwd", pwd);

                                    uidCookie.setMaxAge(CookieLife);
                                    pwdCookie.setMaxAge(CookieLife);

                                    response.addCookie(uidCookie);
                                    response.addCookie(pwdCookie);
                                }
                            }
                        }

                    }
                    catch (Exception ex)
                    {
                        System.out.println("Exception: " + ex.getMessage());
                        response.sendRedirect(getServletContext().getContextPath() +"/login.jsp?action=failed");
                        return;
                    }
                }
                else
                {
                    response.sendRedirect(getServletContext().getContextPath() + "/login.jsp");
                }
            }

            if (wu == null || wu.getUid().equals("") || wu.getUid() == null || wu.getAuthLevel() < 1)
            {
                response.sendRedirect(getServletContext().getContextPath() +"/login.jsp?action=failed");
                return;
            }

        }

        String target = (request.getParameter("dest")==null || request.getParameter("dest")=="")
                ? "index.jsp"
                : request.getParameter("dest") + ".jsp";

        response.sendRedirect(target);
    }
}
