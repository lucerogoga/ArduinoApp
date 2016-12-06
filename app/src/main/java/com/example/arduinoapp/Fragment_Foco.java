package com.example.arduinoapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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


public class Fragment_Foco extends Fragment implements AdaptadorElectrodomestico.EventClick {

    private List<Electrodomestico> focos;
    RecyclerView LrecyclerView;
    RecyclerView.LayoutManager LrecyclerViewlayoutManager;
    RecyclerView.Adapter LrecyclerViewadapter;
    JsonArrayRequest LjsonArrayRequest ;
    RequestQueue LrequestQueue ;

    public Fragment_Foco() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_electrodomesticos, container, false);
        LrecyclerView = (RecyclerView)v.findViewById(R.id.recycler_electrodomesticos);
        focos = new ArrayList<>();
        LrecyclerView.setHasFixedSize(true);
        LrecyclerViewlayoutManager = new LinearLayoutManager(getActivity());
        LrecyclerView.setLayoutManager(LrecyclerViewlayoutManager);
        mtdMostrarDatos();
        return v;
    }

    public void mtdMostrarDatos(){
        LjsonArrayRequest = new JsonArrayRequest(Conexion.FOCOS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray Lresponse) {
                        mtdObtenerDatos(Lresponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError Lerror) {
                        Toast.makeText(getActivity(), "ha ocurrido algún problema al momento de la conexión", Toast.LENGTH_SHORT).show();
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            focos.add(electrodomestico);
        }
        LrecyclerViewadapter = new AdaptadorElectrodomestico(focos, getActivity(), this);
        LrecyclerView.setAdapter(LrecyclerViewadapter);
    }

    @Override
    public void onItemClick(AdaptadorElectrodomestico.ViewHolder holder, int Lintposicion) {
        Fragment_hora sensor = new Fragment_hora();
        FragmentManager fragmentManager = getFragmentManager();

        String id = focos.get(Lintposicion).getId();

        fragmentManager.beginTransaction().replace(R.id.content_main, Fragment_hora.createInstance(id), "Fragment_hora").addToBackStack("tag").commit();

    }
}