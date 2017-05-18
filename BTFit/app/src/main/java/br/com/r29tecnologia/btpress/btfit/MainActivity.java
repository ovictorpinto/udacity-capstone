package br.com.r29tecnologia.btpress.btfit;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.r29tecnologia.btpress.btfit.model.Contratos;
import br.com.r29tecnologia.btpress.btfit.model.Dia;
import br.com.r29tecnologia.btpress.btfit.ui.DiaAdapter;
import br.com.r29tecnologia.btpress.btfit.util.DateUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    
    private static final int DIA_LOADER = 500;
    private static final String TAG = MainActivity.class.getSimpleName();
    
    @BindView(R.id.layout_main)
    RecyclerView recyclerView;
    
    @BindView(R.id.fab)
    FloatingActionButton buttonAdicionar;
    private Date dtFim;
    private Date dtIni;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        
        Calendar fim = getProximoSabado();
        dtFim = fim.getTime();
        
        fim.add(Calendar.DAY_OF_MONTH, -20);//exibe 3 semanas
        dtIni = fim.getTime();
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        getSupportLoaderManager().initLoader(DIA_LOADER, null, this);
        
        recyclerView.setLayoutManager(new GridLayoutManager(this, 7));
    }
    
    private Calendar getProximoSabado() {
        Calendar calendar = DateUtil.getToday();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        return calendar;
    }
    
    @OnClick(R.id.fab)
    public void add(View button) {
        startActivity(new Intent(this, EditActivity.class));
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final Uri uri = Contratos.DIAS.buildUriPesquisa(dtIni, dtFim);
        return new CursorLoader(this, uri, Contratos.DIAS.COLUMNS.toArray(new String[]{}), null, null, null);
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Dia> list = Contratos.DIAS.getListFrom(data);
        final DiaAdapter adapter = new DiaAdapter(fill(list));
        adapter.setListener(new DiaAdapter.DiaListener() {
            @Override
            public void onDiaClick(Dia dia) {
                final Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(Dia.PARAM, dia);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "Resetando…");
    }
    
    private List<Dia> fill(List<Dia> exists) {
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
