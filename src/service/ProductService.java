/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import model.Product;
import model.PerishableProduct;
import model.NonPerishableProduct;
import model.DigitalProduct;
import model.BundleProduct;
import model.ProdukModel;
import database.koneksi;
import java.net.MalformedURLException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ProductService {
    public static String idOtomatis() {
        try (Connection conn = koneksi.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "SELECT id FROM produk ORDER BY id DESC LIMIT 1";
            ResultSet rs = st.executeQuery(sql);

            if (!rs.next()) return "PDC0001";

            String lastId = rs.getString("id");

            if (!lastId.startsWith("PDC") || lastId.length() < 7) {
                return "PDC0001";
            }

            int lastNumber = Integer.parseInt(lastId.substring(3));
            String newId = String.format("PDC%04d", lastNumber + 1);
            return newId;

        } catch (SQLException e) {
            e.printStackTrace();
            return "PDC0001"; // fallback safe default
        }
    }

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        try (Connection conn = koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM produk")) {

            while (rs.next()) {
                String id = rs.getString("id");
                String nama = rs.getString("nama");
                double harga = rs.getDouble("harga");
                String jenis = rs.getString("jenis_produk");

                switch (jenis) {
                    case "Tidak Kedaluwarsa":
                        products.add(new NonPerishableProduct(id, nama, harga));
                        break;

                    case "Kedaluwarsa":
                        java.sql.Date tanggal = rs.getDate("tanggal_kedaluwarsa");
                        products.add(new PerishableProduct(id, nama, harga, tanggal));
                        break;

                    case "Digital":
                        URL url = new URL(rs.getString("url"));
                        String vendor = rs.getString("nama_vendor");
                        products.add(new DigitalProduct(id, nama, harga, url, vendor));
                        break;

                    case "Paket":
                        List<Product> isiPaket = getIsiPaket(id, conn);
                        Double diskon = rs.getDouble("diskon");
                        products.add(new BundleProduct(id, nama, harga, diskon, isiPaket));
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    private static List<Product> getIsiPaket(String idPaket, Connection conn) throws SQLException {
        List<Product> isi = new ArrayList<>();

        String sql = "SELECT p.* FROM isi_paket ip JOIN produk p ON ip.id_produk = p.id WHERE ip.id_paket = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idPaket);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    String nama = rs.getString("nama");
                    double harga = rs.getDouble("harga");
                    String jenis = rs.getString("jenis_produk");

                    switch (jenis) {
                        case "Tidak Kedaluwarsa":
                            isi.add(new NonPerishableProduct(id, nama, harga));
                            break;
                        case "Kedaluwarsa":
                            java.sql.Date tanggal = rs.getDate("tanggal_kedaluwarsa");
                            isi.add(new PerishableProduct(id, nama, harga, tanggal));
                            break;
                        case "Digital":
                            try {
                                URL url = new URL(rs.getString("url"));
                                String vendor = rs.getString("nama_vendor");
                                isi.add(new DigitalProduct(id, nama, harga, url, vendor));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            }
        }

        return isi;
    }
    
    public static List<Product> getIsiPaket(String idPaket) {
        try (Connection conn = koneksi.getConnection()) {
            return getIsiPaket(idPaket, conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public static List<Product> searchProduk(String keyword) throws MalformedURLException {
        List<Product> hasil = new ArrayList<>();

        try (Connection conn = koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM produk WHERE nama LIKE ? OR id LIKE ? OR jenis_produk LIKE ? OR url LIKE ? OR nama_vendor LIKE ? OR harga LIKE ? OR diskon LIKE ? OR tanggal_kedaluwarsa LIKE ?")) {

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            stmt.setString(4, "%" + keyword + "%");
            stmt.setString(5, "%" + keyword + "%");
            stmt.setString(6, "%" + keyword + "%");
            stmt.setString(7, "%" + keyword + "%");
            stmt.setString(8, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String jenis = rs.getString("jenis_produk");
                String id = rs.getString("id");
                String nama = rs.getString("nama");
                double harga = rs.getDouble("harga");

                switch (jenis) {
                    case "Digital":
                        String url = rs.getString("url");
                        String vendor = rs.getString("nama_vendor");
                        hasil.add(new DigitalProduct(id, nama, harga, new URL(url), vendor));
                        break;
                    case "Kedaluwarsa":
                        java.util.Date tanggal = rs.getDate("tanggal_kedaluwarsa");
                        hasil.add(new PerishableProduct(id, nama, harga, tanggal));
                        break;
                    case "Paket":
                        double diskon = rs.getDouble("diskon");
                        BundleProduct bundle = new BundleProduct(id, nama, harga, diskon);
                        bundle.setIsiPaket(getIsiPaket(id, conn)); // ambil isi paketnya juga
                        hasil.add(bundle);
                        break;
                    default:
                        hasil.add(new NonPerishableProduct(id, nama, harga));
                        break;
                }
            }

        } catch (SQLException | MalformedURLException e) {
            e.printStackTrace();
        }

        return hasil;
    }
    
    public static boolean simpanProduk(String ID, String nama, double harga, String jenis, String tanggal, String urlProduk, String vendor) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "INSERT INTO produk (id, nama, harga, jenis_produk, tanggal_kedaluwarsa, url, nama_vendor) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, ID);
            stmt.setString(2, nama);
            stmt.setDouble(3, harga);
            stmt.setString(4, jenis);

            if ("Kedaluwarsa".equals(jenis)) {
                stmt.setString(5, tanggal);
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }

            if ("Digital".equals(jenis)) {
                stmt.setString(6, urlProduk);
                stmt.setString(7, vendor);
            } else {
                stmt.setNull(6, java.sql.Types.VARCHAR);
                stmt.setNull(7, java.sql.Types.VARCHAR);
            }

            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static boolean simpanProdukDigital(DigitalProduct digital) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "INSERT INTO produk (id, nama, harga, jenis_produk, url, nama_vendor) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, digital.getId());
            stmt.setString(2, digital.getNama());
            stmt.setDouble(3, digital.getHarga());
            stmt.setString(4, digital.getJenis());
            stmt.setURL(5, digital.getUrl());
            stmt.setString(6, digital.getNamaVendor());
            stmt.executeUpdate();
            LogService.tambahLog("Tambah Produk Digital", "Menambahkan Produk digital dengan ID :"+digital.getId()+", Nama :"+digital.getNama()+", Harga:"+digital.getHarga()+", Jenis:"+digital.getJenis()+", Url:"+digital.getUrl()+", Vendor:"+digital.getNamaVendor());
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static boolean simpanProdukKedaluwarsa(PerishableProduct perishable) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "INSERT INTO produk (id, nama, harga, jenis_produk, tanggal_kedaluwarsa) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String tanggal = dateFormat.format(perishable.getTanggalKedaluwarsa());
            stmt.setString(1, perishable.getId());
            stmt.setString(2, perishable.getNama());
            stmt.setDouble(3, perishable.getHarga());
            stmt.setString(4, perishable.getJenis());
            stmt.setString(5, tanggal);
            stmt.executeUpdate();
            LogService.tambahLog("Tambah Produk Kedaluwarsa", "Menambahkan Produk Kedaluwarsa dengan ID :"+perishable.getId()+", Nama :"+perishable.getNama()+", Harga:"+perishable.getHarga()+", Jenis:"+perishable.getJenis()+", Tanggal Kedaluwarsa:"+tanggal);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static boolean simpanProdukTidakKedaluwarsa(NonPerishableProduct nonPerishable) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "INSERT INTO produk (id, nama, harga, jenis_produk) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, nonPerishable.getId());
            stmt.setString(2, nonPerishable.getNama());
            stmt.setDouble(3, nonPerishable.getHarga());
            stmt.setString(4, nonPerishable.getJenis());
            stmt.executeUpdate();
            LogService.tambahLog("Tambah Produk Tidak Kedaluwarsa", "Menambahkan Produk Tidak Kedaluwarsa dengan ID :"+nonPerishable.getId()+", Nama :"+nonPerishable.getNama()+", Harga:"+nonPerishable.getHarga()+", Jenis:"+nonPerishable.getJenis());
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static boolean saveBundle(BundleProduct bundle) {
        Connection conn = null;
        PreparedStatement insertProdukStmt = null;
        PreparedStatement insertIsiStmt = null;

        try {
            conn = koneksi.getConnection();
            conn.setAutoCommit(false); // supaya bisa rollback kalau gagal

            // 1. Simpan ke tabel produk
            String sqlProduk = "INSERT INTO produk (id, nama, harga, jenis_produk, diskon) VALUES (?, ?, ?, ?, ?)";
            insertProdukStmt = conn.prepareStatement(sqlProduk);
            insertProdukStmt.setString(1, bundle.getId());
            insertProdukStmt.setString(2, bundle.getNama());
            insertProdukStmt.setDouble(3, bundle.getHarga());
            insertProdukStmt.setString(4, bundle.getJenis());
            insertProdukStmt.setDouble(5, bundle.getDiskon());
            insertProdukStmt.executeUpdate();

            // 2. Simpan ke tabel isi_paket
            String sqlIsi = "INSERT INTO isi_paket (id_paket, id_produk) VALUES (?, ?)";
            insertIsiStmt = conn.prepareStatement(sqlIsi);

            for (Product p : bundle.getIsiPaket()) {
                insertIsiStmt.setString(1, bundle.getId());
                insertIsiStmt.setString(2, p.getId());
                insertIsiStmt.addBatch();
            }
            insertIsiStmt.executeBatch();
            LogService.tambahLog("Tambah Produk Paket", "Menambahkan Produk Paket dengan ID :"+bundle.getId()+", Nama :"+bundle.getNama()+", Harga:"+bundle.getHarga()+", Jenis Paket:"+bundle.getJenis()+", Diskon:"+bundle.getDiskon());
            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // rollback kalau error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;

        } finally {
            try {
                if (insertProdukStmt != null) insertProdukStmt.close();
                if (insertIsiStmt != null) insertIsiStmt.close();
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static boolean editBundle(BundleProduct bundle) {
        Connection conn = null;
        PreparedStatement insertProdukStmt = null;
        PreparedStatement insertIsiStmt = null;
        PreparedStatement deleteIsiStmt = null;

        try {
            conn = koneksi.getConnection();
            conn.setAutoCommit(false);

            String sqlDelete = "DELETE FROM isi_paket WHERE id_paket = ?";
            deleteIsiStmt = conn.prepareStatement(sqlDelete);
            deleteIsiStmt.setString(1, bundle.getId());
            deleteIsiStmt.executeUpdate();
            
            String sqlProduk = "UPDATE produk SET nama = ?, harga = ?, diskon = ? WHERE id = ?";
            insertProdukStmt = conn.prepareStatement(sqlProduk);
            insertProdukStmt.setString(1, bundle.getNama());
            insertProdukStmt.setDouble(2, bundle.getHarga());
            insertProdukStmt.setDouble(3, bundle.getDiskon());
            insertProdukStmt.setString(4, bundle.getId());
            insertProdukStmt.executeUpdate();

            String sqlIsi = "INSERT INTO isi_paket (id_paket, id_produk) VALUES (?, ?)";
            insertIsiStmt = conn.prepareStatement(sqlIsi);

            for (Product p : bundle.getIsiPaket()) {
                insertIsiStmt.setString(1, bundle.getId());
                insertIsiStmt.setString(2, p.getId());
                insertIsiStmt.addBatch();
            }
            insertIsiStmt.executeBatch();
            LogService.tambahLog("Ubah Produk", "Mengubah Produk dengan ID :"+bundle.getId()+", Menjadi => Nama :"+bundle.getNama()+", Harga:"+bundle.getHarga()+", Jenis :Paket, Diskon :"+bundle.getDiskon());
            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // rollback kalau error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;

        } finally {
            try {
                if (insertProdukStmt != null) insertProdukStmt.close();
                if (insertIsiStmt != null) insertIsiStmt.close();
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
   public static ProdukModel getProduk(String ID) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "SELECT * FROM produk WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String nama = rs.getString("nama");
                double harga = rs.getDouble("harga");
                String jenis = rs.getString("jenis_produk");

                java.util.Date tanggalKedaluwarsa = null;
                String url = null;
                String vendor = null;

                if ("Kedaluwarsa".equals(jenis)) {
                    tanggalKedaluwarsa = rs.getDate("tanggal_kedaluwarsa");
                }

                if ("Digital".equals(jenis)) {
                    url = rs.getString("url");
                    vendor = rs.getString("nama_vendor");
                }

                return new ProdukModel(id, nama, harga, jenis, tanggalKedaluwarsa, url, vendor);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Tidak ditemukan
    }
    
    public static boolean updateProduk(String ID, String nama, double harga, String jenis, String tanggal, String urlProduk, String vendor) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "UPDATE produk SET nama = ?, harga = ?, jenis_produk = ?, tanggal_kedaluwarsa = ?, url = ?, nama_vendor = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, nama);
            stmt.setDouble(2, harga);
            stmt.setString(3, jenis);

            if ("Kedaluwarsa".equals(jenis)) {
                stmt.setString(4, tanggal); // pastikan formatnya cocok dengan DATE
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            if ("Digital".equals(jenis)) {
                stmt.setString(5, urlProduk);
                stmt.setString(6, vendor);
            } else {
                stmt.setNull(5, java.sql.Types.VARCHAR);
                stmt.setNull(6, java.sql.Types.VARCHAR);
            }

            stmt.setString(7, ID); // WHERE id = ?

            stmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static boolean deleteProduk(String id) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "DELETE FROM produk WHERE id = ?";
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
