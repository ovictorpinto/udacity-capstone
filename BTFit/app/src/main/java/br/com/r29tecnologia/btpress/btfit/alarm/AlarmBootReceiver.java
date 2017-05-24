package br.com.r29tecnologia.btpress.btfit.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Receiver somente para acordar a aplicação após o boot e reagendar os alarmes
 */
public class AlarmBootReceiver extends BroadcastReceiver {
    
    public static final String TAG = AlarmBootReceiver.class.getSimpleName();
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d(TAG, "onReceive Boot");
        }
    }
}
