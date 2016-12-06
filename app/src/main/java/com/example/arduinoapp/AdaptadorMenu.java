package com.example.arduinoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Christian on 17/11/2016.
 */

public class AdaptadorMenu extends RecyclerView.Adapter<AdaptadorMenu.ViewHolder> {

    private List<Menu> ListaMenu;
    private EventClick PevntClick;
    private Context context;

    public interface EventClick {
        void onItemClick(ViewHolder holder, int Lintposicion);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ImagenMenus;

        public ViewHolder(View itemView) {
            super(itemView);
            ImagenMenus = (ImageView) itemView.findViewById(R.id.ImagenMenu);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PevntClick.onItemClick(this, getAdapterPosition());
        }
    }


    public AdaptadorMenu(List<Menu> ListaMenu, Context PcntContext, EventClick PevntClick) {
        this.ListaMenu = ListaMenu;
        this.context = PcntContext;
        this.PevntClick = PevntClick;
    }

    @Override
    public int getItemCount() {
        return ListaMenu.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_menu, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Menu menu = ListaMenu.get(position);

        Glide.with(context)
                .load(menu.getImagen())
                .crossFade()
                .into(holder.ImagenMenus);
    }

}
