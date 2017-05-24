package com.extour.ex_tour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.widget.EditText;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class SearchMaps extends AppCompatActivity {

    private ArrayList<POItem> items;
    public static final String detailsActivity = "com.extour.ex_tour.details";
    private EditText searchInput;
    private POIClient client;
    private ListView searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_maps);

        items = new ArrayList<>();
        searchInput = (EditText)findViewById(R.id.searchTag);
        searchItem = (ListView)findViewById(R.id.searchItem);
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
