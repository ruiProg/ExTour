package com.extour.ex_tour;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import cz.msebera.android.httpclient.Header;

import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;


public class POIDetails extends AppCompatActivity {

    private POIClient client;
    private TextView titleLabel;
    private ImageView image;
    private TextView categoryLabel;
    private TextView regionLabel;
    private TextView coordLabel;
    private TextView infoLabel;
    private ImageButton checkedLabel;
    private String idLabel;
    private String region;
    private String title;
    private String info;
    private boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poidetails);
        titleLabel = (TextView) findViewById(R.id.titleLable);
        image = (ImageView) findViewById(R.id.imagePOI);
        categoryLabel = (TextView) findViewById(R.id.categoryLabel);
        regionLabel = (TextView) findViewById(R.id.regionLabel);
        coordLabel = (TextView) findViewById(R.id.coordsLabel);
        infoLabel = (TextView) findViewById(R.id.infoLabel);
        infoLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info.length() > 0) {
                    Uri uri = Uri.parse(info);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
        checkedLabel = (ImageButton) findViewById(R.id.checkedLabel);

        Intent intent = getIntent();
        idLabel = intent.getStringExtra(SearchMaps.detailsActivity);
        clicked = Bag.getInstance().findPOI(idLabel);
        loadPOI();
    }

    private void loadPOI() {
        client = new POIClient();
        client.getPOI(idLabel, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    idLabel = response.getString("id");
                    title = response.getString("title");
                    titleLabel.setText(title);
                    categoryLabel.setText(categoryLabel.getText() + response.getString("category"));
                    String parish = response.getString("parish");
                    String council = response.getString("council");
                    String district = response.getString("district");
                    region = district + ", " + council + ", " + parish;
                    regionLabel.setText(regionLabel.getText() + region);
                    coordLabel.setText(coordLabel.getText() + response.getString("coords"));
                    info = response.getString("linkURL");
                    infoLabel.setText(infoLabel.getText() + info);

                    String imageURL = response.getString("imageURL");
                    if(imageURL.length() > 0)
                        Picasso.with(POIDetails.this).load(Uri.parse(imageURL))
                                .error(R.drawable.default_image).into(image);

                    if(clicked)
                        checkedLabel.setImageResource(R.drawable.ic_android_checked);
                }
                catch(Exception e){
                    System.out.println(e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                titleLabel.setText(Integer.toString(statusCode));
            }
        });
    }

    public void addPOI(View view) {

        Bag bag = Bag.getInstance();
        bag.addPOI(new POI(idLabel,title,region));
        checkedLabel.setImageResource(R.drawable.ic_android_checked);
    }
}
