package com.example.awakeners.alarmslist;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awakeners.R;
import com.example.awakeners.createalarm.DayUtil;
import com.example.awakeners.data.Alarm;

public class AlarmViewHolder extends RecyclerView.ViewHolder {
    private TextView alarmTime;
    private ImageView alarmRecurring;
    private TextView alarmRecurringDays;
    private TextView alarmTitle;

    Switch alarmStarted;

    private OnToggleAlarmListener listener;
    private ImageButton deleteAlarm;
    private View itemView;
    private TextView alarmDay;

    public AlarmViewHolder(@NonNull View itemView, OnToggleAlarmListener listener) {
        super(itemView);

        alarmTime = itemView.findViewById(R.id.item_alarm_time);
        alarmStarted = itemView.findViewById(R.id.item_alarm_started);
        alarmRecurring = itemView.findViewById(R.id.item_alarm_recurring);
        alarmRecurringDays = itemView.findViewById(R.id.item_alarm_recurringDays);
        alarmTitle = itemView.findViewById(R.id.item_alarm_title);
        deleteAlarm = itemView.findViewById(R.id.item_alarm_recurring_delete);
        alarmDay = itemView.findViewById(R.id.item_alarm_day);

        this.listener = listener;
        this.itemView = itemView;
    }

    public void bind(Alarm alarm) {
        String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());

        alarmTime.setText(alarmText);
        alarmStarted.setChecked(alarm.isStarted());

        if (alarm.isRecurring()) {
            alarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp);
            alarmRecurringDays.setText(alarm.getRecurringDaysText());
        } else {
            alarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp);
            alarmRecurringDays.setText("Once Off");
        }

        if (alarm.getTitle().length() != 0) {
            alarmTitle.setText(String.format("%s | %d | %d", alarm.getTitle(), alarm.getAlarmId(), alarm.getCreated()));
        } else {
            alarmTitle.setText(String.format("%s | %d | %d", "Alarm", alarm.getAlarmId(), alarm.getCreated()));
        }
        if (alarm.isRecurring()) {
            alarmDay.setText(alarm.getRecurringDaysText());
        } else {
            alarmDay.setText(DayUtil.getDay(alarm.getHour(), alarm.getMinute()));
        }
        alarmStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown() || buttonView.isPressed())
                    listener.onToggle(alarm);
            }
        });

        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(alarm);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(alarm, view);
            }
        });
    }
}
