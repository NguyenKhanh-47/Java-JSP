package com.aptech.books.dbmodels;

import java.sql.Connection;
import java.sql.DriverManager;

public class MssqlConnectionBehavior extends DBUserInfo implements IConnectionBehavior {

	public MssqlConnectionBehavior(String uid, String pwd, String cat) {
		super(uid, pwd, cat);
	}

	@Override
	public Connection getConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").getDeclaredConstructor().newInstance();
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
		return String.format("jdbc:sqlserver://localhost:1433;database=%s;"
				+ "user=%s;password=%s;encrypt=false;trustServerCertificate=false;loginTimeout=30;"
				, getCat()
				, "sa"
				, getPwd());
	}

	@Override
	public String getConnectionDetails() {
		return "MSSQL database is connection to " + getCat();
	}

	@Override
	public String getTablesSchemaQuery() {
		return "select table_name from information_schema.tables where table_schema = " + getCat();
	}

}