package com.example.ezyfoody.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Item implements Parcelable {
    private Integer price;
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }

    public Item(String name, Integer price) {
        this.price = price;
        this.name = name;
    }

    public Item(Parcel in) {
        name = in.readString();
        price = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(price);
    }
}
