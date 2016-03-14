package com.weebly.docrosby.listtaker;


import java.util.UUID;

public class Item {
    private UUID mId;
    private String mName;
    private int mNumOfItems;
    private double mCost;
    private double mTotalCost;

    public Item(String name) {
        this(name, 0, 0);
    }

    public Item(String name, int number, double cost) {
        mId = UUID.randomUUID();
        this.mName = name;
        this.mNumOfItems = number;
        this.mCost = cost;
        this.mTotalCost = (number * cost);
    }

    public Item(UUID mid, String name, int number, double cost) {
        mId = mid;
        this.mName = name;
        this.mNumOfItems = number;
        this.mCost = cost;
        this.mTotalCost = (number * cost);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getNumOfItems() {
        return mNumOfItems;
    }

    public void setNumOfItems(int numOfItems) {
        mNumOfItems = numOfItems;
    }

    public double getCost() {
        return mCost;
    }

    public void setCost(double cost) {
        mCost = cost;
    }

    public double getTotalCost() {
        return mTotalCost;
    }

    public void setTotalCost(int number, double cost) {
        mTotalCost = (number * cost);
    }

    public UUID getId() {
        return mId;
    }
}
