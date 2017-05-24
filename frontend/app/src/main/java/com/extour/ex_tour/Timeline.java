package com.extour.ex_tour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Timeline extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ArrayList<TimelineItem> items = new ArrayList<TimelineItem>();
        TimelineItem a = new TimelineItem(10,20);
        a.addPOI(new POI("a","b","c"));
        items.add(a);

        TimelineItem b = new TimelineItem(20,30);
        b.addPOI(new POI("d","a","s"));
        items.add(b);

        ListAdapter adapter = new TimeLineItemAdapter(this,items);
        ListView poiItem = (ListView)findViewById(R.id.poiItem);
        poiItem.setAdapter(adapter);
    }
}
