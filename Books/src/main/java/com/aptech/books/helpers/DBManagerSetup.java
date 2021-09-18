package com.aptech.books.helpers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.aptech.books.dbmodels.DBManager;
import com.aptech.books.dbmodels.IConnectionBehavior;
import com.aptech.books.dbmodels.MySQLConnectionBehavior;

@WebListener
public class DBManagerSetup implements ServletContextListener {
	
	private DBManager dbm = null;


    public DBManagerSetup() {
        // TODO Auto-generated constructor stub
    }


    public void contextDestroyed(ServletContextEvent sce)  { 
         if (dbm != null) {
        	 if (dbm.isConnected())
        		 dbm.closeConnection(false);
         }
         dbm = null;
    }


    public void contextInitialized(ServletContextEvent sce)  { 
    	String uid = sce.getServletContext().getInitParameter("dbuserid");
		String pwd = sce.getServletContext().getInitParameter("dbuserpwd");
		String cat = sce.getServletContext().getInitParameter("dbinitcat");
		
		// Option 1: Connect to MySQL
		IConnectionBehavior icb = new MySQLConnectionBehavior(uid,pwd,cat);
		
		// Option 2: Connect to MS SQLServer
		// IConnectionBehavior icb = new MssqlConnectionBehavior(uid,pwd,cat);
		
		dbm = new DBManager(icb);
		sce.getServletContext().setAttribute("BooksDBManager", dbm);
		System.out.print("BooksDBManager has been created!");
    }
	
}
