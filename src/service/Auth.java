/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import database.koneksi;
import java.security.MessageDigest;
import java.sql.*;
import views.Menu;

public class Auth {
    public static boolean login(String username, String password) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "SELECT password,nama_pengguna FROM pengguna WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                Menu.namaUser = rs.getString("nama_pengguna");
                return storedHash.equals(hashPassword(password));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}

