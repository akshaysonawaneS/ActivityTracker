package com.example.activitytracker;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter {

    private  ArrayList<String> appName;
    private  ArrayList<String> lastTime;
    private  ArrayList<Long> totalTime;
    private final ArrayList<Drawable> icon;
    private  Activity context;

    public CustomListAdapter(Activity context, ArrayList<String> appName, ArrayList<String> lastTime, ArrayList<Long> totalTime, ArrayList<Drawable> icon) {
        super(context,R.layout.app_list_view,appName);

        this.context = context;
        this.appName = appName;
        this.lastTime = lastTime;
        this.totalTime = totalTime;
        this.icon = icon;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.app_list_view,null,true);

        TextView name = (TextView)rowView.findViewById(R.id.element);
        TextView lasttime = (TextView)rowView.findViewById(R.id.element1);
        TextView totaltime = (TextView)rowView.findViewById(R.id.element2);
        ImageView iconapp = (ImageView)rowView.findViewById(R.id.icon1);

        name.setText(appName.get(position));
        lasttime.setText(lastTime.get(position));
        totaltime.setText(String.valueOf(totalTime.get(position)));
        iconapp.setBackground(icon.get(position));

        CardView cardView = (CardView)rowView.findViewById(R.id.mainCard);
        cardView.setCardElevation(13);
        cardView.setRadius(25);

        return rowView;
    }


}
