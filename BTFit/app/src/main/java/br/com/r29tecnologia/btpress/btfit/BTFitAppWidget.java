package br.com.r29tecnologia.btpress.btfit;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.r29tecnologia.btpress.btfit.model.Contratos;
import br.com.r29tecnologia.btpress.btfit.model.Dia;
import br.com.r29tecnologia.btpress.btfit.ui.DiaView;
import br.com.r29tecnologia.btpress.btfit.util.DateUtil;

/**
 * Implementation of App Widget functionality.
 */
public class BTFitAppWidget extends AppWidgetProvider {
    
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        
        Calendar calendar = DateUtil.getToday();
        Date dtFim = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        Date dtInicio = calendar.getTime();
        
        final Cursor query = context.getContentResolver().query(Contratos.DIAS.buildUriPesquisa(dtInicio, dtFim), null, null, null, null);
        List<Dia> preenchidos = Contratos.DIAS.getListFrom(query);
        List<Dia> filled = DateUtil.fill(preenchidos, dtInicio, dtFim);
        
        // Construct the RemoteViews object
        RemoteViews mainView = new RemoteViews(context.getPackageName(), R.layout.btfit_app_widget);
        mainView.removeAllViews(R.id.content);
        for (int pos = 0; pos < 7; pos++) {
            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ly_item_resumo_dia_widget);
            fillRemote(context, remoteViews, filled.get(pos));
            mainView.addView(R.id.content, remoteViews);
        }
        
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, mainView);
    }
    
    static void fillRemote(Context context, RemoteViews remoteViews, Dia dia) {
        remoteViews.setTextViewText(R.id.textview_week, DiaView.WEEK_FORMAT.format(dia.getDate()));
        remoteViews.setInt(R.id.mainview, "setBackgroundColor", dia.getDayColor(context));
        remoteViews
                .setImageViewResource(R.id.image_dieta_2, dia.getFlagDieta() > 1 ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
        remoteViews
                .setImageViewResource(R.id.image_dieta_3, dia.getFlagDieta() > 2 ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
        remoteViews
                .setImageViewResource(R.id.image_atv_2, dia.getFlagAtvFisica() > 1 ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
        remoteViews
                .setImageViewResource(R.id.image_atv_3, dia.getFlagAtvFisica() > 2 ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
    }
    
    
    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        final ContentObserver observer = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BTFitAppWidget.class));
                onUpdate(context, appWidgetManager, appWidgetIds);
            }
        };
        context.getContentResolver().registerContentObserver(Contratos.DIAS.URI, true, observer);
    }
}

