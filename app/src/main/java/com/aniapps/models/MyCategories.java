package com.aniapps.models;

import java.util.ArrayList;
import java.util.List;


public class MyCategories {
    private String name = "";
    int id = 0;
    private List<SubCategory> subCategory = new ArrayList<SubCategory>();

    public MyCategories(String name, int id, List<SubCategory> subCategory) {
        this.name = name;
        this.id = id;
        this.subCategory = subCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<SubCategory> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<SubCategory> subCategory) {
        this.subCategory = subCategory;
    }

    public MyCategories(String name) {
        this.name = name;
    }
}
