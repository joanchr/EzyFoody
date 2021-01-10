package com.example.ezyfoody.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OrderItem extends Item {

//    public static ArrayList<OrderItem> cart = new ArrayList<>();

    private Integer id;
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }
    public Integer getId() {
        return id;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setId(Integer id) {
        this.quantity = id;
    }

    public OrderItem(Parcel in, Integer quantity, Integer id) {
        super(in);
        this.quantity = quantity;
        this.id = id;
    }

    public OrderItem(String name, Integer price, Integer quantity, Integer id) {
        super(name, price);
        this.quantity = quantity;
        this.id = id;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(quantity);
        parcel.writeInt(id);
    }
}
