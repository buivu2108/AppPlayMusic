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
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.jingle_bells_sms_523);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID2, "PushNotification2",
                NotificationManager.IMPORTANCE_HIGH);
        channel1.setSound(sound, audioAttributes);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);
    }

    private void createNotificationChannel() {
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.jingle_bells_sms_523);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        NotificationChannel channel2 = new NotificationChannel(CHANNEL_ID, "PushNotificationFireBase",
                NotificationManager.IMPORTANCE_HIGH);
        channel2.setSound(sound, audioAttributes);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel2);
    }
    private void createNotificationChannel3() {
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.jingle_bells_sms_523);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        NotificationChannel channel3 = new NotificationChannel(CHANNEL_ID3, "PushNotificationDriver",
                NotificationManager.IMPORTANCE_HIGH);
        channel3.setSound(sound, audioAttributes);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel3);
    }
}


