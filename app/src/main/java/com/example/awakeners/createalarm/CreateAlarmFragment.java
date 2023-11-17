package com.example.awakeners.createalarm;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.example.awakeners.R;
import com.example.awakeners.data.Alarm;

import java.util.Random;

public class CreateAlarmFragment extends Fragment {

    //@BindView(R.id.fragment_createalarm_timePicker)
    TimePicker timePicker;
    //@BindView(R.id.fragment_createalarm_title)
    EditText title;
    //@BindView(R.id.fragment_createalarm_scheduleAlarm)
    Button scheduleAlarm;
    //@BindView(R.id.fragment_createalarm_recurring)
    CheckBox recurring;
    //@BindView(R.id.fragment_createalarm_checkMon)
    CheckBox mon;
    //@BindView(R.id.fragment_createalarm_checkTue)
    CheckBox tue;
    //@BindView(R.id.fragment_createalarm_checkWed)
    CheckBox wed;
    //@BindView(R.id.fragment_createalarm_checkThu)
    CheckBox thu;
    //@BindView(R.id.fragment_createalarm_checkFri)
    CheckBox fri;
    //@BindView(R.id.fragment_createalarm_checkSat)
    CheckBox sat;
    //@BindView(R.id.fragment_createalarm_checkSun)
    CheckBox sun;
    //@BindView(R.id.fragment_createalarm_recurring_options)
    LinearLayout recurringOptions;

    private CreateAlarmViewModel createAlarmViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createAlarmViewModel = ViewModelProviders.of(this).get(CreateAlarmViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_alarm, container, false);

        //ButterKnife.bind(this, view);
        timePicker = view.findViewById(R.id.fragment_createalarm_timePicker);
        title = view.findViewById(R.id.fragment_createalarm_title);
        scheduleAlarm = view.findViewById(R.id.fragment_createalarm_scheduleAlarm);
        recurring = view.findViewById(R.id.fragment_createalarm_recurring);
        mon = view.findViewById(R.id.fragment_createalarm_checkMon);
        tue = view.findViewById(R.id.fragment_createalarm_checkTue);
        wed = view.findViewById(R.id.fragment_createalarm_checkWed);
        thu = view.findViewById(R.id.fragment_createalarm_checkThu);
        fri = view.findViewById(R.id.fragment_createalarm_checkFri);
        sat = view.findViewById(R.id.fragment_createalarm_checkSat);
        sun = view.findViewById(R.id.fragment_createalarm_checkSun);
        recurringOptions = view.findViewById(R.id.fragment_createalarm_recurring_options);

        recurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recurringOptions.setVisibility(View.VISIBLE);
                } else {
                    recurringOptions.setVisibility(View.GONE);
                }
            }
        });

        scheduleAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleAlarm();
                Navigation.findNavController(v).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
            }
        });

        return view;
    }

    private void scheduleAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = new Alarm(
                alarmId,
                TimePickerUtil.getTimePickerHour(timePicker),
                TimePickerUtil.getTimePickerMinute(timePicker),
                title.getText().toString(),
                System.currentTimeMillis(),
                true,
                recurring.isChecked(),
                mon.isChecked(),
                tue.isChecked(),
                wed.isChecked(),
                thu.isChecked(),
                fri.isChecked(),
                sat.isChecked(),
                sun.isChecked()
        );

        createAlarmViewModel.insert(alarm);

        alarm.schedule(getContext());
    }
}