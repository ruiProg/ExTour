package com.extour.ex_tour;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class TimeLineItemAdapter extends ArrayAdapter<TimelineItem> {

    public TimeLineItemAdapter(Context context, ArrayList<TimelineItem> items) {

        super(context, R.layout.actvity_timeline_row, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.actvity_timeline_row, parent, false);
        TimelineItem item = getItem(position);

        TextView time = (TextView)customView.findViewById(R.id.rowTimeLabel);
        TextView title = (TextView)customView.findViewById(R.id.rowTitleLabel);
        TextView region = (TextView)customView.findViewById(R.id.rowRegionLabel);

        time.setText(item.getTime());
        title.setText(item.getTitle());
        region.setText(item.getRegion());

        return customView;
    }
}
