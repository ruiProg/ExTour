package com.extour.ex_tour;

import java.util.Objects;

public class POI {

    private String id;
    private String title;
    private String region;

    public POI(String id, String title, String region){

        this.id = id;
        this.title = title;
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {

        if(o instanceof POI){
            POI p = (POI)o;
            return (this.id.equals(p.id) && this.title.equals(p.title) && this.region.equals(p.region));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + id.hashCode();
        hash = hash * 31 + title.hashCode();
        hash = hash * 13 + region.hashCode();
        return hash;
    }

    @Override
    public String toString(){

        return id + " | " + title + " | " + region;
    }

    public String getID(){

        return id;
    }

    public String getTitle(){

        return title;
    }

    public String getRegion(){

        return region;
    }
}
