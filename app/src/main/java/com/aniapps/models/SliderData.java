package com.aniapps.models;

import java.util.List;



/**
 * Created by hocrox_java on 02/05/18.
 */


public class SliderData {
    //private List<Images> images;

    public void setImages(int[] images) {
        this.images = images;
    }

    public int[] getImages() {
        return images;
    }

    private int[] images;
    private String auctionId;

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

   /* public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }*/
}
