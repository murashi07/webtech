package com.registration;

import jakarta.servlet.RequestDispatcher;


import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;



@WebServlet("/register")
public class RegistrationServlets extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		String uname= request.getParameter("name");
		String upwd= request.getParameter("pass");
		String uemail= request.getParameter("email");
		String umobile= request.getParameter("contact");
		
//		PrintWriter out= response.getWriter();
//		out.print(uname);		
//		out.print(upwd);
//		out.print(uemail);
//		out.print(umobile);
		RequestDispatcher dispatcher = null;
		Connection con= null;
		
		String url="jdbc:mysql://localhost:3306/webtech?useSSL=false";
		String username="root";
		String password="12345";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con= DriverManager.getConnection(url,username,password);
			String sql="insert into users (uname, upwd, uemail, umobile) values(?,?,?,?)";
			PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, uname);
			pst.setString(2, upwd);
			pst.setString(3, uemail);
			pst.setString(4, umobile);
			int rowAffected= pst.executeUpdate();
			dispatcher = request.getRequestDispatcher("registration.jsp");
		
			if(rowAffected>0) {
				
				// Retrieve the auto-generated id
	            ResultSet generatedKeys = pst.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                int id = generatedKeys.getInt(1);
	                // Use the id as needed
				request.setAttribute("status","success");}
			}else {
				request.setAttribute("status","fail");
			}
			dispatcher.forward(request, response);
			//con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}


