package com.example.awakeners.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.awakeners.R;
import com.example.awakeners.alarmslist.AlarmsListViewModel;
import com.example.awakeners.data.Alarm;
import com.example.awakeners.service.AlarmService;

import java.util.Calendar;
import java.util.Random;

public class RingActivity extends AppCompatActivity {
    //@BindView(R.id.activity_ring_dismiss)
    Button dismiss;
    //@BindView(R.id.activity_ring_snooze)
    Button snooze;
    //@BindView(R.id.activity_ring_clock)
    ImageView clock;

    Alarm alarm;

    private AlarmsListViewModel alarmsListViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (keyguardManager != null)
                keyguardManager.requestDismissKeyguard(this, null);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            );
        }

        //ButterKnife.bind(this);
        dismiss = findViewById(R.id.activity_ring_dismiss);
        snooze = findViewById(R.id.activity_ring_snooze);
        clock = findViewById(R.id.activity_ring_clock);

        alarmsListViewModel = ViewModelProviders.of(this).get(AlarmsListViewModel.class);
        Bundle bundle=getIntent().getBundleExtra(getString(R.string.bundle_alarm_obj));
        if (bundle!=null)
            alarm =(Alarm)bundle.getSerializable(getString(R.string.arg_alarm_obj));

        dismiss.setOnClickListener(v -> {
            dismissAlarm();
        });

        snooze.setOnClickListener(v -> {
            snoozeAlarm();
        });

        animateClock();
    }

    private void animateClock() {
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(clock, "rotation", 0f, 20f, 0f, -20f, 0f);
        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimation.setDuration(800);
        rotateAnimation.start();
    }

    private void dismissAlarm(){
        if(alarm!=null) {
            alarm.setStarted(false);
            alarm.cancelAlarm(getBaseContext());
            alarmsListViewModel.update(alarm);
        }
        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
        getApplicationContext().stopService(intentService);
        finish();
    }

    private void snoozeAlarm(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 5);

        if(alarm!=null){
            alarm.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            alarm.setMinute(calendar.get(Calendar.MINUTE));
            alarm.setTitle("Snooze "+alarm.getTitle());
        }
        else {
            alarm = new Alarm(
                    new Random().nextInt(Integer.MAX_VALUE),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    "Snooze",
                    0,
                    true,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    RingtoneManager.getActualDefaultRingtoneUri(getBaseContext(), RingtoneManager.TYPE_ALARM).toString(),
                    false
            );
        }
        alarm.schedule(getApplicationContext());

        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
        getApplicationContext().stopService(intentService);
        finish();
    }
}