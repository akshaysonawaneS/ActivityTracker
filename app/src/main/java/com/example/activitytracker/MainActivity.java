package com.example.activitytracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebHistoryItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<UsageStats> lUsageStatsMap;
    private UsageStatsManager mUageStatsManager;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy HH:mm:ss");
    private ListView listView;
    private ArrayList<String> pkgArrayList;
    private ArrayList<String> timeArrayList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.appList) ;

        AppOpsManager appOps = (AppOpsManager)getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(),getPackageName());



        if(mode == AppOpsManager.MODE_ALLOWED){
            Log.e("Permission","Granted");
        }
        else {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        checkusagestart();

    }

    public void checkusagestart()
    {
        mUageStatsManager = (UsageStatsManager) getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.HOUR,0);
        long startTime = calendar.getTimeInMillis();
        long endTime = System.currentTimeMillis();

        Log.e("DATE", "Range start:" + dateFormat.format(startTime) );
        Log.e("DATE", "Range end:" + dateFormat.format(endTime));


        lUsageStatsMap = mUageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);

        pkgArrayList = new ArrayList<String>();
        timeArrayList = new ArrayList<String>();
        for(UsageStats usageStats: lUsageStatsMap)
        {
            long tt = usageStats.getTotalTimeInForeground();
            tt = tt / 60000;
            pkgArrayList.add(usageStats.getPackageName().toString());
            Log.e("PackageName  "+ usageStats.getPackageName().toString(),String.valueOf(tt));
            Log.e("TimeStamp: "+usageStats.getPackageName(),getDate(usageStats.getLastTimeUsed()));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.app_list_view,R.id.element,pkgArrayList);
        listView.setAdapter(arrayAdapter);
    }

    public String getDate(long timeStamp)
    {
        String dateString = dateFormat.format(new Date(timeStamp));
        return dateString;
    }


}



