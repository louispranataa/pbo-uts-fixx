/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import database.koneksi;
import model.LogModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LogService {

    public static void tambahLog(String aksi, String keterangan) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "INSERT INTO log_aktivitas (aksi, keterangan) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, aksi);
            stmt.setString(2, keterangan);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<LogModel> getAllLog() {
        List<LogModel> list = new ArrayList<>();
        try (Connection conn = koneksi.getConnection()) {
            String sql = "SELECT * FROM log_aktivitas ORDER BY waktu DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LogModel log = new LogModel(
                    rs.getString("aksi"),
                    rs.getString("keterangan")
                );
                log.setIdLog(rs.getInt("id_log"));
                log.setWaktu(rs.getTimestamp("waktu").toLocalDateTime());
                list.add(log);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

