package com.example.appplaymusic;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "push notification id";
    public static final String CHANNEL_ID2 = "push notification id2";
    public static final String CHANNEL_ID3 = "push notification id3";


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        createNotificationChannel2();
        createNotificationChannel3();
    }

    private void createNotificationChannel2() {
        NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID2, "PushNotification2",
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);
    }

    private void createNotificationChannel() {
        NotificationChannel channel2 = new NotificationChannel(CHANNEL_ID, "PushNotificationFireBase",
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel2);
    }
    private void createNotificationChannel3() {

        NotificationChannel channel3 = new NotificationChannel(CHANNEL_ID3, "PushNotificationDriver",
                NotificationManager.IMPORTANCE_HIGH);
        channel3.setSound(null,null);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel3);
    }
}


