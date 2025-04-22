/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

public class PerishableProduct extends Product {
    private Date tanggalKedaluwarsa;

    public PerishableProduct(String id, String nama, double harga, Date tanggalKedaluwarsa) {
        super(id, nama, harga);
        this.tanggalKedaluwarsa = tanggalKedaluwarsa;
    }

    public Date getTanggalKedaluwarsa() {
        return tanggalKedaluwarsa;
    }

    @Override
    public String getJenis() {
        return "Kedaluwarsa";
    }
}
