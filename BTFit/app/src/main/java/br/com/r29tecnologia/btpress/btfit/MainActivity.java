package br.com.r29tecnologia.btpress.btfit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Date;

import br.com.r29tecnologia.btpress.btfit.model.Contratos;
import br.com.r29tecnologia.btpress.btfit.model.Dia;
import br.com.r29tecnologia.btpress.btfit.ui.DiaView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    
    private static final int DIA_LOADER = 500;
    private static final String TAG = MainActivity.class.getSimpleName();
    
    @BindView(R.id.layout_main)
    LinearLayout linearLayout;
    
    @BindView(R.id.fab)
    FloatingActionButton buttonAdicionar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        Dia dia = new Dia();
        dia.setDate(new Date());
        dia.setFlagAtvFisica(1);
        dia.setFlagDieta(1);
        dia.setPreenchido(true);
        
        DiaView view = new DiaView(this);
        view.setDia(dia);
        linearLayout.addView(view.getView());
        
        dia = new Dia();
        dia.setDate(new Date());
        dia.getDate().setDate(9);
        dia.setFlagAtvFisica(2);
        dia.setFlagDieta(2);
        dia.setPreenchido(true);
        
        view = new DiaView(this);
        view.setDia(dia);
        
        linearLayout.addView(view.getView());
        dia = new Dia();
        dia.setDate(new Date());
        dia.getDate().setDate(10);
        dia.setFlagAtvFisica(3);
        dia.setFlagDieta(3);
        dia.setPreenchido(true);
        
        view = new DiaView(this);
        view.setDia(dia);
        
        linearLayout.addView(view.getView());
        getSupportLoaderManager().initLoader(DIA_LOADER, null, this);
    }
    
    @OnClick(R.id.fab)
    public void add(View button) {
        startActivity(new Intent(this, EditActivity.class));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Contratos.DIAS.URI, Contratos.DIAS.COLUMNS.toArray(new String[]{}), null, null, null);
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            for (int i = 0; i < data.getCount(); i++) {
                data.moveToPosition(i);
                Log.d(TAG, String.valueOf(data.getInt(Contratos.DIAS.POSITION_ATV_FISICA)));
                Log.d(TAG, data.getString(Contratos.DIAS.POSITION_DIA));
            }
        } else {
            Log.d(TAG, "Nenhum dia cadastrado =/");
        }
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        
    }
}
