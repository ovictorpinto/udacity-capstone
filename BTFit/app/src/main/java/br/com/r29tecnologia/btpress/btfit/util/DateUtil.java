package br.com.r29tecnologia.btpress.btfit.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.r29tecnologia.btpress.btfit.model.Dia;

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
    
    public static List<Dia> fill(List<Dia> exists, Date dtIni, Date dtFim) {
        List<Dia> total = new ArrayList<>();
        
        Calendar ini = Calendar.getInstance();
        ini.setTime(dtIni);
        int i = 0;
        while (!ini.getTime().after(dtFim)) {
            if (exists.size() > i && ini.getTime().equals(exists.get(i).getDate())) {
                total.add(exists.get(i));
                i++;
            } else {
                final Dia dia = new Dia();
                dia.setDate(ini.getTime());
                total.add(dia);//preenche com dia default
            }
            ini.add(Calendar.DAY_OF_MONTH, 1);
        }
        return total;
    }
    
}
