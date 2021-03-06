package br.com.r29tecnologia.btpress.btfit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Calendar;

import br.com.r29tecnologia.btpress.btfit.model.Contratos;
import br.com.r29tecnologia.btpress.btfit.model.Dia;
import br.com.r29tecnologia.btpress.btfit.util.DateUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditActivity extends AppCompatActivity {
    
    private FirebaseAnalytics mFirebaseAnalytics;
    
    Calendar calendar;
    
    @BindView(R.id.edittextDia)
    EditText campoDia;
    
    @BindView(R.id.ratingDieta)
    RatingBar ratingDieta;
    
    @BindView(R.id.ratingAtv)
    RatingBar ratingAtv;
    
    @BindView(R.id.edittextObservacao)
    EditText campoObservacao;
    
    @BindView(R.id.edittextPeso)
    EditText campoPeso;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_edit_day);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calendar = DateUtil.getToday();
        if (getIntent().hasExtra(Dia.PARAM)) {
            Dia dia = getIntent().getParcelableExtra(Dia.PARAM);
            calendar.setTime(dia.getDate());
            ratingDieta.setRating(dia.getFlagDieta());
            ratingAtv.setRating(dia.getFlagAtvFisica());
            campoObservacao.setText(dia.getObservacao());
            if (dia.getPeso() != null) {
                campoPeso.setText(dia.getPeso().toString());
            }
        }
        fillDate();
        
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
    
    @OnClick(R.id.edittextDia)
    void onClickDia() {
        final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                fillDate();
            }
        };
        new DatePickerDialog(this, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                .get(Calendar.DATE)).show();
    }
    
    private void fillDate() {
        campoDia.setText(DateFormat.format("dd/MM/yyyy", calendar));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_salvar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_salvar) {
            Dia dia = new Dia();
            dia.setDate(calendar.getTime());
            dia.setFlagDieta((int) ratingDieta.getRating());
            dia.setFlagAtvFisica((int) ratingAtv.getRating());
            dia.setPreenchido(true);
            dia.setObservacao(campoObservacao.getText().toString());
            String peso = campoPeso.getText().toString();
            if (peso.length() > 0) {
                dia.setPeso(Float.parseFloat(peso));
            }
            getContentResolver().insert(Contratos.DIAS.URI, Contratos.DIAS.getCVFrom(dia));
            finish();
            
            Bundle bundle = new Bundle();
            bundle.putInt("dieta", dia.getFlagDieta());
            bundle.putInt("atividade_fisica", dia.getFlagDieta());
            bundle.putString("dia", DateFormat.format("dd/MM/yyyy", dia.getDate()).toString());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
