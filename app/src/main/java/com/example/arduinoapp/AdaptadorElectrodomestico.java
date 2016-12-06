package com.example.arduinoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class AdaptadorElectrodomestico extends RecyclerView.Adapter<AdaptadorElectrodomestico.ViewHolder> {

    private List<Electrodomestico> electrodomesticoList;
    private EventClick PevntClick;
    private Context context;

    public interface EventClick {
        void onItemClick(ViewHolder holder, int Lintposicion);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtNombreElectrodomestico;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombreElectrodomestico = (TextView) itemView.findViewById(R.id.txtNombreElectrodomestico);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PevntClick.onItemClick(this, getAdapterPosition());
        }
    }


    public AdaptadorElectrodomestico(List<Electrodomestico> electrodomesticoList, Context PcntContext, EventClick PevntClick) {
        this.electrodomesticoList = electrodomesticoList;
        this.context = PcntContext;
        this.PevntClick = PevntClick;
    }

    @Override
    public int getItemCount() {
        return electrodomesticoList.size();
    }

    @Override
    public AdaptadorElectrodomestico.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_electrodomesticos, parent, false);
        return new AdaptadorElectrodomestico.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Electrodomestico elect = electrodomesticoList.get(position);

        holder.txtNombreElectrodomestico.setText(elect.getNombre());


    }
}
