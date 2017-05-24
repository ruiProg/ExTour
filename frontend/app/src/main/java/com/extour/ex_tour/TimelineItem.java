package com.extour.ex_tour;

public class TimelineItem {

    private POI poi;
    private int hours;
    private int minutes;

    public TimelineItem(int hours, int minutes){

        this.hours = hours;
        this.minutes = minutes;
        this.poi = null;
    }

    public void addPOI(POI poi) {

        this.poi = poi;
    }

    public String getTitle(){

        return poi.getTitle();
    }

    public String getRegion(){

        return poi.getRegion();
    }

    public int getHours(){

        return hours;
    }

    public int getMinutes(){

        return minutes;
    }

    public String getTime(){

        return Integer.toString(hours) + ":" + Integer.toString(minutes);
    }
}
