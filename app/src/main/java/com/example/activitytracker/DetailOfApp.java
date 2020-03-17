package com.example.activitytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.usage.UsageStats;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DetailOfApp extends AppCompatActivity {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_app2);

        UsageStats pkgId = getIntent().getParcelableExtra("pkgId");

        TextView textView = (TextView)findViewById(R.id.disname);
        CardView cardView = (CardView)findViewById(R.id.cardview);
        TextView dateandtime = (TextView)findViewById(R.id.lastTime);
        TextView usagetoday = (TextView)findViewById(R.id.usage);
        dateandtime.setText(getDate(pkgId.getLastTimeStamp()));

        cardView.setCardElevation(20);
        cardView.setRadius(20);
        textView.setText(getAppName(pkgId.getPackageName()));
        setUsage(usagetoday,pkgId);

    }

    public void setUsage(TextView usage, UsageStats pkgId)
    {
        long tt = pkgId.getTotalTimeInForeground();
        tt = tt/60000;
        if(tt < 60)
        {
            usage.setText("Low");
        }
        else if (tt > 60 && tt < 120)
        {
            usage.setText("Moderate");
        }
        else {
            usage.setText("High");
            Toast.makeText(getApplicationContext(),"Try to use this app less",Toast.LENGTH_SHORT);
        }
    }

    public String getAppName(String pkgName)
    {
        try{
            PackageManager packageManager = getApplicationContext().getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(pkgName,packageManager.GET_META_DATA);
            String appName = (String)packageManager.getApplicationLabel(info);
            return appName;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";

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
