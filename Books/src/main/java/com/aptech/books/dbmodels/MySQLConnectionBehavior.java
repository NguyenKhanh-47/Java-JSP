package com.aptech.books.dbmodels;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnectionBehavior extends DBUserInfo implements IConnectionBehavior {

	public MySQLConnectionBehavior(String uid, String pwd, String cat) {
		super(uid, pwd, cat);
	}

	@Override
	public Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
			Connection cn = DriverManager.getConnection(getConnectionURL());
			return cn;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	@Override
	public String getConnectionURL() {
		return String.format("jdbc:mysql://localhost/%s"
				+ "?user=%s&password=%s"
				, getCat()
				, getUid()
				, getPwd());
	}

	@Override
	public String getConnectionDetails() {
		return "MySQL database is connection to " + getCat();
	}

	@Override
	public String getTablesSchemaQuery() {
		return "select table_name from information_schema.tables where table_schema = " + getCat();
	}

}