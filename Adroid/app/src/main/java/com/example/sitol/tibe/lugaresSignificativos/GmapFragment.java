package com.example.sitol.tibe.lugaresSignificativos;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sitol.tibe.R;
import com.example.sitol.tibe.pirncipales.NavegadorPrincipal;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.util.ArrayList;

public class GmapFragment extends Fragment implements OnMapReadyCallback, Response.Listener<JSONArray>,Response.ErrorListener {
    private GoogleMap mMap;
     static Object listaEsta[]=null;
    static Object listaPar[]=null;
    static Object listaMus[]=null;
    static Object listaBar[]=null;
    ProgressDialog progressDialog;
    RequestQueue request;
    JsonArrayRequest jsonObjectRequest;
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gmaps, container, false);


    }




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment fragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            request= Volley.newRequestQueue(getContext());
        }

    }
    // este metodo era para caragar los datos pero nunca funciono como queise porque no sabia manerar manejra bien lo json
public void CargarDatos(){

        String url="http://192.168.0.16/BASEDATOS/ConsultaServicios.php";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                }

            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                }
            }
        }
        );
      // jsonObjectRequest= new JsonArrayRequest(Request.Method.GET,url,null,this,this);
       request.add(stringRequest);
}

    @Override
    public void onErrorResponse(VolleyError error) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toast.makeText(getContext(),"error"+error,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onResponse(JSONArray response) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toast.makeText(getContext(),"hola"+response,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;

            NavegadorPrincipal.locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.clear();
                            // aqui se mete el mototo para repintar los estacionamineto ya que cada vez que cambia la poscion del usuario va a estar limpiando el mapa y pintando su ubicacion actual asi que se vuelven a pintar
                            estacionamientos(mMap);
                            mMap.addMarker(new MarkerOptions().position(sydney).title("Tu Ubicación").flat(true));
                            parques(mMap);
                            museos(mMap);
                            bares(mMap);


                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    NavegadorPrincipal.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, NavegadorPrincipal.locationListener);
                } else {
                    NavegadorPrincipal.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, NavegadorPrincipal.locationListener);
                    Location ultimaPosicion = NavegadorPrincipal.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    LatLng sydney = new LatLng(ultimaPosicion.getLatitude(), ultimaPosicion.getLongitude());
                    mMap.clear();
// este metodo me perimte meter la latitu y la logintud juto con los nombres de los estacionbamiento en el mapa
                    estacionamientos(mMap);
                    parques(mMap);
                    museos(mMap);
                    bares(mMap);

                    CameraPosition cameraPosition= new CameraPosition.Builder()
                            .target(sydney)
                            .zoom(14)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    mMap.addMarker(new MarkerOptions().position(sydney).title("Your Position").flat(true));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }
            }

        }catch(Exception e){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Toast.makeText(getContext(),"No disponible",Toast.LENGTH_LONG).show();
            }
        }

    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public static void estacionamientos(GoogleMap mMap) {
         // en esta parte se meto valores por defecto de los estacionamimeto tendrias que bajar la latitud y la longitud de la base de datos y solo irlos agregando
        // lo agao creando un array list con la latitu, la longitud y el nombre de cada estaciomaniento una vez hecho eso, lo insertio en un arreglo de objetos dinamico con el cual es lo que me perite meter esa informacion en el mapa
        ArrayList<String> es1 = new ArrayList<>();
        ArrayList<String> es2 = new ArrayList<>();
        ArrayList<String> es3 = new ArrayList<>();
        ArrayList<String> es4 = new ArrayList<>();
        ArrayList<String> es5 = new ArrayList<>();

        es1.add("19.2823608");
        es1.add("-99.5030706");
        es1.add("Plazas Outlet ");

        es2.add("19.2742662");
        es2.add("-99.5761204");
        es2.add("Plaza Mia ");
//19.2739689,-99.5757497
        es3.add("19.2903221");
        es3.add("-99.6244606");
        es3.add("Galerias Toluca ");

        es4.add("19.2584464");
        es4.add("-99.6205986");
        es4.add("Galerias Metepec ");

        es5.add("19.2739689");
        es5.add("-99.5757497");
        es5.add("Square Plaza");

        listaEsta = NavegadorPrincipal.inserta(listaEsta, es1);
        listaEsta = NavegadorPrincipal.inserta(listaEsta, es2);
        listaEsta = NavegadorPrincipal.inserta(listaEsta, es3);
        listaEsta = NavegadorPrincipal.inserta(listaEsta, es4);
        listaEsta = NavegadorPrincipal.inserta(listaEsta, es5);


        for (int i = 0; i < listaEsta.length; i++) {
            //esto toma un archivo que no es xml del drawable normalnet imagnes png
            Drawable drawable= NavegadorPrincipal.context.getDrawable(R.drawable.comerciales);

            //con esto el archivo drawable que se selecciono arriba es para convertirlo en un bitmap
            Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();

            //se cera un objteo de tipo markeroption
            MarkerOptions markerOptions=new MarkerOptions();

            ArrayList<String> aux = (ArrayList<String>) listaEsta[i];
            Double lat = Double.parseDouble(aux.get(0));
            Double lon = Double.parseDouble(aux.get(1));
            LatLng sydney = new LatLng(lat, lon);

            //con esto se colocal la poisicion del marcador en base a un objeto LatLng
            markerOptions.position(sydney);
            //con estomse cambia el icono del markaro en este caso cun bitmap
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            //se coloca el titulo
            markerOptions.title(aux.get(2));
            // esto perite colocar ua breve descripcion en el marcador
            markerOptions.snippet("cetrocomercial");
            // y se agrega el markador al mapa
            mMap.addMarker(markerOptions);

        }

    }

    public static void parques(GoogleMap mMap) {
        // en esta parte se meto valores por defecto de los estacionamimeto tendrias que bajar la latitud y la longitud de la base de datos y solo irlos agregando
        // lo agao creando un array list con la latitu, la longitud y el nombre de cada estaciomaniento una vez hecho eso, lo insertio en un arreglo de objetos dinamico con el cual es lo que me perite meter esa informacion en el mapa
        ArrayList<String> es1 = new ArrayList<>();
        ArrayList<String> es2 = new ArrayList<>();
        ArrayList<String> es3 = new ArrayList<>();
        ArrayList<String> es4 = new ArrayList<>();
        ArrayList<String> es5 = new ArrayList<>();
//19.2493933,-99.590444
        es1.add("19.2493933");
        es1.add("-99.590444");
        es1.add("Parque Ambiental Bicentenario");
//19.2605754,-99.605028
        es2.add("19.2605754");
        es2.add("-99.605028");
        es2.add("Parque San José La Pilita");
//19.2605754,-99.605028
        es3.add("19.2605754");
        es3.add("-99.605028");
        es3.add("Parque Rancho San Lucas");
//19.2531317,-99.6197547
        es4.add("19.2531317");
        es4.add("-99.6197547");
        es4.add("Parque Municipal Metepec");



        listaPar = NavegadorPrincipal.inserta(listaPar, es1);
        listaPar = NavegadorPrincipal.inserta(listaPar, es2);
        listaPar = NavegadorPrincipal.inserta(listaPar, es3);
        listaPar = NavegadorPrincipal.inserta(listaPar, es4);



        for (int i = 0; i < listaPar.length; i++) {
            //esto toma un archivo que no es xml del drawable normalnet imagnes png

            Drawable drawable= NavegadorPrincipal.context.getDrawable(R.drawable.parques2);

            //con esto el archivo drawable que se selecciono arriba es para convertirlo en un bitmap
            Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();

            //se cera un objteo de tipo markeroption
            MarkerOptions markerOptions=new MarkerOptions();

            ArrayList<String> aux = (ArrayList<String>) listaPar[i];
            Double lat = Double.parseDouble(aux.get(0));
            Double lon = Double.parseDouble(aux.get(1));
            LatLng sydney = new LatLng(lat, lon);

            //con esto se colocal la poisicion del marcador en base a un objeto LatLng
            markerOptions.position(sydney);
            //con estomse cambia el icono del markaro en este caso cun bitmap
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            //se coloca el titulo
            markerOptions.title(aux.get(2));
            // esto perite colocar ua breve descripcion en el marcador
            markerOptions.snippet("parque recreativo");
            // y se agrega el markador al mapa
            mMap.addMarker(markerOptions);

        }

    }
    public static void museos(GoogleMap mMap) {
        // en esta parte se meto valores por defecto de los estacionamimeto tendrias que bajar la latitud y la longitud de la base de datos y solo irlos agregando
        // lo agao creando un array list con la latitu, la longitud y el nombre de cada estaciomaniento una vez hecho eso, lo insertio en un arreglo de objetos dinamico con el cual es lo que me perite meter esa informacion en el mapa
        ArrayList<String> es1 = new ArrayList<>();
        ArrayList<String> es2 = new ArrayList<>();
        ArrayList<String> es3 = new ArrayList<>();
        ArrayList<String> es4 = new ArrayList<>();
        ArrayList<String> es5 = new ArrayList<>();
//19.2475144,-99.6046892
        es1.add("19.2475144");
        es1.add("-99.6046892");
        es1.add("Casa de la Tierra");
//19.2501783,-99.6274137
        es2.add("19.2501783");
        es2.add("-99.6274137");
        es2.add("Museo Metepec de Arte Contemporáneo");
//19.2501783,-99.6274137,
        es3.add("19.2501783");
        es3.add("-99.6274137");
        es3.add("Museo del Barro");
//19.2501783,-99.6274137
        es4.add("19.2501783");
        es4.add("-99.6274137");
        es4.add("iglesia Del Calvario");


        listaMus = NavegadorPrincipal.inserta(listaMus, es1);
        listaMus = NavegadorPrincipal.inserta(listaMus, es2);
        listaMus = NavegadorPrincipal.inserta(listaMus, es3);
        listaMus = NavegadorPrincipal.inserta(listaMus, es4);



        for (int i = 0; i < listaMus.length; i++) {
            //esto toma un archivo que no es xml del drawable normalnet imagnes png
            Drawable drawable= NavegadorPrincipal.context.getDrawable(R.drawable.museo);

            //con esto el archivo drawable que se selecciono arriba es para convertirlo en un bitmap
            Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();

            //se cera un objteo de tipo markeroption
            MarkerOptions markerOptions=new MarkerOptions();

            ArrayList<String> aux = (ArrayList<String>) listaMus[i];
            Double lat = Double.parseDouble(aux.get(0));
            Double lon = Double.parseDouble(aux.get(1));
            LatLng sydney = new LatLng(lat, lon);

            //con esto se colocal la poisicion del marcador en base a un objeto LatLng
            markerOptions.position(sydney);
            //con estomse cambia el icono del markaro en este caso cun bitmap
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            //se coloca el titulo
            markerOptions.title(aux.get(2));
            // esto perite colocar ua breve descripcion en el marcador
            markerOptions.snippet("Museo, cultura");
            // y se agrega el markador al mapa
            mMap.addMarker(markerOptions);

        }

    }
    public static void bares(GoogleMap mMap) {
        // en esta parte se meto valores por defecto de los estacionamimeto tendrias que bajar la latitud y la longitud de la base de datos y solo irlos agregando
        // lo agao creando un array list con la latitu, la longitud y el nombre de cada estaciomaniento una vez hecho eso, lo insertio en un arreglo de objetos dinamico con el cual es lo que me perite meter esa informacion en el mapa
        ArrayList<String> es1 = new ArrayList<>();
        ArrayList<String> es2 = new ArrayList<>();
        ArrayList<String> es3 = new ArrayList<>();
        ArrayList<String> es4 = new ArrayList<>();
        ArrayList<String> es5 = new ArrayList<>();
//19.2803801,-99.6087885
        es1.add("19.2803801");
        es1.add("-99.6087885");
        es1.add("La Cantrina");
//19.2648242,-99.624238
        es2.add("19.2648242");
        es2.add("-99.624238");
        es2.add("Bora Bora");
//19.2546148,-99.6362543
        es3.add("19.2546148");
        es3.add("-99.6362543");
        es3.add("Jaz");
//19.2465116,-99.6280146
        es4.add("19.2465116");
        es4.add("-99.6280146");
        es4.add("Bar 2 de Abril");



        listaBar = NavegadorPrincipal.inserta(listaBar, es1);
        listaBar = NavegadorPrincipal.inserta(listaBar, es2);
        listaBar = NavegadorPrincipal.inserta(listaBar, es3);
        listaBar = NavegadorPrincipal.inserta(listaBar, es4);



        for (int i = 0; i < listaBar.length; i++) {
            //esto toma un archivo que no es xml del drawable normalnet imagnes png
            Drawable drawable= NavegadorPrincipal.context.getDrawable(R.drawable.bar);

            //con esto el archivo drawable que se selecciono arriba es para convertirlo en un bitmap
            Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();

            //se cera un objteo de tipo markeroption
            MarkerOptions markerOptions=new MarkerOptions();

            ArrayList<String> aux = (ArrayList<String>) listaBar[i];
            Double lat = Double.parseDouble(aux.get(0));
            Double lon = Double.parseDouble(aux.get(1));
            LatLng sydney = new LatLng(lat, lon);

            //con esto se colocal la poisicion del marcador en base a un objeto LatLng
            markerOptions.position(sydney);
            //con estomse cambia el icono del markaro en este caso cun bitmap
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            //se coloca el titulo
            markerOptions.title(aux.get(2));
            // esto perite colocar ua breve descripcion en el marcador
            markerOptions.snippet("Bares, restaurantes");
            // y se agrega el markador al mapa
            mMap.addMarker(markerOptions);

        }

    }

    public static Object[] inserta(Object[] arr, JSONArray obj) {
        Object arrn[] = null;
        if (arr == null) {
            arrn = new Object[1];
            arrn[0] = obj;
        } else {
            arrn = new Object[arr.length + 1];
            System.arraycopy(arr, 0, arrn, 0, arr.length);
            arrn[arr.length] = obj;
        }
        return arrn;

    }
}
