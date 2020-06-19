package com.aniapps.models;

import java.util.ArrayList;

public class NewArrivals {
    String id="",name="";
    ArrayList<MyProduct> products=new ArrayList<>();

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

    public ArrayList<MyProduct> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<MyProduct> products) {
        this.products = products;
    }

    public NewArrivals(String id, String name, ArrayList<MyProduct> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }
}
