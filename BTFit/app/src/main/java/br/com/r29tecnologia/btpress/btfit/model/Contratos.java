package br.com.r29tecnologia.btpress.btfit.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.google.common.collect.ImmutableList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by victorpinto on 10/05/17. 
 */

public final class Contratos {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    
    static final String AUTHORITY = "br.com.r29tecnologia.btpress.btfit";
    static final String PATH_DIA = "dia";
    static final String PATH_DIA_ESPECIFICO = "dia/*";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    
    public static final class DIAS implements BaseColumns {
        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_DIA).build();
        
        public static final String TABLE_NAME = "dias";
        public static final String COLUMN_DIA = "dia";
        public static final String COLUMN_DIETA = "dieta";
        public static final String COLUMN_ATV_FISICA = "atv_fisica";
        public static final String COLUMN_OBSERVACAO = "observacao";
        public static final String COLUMN_PREENCHIDO = "preenchido";
        
        public static final int POSITION_ID = 0;
        public static final int POSITION_DIA = 1;
        public static final int POSITION_DIETA = 2;
        public static final int POSITION_ATV_FISICA = 3;
        public static final int POSITION_OBSERVACAO = 4;
        public static final int POSITION_PREENCHIDO = 5;
        
        public static final ImmutableList<String> COLUMNS = ImmutableList
                .of(_ID, COLUMN_DIA, COLUMN_DIETA, COLUMN_ATV_FISICA, COLUMN_OBSERVACAO, COLUMN_PREENCHIDO);
        
        public static String CREATE = String
                .format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT " +
                        "" + "" + "" + "" + "" + "" + "" + "NULL, %s TEXT NULL, %s INTEGER NOT NULL, UNIQUE (%s) ON CONFLICT REPLACE);",
                        TABLE_NAME, _ID, COLUMN_DIA, COLUMN_DIETA, COLUMN_ATV_FISICA, COLUMN_OBSERVACAO, COLUMN_PREENCHIDO, COLUMN_DIA);
        
        public static String getDiaEspecificoFromUri(Uri queryUri) {
            return queryUri.getLastPathSegment();
        }
        
        public static ContentValues getCVFrom(Dia dia) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_DIA, dia.getDate().getTime());
            cv.put(COLUMN_DIETA, dia.getFlagDieta());
            cv.put(COLUMN_ATV_FISICA, dia.getFlagAtvFisica());
            cv.put(COLUMN_OBSERVACAO, dia.getObservacao());
            cv.put(COLUMN_PREENCHIDO, dia.isPreenchido() ? 1 : 0);
            return cv;
        }
        
        public static List<Dia> getListFrom(Cursor data) {
            List<Dia> list = new ArrayList<>();
            if (data.moveToFirst()) {
                for (int i = 0; i < data.getCount(); i++) {
                    data.moveToPosition(i);
                    Dia dia = new Dia();
                    dia.setDate(new Date(data.getLong(POSITION_DIA)));
                    dia.setFlagDieta(data.getInt(POSITION_DIETA));
                    dia.setFlagAtvFisica(data.getInt(POSITION_ATV_FISICA));
                    dia.setObservacao(data.getString(POSITION_OBSERVACAO));
                    dia.setPreenchido(data.getInt(POSITION_PREENCHIDO) > 0);
                    list.add(dia);
                }
            }
            return list;
        }
        
        public static Uri buildUriPesquisa(Date dtIni, Date dtFim) {
            return BASE_URI.buildUpon().appendPath(PATH_DIA).appendQueryParameter("dtIni", String.valueOf(dtIni.getTime()))
                           .appendQueryParameter("dtFim", String.valueOf(dtFim.getTime())).build();
        }
        
    }
}
