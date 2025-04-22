/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

public class ProdukModel extends Product {
    private double diskon;
    private String jenisProduk;
    private Date tanggalKedaluwarsa;
    private String url;
    private String vendor;

    public ProdukModel(String id, String nama, double harga, String jenisProduk,
                       Date tanggalKedaluwarsa, String url, String vendor) {
        super(id, nama, harga);
        this.jenisProduk = jenisProduk;
        this.tanggalKedaluwarsa = tanggalKedaluwarsa;
        this.url = url;
        this.vendor = vendor;
    }
    public ProdukModel(String id, String nama, double harga, double diskon) {
        super(id, nama, harga); 
        this.diskon = diskon;
    }

    // Constructor Tanpa field opsional
    public ProdukModel(String id, String nama, double harga, String jenisProduk) {
        this(id, nama, harga, jenisProduk, null, null, null);
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }
    
    public double getDiskon() { return diskon; }
    public void setDiskon(double diskon) { this.diskon = diskon; }

    public String getJenisProduk() { return jenisProduk; }
    public void setJenisProduk(String jenisProduk) { this.jenisProduk = jenisProduk; }

    public Date getTanggalKedaluwarsa() { return tanggalKedaluwarsa; }
    public void setTanggalKedaluwarsa(Date tanggalKedaluwarsa) { this.tanggalKedaluwarsa = tanggalKedaluwarsa; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }
    
     @Override
    public String getJenis() {
        return jenisProduk;
    }
}
