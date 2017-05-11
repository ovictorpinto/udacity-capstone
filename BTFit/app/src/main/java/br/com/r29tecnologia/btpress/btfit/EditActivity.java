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

import java.util.Calendar;

import br.com.r29tecnologia.btpress.btfit.model.Contratos;
import br.com.r29tecnologia.btpress.btfit.model.Dia;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditActivity extends AppCompatActivity {
    
    Calendar calendar;
    
    @BindView(R.id.edittextDia)
    EditText campoDia;
    
    @BindView(R.id.ratingDieta)
    RatingBar ratingDieta;
    
    @BindView(R.id.ratingAtv)
    RatingBar ratingAtv;
    
    @BindView(R.id.edittextObservacao)
    EditText campoObservacao;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_edit_day);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calendar = Calendar.getInstance();
        fillDate();
        
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
            getContentResolver().insert(Contratos.DIAS.URI, Contratos.DIAS.getCVFrom(dia));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
