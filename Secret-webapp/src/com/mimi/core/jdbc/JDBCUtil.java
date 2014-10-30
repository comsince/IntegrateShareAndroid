package com.mimi.core.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JDBCUtil {
	 
		 
		 private JDBCUtil(){}
		 
		 public static Connection getConnection()  
		 {
			
			try {
				Class.forName("com.mysql.jdbc.Derver");
				return DriverManager.getConnection("jdbc:mysql://w.rdc.sae.sina.com.cn:3307/app_mimiapi?useUnicode=true&amp;characterEncoding=UTF-8", "zloxk1j5mx", "2h3mizmijhl2j151m4jwyzyh02k4hmyj1kiihh4l");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return null;
			}
		 }
		 
		 public static void close(Connection c,PreparedStatement ps,ResultSet set) 
		 {
			 try {
				set.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				c.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 
		 public static void close(Connection c,PreparedStatement ps) 
		 {
			 
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				c.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 public static void close(Connection c) 
		 {
			 
			 
			try {
				c.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
}
