package com.websystique.springmvc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.websystique.springmvc.model.User;

public class ConnectionDao {
	Connection con = null;
	public Connection RetriveConnection() {
	try {
		Class.forName("com.mysql.jdbc.Driver");  
		con=DriverManager.getConnection(  
		"jdbc:mysql://awsusers.cgzjtkh58i23.ap-south-1.rds.amazonaws.com:3306/awsusers","admin","password123");
		System.out.println("********************Connection established successfully.....");
		}catch(Exception e){ System.out.println(e);}
	return con;  
	}
	
	public List<User> getUserList(Connection conn){
		if(conn == null) {
			conn = RetriveConnection();
		}
		List<User> userList = new ArrayList<>();
		try {
		Statement stmt=conn.createStatement();  
		ResultSet rs=stmt.executeQuery("select * from user");  
		while(rs.next())  {
			User user = new User();
			user.setId(rs.getInt(1));
			user.setUsername(rs.getString(2));
			user.setAddress(rs.getString(3));
			user.setEmail(rs.getString(4));
			userList.add(user);
		}
		System.out.println("list is: "+userList.size());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return userList;
		
		//con.close();  
		
	}
	
	public boolean createNewUserRecord(User fromUser) {
		User user = null;
		boolean flagValue = false;
		System.out.println("Calling insert new user");
		Connection l_con = con;
		if(l_con == null) {
			l_con = RetriveConnection();
		}
		
		try {
			if(fromUser != null) {
				int id = getUserIdCount(l_con);
				String query = "insert into user values (?,?,?,?)";
				PreparedStatement pstm = l_con.prepareStatement(query);
				pstm.setLong(1, id+1);
				pstm.setString(2, fromUser.getUsername());
				pstm.setString(3, fromUser.getAddress());
				pstm.setString(4, fromUser.getEmail());
				
				flagValue = pstm.execute();
			}
		}catch(Exception exe) {
			exe.printStackTrace();
		}finally {
			try {
				l_con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flagValue;
		
	}
	
	public int getUserIdCount(Connection conn) {
		int count = 0;
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery("select max(id) from user");
			while(res.next()) {
				count = res.getInt("max(id)");
				System.out.println(" count : "+count);
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error occurred");
		}finally {
			
		}
		return count;
	}

}
