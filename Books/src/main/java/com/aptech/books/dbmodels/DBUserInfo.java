package com.aptech.books.dbmodels;

public class DBUserInfo {
	private String uid;
	private String pwd;
	private String cat;
	
	public DBUserInfo() { }
	
	public DBUserInfo(String uid, String pwd, String cat) {
		this.uid = uid;
		this.pwd = pwd;
		this.cat = cat;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}
	
	
}
