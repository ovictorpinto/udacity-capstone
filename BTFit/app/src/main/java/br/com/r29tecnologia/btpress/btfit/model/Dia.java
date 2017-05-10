package br.com.r29tecnologia.btpress.btfit.model;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;

import java.util.Date;

import br.com.r29tecnologia.btpress.btfit.R;

/**
 * Created by victorpinto on 04/05/17. 
 */

public class Dia {
    private boolean preenchido;
    private Date date;
    private int flagDieta;
    private int flagAtvFisica;
    private String observacao;
    
    @ColorInt
    public int getDayColor(Context context) {
        if (!preenchido) {
            return ContextCompat.getColor(context, R.color.dia_default);
        } else {
            int sum = flagDieta + flagAtvFisica;
            if (sum == 2) {
                return ContextCompat.getColor(context, R.color.dia_ruim);
            }
            if (sum >= 5) {
                return ContextCompat.getColor(context, R.color.dia_otimo);
            }
            return ContextCompat.getColor(context, R.color.dia_medio);
        }
    }
    
    public boolean isPreenchido() {
        return preenchido;
    }
    
    public void setPreenchido(boolean preenchido) {
        this.preenchido = preenchido;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public int getFlagDieta() {
        return flagDieta;
    }
    
    public void setFlagDieta(int flagDieta) {
        this.flagDieta = flagDieta;
    }
    
    public int getFlagAtvFisica() {
        return flagAtvFisica;
    }
    
    public void setFlagAtvFisica(int flagAtvFisica) {
        this.flagAtvFisica = flagAtvFisica;
    }
    
    public String getObservacao() {
        return observacao;
    }
    
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
