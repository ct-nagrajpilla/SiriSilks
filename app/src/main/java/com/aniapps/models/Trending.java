package com.aniapps.models;

import java.util.ArrayList;

public class Trending {
    String id="",name="";
    ArrayList<Products> products=new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Products> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Products> products) {
        this.products = products;
    }

    public Trending(String id, String name, ArrayList<Products> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }
}
