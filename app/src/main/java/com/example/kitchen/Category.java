package com.example.kitchen;

public class Category {

    private int keyID = -1;
    private String name = null;


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
