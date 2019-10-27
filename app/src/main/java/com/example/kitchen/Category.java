package com.example.kitchen;

public class Category {

    private int keyID;
    private String name;

    public Category() {
        keyID = -1;
    }

    public Category(int keyID, String name) {
        this.keyID = keyID;
        this.name = name;
    }


    public int getKeyID() { return keyID; }

    public void setKeyID(int keyID) { this.keyID = keyID; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "keyID=" + keyID +
                ", name='" + name + '\'' +
                '}' + "\n";
    }
}
