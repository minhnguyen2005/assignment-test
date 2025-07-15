/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import utils.DbUtils;

/**
 *
 * @author admin
 */
public class UserDAO {

    public UserDAO() {

    }

    public boolean login(String username, String password) {
        UserDTO user = getUserByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return true;
        }
        return false;
    }

    public UserDTO getUserByUsername(String username) {
        UserDTO user = null;
        try {
            String sql = "SELECT * FROM [User] WHERE name = ?";
            Connection conn = DbUtils.getConnection();
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, username);

            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("user_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String pass = rs.getString("password");
                String address = rs.getString("address");
                String phoneNumber = rs.getString("phone_number");
                String role = rs.getString("role");
                Timestamp createdAt = rs.getTimestamp("created_at");

                user = new UserDTO(id, name, email, pass, address, phoneNumber, role, createdAt);
            }

            rs.close();
            pre.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> userList = new ArrayList<>();
        String sql = "SELECT userID, fullName, password, roleID, status FROM tblUsers ORDER BY userID";
        try {
            Connection conn = DbUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UserDTO user = new UserDTO();
                user.setUserId(rs.getInt("userID"));
                user.setName(rs.getString("fullName"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setRole(rs.getString("role"));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void updatePassword(int userId, String encryptSHA256) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
