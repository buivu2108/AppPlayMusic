package com.example.appplaymusic.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.appplaymusic.MainActivity;
import com.example.appplaymusic.MyApplication;
import com.example.appplaymusic.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //private static final String TAG = "token tag";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification == null) {
            return;
        }
        String strTitle = notification.getTitle();
        String strMessage = notification.getBody();  //data kiể mặc định của FireBase với Title và Body

        //Data Message tự tạo token và send từ API cho FireBase và FireBase trả về App

//        Map<String,String> map = remoteMessage.getData();
//
//        String strTitle = map.get("Value_title");// trỏ đến key value từ API để lấy dữ liệu Body Json và gửi cho FireBase token gồm key_value
//        String strMessage = map.get("Value_mess");
//
         sendNotification(strTitle, strMessage);
    }

    private void sendNotification(String strTitle, String strMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle(strTitle)
                .setContentText(strMessage)
                .setSmallIcon(R.drawable.hearticon)
                .setContentIntent(pendingIntent);

        Notification notification = notificationBuilder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(2, notification);
        }
    }

//    @Override
//    public void onNewToken(@NonNull String token) {
//        super.onNewToken(token);
//        Log.d(TAG, "Refreshed token: " + token);
//    }

}
