package com.aniapps.models;

import java.util.ArrayList;

public class Product {
    String product_name="", product_id="",product_details="";
    public ArrayList<String> product_all_images = null;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_details() {
        return product_details;
    }

    public void setProduct_details(String product_details) {
        this.product_details = product_details;
    }

    public ArrayList<String> getProduct_all_images() {
        return product_all_images;
    }

    public void setProduct_all_images(ArrayList<String> product_all_images) {
        this.product_all_images = product_all_images;
    }
}
