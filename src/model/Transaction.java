/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

public abstract class Transaction {
    protected String transactionId;
    protected Date date;
    

    public Transaction(String transactionId, Date date) {
        this.transactionId = transactionId;
        this.date = date;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public abstract String getJenis();
    public abstract double calculateTotal();
    public abstract String processTransaction();
    public abstract String serializeTransaction();
}
