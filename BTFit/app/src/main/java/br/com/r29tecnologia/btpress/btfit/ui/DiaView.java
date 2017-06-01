package br.com.r29tecnologia.btpress.btfit.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.r29tecnologia.btpress.btfit.R;
import br.com.r29tecnologia.btpress.btfit.model.Dia;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by victorpinto on 04/05/17. 
 */

@SuppressWarnings("WeakerAccess")
public class DiaView {
    
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM", Locale.getDefault());
    public final static SimpleDateFormat WEEK_FORMAT = new SimpleDateFormat("EEE", Locale.getDefault());
    private final View mainView;
    private Context context;
    private Dia dia;
    
    @BindView(R.id.image_dieta_2)
    ImageView dieta2;
    @BindView(R.id.image_dieta_3)
    ImageView dieta3;
    @BindView(R.id.image_atv_2)
    ImageView atv2;
    @BindView(R.id.image_atv_3)
    ImageView atv3;
    @BindView(R.id.textview_date)
    TextView textviewDate;
    @BindView(R.id.textview_week)
    TextView textviewWeek;
    
    public DiaView(Context context) {
        mainView = LayoutInflater.from(context).inflate(R.layout.ly_item_resumo_dia, null);
        this.context = context;
        ButterKnife.bind(this, mainView);
    }
    
    private void fill() {
        final String dateFormatted = DATE_FORMAT.format(dia.getDate());
        textviewDate.setText(dateFormatted);
        textviewWeek.setText(WEEK_FORMAT.format(dia.getDate()));
        mainView.setBackgroundColor(dia.getDayColor(context));
        dieta2.setImageResource(dia.getFlagDieta() > 1 ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
        dieta3.setImageResource(dia.getFlagDieta() > 2 ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
        atv2.setImageResource(dia.getFlagAtvFisica() > 1 ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
        atv3.setImageResource(dia.getFlagAtvFisica() > 2 ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
        if (dateFormatted.equalsIgnoreCase(DATE_FORMAT.format(new Date()))) {
            final int color = ContextCompat.getColor(context, R.color.dia_hoje);
            textviewDate.setTextColor(color);
            textviewWeek.setTextColor(color);
        }
        
    }
    
    public void setDia(Dia dia) {
        this.dia = dia;
        fill();
    }
    
    public View getView() {
        return mainView;
    }
}
