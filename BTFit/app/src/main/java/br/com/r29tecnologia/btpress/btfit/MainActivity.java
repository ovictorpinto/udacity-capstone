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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
    
    @BindView(R.id.chart)
    LineChart chart;
    
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
        List<Dia> preenchidos = Contratos.DIAS.getListFrom(data);
        List<Dia> list = DateUtil.fill(preenchidos, dtIni, dtFim);
        final DiaAdapter adapter = new DiaAdapter(list);
        adapter.setListener(new DiaAdapter.DiaListener() {
            @Override
            public void onDiaClick(Dia dia) {
                final Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(Dia.PARAM, dia);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        
        List<Entry> entries = new ArrayList<Entry>();
        for (Dia dia : preenchidos) {
            // turn your data into Entry objects
            entries.add(new Entry(dia.getDate().getDate(), dia.getPeso()));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Peso"); // add entries to dataset
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mensal, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_mensal) {
            Intent intent = new Intent(this, MesActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "Resetando…");
    }
}
