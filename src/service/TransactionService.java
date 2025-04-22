/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import database.koneksi;
import java.net.URL;
import model.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class TransactionService {
    public static String idOtomatis() {
        try (Connection conn = koneksi.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "SELECT id FROM transaksi ORDER BY id DESC LIMIT 1"; 
            ResultSet rs = st.executeQuery(sql);

            if (!rs.next()) return "TRX0001"; 

            String lastId = rs.getString("id");

            if (!lastId.startsWith("TRX") || lastId.length() < 7) {
                return "TRX0001"; 
            }

            int lastNumber = Integer.parseInt(lastId.substring(3));
            String newId = String.format("TRX%04d", lastNumber + 1); 
            return newId;

        } catch (SQLException e) {
            e.printStackTrace();
            return "TRX0001"; // fallback safe default 
        }
    }
    public static Product getProduk(String ID) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "SELECT * FROM produk WHERE id LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String nama = rs.getString("nama");
                double harga = rs.getDouble("harga");
                Date tanggal = rs.getDate("tanggal_kedaluwarsa");
                String jenis = rs.getString("jenis_produk");

                switch (jenis) {
                    case "Tidak Kedaluwarsa":
                        return new NonPerishableProduct(id, nama, harga);
                    case "Kedaluwarsa":
                        // Menambahkan pengecekan null untuk tanggal_kedaluwarsa
                        if (rs.getDate("tanggal_kedaluwarsa") != null) {
                            return new PerishableProduct(id, nama, harga, tanggal);
                        } else {
                            System.err.println("Tanggal kedaluwarsa tidak tersedia untuk produk ID: " + id);
                            return null;  // Atau bisa dilemparkan exception sesuai kebutuhan
                        }
                    case "Paket":
                        double diskon = rs.getDouble("diskon");
                        return new BundleProduct(id, nama, harga, diskon);
                    case "Digital":
                        // Cek jika kolom URL atau vendor null
                        URL url = rs.getString("url") != null ? new URL(rs.getString("url")) : null;
                        String vendor = rs.getString("nama_vendor");
                        return new DigitalProduct(id, nama, harga, url, vendor);
                    default:
                        System.err.println("Jenis produk tidak dikenali untuk ID: " + id);
                        return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Produk tidak ditemukan
    }   

    public boolean simpanTransaksiPenjualan(PurchaseTransaction transaksi) {
        try (Connection conn = koneksi.getConnection()) {
            // Simpan ke tabel transaksi
            String sql = "INSERT INTO transaksi (id, tanggal, total, jenis_transaksi) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, transaksi.getTransactionId());
            stmt.setDate(2, new java.sql.Date(transaksi.getDate().getTime()));
            stmt.setDouble(3, transaksi.calculateTotal());
            stmt.setString(4, transaksi.getJenis());
            stmt.executeUpdate();

            // Simpan ke tabel detail_transaksi
            String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_produk, jumlah, sub_total) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtDetail = conn.prepareStatement(sqlDetail);

            for (TransactionItem item : transaksi.getItems()) {
                stmtDetail.setString(1, transaksi.getTransactionId());
                stmtDetail.setString(2, item.getProduct().getId());
                stmtDetail.setInt(3, item.getQuantity());
                stmtDetail.setDouble(4, item.getSubtotal());
                stmtDetail.addBatch();
            }

            stmtDetail.executeBatch();
            LogService.tambahLog("Transaksi Penjualan", "Transaksi ID :"+transaksi.getTransactionId()+", Jenis Transaksi :"+transaksi.getJenis()+", Total bayar:"+transaksi.calculateTotal());
            for (TransactionItem item : transaksi.getItems()) {
                LogService.tambahLog("ITEM TRANSAKSI",
                    "Transaksi ID: " + transaksi.getTransactionId() +
                    ", Produk: " + item.getProduct().getNama() +
                    ", Jumlah: " + item.getQuantity()+
                    ", SubTotal: " + item.getSubtotal());
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean simpanTransaksiRefund(RefundTransaction transaksi) {
        try (Connection conn = koneksi.getConnection()) {
            // Simpan ke tabel transaksi
            String sql = "INSERT INTO transaksi (id, tanggal, total, jenis_transaksi, alasan_retur) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, transaksi.getTransactionId());
            stmt.setDate(2, new java.sql.Date(transaksi.getDate().getTime()));
            stmt.setDouble(3, transaksi.calculateTotal());
            stmt.setString(4, transaksi.getJenis());
            stmt.setString(5, transaksi.getReason());
            stmt.executeUpdate();

            // Simpan ke tabel detail_transaksi
            String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_produk, jumlah, sub_total) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtDetail = conn.prepareStatement(sqlDetail);

            for (TransactionItem item : transaksi.getItems()) {
                stmtDetail.setString(1, transaksi.getTransactionId());
                stmtDetail.setString(2, item.getProduct().getId());
                stmtDetail.setInt(3, item.getQuantity());
                stmtDetail.setDouble(4, item.getSubtotal());
                stmtDetail.addBatch();
            }

            stmtDetail.executeBatch();
            LogService.tambahLog("Transaksi Retur", "Transaksi ID :"+transaksi.getTransactionId()+", Jenis Transaksi :"+transaksi.getJenis()+", Total bayar:"+transaksi.calculateTotal());
            for (TransactionItem item : transaksi.getItems()) {
                LogService.tambahLog("ITEM TRANSAKSI",
                    "Transaksi ID: " + transaksi.getTransactionId() +
                    ", Produk: " + item.getProduct().getNama() +
                    ", Jumlah: " + item.getQuantity()+
                    ", SubTotal: " + item.getSubtotal());
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
//    public static ProdukModel getProduk(String ID) {
//        try (Connection conn = koneksi.getConnection()) {
//            String sql = "SELECT * FROM produk WHERE id LIKE ?";
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            stmt.setString(1, ID);
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                String id = rs.getString("id");
//                String nama = rs.getString("nama");
//                double harga = rs.getDouble("harga");
//                double diskon = rs.getDouble("diskon");
//
//                return new ProdukModel(id, nama, harga, diskon);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null; // Tidak ditemukan
//    }

//    public boolean simpanTransaksi(PurchaseTransaction transaksi) {
//        try (Connection conn = koneksi.getConnection()) {
//            // Simpan ke tabel transaksi
//            String sql = "INSERT INTO transaksi (id, tanggal, total, jenis_transaksi) VALUES (?, ?, ?, ?)";
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            stmt.setString(1, transaksi.getTransactionId());
//            stmt.setDate(2, java.sql.Date.valueOf(transaksi.getDate()));
//            stmt.setDouble(3, transaksi.calculateTotal());
//            stmt.setString(4, transaksi.getJenis());
//            stmt.executeUpdate();
//
//            // Simpan ke tabel detail_transaksi
//            String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_produk, jumlah, sub_total) VALUES (?, ?, ?, ?)";
//            PreparedStatement stmtDetail = conn.prepareStatement(sqlDetail);
//
//            for (TransactionItem item : transaksi.getItems()) {
//                stmtDetail.setString(1, transaksi.getTransactionId());
//                stmtDetail.setString(2, item.getProduct().getId());
//                stmtDetail.setInt(3, item.getQuantity());
//                stmtDetail.setDouble(4, item.getSubtotal());
//                stmtDetail.addBatch();
//            }
//
//            stmtDetail.executeBatch();
//            LogService.tambahLog("Transaksi", "Transaksi ID :"+transaksi.getTransactionId()+", Jenis Transaksi :"+transaksi.getJenis()+", Total bayar:"+transaksi.calculateTotal());
//            for (TransactionItem item : transaksi.getItems()) {
//                LogService.tambahLog("ITEM TRANSAKSI",
//                    "Transaksi ID: " + transaksi.getTransactionId() +
//                    ", Produk: " + item.getProduct().getNama() +
//                    ", Jumlah: " + item.getQuantity()+
//                    ", SubTotal: " + item.getSubtotal());
//            }
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
}


