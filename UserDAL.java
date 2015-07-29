package com.itaas.dal;

/**
 * User 实体访问类
 * Wed Jul 29 16:57:25 CST 2015 Auto generated,Not allowed modification
*/ 
public class UserDAL {
	/**
	 * User Insert 对象
	*/ 
	public void InsertUser(User userEntity){
		StringBuffer sqlSb=new StringBuffer();
		sqlSb.append("insert into User values(")
		sqlSb.append("'"+userEntity.getID()+"',");
		sqlSb.append("'"+userEntity.getUserId()+"',");
		sqlSb.append("'"+userEntity.getUserName()+"',");
		sqlSb.append("'"+userEntity.getAge()+"',");
		sqlSb.append("'"+userEntity.getAddr()+"'");
		sqlSb.append("\";");
		String sql=sqlSb.toString();
	}

	/**
	 * User Update 对象
	*/ 
	public void UpdateUser(User userEntity){
		StringBuffer sqlSb=new StringBuffer();
		sqlSb.append("update User set ")
		sqlSb.append("ID='"+userEntity.getID()+"',");
		sqlSb.append("UserId='"+userEntity.getUserId()+"',");
		sqlSb.append("UserName='"+userEntity.getUserName()+"',");
		sqlSb.append("Age='"+userEntity.getAge()+"',");
		sqlSb.append("Addr='"+userEntity.getAddr()+"',");
		sqlSb.append("where ID='"+userEntity.getID()+"'");
		String sql=sqlSb.toString();
	}

	/**
	 * User Select 对象
	*/ 
	public User GetUserByKeyId(String ID){
		String sql="select * from User where ID='"+ID+"'";
		Cursor c = null;//todo 查询数据
		User UserEntity=null;
		if(c.moveToNext()){
			UserEntity=new User();
			UserEntity.setID(c.getInt(c.getColumnIndex("ID")));
			UserEntity.setUserId(c.getString(c.getColumnIndex("UserId")));
			UserEntity.setUserName(c.getString(c.getColumnIndex("UserName")));
			UserEntity.setAge(c.getInt(c.getColumnIndex("Age")));
			UserEntity.setAddr(c.getString(c.getColumnIndex("Addr")));
		}
		c.close();
		return UserEntity;
	}
}

