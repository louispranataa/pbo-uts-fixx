/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class PurchaseTransaction extends Transaction implements Payable {
    private List<TransactionItem> items;
    private double bayar;
    
    public PurchaseTransaction(String transactionId, Date date, double bayar, List<TransactionItem> items) {
        super(transactionId, date);
        this.items = items;
        this.bayar = bayar;
    }

    public void setBayar(double bayar) {
        this.bayar = bayar;
    }

    public double getBayar() {
        return bayar;
    }

    public void addItem(Product product, int quantity) {
        items.add(new TransactionItem(product, quantity));
    }

    public List<TransactionItem> getItems() {
        return items;
    }
    
    @Override
    public String getJenis() {
        return "Penjualan";
    }

    @Override
    public double calculateTotal() {
        return items.stream().mapToDouble(TransactionItem::getSubtotal).sum();
    }

    @Override
    public String processTransaction() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaksi ID: ").append(transactionId).append(" telah diproses").append("\n\n");
        return sb.toString();
    }

    @Override
    public String serializeTransaction() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaksi dengan ID: ").append(transactionId).append("\nTanggal: ").append(date).append("\n\n");
        for (TransactionItem item : items) {
            sb.append(item.getProduct().getNama()).append(" x").append(item.getQuantity())
              .append(" = ").append(item.getSubtotal()).append("\n");
        }
        sb.append("\nTotal: ").append(calculateTotal());
        return sb.toString();
    }
}

