package com.example.arduinoapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Electrodomestico extends Fragment implements AdaptadorElectrodomestico.EventClick {

    private List<Electrodomestico> electrodomesticos;
    RecyclerView LrecyclerView;
    RecyclerView.LayoutManager LrecyclerViewlayoutManager;
    RecyclerView.Adapter LrecyclerViewadapter;
    JsonArrayRequest LjsonArrayRequest ;
    RequestQueue LrequestQueue ;

    public Fragment_Electrodomestico() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_electrodomesticos, container, false);
        LrecyclerView = (RecyclerView)v.findViewById(R.id.recycler_electrodomesticos);

        electrodomesticos = new ArrayList<>();
        LrecyclerView.setHasFixedSize(true);
        LrecyclerViewlayoutManager = new LinearLayoutManager(getActivity());
        LrecyclerView.setLayoutManager(LrecyclerViewlayoutManager);
        mtdMostrarDatos();
        return v;
    }

    public void mtdMostrarDatos(){
        LjsonArrayRequest = new JsonArrayRequest(Conexion.ELECTRODOMESTICOS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray Lresponse) {
                        mtdObtenerDatos(Lresponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Lerror) {
                        Toast.makeText(getActivity(), "Ah ocurrido algún problema al momento de la conexión", Toast.LENGTH_SHORT).show();
                    }
                });
        LrequestQueue = Volley.newRequestQueue(getActivity());
        LrequestQueue.add(LjsonArrayRequest);
    }
    public void mtdObtenerDatos(JSONArray array){
        for(int Linti = 0; Linti<array.length(); Linti++) {
            Electrodomestico electrodomestico = new Electrodomestico();
            JSONObject Ljson = null;
            try {
                Ljson = array.getJSONObject(Linti);
                electrodomestico.setId(Ljson.getString(Conexion.TAG_ID));
                electrodomestico.setNombre(Ljson.getString(Conexion.TAG_NOMBRE));
              /*  electrodomestico.setPotencia(Ljson.getString(Conexion.TAG_IMG));
                electrodomestico.setUbicacion(Ljson.getString(Conexion.TAG_IMG));*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
            electrodomesticos.add(electrodomestico);
        }
        LrecyclerViewadapter = new AdaptadorElectrodomestico(electrodomesticos, getActivity(), this);
        LrecyclerView.setAdapter(LrecyclerViewadapter);
    }

    @Override
    public void onItemClick(AdaptadorElectrodomestico.ViewHolder holder, int Lintposicion) {

    }
}
