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
    static final String PATH_DIA_ESPECIFICO = "dia/#";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    
    public static final class DIAS implements BaseColumns {
        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_DIA).build();
        
        public static final String TABLE_NAME = "dias";
        public static final String COLUMN_DIA = "dia";
        public static final String COLUMN_DIETA = "dieta";
        public static final String COLUMN_ATV_FISICA = "atv_fisica";
        public static final String COLUMN_OBSERVACAO = "observacao";
        public static final String COLUMN_PREENCHIDO = "preenchido";
        public static final String COLUMN_PESO = "peso";
        
        public static final int POSITION_ID = 0;
        public static final int POSITION_DIA = 1;
        public static final int POSITION_DIETA = 2;
        public static final int POSITION_ATV_FISICA = 3;
        public static final int POSITION_OBSERVACAO = 4;
        public static final int POSITION_PREENCHIDO = 5;
        public static final int POSITION_PESO = 6;
        
        public static final ImmutableList<String> COLUMNS = ImmutableList
                .of(_ID, COLUMN_DIA, COLUMN_DIETA, COLUMN_ATV_FISICA, COLUMN_OBSERVACAO, COLUMN_PREENCHIDO, COLUMN_PESO);
        
        public static String CREATE;
        
        static {
            StringBuilder tmp = new StringBuilder("CREATE TABLE ");
            tmp.append(TABLE_NAME).append("( ").append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
            tmp.append(COLUMN_DIA).append(" INTEGER NOT NULL, ");
            tmp.append(COLUMN_DIETA).append(" INTEGER NOT NULL, ");
            tmp.append(COLUMN_ATV_FISICA).append(" INTEGER NOT NULL, ");
            tmp.append(COLUMN_OBSERVACAO).append(" TEXT NULL, ");
            tmp.append(COLUMN_PREENCHIDO).append(" INTEGER NOT NULL, ");
            tmp.append(COLUMN_PESO).append(" REAL NULL, ");
            tmp.append("UNIQUE (").append(COLUMN_DIA).append(") ON ").append("CONFLICT ").append("REPLACE);");
            CREATE = tmp.toString();
        }
        
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
            cv.put(COLUMN_PESO, dia.getPeso());
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
                    if (!data.isNull(POSITION_PESO)) {
                        dia.setPeso(data.getFloat(POSITION_PESO));
                    }
                    list.add(dia);
                }
            }
            return list;
        }
        
        public static Uri buildUriPesquisa(Date dtIni, Date dtFim) {
            return BASE_URI.buildUpon().appendPath(PATH_DIA).appendQueryParameter("dtIni", String.valueOf(dtIni.getTime()))
                           .appendQueryParameter("dtFim", String.valueOf(dtFim.getTime())).build();
        }
        
        public static Uri buildUriDiaEspecifico(Date dia) {
            return URI.buildUpon().appendPath(String.valueOf(dia.getTime())).build();
        }
    }
}