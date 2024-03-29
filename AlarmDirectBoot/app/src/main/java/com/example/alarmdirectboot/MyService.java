package com.example.alarmdirectboot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    static String TAG = "ALARM_DIRECT_BOOT";
    String fileName = "alarmsFile";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Creating service");
        setAlarm();
    }

    public MyService() {
        Log.i(TAG, "Service constructor");
    }

    void setAlarm() {
        // not working
        Log.i(TAG, "Obtaining preferences file");
        Context directBootContext = getApplicationContext().createDeviceProtectedStorageContext();

        SharedPreferences sharedPreferences;
        if (MainActivity.directBootAware) {
             sharedPreferences = directBootContext.getSharedPreferences(fileName, MODE_PRIVATE);
        }
        else {
            sharedPreferences = getSharedPreferences(fileName, MODE_PRIVATE);
        }
        long alarmTime = sharedPreferences.getLong("ALARM", -1);
        Log.i(TAG, "Alarm obtained from preferences file");

        // Remove alarm if time expired
        if (alarmTime < System.currentTimeMillis()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("ALARM");
            editor.apply();
            Log.i(TAG, "Expired alarm removed");
            Toast.makeText(this, "Expired alarm removed",Toast.LENGTH_LONG).show();
            stopSelf();
        }

        else {
            Intent intent = new Intent(this, MyBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this.getApplicationContext(), 234324243, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
            Log.i(TAG, "Alarm set for " + (alarmTime - System.currentTimeMillis()) + "ms");
            Toast.makeText(this, "Alarm set in " + ((alarmTime - System.currentTimeMillis()) / 1000 + 1) + " seconds", Toast.LENGTH_LONG).show();
            stopSelf();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}