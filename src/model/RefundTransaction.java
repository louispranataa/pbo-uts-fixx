/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;
import java.util.List;

public class RefundTransaction extends Transaction implements Payable {

    private List<TransactionItem> items;
    private String reason;
    private double total;

    public RefundTransaction(String transactionId, Date date, double total, List<TransactionItem> items, String reason) {
        super(transactionId, date);
        this.items = items;
        this.total = total;
        this.reason = reason;
    }

    public double getTotal() {
        return total;
    }
    
    public List<TransactionItem> getItems() {
        return items;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String getJenis() {
        return "Retur";
    }
    
    @Override
    public double calculateTotal() {
        // total pengembalian dana
        return items.stream()
                    .mapToDouble(TransactionItem::getSubtotal)
                    .sum();
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
        sb.append("Refund dengan ID: ").append(transactionId).append(" pada ").append(date).append(":\n");
        for (TransactionItem item : items) {
            sb.append("- ").append(item.getProduct().getNama())
              .append(" x").append(item.getQuantity())
              .append(" = ").append(item.getSubtotal()).append("\n");
        }
        sb.append("Total Refund: ").append(calculateTotal()).append("\n");
        sb.append("Alasan: ").append(reason);
        return sb.toString();
    }
}

