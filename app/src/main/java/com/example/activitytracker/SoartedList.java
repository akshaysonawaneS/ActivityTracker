package com.example.activitytracker;

import android.app.usage.UsageStats;

class SoartedList implements java.util.Comparator<UsageStats> {

    @Override
    public int compare(UsageStats o1, UsageStats o2) {
        return (int)(o1.getTotalTimeInForeground()-o2.getTotalTimeInForeground());
    }
}
