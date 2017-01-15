package com.example.asif.contestremind;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Created by asif on 1/9/17.
 */

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
          String message=intent.getExtras().getString("ContestSite");
           int NOTI_ID = intent.getExtras().getInt("notification_id");
        int color = 0xff123456;
        Uri sound= Uri.parse("android.resource://"+"com.example.asif.contestremind"+R.raw.sound2);
        Intent notificationIntent = new Intent(context,MainActivity.class);
        PendingIntent intent1 = PendingIntent.getActivity(context,0,notificationIntent,0);
        NotificationManager notificationManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(color)
                .setContentTitle("Contest Reminder")
                .setSound(sound)
                .setContentIntent(intent1)
                .setContentText(message);

        notificationManager.notify(++NOTI_ID,builder.build());

    }
}
