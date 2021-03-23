package edu.neu.madcourse.emoji_chat.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import edu.neu.madcourse.emoji_chat.R;
import edu.neu.madcourse.emoji_chat.activities.AllMessagesActivity;

public class StickerMessagingService extends FirebaseMessagingService {
    private static final String TAG = StickerMessagingService.class.getSimpleName();
    private static DatabaseReference mDatabase;

    public StickerMessagingService() {
        super();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        Log.d(TAG, "User: " + LoginFragment.username);
//        if(LoginFragment.username != null) {
//            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
//                @Override
//                public void onSuccess(InstanceIdResult instanceIdResult) {
//                    String token = instanceIdResult.getToken();
//                    Log.d(TAG, "Token: " + token);
//                    mDatabase.child("users").child(LoginFragment.username).child("token").setValue(token);
//                }
//            });
//        }
    }

    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);
//        Log.d(TAG, "Refreshed token: " + newToken);
//        if(LoginFragment.username != null) {
//            mDatabase.child("users").child(LoginFragment.username).child("token").setValue(newToken);
//        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            System.out.println("message received!!!!!!!");
            showNotification(remoteMessage);
        }
    }

    private void showNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, AllMessagesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification notification;
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        NotificationChannel notificationChannel = new NotificationChannel("channel-id",
                "channel-name", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("channel-description");
        notificationManager.createNotificationChannel(notificationChannel);

        builder = new NotificationCompat.Builder(this, "channel-id");
        notification = builder.setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(0,notification);
    }
}
