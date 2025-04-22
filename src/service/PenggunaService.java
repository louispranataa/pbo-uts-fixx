/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import database.koneksi;
import model.PenggunaModel;

import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PenggunaService {

    public static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public static String IdOtomatis() {
        try (Connection conn = koneksi.getConnection()) {
            Statement st = conn.createStatement();
            String sql = "SELECT * FROM pengguna ORDER BY id DESC LIMIT 1";
            ResultSet rs = st.executeQuery(sql);
            if (!rs.next()) return "P-001";

            String ID = rs.getString("id");
            int kodeAngka = Integer.parseInt(ID.substring(2)) + 1;
            return String.format("P-%03d", kodeAngka);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean validasiUsername(String username) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "SELECT COUNT(*) FROM pengguna WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // anggap sudah dipakai jika error
        }
    }
    
    public static boolean validasiUsernameUpdate(String username, String id) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "SELECT COUNT(*) FROM pengguna WHERE username = ? AND id != ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // anggap sudah dipakai jika error
        }
    }
        
    public static boolean addPengguna(PenggunaModel penggunaP) {
        try (Connection conn = koneksi.getConnection()) {
            if (validasiUsername(penggunaP.getUsername())) {
                return false;
            }

            String sql = "INSERT INTO pengguna (id, username, password, nama_pengguna) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, penggunaP.getId());
            stmt.setString(2, penggunaP.getUsername());
            stmt.setString(3, hashPassword(penggunaP.getPasswordHash()));
            stmt.setString(4, penggunaP.getName());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<PenggunaModel> getAllPengguna() {
        List<PenggunaModel> list = new ArrayList<>();
        try (Connection conn = koneksi.getConnection()) {
            String sql = "SELECT * FROM pengguna";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PenggunaModel user = new PenggunaModel(
                        rs.getString("id"),
                        rs.getString("nama_pengguna"),
                        rs.getString("username")
                );
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean updatePenggunaP(PenggunaModel penggunaP) {
        try (Connection conn = koneksi.getConnection()) {
            if (validasiUsernameUpdate(penggunaP.getUsername(), penggunaP.getId())) {
                return false;
            }
            String sql = "UPDATE pengguna SET username = ?, password = ?, nama_pengguna = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, penggunaP.getUsername());
            stmt.setString(2, hashPassword(penggunaP.getPasswordHash()));
            stmt.setString(3, penggunaP.getName());
            stmt.setString(4, penggunaP.getId());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updatePengguna(PenggunaModel pengguna) {
        try (Connection conn = koneksi.getConnection()) {
            if (validasiUsernameUpdate(pengguna.getUsername(), pengguna.getId())) {
                return false;
            }
            String sql = "UPDATE pengguna SET username = ?, nama_pengguna = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, pengguna.getUsername());
            stmt.setString(2, pengguna.getName());
            stmt.setString(3, pengguna.getId());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deletePengguna(String id) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "DELETE FROM pengguna WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
