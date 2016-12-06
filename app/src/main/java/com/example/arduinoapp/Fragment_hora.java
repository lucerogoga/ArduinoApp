package com.example.arduinoapp;

import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by usuario on 23/11/2016.
 */

public class Fragment_hora extends Fragment implements View.OnClickListener {

    int hora=0, minuto=0 , segundo=0 ;
    Thread iniReloj = null;
    Runnable r;
    String sec, min, hor;
    boolean isUpdate = false;
    String curTime;
    private String extra;
    private static final String EXTRA_ID = "IDMETA";
    /*
    Controles
    */
    EditText hora_prendido;
    EditText hora_apagado;
    TextView estado;
    TextView horaactual;
    private Button buttonAdd;
    private Button buttonOn;
    private Button buttonOff;
   /*
   Obtener Duracion
   */
    String fechaprendido,fechaapagado;
    String date = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());

    public void FechaPrendido(){
    fechaprendido=(date);
    System.out.println(fechaprendido);
}
    public void FechaApagado(){
        fechaapagado=(date);
        System.out.println(fechaapagado);
    }

    public static Fragment_hora createInstance(String id) {
        Fragment_hora detailFragment = new Fragment_hora();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ID, id);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Obtención de instancias controles
        View v = inflater.inflate(R.layout.fragment_horas, container, false);
     //   hora_prendido = (EditText) v.findViewById(R.id.hora_prendido);
      //  hora_apagado = (EditText) v.findViewById(R.id.hora_apagado);
        buttonAdd = (Button) v.findViewById(R.id.buttonAdd);
        buttonOn = (Button) v.findViewById(R.id.buttonON);
        buttonOff = (Button) v.findViewById(R.id.buttonOFF);
        horaactual = (TextView)v.findViewById(R.id.hora_actual);
        estado =(TextView) v.findViewById(R.id.estado_elec);
        extra = getArguments().getString(EXTRA_ID);
        r=new RefreshClock();
        iniReloj= new Thread(r);
        iniReloj.start();

        buttonOff.setOnClickListener(this);
        buttonOn.setOnClickListener(this);

        return v;
    }

    private void initClock() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                try{
                    if(isUpdate){
                        settingNewClock();
                    } else {
                        updateTime();
                    }
                    curTime =-5+ hora + min + minuto + sec + segundo;
                    horaactual.setText(curTime);
                }catch (Exception e) {}
            }
        });
    }

    class RefreshClock implements Runnable{
        // @Override
        @SuppressWarnings("unused")
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    initClock();
                    obtenerEstados();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

    private void updateTime(){
        Calendar c =new GregorianCalendar();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minuto = c.get(Calendar.MINUTE);
        segundo = c.get(Calendar.SECOND);
        setZeroClock();
    }

    private void setZeroClock(){
        if(hora >=0 & hora <=9){
            hor = "0";
        }else{
            hor = "";
        }
        if(minuto >=0 & minuto <=9){
            min = ":0";
        }else{
            min = ":";
        }
        if(segundo >=0 & segundo <=9){
            sec = ":0";
        }else{
            sec = ":";
        }
    }

    private void settingNewClock(){
        segundo +=1;
        setZeroClock();
        if(segundo >=0 & segundo <=59){
        }else {
            segundo = 0;
            minuto +=1;
        }
        if(minuto >=0 & minuto <=59){

        }else{
            minuto = 0;
            hora +=1;
        }
        if(hora >= 0 & hora <= 24){
        }else{
            hora = 0;
        }
    }

    private void obtenerEstados() {
        class obtenerEstados extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                MostrarEstado(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Conexion.ENVIARESTADO,extra);
                return s;
            }
        }
        obtenerEstados ge = new obtenerEstados();
        ge.execute();
    }


    private void MostrarEstado(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Conexion.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String valor = c.getString(Conexion.TAG_ESTADO);
            // estado.setText(valor);
            if(valor.equals("1")){
                estado.setText("prendido");
                buttonOn.setBackgroundColor(Color.GRAY);
                buttonOff.setBackgroundColor(Color.RED);
                buttonOn.setEnabled(false);
                buttonOff.setEnabled(true);
            }else if(valor.equals("0"))
            {
                estado.setText("apagado");
                buttonOn.setEnabled(true);
                buttonOn.setBackgroundColor(Color.GREEN);
                buttonOff.setBackgroundColor(Color.GRAY);
                buttonOff.setEnabled(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        if (view == buttonAdd) {
            Thread iniSensor = null;
            Runnable r;
            r = new Fragment_hora.RefreshEstado();
            iniSensor = new Thread(r);
            iniSensor.start();
        }
         if (view == buttonOn) {
                prenderfoco();
            }
        if (view == buttonOff) {
                apagarfoco();
            }

    }

    class RefreshEstado implements Runnable{
        // @Override
        @SuppressWarnings("unused")
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                 //   comparar();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

   /* private void comparar() {
        String horaprendido = hora_prendido.getText().toString().trim();
        String horaapagado = hora_apagado.getText().toString().trim();
        String hora = horaactual.getText().toString().trim();
        double temperaturaint=Integer.parseInt(hora);
        Double temperaturaonint=Integer.parseInt(horaprendido);
        int temperaturaoffint=Integer.parseInt(horaapagado);
        boolean apagado = true;
        String _estado ="";
        if (temperaturaint > temperaturaonint) {
            _estado = "1";
        } else if (temperaturaint < temperaturaoffint) {
            //Comando para apagar
            _estado = "0";
        }
        if (apagado && _estado == "1") {
            EnviarEstado(_estado);
            apagado = false;
            System.out.println("Insertar en la BD ENCENDIDO");
         //   FechaPrendido();
            //guardar info en la BD
        }
        if (!apagado && _estado == "0") {
            EnviarEstado(_estado);
            apagado = true;
            //guardar info en la BD
            System.out.println("ACTUALIZAR en la BD APAGADO");
         //   FechaApagado();
         //   llenartablaelectrodomestico();
        }
    }
*/
    private void prenderfoco() {
        String _estado = "1";
        EnviarEstado(_estado);
        System.out.println("Insertar en la BD ENCENDIDO");

    }
    private void apagarfoco(){
        String _estado = "0";
        EnviarEstado(_estado);
        System.out.println("Insertar en la BD ENCENDIDO");

    }

    private void EnviarEstado(final String estado)
    {
        class enviarDatos extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Conexion.RECIBIRESTADO,"?id_obj="+extra+"&estado_obj="+ estado);
                return s;
            }
        }
        enviarDatos ge = new enviarDatos();
        ge.execute();
    }

}
