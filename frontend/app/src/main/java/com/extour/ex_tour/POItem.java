package com.extour.ex_tour;

public class POItem {

    private String id;
    private String title;
    private String category;
    private String region;
    private String image;

    public POItem(String id, String title, String category, String region, String image){

        this.id = id;
        this.title = title;
        this.category = category;
        this.region = region;
        this.image = image;
    }

    public String getID(){

        return id;
    }

    public String getTitle(){

        return title;
    }

    public String getCategory(){

        return category;
    }

    public String getRegion(){

        return region;
    }

    public String getImage(){

        return image;
    }
}
