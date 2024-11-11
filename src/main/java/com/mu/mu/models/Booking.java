package com.mu.mu.models;

import java.time.LocalDateTime;

public class Booking {

    private int tableNumber;
    private String customerName;
    private int tableSize;
    private String dateTime;

    public Booking(int tableNumber, String customerName, int tableSize, String dateTime) {
        this.tableNumber = tableNumber;
        this.customerName = customerName;
        this.tableSize = tableSize;
        this.dateTime = dateTime;
    }

    public Booking(){}

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getTableSize() {
        return tableSize;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
