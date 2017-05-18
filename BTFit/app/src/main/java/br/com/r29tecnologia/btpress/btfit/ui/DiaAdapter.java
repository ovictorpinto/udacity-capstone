package br.com.r29tecnologia.btpress.btfit.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.r29tecnologia.btpress.btfit.model.Dia;

/**
 * Created by victor on 15/05/17.
 */

public class DiaAdapter extends RecyclerView.Adapter<DiaAdapter.ViewHolder> {
    
    public interface DiaListener {
        void onDiaClick(Dia dia);
    }
    
    private final List<Dia> list;
    private DiaListener listener;
    
    public static final class ViewHolder extends RecyclerView.ViewHolder {
        
        final DiaView diaView;
        
        public ViewHolder(DiaView diaView) {
            super(diaView.getView());
            this.diaView = diaView;
        }
    }
    
    public DiaAdapter(List<Dia> list) {
        this.list = list;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final DiaView diaView = new DiaView(parent.getContext());
        return new ViewHolder(diaView);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Dia item = list.get(position);
        holder.diaView.setDia(item);
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDiaClick(item);
                }
            });
        }
    }
    
    public void setListener(DiaListener listener) {
        this.listener = listener;
    }
    
    @Override
    public int getItemCount() {
        return list.size();
    }
}
