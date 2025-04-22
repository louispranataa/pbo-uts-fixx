/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.net.URL;

public class DigitalProduct extends Product {
    private URL url;
    private String namaVendor;

    public DigitalProduct(String id, String nama, double harga, URL url, String namaVendor) {
        super(id, nama, harga);
        this.url = url;
        this.namaVendor = namaVendor;
    }

    public URL getUrl() {
        return url;
    }

    public String getNamaVendor() {
        return namaVendor;
    }

    @Override
    public String getJenis() {
        return "Digital";
    }
}

