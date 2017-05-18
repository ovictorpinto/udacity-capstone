package br.com.r29tecnologia.btpress.btfit.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;

import static br.com.r29tecnologia.btpress.btfit.model.Contratos.DIAS.TABLE_NAME;

public class DiaProvider extends ContentProvider {
    
    private static final String TAG = DiaProvider.class.getSimpleName();
    
    private static final int CODIGO_DIA = 100;
    private static final int CODIGO_PESQUISA_DIA_ESPECIFICO = 101;
    
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private DbHelper dbHelper;
    
    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(Contratos.AUTHORITY, Contratos.PATH_DIA, CODIGO_DIA);
        matcher.addURI(Contratos.AUTHORITY, Contratos.PATH_DIA_ESPECIFICO, CODIGO_PESQUISA_DIA_ESPECIFICO);
        return matcher;
    }
    
    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }
    
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        switch (uriMatcher.match(uri)) {
            case CODIGO_DIA:
                Log.d(TAG, "Buscando todos...");
                
                if (uri.getQueryParameter("dtIni") != null) {
                    long dtIni = Long.parseLong(uri.getQueryParameter("dtIni"));
                    long dtFim = Long.parseLong(uri.getQueryParameter("dtFim"));
                    Log.d(TAG, "De..." + new Date(dtIni).toString());
                    Log.d(TAG, "Até..." + new Date(dtFim).toString());
                    final String selectionPeriodo = Contratos.DIAS.COLUMN_DIA + " >= ? and " + Contratos.DIAS.COLUMN_DIA + " <= ?";
                    final String[] selectionArgsPeriodo = {String.valueOf(dtIni), String.valueOf(dtFim)};
                    final String orderPeriodo = Contratos.DIAS.COLUMN_DIA;
                    returnCursor = db.query(TABLE_NAME, projection, selectionPeriodo, selectionArgsPeriodo, null, null, orderPeriodo);
                } else {
                    returnCursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                }
                break;
            
            case CODIGO_PESQUISA_DIA_ESPECIFICO:
                Log.d(TAG, "Buscando por dia...");
                final String diaEspecifico = Contratos.DIAS.getDiaEspecificoFromUri(uri);
                final String[] selectionArgs1 = {diaEspecifico};
                final String selection1 = Contratos.DIAS.COLUMN_DIA + " = ?";
                returnCursor = db.query(TABLE_NAME, projection, selection1, selectionArgs1, null, null, sortOrder);
                break;
            
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }
        
        Context context = getContext();
        if (context != null) {
            returnCursor.setNotificationUri(context.getContentResolver(), uri);
        }
        
        return returnCursor;
    }
    
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
    
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;
        
        switch (uriMatcher.match(uri)) {
            case CODIGO_DIA:
                Log.d(TAG, "Inserindo...");
                db.insert(TABLE_NAME, null, values);
                returnUri = Contratos.DIAS.URI;
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }
        
        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }
        
        return returnUri;
    }
    
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Unknown URI:" + uri);
    }
    
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
