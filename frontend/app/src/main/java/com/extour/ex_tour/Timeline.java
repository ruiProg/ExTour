package com.extour.ex_tour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.ArrayList;

public class Timeline extends AppCompatActivity {

    private ArrayList<TimelineItem> items;
    private ListView poiItem;
    private NumberPicker minutesPicker;
    private NumberPicker hourPicker;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


        items = new ArrayList<TimelineItem>();
        /*
        TimelineItem a = new TimelineItem(10,20);
        a.addPOI(new POI("a","b","c"));
        items.add(a);
        TimelineItem b = new TimelineItem(20,30);
        b.addPOI(new POI("d","a","s"));
        items.add(b);
        Bag.getInstance().addPOI(new POI("a","b","c"));
        Bag.getInstance().addPOI(new POI("h","r","f"));
        */

        poiItem = (ListView)findViewById(R.id.poiItem);
        spinner = (Spinner)findViewById(R.id.spinner);
        hourPicker = (NumberPicker)findViewById(R.id.hourPicker);
        hourPicker.setMaxValue(23);
        hourPicker.setMinValue(0);
        minutesPicker = (NumberPicker)findViewById(R.id.minutesPicker);
        minutesPicker.setMaxValue(59);
        minutesPicker.setMinValue(0);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
                Bag.getInstance().getArray());
        spinner.setAdapter(adapterSpinner);
        ListAdapter adapter = new TimeLineItemAdapter(this,items);
        poiItem.setAdapter(adapter);
    }

    public void addItem(View view){

        int hours = hourPicker.getValue();
        int minutes = minutesPicker.getValue();
        TimelineItem a = new TimelineItem(hours,minutes);
        a.addPOI(Bag.getInstance().getItem(spinner.getSelectedItemPosition()));
        items.add(a);

        ListAdapter adapter = new TimeLineItemAdapter(this,items);
        poiItem.setAdapter(adapter);
    }

    public void navigateBack(View view) {

        Intent intent = new Intent(this, SearchMaps.class);
        startActivity(intent);
    }
}
