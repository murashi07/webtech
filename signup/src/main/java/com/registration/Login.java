package com.registration;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.*;
import java.sql.*;


@WebServlet("/login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uemail = request.getParameter("username");
        String upwd = request.getParameter("password");

        HttpSession session = request.getSession();
        RequestDispatcher dispatcher = null;
        Connection con = null;

        String url = "jdbc:mysql://localhost:3306/webtech?useSSL=false";
        String username = "root";
        String password = "12345";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
            String sql = "select * from users where uemail=? and upwd=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, uemail);
            pst.setString(2, upwd);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                session.setAttribute("name", rs.getString("uname"));
                dispatcher = request.getRequestDispatcher("index.jsp");
            } else {
                request.setAttribute("status", "failed");
                dispatcher = request.getRequestDispatcher("login.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Consider logging the error instead
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Consider logging the error instead
                }
            }
        }

        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }
}

