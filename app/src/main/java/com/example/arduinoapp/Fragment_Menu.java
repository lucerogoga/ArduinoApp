package com.example.arduinoapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Menu extends Fragment implements AdaptadorMenu.EventClick {

    private List<Menu> ListaMenu;
    RecyclerView LrecyclerView;
    RecyclerView.LayoutManager LrecyclerViewlayoutManager;
    RecyclerView.Adapter LrecyclerViewadapter;
    public Fragment_Menu() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        ListaMenu = new ArrayList<>();
        ListaMenu.add(new Menu("1",R.drawable.ventiladores,"Ventiladores"));
        ListaMenu.add(new Menu("2",R.drawable.focos,"Focos"));
    //    ListaMenu.add(new Menu("3",R.drawable.electrodomesticos,"Electrodomesticos"));
       // ListaMenu.add(new Menu("4",R.drawable.reportes,"Reportes"));
        ListaMenu.add(new Menu("5",R.drawable.consumo,"Consumo"));
        LrecyclerView = (RecyclerView)v.findViewById(R.id.recycler_menu);
        LrecyclerView.setHasFixedSize(true);
        LrecyclerViewlayoutManager = new GridLayoutManager(getActivity(),2);
        LrecyclerView.setLayoutManager(LrecyclerViewlayoutManager);
        LrecyclerViewadapter = new AdaptadorMenu(ListaMenu,getActivity(),this);
        LrecyclerView.setAdapter(LrecyclerViewadapter);
        return v;
    }

    @Override
    public void onItemClick(AdaptadorMenu.ViewHolder holder, int Lintposicion) {

        String id = ListaMenu.get(Lintposicion).getId();

        if(id.equals("1")){

            Fragment_Ventilador ventilador = new Fragment_Ventilador();

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, ventilador).addToBackStack("tag").commit();
        }else if(id.equals("2")){
            Fragment_Foco foco = new Fragment_Foco();

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, foco).addToBackStack("tag").commit();

        }
      /*  else if(id.equals("3")){
          Fragment_AgregarElect agregarelec = new Fragment_AgregarElect();

          FragmentManager fragmentManager = getFragmentManager();
          fragmentManager.beginTransaction()
                    .replace(R.id.content_main, agregarelec).addToBackStack("tag").commit();
        }else if(id.equals("4")){
            Fragment_consumo consumo = new Fragment_consumo();

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, consumo).addToBackStack("tag").commit();

        }*/else if(id.equals("5")){
            Fragment_Electrodomestico electrodomestico = new Fragment_Electrodomestico();

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, electrodomestico).addToBackStack("tag").commit();

        }
    }
}
