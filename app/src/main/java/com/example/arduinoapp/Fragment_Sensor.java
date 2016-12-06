package com.example.arduinoapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
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

import static android.content.Context.NOTIFICATION_SERVICE;

public class Fragment_Sensor extends Fragment implements  View.OnClickListener{

    public static final int NOTIFICACION_ID=1;



    private Context mContext;

    String fechaprendido,fechaapagado;
    private String extra;
    private String id_obj,estado_obj;
    private static final String EXTRA_ID = "IDMETA";
    int valor_estado;
    /*
    Controles
    */
    EditText temp_prendido;
    EditText temp_apagado;
    TextView tempsensor;
    TextView estado;
    private Button buttonAdd;
    private Button buttonOn;
    private Button buttonOff;/*
   Obtener Duracion
   */
    String date = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
    public void FechaPrendido(){
        fechaprendido=(date);
        System.out.println(fechaprendido);
    }
    public void FechaApagado(){
        fechaapagado=(date);
        System.out.println(fechaapagado);
    }


    public Fragment_Sensor() {
    }

    public static Fragment_Sensor createInstance(String id) {
        Fragment_Sensor detailFragment = new Fragment_Sensor();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ID, id);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_sensor, container, false);
        // Obtención de instancias controles
        temp_prendido = (EditText) v.findViewById(R.id.temp_prendido);
        temp_apagado = (EditText) v.findViewById(R.id.temp_apagado);
        buttonAdd = (Button) v.findViewById(R.id.buttonAdd);
        buttonOn = (Button) v.findViewById(R.id.buttonON);
        buttonOff = (Button) v.findViewById(R.id.buttonOFF);
        tempsensor = (TextView) v.findViewById(R.id.temp_sensor);
        estado =(TextView) v.findViewById(R.id.estado_elec);
        extra = getArguments().getString(EXTRA_ID);
        Thread iniSensor = null;
        Runnable r;
        r=new Fragment_Sensor.RefreshTemperatura();
        iniSensor= new Thread(r);
        iniSensor.start();

        buttonAdd.setOnClickListener(this);
        buttonOff.setOnClickListener(this);
        buttonOn.setOnClickListener(this);





        return v;
    }



    class RefreshTemperatura implements Runnable{
        // @Override
        @SuppressWarnings("unused")
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    obtenerTemperaturas();
                    obtenerEstados();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

    private void obtenerTemperaturas() {
        class obtenerTemperaturas extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                MostrarTemperaturas(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Conexion.SENSOR,extra);
               return s;
            }
        }
        obtenerTemperaturas ge = new obtenerTemperaturas();
        ge.execute();
    }

    private void MostrarTemperaturas(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Conexion.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String valor = c.getString(Conexion.TAG_VALOR);
            tempsensor.setText(valor);

        } catch (JSONException e) {

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


            String valorestado=estado.getText().toString().trim();
            if(valorestado.equals("prendido")&& valor.equals("1")){

            }else if (valorestado.equals("apagado")&& valor.equals("0")){

            }else if (valorestado.equals("prendido")&& valor.equals("0")){
                Notificacionapagado();
            }else if (valorestado.equals("apagado")&& valor.equals("1")){
                Notificacionprendido();
            }
            if(valor.equals("1")){
              estado.setText("prendido");
                buttonOn.setBackgroundColor(Color.GRAY);
                buttonOff.setBackgroundColor(Color.RED);
                buttonOn.setEnabled(false);
                buttonOff.setEnabled(true);
           //     Notificacionprendido();
          }else if(valor.equals("0"))
         {
            estado.setText("apagado");
             buttonOn.setEnabled(true);
             buttonOn.setBackgroundColor(Color.GREEN);
             buttonOff.setBackgroundColor(Color.GRAY);
             buttonOff.setEnabled(false);

           //  Notificacionapagado();
         }

        } catch (JSONException e) {

        }
    }


    @Override
    public void onClick(View view) {
        if (view == buttonAdd) {
            Thread iniSensor = null;
            Runnable r;
            r=new Fragment_Sensor.RefreshEstado();
            iniSensor= new Thread(r);
            iniSensor.start();
        }
        if (view == buttonOn) {
           prenderventilador();
        }
        if (view == buttonOff) {
            apagarventilador();
            temp_prendido.setText("");
            temp_apagado.setText("");

        }
    }

    class RefreshEstado implements Runnable{
        // @Override
        @SuppressWarnings("unused")
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    comparar();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

            private void comparar() {
                String tempprendido = temp_prendido.getText().toString().trim();
                String tempapagado = temp_apagado.getText().toString().trim();
                String temper = tempsensor.getText().toString().trim();
                double temperaturaint=Double.parseDouble(temper);
                double temperaturaonint=Double.parseDouble(tempprendido);
                double temperaturaoffint=Double.parseDouble(tempapagado);
                String _estado ="";
                if (temperaturaint > temperaturaonint) {
                    _estado = "1";
                    EnviarEstado(_estado);
                } else if (temperaturaint < temperaturaoffint) {
                    //Comando para apagar
                    _estado = "0";
                    EnviarEstado(_estado);
                }
            }

   private void prenderventilador() {
           String _estado = "1";
           EnviarEstado(_estado);
          }

    private void apagarventilador(){
        String _estado = "0";
        EnviarEstado(_estado);

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
                String s = rh.sendGetRequestParam(Conexion.RECIBIRESTADO,"?id_obj="+extra+"&estado_obj="+estado);
                return s;
            }
        }
        enviarDatos ge = new enviarDatos();
        ge.execute();
    }

    private void llenartablaelectrodomestico() {

    }

    public  void Notificacionprendido(){



        //Construccion de la accion del intent implicito
       Intent intent= new Intent(getActivity(), Fragment_Sensor.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(getActivity(),0,intent,0);

        //Construccion de la notificacion;
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getActivity());
        builder.setSmallIcon(R.drawable.ic_stat_notification);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.n_icon3));
        builder.setContentTitle("PRENDIDO");
        builder.setContentText("ventilador prendido!");
        builder.setSubText("Toca para ver la app.");

        //Enviar la notificacion
        NotificationManager notificationManager= (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICACION_ID,builder.build());


    }


    public  void Notificacionapagado(){

     //   notificationUtils.playNotificationSound();

        //Construccion de la accion del intent implicito
        Intent intent= new Intent(getActivity(), Fragment_Sensor.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(getActivity(),0,intent,0);
        //Construccion de la notificacion;
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getActivity());
        builder.setSmallIcon(R.drawable.ic_stat_notification);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.n_icon3));
        builder.setContentTitle("APAGADO");
        builder.setContentText("ventilador apagado!");
        builder.setSubText("Toca para ver la app.");

        //Enviar la notificacion
        NotificationManager notificationManager= (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICACION_ID,builder.build());
        playNotificationSound();



    }

    // Reproducir el sonido de la notificaciÃ³n
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
