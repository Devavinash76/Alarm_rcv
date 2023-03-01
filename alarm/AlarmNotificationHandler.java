package com.notebook.app.alarm;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.appmeno.AlarmCreateActivity.EXTRA_ALARM_ID;
import static com.example.appmeno.AlarmCreateActivity.EXTRA_ALARM_TEXT;
import static com.example.appmeno.AlarmReceiver.NOTIFICATION_ALARM_ID;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.appmeno.AlarmFireActivity;
import com.example.appmeno.AlarmReceiver;
import com.example.appmeno.ApplicationClass;

import java.util.Calendar;

public class AlarmNotificationHandler extends BroadcastReceiver {
    public static final String INTENT_ACTION_OPEN_ALARM = "com.notebook.notes.OPEN_ALARM";
    public static final String INTENT_ACTION_SNOOZE_ALARM = "com.notebook.notes.SNOOZE_ALARM";
    public static final String INTENT_ACTION_DISMISS_ALARM = "com.notebook.notes.DISMISS_ALARM";

    public static final String INTENT_EXTRA_ALARM_ID = "ALARM_ID";
    public static final String INTENT_EXTRA_ALARM_TEXT = "ALARM_TEXT";
    private final static String TAG = AlarmNotificationHandler.class.getSimpleName();
    private Context mContext;
    private int alarmId;
    private String alarmText;

    public static void dismissAlarmNotification() {
        Log.d(TAG, "dismissAlarmNotification");
        NotificationManager mNotificationManager = (NotificationManager) ApplicationClass.instance.getSystemService(NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.cancel(NOTIFICATION_ALARM_ID);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String action = intent.getAction();
        if (action != null) {
            Log.d(TAG, "onReceive: Action: " + action);
            alarmId = intent.getIntExtra(INTENT_EXTRA_ALARM_ID, -1);
            alarmText = intent.getStringExtra(INTENT_EXTRA_ALARM_TEXT);
            switch (action) {
                case INTENT_ACTION_OPEN_ALARM:
                    openAlarm();
                    break;
                case INTENT_ACTION_SNOOZE_ALARM:
                    snoozeAlarm();
                    break;
                case INTENT_ACTION_DISMISS_ALARM:
                    dismissAlarmNotification();
                    break;
            }
        }
    }

    private void openAlarm() {
        Intent intent = new Intent(mContext, AlarmFireActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_ALARM_ID, alarmId);
        intent.putExtra(EXTRA_ALARM_TEXT, alarmText);
        mContext.startActivity(intent);
        dismissAlarmNotification();
    }

    private void snoozeAlarm() {
        final Calendar calendar = Calendar.getInstance();
        try {
            calendar.add(Calendar.MINUTE, getTimeForSnooze());
        } catch (Exception e) {
            Log.e(TAG, "snoozeAlarm: Exception in Parsing time: ", e);
        }
        Log.d(TAG, "snooze time: " + getTimeForSnooze());
        Log.d(TAG, "snooze alarm until: " + calendar.toString());
        if (calendar.after(Calendar.getInstance())) {
            Intent intentAlarm = new Intent(mContext, AlarmReceiver.class);
            intentAlarm.putExtra(EXTRA_ALARM_ID, alarmId);
            PendingIntent pending = PendingIntent.getBroadcast(mContext, alarmId, intentAlarm, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            // alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
        }
        dismissAlarmNotification();
    }

    private int getTimeForSnooze() {
        SharedPreferences myPenPreference = mContext.getSharedPreferences("PenColor_Preferences",
                MODE_PRIVATE);
        final String KEY = "snoozeinterval";
        final String value = myPenPreference.getString(KEY, "5mins");
        switch (value) {
            case "10mins":
                return 10;
            case "15mins":
                return 15;
            case "30mins":
                return 30;
            case "1hour":
                return 60;
            default:
                return 5;
        }
    }
}
