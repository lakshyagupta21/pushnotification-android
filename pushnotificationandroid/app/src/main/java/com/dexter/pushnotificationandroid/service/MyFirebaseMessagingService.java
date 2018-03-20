package com.dexter.pushnotificationandroid.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dexter.pushnotificationandroid.Constants;
import com.dexter.pushnotificationandroid.activity.MainActivity;
import com.dexter.pushnotificationandroid.R;
import com.dexter.pushnotificationandroid.activity.ReplyActivity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.Map;

/**
 * Firebase-androidquickstart
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            handleNow();
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        sendNotification(remoteMessage.getData());
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendNotification(Map<String,String> messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        String message;
        try {
            message = new ObjectMapper().writeValueAsString(messageBody);
        } catch (IOException e) {
            message = "error";
            e.printStackTrace();
        }
        Intent intentReply = new Intent(this, ReplyActivity.class);
        intentReply.putExtra(Constants.TYPE,1);
        intentReply.putExtra(Constants.MESSAGE, message);

        Intent intentReplyAll = new Intent(this, ReplyActivity.class);
        intentReplyAll.putExtra(Constants.TYPE, 2);
        intentReplyAll.putExtra(Constants.MESSAGE , message);

        PendingIntent pendingIntentReply = PendingIntent.getActivity(this, 0, intentReply,
                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentReplyAll = PendingIntent.getActivity(this, 0, intentReplyAll,
                PendingIntent.FLAG_UPDATE_CURRENT);


        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .addAction(R.drawable.ic_reply_black_24dp,"Reply",pendingIntentReply)
                        .addAction(R.drawable.ic_reply_all_black_24dp, "Reply all", pendingIntentReplyAll)
                        .setContentTitle(messageBody.get(Constants.TITLE))
                        .setContentText(messageBody.get(Constants.MESSAGE))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onMessageSent(String msgId) {
        super.onMessageSent(msgId);
        Log.d(TAG, "Message sent: " + msgId);
    }

    @Override
    public void onSendError(String msgId, Exception e) {
        super.onSendError(msgId, e);
        Log.e(TAG, "Error sending upstream message: " + e);
    }
}