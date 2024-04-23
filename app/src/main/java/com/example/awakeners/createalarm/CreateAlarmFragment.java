package com.example.awakeners.createalarm;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

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
    ToggleButton mon;
    //@BindView(R.id.fragment_createalarm_checkTue)
    ToggleButton tue;
    //@BindView(R.id.fragment_createalarm_checkWed)
    ToggleButton wed;
    //@BindView(R.id.fragment_createalarm_checkThu)
    ToggleButton thu;
    //@BindView(R.id.fragment_createalarm_checkFri)
    ToggleButton fri;
    //@BindView(R.id.fragment_createalarm_checkSat)
    ToggleButton sat;
    //@BindView(R.id.fragment_createalarm_checkSun)
    ToggleButton sun;
    //@BindView(R.id.fragment_createalarm_recurring_options)
    HorizontalScrollView recurringOptions;

    TextView toneName;
    Switch vibrateSwitch;
    CardView cardSound;
    TextView alarmHeading;

    boolean isVibrate=false;
    String tone;
    Ringtone ringtone;

    Alarm alarm;

    private CreateAlarmViewModel createAlarmViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            alarm= (Alarm) getArguments().getSerializable(getString(R.string.arg_alarm_obj));
        }
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
        toneName = view.findViewById(R.id.fragment_createalarm_setToneName);
        recurringOptions = view.findViewById(R.id.fragment_createalarm_recurring_options);
        vibrateSwitch = view.findViewById(R.id.fragment_createalarm_vibrate_switch);
        cardSound = view.findViewById(R.id.fragment_createalarm_card_sound);
        alarmHeading = view.findViewById(R.id.fragment_createalarm_scheduleAlarmHeading);

        tone= RingtoneManager.getActualDefaultRingtoneUri(this.getContext(), RingtoneManager.TYPE_ALARM).toString();
        ringtone = RingtoneManager.getRingtone(getContext(), Uri.parse(tone));
        toneName.setText(ringtone.getTitle(getContext()));

        if(alarm!=null){
            updateAlarmInfo(alarm);
        }

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
                if(alarm!=null) {
                    updateAlarm();
                }
                else{
                    scheduleAlarm();
                }

                Navigation.findNavController(v).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
            }
        });

        cardSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Sound");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) Uri.parse(tone));
                startActivityForResult(intent, 5);
            }
        });

        vibrateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    isVibrate=true;
                }
                else{
                    isVibrate=false;
                }
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                alarmHeading.setText(DayUtil.getDay(TimePickerUtil.getTimePickerHour(timePicker), TimePickerUtil.getTimePickerMinute(timePicker)));
            }
        });

        return view;
    }

    private void updateAlarmInfo(Alarm alarm){
        title.setText(alarm.getTitle());
        timePicker.setHour(alarm.getHour());
        timePicker.setMinute(alarm.getMinute());
        if(alarm.isRecurring()){
            recurring.setChecked(true);
            recurringOptions.setVisibility(View.VISIBLE);
            if(alarm.isMonday())
                mon.setChecked(true);
            if(alarm.isTuesday())
                tue.setChecked(true);
            if(alarm.isWednesday())
                wed.setChecked(true);
            if(alarm.isThursday())
                thu.setChecked(true);
            if(alarm.isFriday())
                fri.setChecked(true);
            if(alarm.isSaturday())
                sat.setChecked(true);
            if(alarm.isSunday())
                sun.setChecked(true);
            tone=alarm.getTone();
            ringtone = RingtoneManager.getRingtone(getContext(), Uri.parse(tone));
            toneName.setText(ringtone.getTitle(getContext()));
            if(alarm.isVibrate())
                vibrateSwitch.setChecked(true);
        }
    }

    private void scheduleAlarm() {
        String alarmTitle=getString(R.string.alarm_title);
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        if(!title.getText().toString().isEmpty()){
            alarmTitle=title.getText().toString();
        }

        Alarm alarm = new Alarm(
                alarmId,
                TimePickerUtil.getTimePickerHour(timePicker),
                TimePickerUtil.getTimePickerMinute(timePicker),
                alarmTitle,
                System.currentTimeMillis(),
                true,
                recurring.isChecked(),
                mon.isChecked(),
                tue.isChecked(),
                wed.isChecked(),
                thu.isChecked(),
                fri.isChecked(),
                sat.isChecked(),
                sun.isChecked(),
                tone,
                isVibrate
        );

        createAlarmViewModel.insert(alarm);

        alarm.schedule(getContext());
    }

    private void updateAlarm(){
        String alarmTitle=getString(R.string.alarm_title);

        if(!title.getText().toString().isEmpty()){
            alarmTitle=title.getText().toString();
        }
        Alarm updatedAlarm = new Alarm(
                alarm.getAlarmId(),
                TimePickerUtil.getTimePickerHour(timePicker),
                TimePickerUtil.getTimePickerMinute(timePicker),
                alarmTitle,
                System.currentTimeMillis(),
                true,
                recurring.isChecked(),
                mon.isChecked(),
                tue.isChecked(),
                wed.isChecked(),
                thu.isChecked(),
                fri.isChecked(),
                sat.isChecked(),
                sun.isChecked(),
                tone,
                isVibrate
        );
        createAlarmViewModel.update(updatedAlarm);
        updatedAlarm.schedule(getContext());
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == 5) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            ringtone = RingtoneManager.getRingtone(getContext(), uri);
            String title = ringtone.getTitle(getContext());
            if (uri != null) {
                tone=uri.toString();
                if(title!=null && !title.isEmpty())
                    toneName.setText(title);
            } else {
                toneName.setText("");
            }
        }
    }
}