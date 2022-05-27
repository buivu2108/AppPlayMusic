package com.example.appplaymusic.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.appplaymusic.MainActivity;
import com.example.appplaymusic.MyApplication;
import com.example.appplaymusic.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.time.format.TextStyle;
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
        sendNotification(strTitle, strMessage);
    }

    private void sendNotification(String strTitle, String strMessage) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_miss);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID2)
                .setContentTitle(strTitle)
                .setContentText(strMessage)
                .setLargeIcon(bitmap)
                .setSound(null)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(strMessage))
                .setSmallIcon(R.drawable.hearticon)
                .setColor(getResources().getColor(R.color.design_default_color_error))
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
