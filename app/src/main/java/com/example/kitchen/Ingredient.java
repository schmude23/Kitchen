package com.example.kitchen;

public class Ingredient {
    private int keyID;
    private String name;

    public Ingredient() {
        keyID = -1;
    }

    public Ingredient(int keyID, String name) {
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
        return "Ingredient{" +
                "keyID=" + keyID +
                ", name='" + name + '\'' +
                '}' + "\n" ;
    }
}
