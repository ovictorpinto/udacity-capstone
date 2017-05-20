package br.com.r29tecnologia.btpress.btfit.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.NumberPicker;

import java.util.Calendar;

import br.com.r29tecnologia.btpress.btfit.R;

/**
 * Created by victor on 19/05/17.
 */

public class MonthDialog {
    
    private final Context context;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    private final int year;
    private final int monthOfYear;
    
    public MonthDialog(Context context, DatePickerDialog.OnDateSetListener onDateSetListener, int year, int monthOfYear) {
        this.context = context;
        this.onDateSetListener = onDateSetListener;
        this.year = year;
        this.monthOfYear = monthOfYear;
    }
    
    public void show() {
        
        final String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro",
                "Novembro", "Dezembro"};
        
        final View rootView;
        final NumberPicker numberPickerMes, numberPickerAno;
        
        rootView = View.inflate(context, R.layout.ly_dialog_mes_ano, null);
        
        numberPickerMes = (NumberPicker) rootView.findViewById(R.id.numberPickerMes);
        numberPickerMes.setDisplayedValues(meses);
        numberPickerMes.setMinValue(0);
        numberPickerMes.setMaxValue(11);
        numberPickerMes.setWrapSelectorWheel(false);
        numberPickerMes.setValue(monthOfYear);
        
        numberPickerAno = (NumberPicker) rootView.findViewById(R.id.numberPickerAno);
        numberPickerAno.setMinValue(2016);
        numberPickerAno.setMaxValue(Calendar.getInstance().get(Calendar.YEAR));
        numberPickerAno.setWrapSelectorWheel(false);
        numberPickerAno.setValue(year);
        
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.titulo_dialogo_mes);
        builder.setView(rootView).setCancelable(false).setNegativeButton(android.R.string.cancel, null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       onDateSetListener.onDateSet(null, numberPickerAno.getValue(), numberPickerMes.getValue(), 1);
                   }
               });
        builder.create().show();
    }
}
