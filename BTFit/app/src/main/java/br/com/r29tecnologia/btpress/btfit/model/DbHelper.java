package br.com.r29tecnologia.btpress.btfit.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DbHelper extends SQLiteOpenHelper {
    
    private static final String NAME = "btfit.db";
    private static final int VERSION = 1;
    private static final String TAG = DbHelper.class.getSimpleName();
    
    DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, 0, VERSION);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Atualizando... " + oldVersion);
        switch (oldVersion) {
            case 0:
                db.execSQL(Contratos.DIAS.CREATE);
        }
    }
}
