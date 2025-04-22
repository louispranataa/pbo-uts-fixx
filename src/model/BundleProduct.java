/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

public class BundleProduct extends Product {
    private List<Product> isiPaket;
    private double diskon;

    public BundleProduct(String id, String nama, double harga, double diskon, List<Product> isiPaket) {
        super(id, nama, harga);
        this.diskon = diskon;
        this.isiPaket = isiPaket;
    }

    public BundleProduct(String id, String nama, double harga, double diskon) {
        super(id, nama, harga);
        this.diskon = diskon;
        this.isiPaket = new ArrayList<>();
    }

    public void setIsiPaket(List<Product> isiPaket) {
        this.isiPaket = isiPaket;
    }
    
    public List<Product> getIsiPaket() {
        return isiPaket;
    }
    
    public double getDiskon() {
        return diskon;
    }

    public double getHargaDiskon() {
        double totalHarga = 0;
        for (Product p : isiPaket) {
            totalHarga += p.getHarga();
        }
        return totalHarga - harga; // diskon = total harga satuan - harga bundle
    }

    @Override
    public String getJenis() {
        return "Paket";
    }
}

