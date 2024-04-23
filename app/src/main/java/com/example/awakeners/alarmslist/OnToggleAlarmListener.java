package com.example.awakeners.alarmslist;

import android.view.View;

import com.example.awakeners.data.Alarm;

public interface OnToggleAlarmListener {
    void onToggle(Alarm alarm);
    void onDelete(Alarm alarm);
    void onItemClick(Alarm alarm, View view);
}
