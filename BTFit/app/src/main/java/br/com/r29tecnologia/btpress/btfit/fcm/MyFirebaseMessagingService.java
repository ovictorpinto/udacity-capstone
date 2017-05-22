package br.com.r29tecnologia.btpress.btfit.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.com.r29tecnologia.btpress.btfit.R;

/**
 * Created by victor on 21/05/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    
    public static final String TAG = "FirebaseMessagingSvc";
    
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            showNotification(remoteMessage.getNotification().getBody());
        }
        
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    
    private void showNotification(String body) {
        NotificationCompat.Builder parseBuilder = new NotificationCompat.Builder(this);
        parseBuilder.setContentTitle(getString(R.string.app_name)).setContentText(body).setSmallIcon(R.drawable.ic_stat_notification)
                    .setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL);
        parseBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        final Notification notification = parseBuilder.build();
        
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Pick an id that probably won't overlap anything
        int notificationId = (int) System.currentTimeMillis();
        
        try {
            nm.notify(notificationId, notification);
        } catch (SecurityException e) {
            // Some phones throw an exception for unapproved vibration
            notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND;
            nm.notify(notificationId, notification);
        }
    }
}
