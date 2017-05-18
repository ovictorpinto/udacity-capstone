package br.com.r29tecnologia.btpress.btfit.util;

import java.util.Calendar;

/**
 * Created by victor on 15/05/17.
 */

public class DateUtil {
    public static Calendar getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
    
}
