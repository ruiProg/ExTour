package com.extour.ex_tour;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class SearchMaps extends AppCompatActivity {

    private ArrayList<POItem> items;
    private EditText searchInput;
    private POIClient client;
    private ListView searchItem;
    public static final String poiMessage = "com.extour.ex_tour.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_maps);

        items = new ArrayList<>();
        searchInput = (EditText)findViewById(R.id.searchTag);
        searchItem = (ListView)findViewById(R.id.searchItem);
        searchItem.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchMaps.this, POIDetails.class);
                POItem item = (POItem) parent.getItemAtPosition(position);
                intent.putExtra(poiMessage, item.getID());
                startActivity(intent);
            }
        });
    }

    public void navigate(View view) {

        Intent intent = new Intent(this, Timeline.class);
        startActivity(intent);
    }
    
    public void search(View view){
        
        String tag = searchInput.getText().toString();
        loadPOIS(tag);
    }

    private void loadPOIS(String tag) {
        client = new POIClient();
        client.getTagsPOI(tag, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject row = response.getJSONObject(i);
                        POItem item = new POItem(row.getString("id"),row.getString("title"),
                                row.getString("category"),row.getString("parish") + ", " + row.getString("council")
                                + ", " + row.getString("district"), row.getString("imageURL"));
                        items.add(item);
                    }
                    ListAdapter adapter = new POItemAdapter(SearchMaps.this,items);
                    searchItem.setAdapter(adapter);
                }
                catch(Exception e){
                    System.out.println(e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }
}
