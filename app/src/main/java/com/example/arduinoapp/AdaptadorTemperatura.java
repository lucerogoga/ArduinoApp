package com.example.arduinoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by usuario on 18/11/2016.
 */

public class AdaptadorTemperatura  extends RecyclerView.Adapter<AdaptadorTemperatura.ViewHolder> {

    private List<Temps> TempsList;
    private AdaptadorTemperatura.EventClick PevntClick;
    private Context context;

    public interface EventClick {
        void onItemClick(AdaptadorTemperatura.ViewHolder holder, int Lintposicion);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tempsensor;

        public ViewHolder(View itemView) {
            super(itemView);
            tempsensor = (TextView) itemView.findViewById(R.id.temp_sensor);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PevntClick.onItemClick(this, getAdapterPosition());
        }
    }


    public AdaptadorTemperatura (List<Temps> TempsList, Context PcntContext, AdaptadorTemperatura.EventClick PevntClick) {
        this.TempsList = TempsList;
        this.context = PcntContext;
        this.PevntClick = PevntClick;
    }

    @Override
    public int getItemCount() {
        return TempsList.size();
    }

    @Override
    public AdaptadorTemperatura.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_electrodomesticos, parent, false);
        return new AdaptadorTemperatura.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdaptadorTemperatura.ViewHolder holder, int position) {

        Temps temps = TempsList.get(position);

        holder.tempsensor.setText(temps.getValor());


    }
}
