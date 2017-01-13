package com.example.asif.contestremind;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Created by asif on 1/9/17.
 */

public class AlertReceiver extends BroadcastReceiver {
    int NOTI_ID=100;
    @Override
    public void onReceive(Context context, Intent intent) {
          String message=intent.getExtras().getString("ContestSite");

        Uri sound= Uri.parse("android.resource://"+"com.example.asif.contestremind"+R.raw.sound2);

        NotificationManager notificationManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Contest Reminder")
                .setSound(sound)
                .setContentText(message);
        notificationManager.notify(NOTI_ID,builder.build());

    }
}
