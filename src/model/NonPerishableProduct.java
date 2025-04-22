/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class NonPerishableProduct extends Product {
    public NonPerishableProduct(String id, String nama, double harga) {
        super(id, nama, harga);
    }

    @Override
    public String getJenis() {
        return "Tidak Kedaluwarsa";
    }
}
