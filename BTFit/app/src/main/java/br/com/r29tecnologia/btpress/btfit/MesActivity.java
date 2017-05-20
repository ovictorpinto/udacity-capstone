package br.com.r29tecnologia.btpress.btfit;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.r29tecnologia.btpress.btfit.model.Contratos;
import br.com.r29tecnologia.btpress.btfit.model.Dia;
import br.com.r29tecnologia.btpress.btfit.ui.DiaAdapter;
import br.com.r29tecnologia.btpress.btfit.ui.MonthDialog;
import br.com.r29tecnologia.btpress.btfit.util.DateUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    
    private static final int DIA_LOADER = 500;
    private static final String TAG = MesActivity.class.getSimpleName();
    
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    
    private Date dtFim;
    private Date dtIni;
    private Calendar mesReferencia;
    private List<Dia> diaList;
    private AsyncTask<String, String, Boolean> acaoCompartilhar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_mes);
        ButterKnife.bind(this);
        
        mesReferencia = DateUtil.getToday();
        setPeriodo();
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        getSupportLoaderManager().initLoader(DIA_LOADER, null, this);
        
        recyclerView.setLayoutManager(new GridLayoutManager(this, 7));
    }
    
    private void setPeriodo() {
        Calendar calendar = (Calendar) mesReferencia.clone();
        setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        int ultimoDia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, ultimoDia);
        //primeiro sabado depois do ultimo dia do mes
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        dtFim = calendar.getTime();
        
        //primeiro domingo antes do primeiro dia do mes
        calendar = (Calendar) mesReferencia.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        dtIni = calendar.getTime();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_periodo, menu);
        getMenuInflater().inflate(R.menu.menu_compartilhar, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_periodo) {
            final MonthDialog dialog = new MonthDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mesReferencia.set(Calendar.MONTH, month);
                    mesReferencia.set(Calendar.YEAR, year);
                    setPeriodo();
                    getSupportLoaderManager().initLoader(DIA_LOADER, null, MesActivity.this);
                }
            }, mesReferencia.get(Calendar.YEAR), mesReferencia.get(Calendar.MONTH));
            dialog.show();
            return true;
        } else if (item.getItemId() == R.id.action_compartilhar) {
            compartilhar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void compartilhar() {
        acaoCompartilhar = new AsyncTask<String, String, Boolean>() {
            
            private ProgressDialog waiting;
            
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                waiting = ProgressDialog.show(MesActivity.this, "", getString(R.string.gerando_relatorio_), true, true);
                waiting.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancel(true);
                    }
                });
                waiting.show();
            }
            
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }
            
            @Override
            protected void onPostExecute(Boolean sucesso) {
                super.onPostExecute(sucesso);
                if (!isCancelled()) {
                    if (waiting.isShowing()) {
                        waiting.dismiss();
                    }
                }
            }
        };
        AsyncTaskCompat.executeParallel(acaoCompartilhar);
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final Uri uri = Contratos.DIAS.buildUriPesquisa(dtIni, dtFim);
        return new CursorLoader(this, uri, Contratos.DIAS.COLUMNS.toArray(new String[]{}), null, null, null);
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        diaList = Contratos.DIAS.getListFrom(data);
        final DiaAdapter adapter = new DiaAdapter(DateUtil.fill(diaList, dtIni, dtFim));
        recyclerView.setAdapter(adapter);
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "Resetando…");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (acaoCompartilhar != null) {
            acaoCompartilhar.cancel(true);
        }
    }
}
