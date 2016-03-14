package com.weebly.docrosby.listtaker;

import java.util.ArrayList;
import java.util.UUID;

public class Project {
    private UUID mId;
    private String mTitle;
    private String mDescription;
    private ArrayList<Item> items;

    public Project() {
        mId = UUID.randomUUID();
        this.items = new ArrayList<>();
    }

    public Project(UUID mid, String title, String description) {
        mId = mid;
        this.mTitle = title;
        this.mDescription = description;
        this.items = new ArrayList<>();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public ArrayList<Item> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void addItem(Item itemToAdd) {
        itemToAdd.setTotalCost(itemToAdd.getNumOfItems(), itemToAdd.getCost());
        this.items.add(itemToAdd);
    }

    @Override
    public String toString() {
        return mTitle;
    }

}
