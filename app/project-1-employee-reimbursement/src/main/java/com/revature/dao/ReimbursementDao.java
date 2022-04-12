package com.revature.dao;

import com.revature.dto.AddReimbursementDTO;
import com.revature.dto.ResponseReimbursementDTO;
import com.revature.dto.UpdateReimbursementDTO;
import com.revature.dto.UpdateReimbursementStatusDTO;
import com.revature.model.*;
import com.revature.utility.ConnectionUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReimbursementDao {
    // With each reimbursement need:
        // id
        // amount
        // type
        // author -- names from users table
        // status -- associated with status type table
        // who resolved -- names from users table
        // description
        // image of receipt
        // type of reimbursement -- associated with reimb type table



    // TODO 11. deleteReimbursement


    // Create
    public ResponseReimbursementDTO addReimbursement(AddReimbursementDTO addReimbursementDTO, String receiptUrl) throws SQLException {
        try(Connection con = ConnectionUtility.getConnection()) {
            con.setAutoCommit(false);
            String sql = "insert into reimbursements (reimb_amount, reimb_submitted, reimb_description, reimb_receipt, reimb_author, reimb_type_id) " +
                    "values " +
                    "(?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setDouble(1, addReimbursementDTO.getReimbAmount());//Amount
            pstmt.setTimestamp(2, addReimbursementDTO.getReimbSubmitted()); //Timestamp
            pstmt.setString(3, addReimbursementDTO.getReimbDescription()); //Description
            pstmt.setString(4, receiptUrl); //ReceiptURL
            pstmt.setInt(5, addReimbursementDTO.getReimbAuthor()); //AuthorID
            pstmt.setInt(6, addReimbursementDTO.getReimbType()); //TypeID

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            rs.next();
            int reimbId = rs.getInt(1);

            String sqlSelect = "SELECT * " +
                                "FROM tickets t " +
                                "WHERE  t.reimb_id = ?";

            PreparedStatement pstmtSelect = con.prepareStatement(sqlSelect);
            pstmtSelect.setInt(1, reimbId);

            ResultSet rsNewReimb = pstmtSelect.executeQuery();
            rsNewReimb.next();

            int id = rsNewReimb.getInt("reimb_id");
            double amount = rsNewReimb.getDouble("reimb_amount");
            String date = new Date(rsNewReimb.getTimestamp("reimb_submitted").getTime()).toString();
            String description = rsNewReimb.getString("reimb_description");
            String firstName = rsNewReimb.getString("first_name");
            String lastName = rsNewReimb.getString("last_name");
            String type = rsNewReimb.getString("reimb_type");
            String status = rsNewReimb.getString("reimb_status");

            String urlDetails = "http://130.211.221.169:2000/users/" + addReimbursementDTO.getReimbAuthor() + "/reimbursements/" + id;


            ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO(id, amount, date, description, firstName, lastName, status, type, urlDetails);
            con.commit();
            return reimbursement;

        }
    }

    // R
    public Reimbursement getReimbursementById(int reimbId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            con.setAutoCommit(false);
            String sql = "SELECT *" +
                    "FROM tickets t " +
                    "WHERE t.reimb_id = ?";
            PreparedStatement pstmtSelect = con.prepareStatement(sql);
            pstmtSelect.setInt(1, reimbId);

            ResultSet rs = pstmtSelect.executeQuery();
            if (!rs.next())  {
                return null;
            }

            int id = rs.getInt("reimb_id");
            double amount = rs.getDouble("reimb_amount");

            String submitDateString = new Date(rs.getTimestamp("reimb_submitted").getTime()).toString();

            Date resolveDate = rs.getTimestamp("reimb_resolved");
            String resolveDateString;
            if (resolveDate != null) {
                resolveDateString = new Date(rs.getTimestamp("reimb_resolved").getTime()).toString();
            } else {
                resolveDateString = null;
            }
            String description = rs.getString("reimb_description");
            String url = rs.getString("reimb_receipt");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String email = rs.getString("user_email");
            int resolverId = rs.getInt("reimb_resolver");
            String type = rs.getString("reimb_type");
            String status = rs.getString("reimb_status");


            Reimbursement reimbursement = new Reimbursement(id, amount, submitDateString, resolveDateString, description, url, firstName, lastName, email, resolverId, type, status);
            con.commit();
            return reimbursement;
        }
    }


    public List<ResponseReimbursementDTO> getReimbursementsByUser(int userId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()){
            String sql = "SELECT * " +
                    "FROM employees e " +
                    "WHERE e.user_id = ?";
            PreparedStatement pstmtSelect = con.prepareStatement(sql);
            pstmtSelect.setInt(1, userId);

            ResultSet rsEmail = pstmtSelect.executeQuery();
            rsEmail.next();

            // Get user email for second query
            String email = rsEmail.getString("user_email");


            String sqlSelect = "SELECT * " +
                    "FROM tickets t " +
                    "WHERE t.user_email = ?";
            PreparedStatement pstmt = con.prepareStatement(sqlSelect);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            List<ResponseReimbursementDTO> reimbursements = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("reimb_id");
                double amount = rs.getDouble("reimb_amount");
                String date = new Date(rs.getTimestamp("reimb_submitted").getTime()).toString();
                String description = rs.getString("reimb_description");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String type = rs.getString("reimb_type");
                String status = rs.getString("reimb_status");

                String urlDetails = "http://130.211.221.169:2000/users/" + userId + "/reimbursements/" + id;


                ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO(id, amount, date, description, firstName, lastName, status, type, urlDetails);
                reimbursements.add(reimbursement);
            }
            return reimbursements;
            }
        }
    public List<ResponseReimbursementDTO> getReimbursementsByUserAndStatus(int userId, String currentStatus) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()){
            String sql = "SELECT * " +
                    "FROM employees e " +
                    "WHERE e.user_id = ? ";
            PreparedStatement pstmtSelect = con.prepareStatement(sql);
            pstmtSelect.setInt(1, userId);

            ResultSet rsEmail = pstmtSelect.executeQuery();
            rsEmail.next();

            // Get user email for second query
            String email = rsEmail.getString("user_email");


            String sqlSelect = "SELECT * " +
                    "FROM tickets t " +
                    "WHERE t.user_email = ? AND t.reimb_status = ?";
            PreparedStatement pstmt = con.prepareStatement(sqlSelect);
            pstmt.setString(1, email);
            pstmt.setString(2, currentStatus);
            ResultSet rs = pstmt.executeQuery();

            List<ResponseReimbursementDTO> reimbursements = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("reimb_id");
                double amount = rs.getDouble("reimb_amount");
                String date = new Date(rs.getTimestamp("reimb_submitted").getTime()).toString();
                String description = rs.getString("reimb_description");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String type = rs.getString("reimb_type");
                String status = rs.getString("reimb_status");

                String urlDetails = "http://130.211.221.169:2000/users/" + userId + "/reimbursements/" + id;


                ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO(id, amount, date,  description, firstName, lastName, status, type, urlDetails);
                reimbursements.add(reimbursement);
            }
            return reimbursements;
        }
    }
    public List<ResponseReimbursementDTO> getReimbursementsByDepartment(String department) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()){

            String sqlSelect = "SELECT * " +
                    "FROM tickets t " +
                    "WHERE t.user_role = ?";
            PreparedStatement pstmt = con.prepareStatement(sqlSelect);
            pstmt.setString(1, department);
            ResultSet rs = pstmt.executeQuery();

            List<ResponseReimbursementDTO> reimbursements = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("reimb_id");
                double amount = rs.getDouble("reimb_amount");
                String date = new Date(rs.getTimestamp("reimb_submitted").getTime()).toString();
                String description = rs.getString("reimb_description");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String type = rs.getString("reimb_type");
                String status = rs.getString("reimb_status");
                int userId = rs.getInt("user_id");

                String urlDetails = "http://130.211.221.169:2000/users/" + userId + "/reimbursements/" + id;


                ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO(id, amount, date, description, firstName, lastName, status, type, urlDetails);
                reimbursements.add(reimbursement);
            }
            return reimbursements;
        }
    }

    public List<ResponseReimbursementDTO> getAllReimbursements() throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()){


            String sqlSelect = "SELECT * " +
                    "FROM tickets t ";
            PreparedStatement pstmt = con.prepareStatement(sqlSelect);

            ResultSet rs = pstmt.executeQuery();

            List<ResponseReimbursementDTO> reimbursements = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("reimb_id");
                double amount = rs.getDouble("reimb_amount");
                String date = new Date(rs.getTimestamp("reimb_submitted").getTime()).toString();
                String description = rs.getString("reimb_description");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String type = rs.getString("reimb_type");
                String status = rs.getString("reimb_status");

                int userId = rs.getInt("user_id");

                String urlDetails = "http://130.211.221.169:2000/users/" + userId + "/reimbursements/" + id;


                ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO(id, amount, date, description, firstName, lastName, status, type, urlDetails);
                reimbursements.add(reimbursement);
            }
            return reimbursements;
        }
    }
    public List<ResponseReimbursementDTO> getAllReimbursementsByStatus(String status) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()){


            String sqlSelect = "SELECT * " +
                    "FROM tickets t " +
                    "WHERE reimb_status = ?";
            PreparedStatement pstmt = con.prepareStatement(sqlSelect);

            pstmt.setString(1, status);

            ResultSet rs = pstmt.executeQuery();


            List<ResponseReimbursementDTO> reimbursements = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("reimb_id");
                double amount = rs.getDouble("reimb_amount");
                String date = new Date(rs.getTimestamp("reimb_submitted").getTime()).toString();
                String description = rs.getString("reimb_description");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String type = rs.getString("reimb_type");

                int userId = rs.getInt("user_id");
                String urlDetails = "http://130.211.221.169:2000/users/" + userId + "/reimbursements/" + id;

                ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO(id, amount, date, description, firstName, lastName, status, type, urlDetails);
                reimbursements.add(reimbursement);
            }
            return reimbursements;
        }
    }

    public List<ResponseReimbursementDTO> getAllReimbursementsByStatusAndDepartment(String status, String department) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()){


            String sqlSelect = "SELECT * " +
                    "FROM tickets t " +
                    "WHERE reimb_status = ? AND user_role = ?";
            PreparedStatement pstmt = con.prepareStatement(sqlSelect);

            pstmt.setString(1, status);
            pstmt.setString(2, department);

            ResultSet rs = pstmt.executeQuery();

            List<ResponseReimbursementDTO> reimbursements = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("reimb_id");
                double amount = rs.getDouble("reimb_amount");
                String date = new Date(rs.getTimestamp("reimb_submitted").getTime()).toString();
                String description = rs.getString("reimb_description");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String type = rs.getString("reimb_type");
                int userId = rs.getInt("user_id");
                String urlDetails = "http://130.211.221.169:2000/users/" + userId + "/reimbursements/" + id;


                ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO(id, amount, date, description, firstName, lastName, status, type, urlDetails);
                reimbursements.add(reimbursement);
            }
            return reimbursements;
        }
    }

    public Reimbursement editUnresolvedReimbursement(int reimbId, UpdateReimbursementDTO reimbursement) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            con.setAutoCommit(false);
            String sql = "UPDATE reimbursements r " +
                    "SET REIMB_AMOUNT=?, REIMB_DESCRIPTION=?, REIMB_RECEIPT=?, REIMB_TYPE_ID=? " +
                    "WHERE r.reimb_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setDouble(1, reimbursement.getAmount());
            pstmt.setString(2, reimbursement.getDescription());
            pstmt.setString(3, reimbursement.getReceiptUrl());
            pstmt.setInt(4, reimbursement.getType());
            pstmt.setInt(5, reimbId);

            pstmt.executeUpdate();

            ReimbursementDao dao = new ReimbursementDao();

            con.commit();
            return dao.getReimbursementById(reimbId);
        }
    }
    public boolean updateReimbursementStatus(UpdateReimbursementStatusDTO dto, int reimbId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            con.setAutoCommit(false);
            String sql = "UPDATE reimbursements  " +
                    "SET REIMB_STATUS_ID = ?, REIMB_RESOLVED = ?, REIMB_RESOLVER = ?  " +
                    "WHERE reimb_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, dto.getStatusId());
            pstmt.setTimestamp(2, dto.getTimestamp());
            pstmt.setInt(3, dto.getResolverId());
            pstmt.setInt(4, reimbId);

            if(pstmt.executeUpdate() == 1) {
                con.commit();
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean deleteUnresolvedReimbursement(int reimbId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            con.setAutoCommit(false);
            String sql = "DELETE FROM reimbursements r " +
                    "WHERE r.reimb_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, reimbId);

            if(pstmt.executeUpdate() == 1) {
                con.commit();
                return true;
            } else {
                return false;
            }
        }
    }
}
