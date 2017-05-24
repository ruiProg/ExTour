package com.extour.ex_tour;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class POItemAdapter extends ArrayAdapter<POItem> {

    public POItemAdapter(Context context, ArrayList<POItem> items) {

        super(context, R.layout.activity_search_row, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.activity_search_row, parent, false);
        POItem item = getItem(position);

        TextView title = (TextView)customView.findViewById(R.id.searchTitleLabel);
        TextView category = (TextView)customView.findViewById(R.id.searchCategoryLabel);
        TextView id = (TextView)customView.findViewById(R.id.searchIDLabel);
        TextView region = (TextView)customView.findViewById(R.id.searchRegionLabel);
        ImageView image = (ImageView)customView.findViewById(R.id.searchImage);

        title.setText(item.getTitle());
        category.setText(category.getText() + item.getCategory());
        id.setText(item.getID());
        region.setText(region.getText() + item.getRegion());
        title.setText(item.getTitle());
        String imageURL = item.getImage();
        if(imageURL.length() > 0)
            Picasso.with(getContext()).load(Uri.parse(imageURL))
                    .error(R.drawable.default_image).into(image);

        return customView;
    }
}
