package com.example.activitytracker;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter {

    private  ArrayList<String> appName;
    private  ArrayList<String> lastTime;
    private  ArrayList<String> totalTime;
    private ArrayList<Drawable> icon;
    private  Activity context;

    public CustomListAdapter(Activity context, ArrayList<String> appName, ArrayList<String> lastTime, ArrayList<String> totalTime, ArrayList<Drawable> icon) {
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
        totaltime.setText(totalTime.get(position));
        iconapp.setBackground(icon.get(position));

        return rowView;
    }


}
