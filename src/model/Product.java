/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public abstract class Product {
    protected String id;
    protected String nama;
    protected double harga;

    public Product(String id, String nama, double harga) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
    }

    public String getId() { return id; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }

    public abstract String getJenis(); // bisa return "Perishable", "Digital", dll.
}
