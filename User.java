package com.itaas.entity;

   /**
    * User ʵ����
    * Wed Jul 29 14:22:17 CST 2015 Autogenerated
    */ 
public class User {
	private int ID;
	private String UserId;
	private String UserName;
	private int Age;
	private String Addr;
	public void setID(int ID){
		this.ID=ID;
	}
	public int getID(){
		return ID;
	}
	public void setUserId(String UserId){
		this.UserId=UserId;
	}
	public String getUserId(){
		return UserId;
	}
	public void setUserName(String UserName){
		this.UserName=UserName;
	}
	public String getUserName(){
		return UserName;
	}
	public void setAge(int Age){
		this.Age=Age;
	}
	public int getAge(){
		return Age;
	}
	public void setAddr(String Addr){
		this.Addr=Addr;
	}
	public String getAddr(){
		return Addr;
	}
}

