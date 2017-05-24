package br.com.r29tecnologia.btpress.btfit.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

import br.com.r29tecnologia.btpress.btfit.EditActivity;
import br.com.r29tecnologia.btpress.btfit.R;
import br.com.r29tecnologia.btpress.btfit.model.Contratos;
import br.com.r29tecnologia.btpress.btfit.util.DateUtil;

public class LembreteReceiver extends BroadcastReceiver {
    
    public static final String TAG = LembreteReceiver.class.getSimpleName();
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "nnReceive Lembrete");
        //só mostra a notificação se não tiver
        Calendar hoje = DateUtil.getToday();
        Uri uri = Contratos.DIAS.buildUriDiaEspecifico(hoje.getTime());
        Cursor query = context.getContentResolver().query(uri, null, null, null, null);
        if (query == null || !query.moveToFirst()) {
            showNotification(context);
        }
        if (query != null) {
            query.close();
        }
    }
    
    private void showNotification(Context context) {
        NotificationCompat.Builder parseBuilder = new NotificationCompat.Builder(context);
        String body = context.getString(R.string.texto_lembrete);
        parseBuilder.setContentTitle(context.getString(R.string.app_name)).setContentText(body)
                    .setSmallIcon(R.drawable.ic_stat_notification).setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL);
        parseBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        
        Intent intent = new Intent(context, EditActivity.class);
        PendingIntent open = PendingIntent.getActivity(context, 0, intent, 0);
        parseBuilder.setContentIntent(open);
        
        final Notification notification = parseBuilder.build();
        
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
