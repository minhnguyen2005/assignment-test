/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import model.UserDTO;
import utils.DbUtils;
import org.mindrot.jbcrypt.BCrypt;
import model.UserDAO;

/**
 *
 * @author admin
 */
@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    private static final String HOME = "home.jsp";
    private static final String LOGIN_PAGE = "login.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = LOGIN_PAGE;
        try {
            String action = request.getParameter("action");
            if ("login".equals(action)) {
                url = handleLogin(request, response);
            } else if ("logout".equals(action)) {
                url = handleLogout(request, response);
            } else if ("register".equals(action)) {
                url = handleRgister(request, response);
            } else {
                request.setAttribute("message", "Invalid action:" + action);
                url = LOGIN_PAGE;
            }
        } catch (Exception e) {

        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

   private String handleLogin(HttpServletRequest request, HttpServletResponse response) {
    String url = LOGIN_PAGE; 
    try {
        // Get input values
        String username = request.getParameter("username");  // make sure your JSP input is named "username"
        String password = request.getParameter("password");

        // Call DAO
        UserDAO dao = new UserDAO();
        UserDTO user = dao.getUserByUsername(username); 

        // Check hashed password
        if (user != null && org.mindrot.jbcrypt.BCrypt.checkpw(password, user.getPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute("USER", user);
            url = HOME; 
        } else {
            request.setAttribute("message", "Invalid username or password.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("message", "An error occurred: " + e.getMessage());
    }

    return url;
}



    private String handleLogout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return LOGIN_PAGE;
    }

    private String handleRgister(HttpServletRequest request, HttpServletResponse response) {
        String url = LOGIN_PAGE;
        try {
            // 1. Get form data
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String address = request.getParameter("address");
            String phoneNumber = request.getParameter("phoneNumber");

            // 2. Hash the password
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // 3. Set default values
            String role = "user";
            Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());

            // 4. Save user to database
            Connection conn = DbUtils.getConnection();
            String sql = "INSERT INTO tblUsers (name, email, password, address, phoneNumber, role, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, hashedPassword);
            ps.setString(4, address);
            ps.setString(5, phoneNumber);
            ps.setString(6, role);
            ps.setTimestamp(7, createdAt);

            int result = ps.executeUpdate();

            if (result > 0) {
                request.setAttribute("message", "Registration successful. Please login.");
            } else {
                request.setAttribute("message", "Registration failed. Try again.");
            }

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Error: " + e.getMessage());
        }
        return LOGIN_PAGE;
    }

}
