package br.com.r29tecnologia.btpress.btfit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import br.com.r29tecnologia.btpress.btfit.alarm.LembreteReceiver;
import br.com.r29tecnologia.btpress.btfit.util.DateUtil;

/**
 * Created by victorpinto on 22/05/17. 
 */

public class Application extends android.app.Application {
    private static final String TAG = Application.class.getSimpleName();
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate Application");
        agendarAlarmeDiario();
    }
    
    private void agendarAlarmeDiario() {
        Log.d(TAG, "Agendando…");
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, LembreteReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        
        Calendar calendar = DateUtil.getToday();
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        
        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}
