package com.extour.ex_tour;

import java.util.HashSet;

public class Bag {

    private HashSet<POI> listPOIS;
    private static Bag instance = null;

    public static Bag getInstance() {

        if(instance == null) {
            instance = new Bag();
            instance.listPOIS = new HashSet<>();
        }
        return instance;
    }

    public void addPOI(POI poi) {

        listPOIS.add(poi);
        for(POI p:listPOIS)
            System.out.println(p.toString());
    }

    public void clearBag() {

        listPOIS.clear();
    }

    public boolean findPOI(String id){

        for(POI p:listPOIS)
            if(id.equals(p.getID()))
                return true;
        return false;
    }
}
