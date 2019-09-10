package com.example.sitol.tibe.turistas_AgendarCitas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.NonNull;

import com.example.sitol.tibe.Detalles;
import com.example.sitol.tibe.R;
import com.example.sitol.tibe.ServiceHandler;
import com.example.sitol.tibe.adaptadorLista.InfoGuias;
import com.example.sitol.tibe.objetos.Guia;
import com.example.sitol.tibe.objetos.ObjCita;
import com.example.sitol.tibe.pirncipales.NavegadorPrincipal;
import com.example.sitol.tibe.turistas_citas.InfoCitas;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class Maps extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener,Response.Listener<JSONObject>, Response.ErrorListener {
    private Button btnAgendar;
    private GoogleMap mMap;
    public static Double latitud;
    public static Double longitud;
    public static ObjCita cita;
    RequestQueue rq;
    JsonRequest jrq;
    public static Guia guia;
    int citaN;
    public static Boolean agendarCita=false;
    public static Boolean ubicacionCita=false;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnAgendar=findViewById(R.id.btnAgendar2);
        btnAgendar.setVisibility(View.INVISIBLE);
        btnAgendar.setOnClickListener(this);
        inicializarFirebase();
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            final LatLng[] sydney = {null};
            mMap = googleMap;
            mMap.setMaxZoomPreference(15);
            mMap.setMinZoomPreference(10);
            NavegadorPrincipal.locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    sydney[0] = new LatLng(location.getLatitude(),location.getLongitude());

// este metodo me perimte meter la latitu y la logintud juto con los nombres de los estacionbamiento en el map
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(sydney[0])
                            .zoom(14)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getParent(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    NavegadorPrincipal.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, NavegadorPrincipal.locationListener);
                } else {
                    NavegadorPrincipal.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, NavegadorPrincipal.locationListener);
                    Location ultimaPosicion = NavegadorPrincipal.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                     sydney[0] = new LatLng(ultimaPosicion.getLatitude(), ultimaPosicion.getLongitude());
                    mMap.clear();
// este metodo me perimte meter la latitu y la logintud juto con los nombres de los estacionbamiento en el map
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(sydney[0])
                            .zoom(14)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    //mMap.addMarker(new MarkerOptions().position(sydney).title("Your Position").flat(true));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }
                if (agendarCita) {
                    mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng latLng) {
                            mMap.clear();
                            LatLng puntoEncuentro = new LatLng(latLng.latitude, latLng.longitude);
                            mMap.addMarker(new MarkerOptions().position(puntoEncuentro).title("Punto de encuentro"));
                            latitud = latLng.latitude;
                            longitud = latLng.longitude;
                            btnAgendar.setVisibility(View.VISIBLE);

                        }
                    });
                }else if (ubicacionCita){

                    mMap.addMarker(new MarkerOptions().position(sydney[0]).title("Your Position").flat(true));

                    LatLng punto= new LatLng(latitud,longitud);

                    mMap.addMarker(new MarkerOptions().position(punto).title("Punto de ecuentro").flat(true));
                    btnAgendar.setVisibility(View.VISIBLE);
                    btnAgendar.setText("REGRESAR");
            }

            }
        }catch (NullPointerException e){

            Toast.makeText(this,"gps no disponible", Toast.LENGTH_LONG).show();
        }
        /*
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
        }

    @Override
    public void onClick(View v) {
        if(btnAgendar.getText().equals("REGRESAR")){
            InfoCitas.cita=cita;
            Intent i= new Intent(getApplicationContext(), InfoCitas.class);
            startActivity(i);
        }else{
            //aqui va ameter los datos para agendar la cita
            ObjCita cita= new ObjCita(UUID.randomUUID().toString(),AgendarCitas.fecha_reser,latitud,longitud,InfoGuias.guia.getNombre(),InfoGuias.guia.getApPat(),InfoGuias.guia.getApMat(),NavegadorPrincipal.turista.getNombre(),NavegadorPrincipal.turista.getApellidoPaterno(),NavegadorPrincipal.turista.getApellidoMaterno(),InfoGuias.guia.getIdG(),NavegadorPrincipal.turista.getIdTurista(),"1",AgendarCitas.horaR);
            InfoGuias.guia=guia;
            crearCita(cita);
            Intent i= new Intent(getApplicationContext(), InfoGuias.class);
            startActivity(i);
            finish();
        }




    }

    private void crearCita(ObjCita cita) {

        databaseReference.child("Citas").child(cita.getId_cita()).setValue(cita);
        Toast.makeText(this, "citaAgregada", Toast.LENGTH_LONG).show();

    }


    public void RegistroCita (){

        String url ="http://192.168.0.5/TIBE/citaNueva.php?latitud="+latitud+"&"+"longitud="+longitud+"&"+"id_tur="+1+"&"+"id_guia="+Detalles.idG+"&fecha="+ AgendarCitas.fecha_reser +" "+ AgendarCitas.horaR;
        url=url.replace(" ","%20");

        Log.d("sql",url);

        jrq = new JsonObjectRequest(Request.Method.GET, url, null,  this, this);
        rq.add(jrq);


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "No se pudo registrar el  usuario", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getApplicationContext(), "Se registro correctamente", Toast.LENGTH_LONG).show();


    }


    public void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if (grantResults.length>0&& grantResults[0]== PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                        NavegadorPrincipal.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, NavegadorPrincipal.locationListener);
                    }
                }
            }
        }
    }
}
