package com.example.activitytracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<UsageStats> lUsageStatsMap;
    private UsageStatsManager mUageStatsManager;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy HH:mm:ss");
    private ListView listView;
    private ArrayList<String> pkgArrayList  = new ArrayList<String>();
    private ArrayList<Long> timeArrayList = new ArrayList<Long>();
    private ArrayList<String> dateArrayList = new ArrayList<String>();
    private ArrayList<Drawable> iconArrayList = new ArrayList<Drawable>();
    private ArrayList<UsageStats> newUsageStats = new ArrayList<UsageStats>();
    private String appName;

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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        checkusagestart(calendar);

    }

    public void checkusagestart(Calendar calendar)
    {

        mUageStatsManager = (UsageStatsManager) getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);

        long startTime = calendar.getTimeInMillis();
        long endTime = System.currentTimeMillis();
        Log.e("DATE", "Range start:" + dateFormat.format(startTime) );
        Log.e("DATE", "Range end:" + dateFormat.format(endTime));


        lUsageStatsMap = mUageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime,endTime);
        Collections.sort(lUsageStatsMap,new SoartedList());
        Collections.reverse(lUsageStatsMap);


        for(UsageStats usageStats: lUsageStatsMap)
        {
            long tt = usageStats.getTotalTimeInForeground();
            if( tt > 0 && getAppName(usageStats.getPackageName()) != ""){
                arrangeDuplicate(usageStats);
            }
        }

        for (UsageStats usagestats: newUsageStats)
        {
            Log.e("newlist",getAppName(usagestats.getPackageName()));
            dateArrayList.add(getDate(usagestats.getLastTimeStamp()));
            try {
                Drawable icon = getApplicationContext().getPackageManager().getApplicationIcon(usagestats.getPackageName());
                iconArrayList.add(icon);
            }
            catch (PackageManager.NameNotFoundException e){
                e.printStackTrace();
            }
        }

        Log.e("iconlist",iconArrayList.toString());
        CustomListAdapter customListAdapter = new CustomListAdapter(this,pkgArrayList,dateArrayList,timeArrayList,iconArrayList);
        listView.setAdapter(customListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailOfApp.class);
                intent.putExtra("pkgId", newUsageStats.get(position));
                intent.putExtra("time",timeArrayList.get(position));
                startActivity(intent);
            }
        });



    }

    public String getAppName(String pkgName)
    {
        try{
            PackageManager packageManager = getApplicationContext().getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(pkgName,packageManager.GET_META_DATA);
            appName = (String)packageManager.getApplicationLabel(info);
            return appName;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";

        }
    }

    public void arrangeDuplicate(UsageStats usageStats)
    {
        if(pkgArrayList.isEmpty()){
            long tt = usageStats.getTotalTimeInForeground();
            pkgArrayList.add(getAppName(usageStats.getPackageName()));
            newUsageStats.add(usageStats);
            timeArrayList.add(tt/60000);
            Log.e("empty","Empty");
        }
        if (pkgArrayList.contains(getAppName(usageStats.getPackageName())))
        {
            Log.e("inside","pehle se hai");
            long val=0,newVal=0;
            int index = pkgArrayList.indexOf(getAppName(usageStats.getPackageName()));
            newVal = usageStats.getTotalTimeInForeground();
            newVal = newVal/60000;

            val = timeArrayList.get(index);
            val = val + newVal;
            Log.e("newvalue",String.valueOf(newVal)+" "+String.valueOf(val));
            timeArrayList.set(index,val);
            newUsageStats.set(index,usageStats);
        }
        else {
            long tt = usageStats.getTotalTimeInForeground();
            pkgArrayList.add(getAppName(usageStats.getPackageName()));
            newUsageStats.add(usageStats);
            timeArrayList.add(tt/60000);
            Log.e("not","third");
        }

    }
    public String getDate(long timeStamp)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        if(calendar.getTimeInMillis() > timeStamp)
        {
            return "Not Used Today";
        }
        String dateString = dateFormat.format(new Date(timeStamp));
        return dateString;
    }

}





