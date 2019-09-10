package com.example.sitol.tibe.turistas_AgendarCitas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.sitol.tibe.Detalles;
import com.example.sitol.tibe.R;
import com.example.sitol.tibe.ServiceHandler;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AgendarCitas extends Activity implements View.OnClickListener {
    CalendarView calendar;
    int año, mes, dia;
    int aux;
    boolean cita=false;
    Button btnAgendar;
    Spinner hora;
    TextView horaL;
    ProgressDialog pDialog;
    Toolbar toolbar;
    static String horaR;
     static String fecha_reser;

     static String id_turista="";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_citas);
        inicializarFirebase();
        calendar= findViewById(R.id.calendar);
        btnAgendar= findViewById(R.id.btnAgendar);
        hora=findViewById(R.id.hora);
        horaL=findViewById(R.id.horaL);
        btnAgendar.setOnClickListener(this);
       calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
           @Override
           public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
            año=year;
            mes=month+1;

            dia=dayOfMonth;
            new GetCita().execute();
           }
       });

    }

    @Override
    public void onClick(View v) {
        if(hora.getSelectedItem().toString().equals("-----")){
            Snackbar.make(findViewById(android.R.id.content), "Seleccione una hora por favor", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }else{
            String mess="";
            String diaa="";
            fecha_reser="";
            if( mes <= 9 ){
                mess="0"+String.valueOf(mes);
            }else{
                mess=String.valueOf(mes);
            }
            if (dia<=9){
                diaa="0"+String.valueOf(dia);
            }else{
                diaa=String.valueOf(dia);
            }
            fecha_reser=año+"-"+mess+"-"+diaa;
            horaR=hora.getSelectedItem().toString();
            Maps.agendarCita=true;
            Maps.ubicacionCita=false;
            Intent intent= new Intent(getApplicationContext(), Maps.class);
            startActivity(intent);
        }



    }

    private class GetCita extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String mess="";
            String diaa="";
            fecha_reser="";
            if( mes <= 9 ){
                mess="0"+String.valueOf(mes);
            }else{
                mess=String.valueOf(mes);
            }
            if (dia<=9){
                diaa="0"+String.valueOf(dia);
            }else{
                diaa=String.valueOf(dia);
            }
            String url="http://192.168.216.156/Tibe/Turistas/consultarFechaCita.php?id="+ Detalles.idG+"&"+"fecha="+año+"-"+mess+"-"+diaa;
            url=url.replace(" ","%20");
            Log.d("sql",url);
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(url , ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray institu = jsonObj.getJSONArray("cita");

                        for (int i = 0; i < institu.length(); i++) {
                            JSONObject catObj = (JSONObject) institu.get(i);
                            aux=catObj.getInt("id_cita");
                            if (aux!=0){
                                Log.e("Response: ", "Hya cita");


                            }else{
                                Log.e("Response: ", "No hay cita" + json);


                            }

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "¿No ha recibido ningún dato desde el servidor!");

            }

            return null;
        }
        ///////////////////////////////////////////////////aqui le lo que se supone va crear mis historial///////////
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Toast.makeText(getApplicationContext(), "Se encontro al usuario", Toast.LENGTH_LONG).show();
            if (aux!=0){
                Toast.makeText(getApplicationContext(), "Dia no disponible", Toast.LENGTH_LONG).show();
                cita=false;
                activarPorCita(cita);
            }else{
                cita=true;
                Toast.makeText(getApplicationContext(), "Dia disponible", Toast.LENGTH_LONG).show();
                activarPorCita(cita);


            }

        }
    }
    public void activarPorCita(Boolean bendera){

        if (bendera){
            hora.setVisibility(View.VISIBLE);
            horaL.setVisibility(View.VISIBLE);
            btnAgendar.setVisibility(View.VISIBLE);
        }else{
            hora.setVisibility(View.INVISIBLE);
            horaL.setVisibility(View.INVISIBLE);
            btnAgendar.setVisibility(View.INVISIBLE);

        }
    }
    public void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

    }
}
