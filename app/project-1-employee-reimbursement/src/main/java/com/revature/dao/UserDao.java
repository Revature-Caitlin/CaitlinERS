package com.revature.dao;

import com.revature.dto.LoginDTO;
import com.revature.dto.UserDTO;
import com.revature.model.User;
import com.revature.model.UserRole;
import com.revature.utility.ConnectionUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public UserDao(){}

    public User getUserByUsernameAndPassword(LoginDTO dto) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "select u.user_id, u.first_name, u.last_name, u.username, u.user_email, ur.user_role_id, ur.user_role " +
                    "from users u " +
                    "inner join user_roles ur " +
                    "on ur.user_role_id = u.user_role_id " +
                    "where u.username = ? and u.user_password = crypt(? , u.user_password)";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, dto.getUsername());
            pstmt.setString(2, dto.getPassword());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { //login successful
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("user_email");
                int userRoleId = rs.getInt("user_role_id");
                String role = rs.getString("user_role");

                UserRole userRole = new UserRole(userRoleId, role);
                return new User(userId, username, firstName, lastName, email, userRole);
            }


            return null;
        }
    }

    public UserDTO getUserByUserId(int id) throws SQLException {

        try(Connection con = ConnectionUtility.getConnection()) {
            String sql = "SELECT * " +
                    "FROM employees e " +
                    "WHERE user_id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { //login successful
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("user_email");
                int userRoleId = rs.getInt("user_role_id");
                String role = rs.getString("user_role");

                UserRole userRole = new UserRole(userRoleId, role);
                return new UserDTO(id, firstName, lastName, email, userRole);
            }


            return null;

            }



    }

    public List<UserDTO> getAllUsers() throws SQLException {
        try(Connection con = ConnectionUtility.getConnection()) {
            String sql = "SELECT * " +
                    "FROM employees";

            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();

            List<UserDTO> users = new ArrayList<>();
            while (rs.next()) { //login successful
                int id = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("user_email");
                int userRoleId = rs.getInt("user_role_id");
                String role = rs.getString("user_role");

                UserRole userRole = new UserRole(userRoleId, role);
                UserDTO user = new UserDTO(id, firstName, lastName, email, userRole);

                users.add(user);

            }
            return users;
        }
    }
    public List<UserDTO> getAllUsersByDepartment(String department) throws SQLException {
        try(Connection con = ConnectionUtility.getConnection()) {
            String sql = "SELECT * " +
                    "FROM employees e " +
                    "WHERE e.user_role = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, department);

            ResultSet rs = pstmt.executeQuery();

            List<UserDTO> users = new ArrayList<>();
            while (rs.next()) { //login successful
                int id = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("user_email");
                int userRoleId = rs.getInt("user_role_id");
                String role = rs.getString("user_role");

                UserRole userRole = new UserRole(userRoleId, role);
                UserDTO user = new UserDTO(id, firstName, lastName, email, userRole);

                users.add(user);

            }
            return users;
        }
    }
}
