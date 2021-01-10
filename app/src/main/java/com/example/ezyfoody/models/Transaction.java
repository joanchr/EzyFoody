package com.example.ezyfoody.models;

import android.os.Parcel;

import java.util.ArrayList;

public class Transaction {

    private String address;
    private String date;
    private Integer totalItems;
    private Integer totalPrice;
    private ArrayList<OrderItem> items;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderItem> items) {
        this.items = items;
    }

    public Transaction(String address, String date, Integer totalItems, Integer totalPrice, ArrayList<OrderItem> items) {
        this.address = address;
        this.date = date;
        this.totalItems = totalItems;
        this.totalPrice = totalPrice;
        this.items = items;
    }
}
