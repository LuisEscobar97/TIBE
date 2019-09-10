package com.example.sitol.tibe.pirncipales;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.sitol.tibe.lugaresSignificativos.GmapFragment;
import com.example.sitol.tibe.ListaGuias;
import com.example.sitol.tibe.R;
import com.example.sitol.tibe.objetos.Turista;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sitol.tibe.adaptadorLista.ListaGuiasRecicler;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NavegadorPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListaGuias.OnFragmentInteractionListener,ListaGuiasRecicler.OnFragmentInteractionListener {
    public static LocationManager locationManager;
    public static LocationListener locationListener;
    Toolbar toolbar;
   public static Context context;
    public static int opc=0;
    public static Turista turista;
    private FirebaseAuth mAuth;
    public static String id;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseFirestore db1;
    TextView nombre,co;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegador_principal);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nombre=(navigationView.getHeaderView(0)).findViewById(R.id.nombrelbl);
        nombre.setText(turista.getNombre()+" "+turista.getApellidoPaterno()+" "+turista.getApellidoMaterno());
        mAuth = FirebaseAuth.getInstance();
        co=(navigationView.getHeaderView(0)).findViewById(R.id.corr);
        co.setText(turista.getCorreo());

        inicializarFirebase();

        FragmentManager fm =getFragmentManager();
        androidx.fragment.app.Fragment fragment= null;
        Fragment frag= null;

        frag= new ListaGuiasRecicler();
        fm.beginTransaction().replace(R.id.contenedor,frag).commit();
        toolbar.setTitle("Guias");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        context=getApplicationContext();
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

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


        locationManager = (LocationManager) this.getSystemService(getApplicationContext().LOCATION_SERVICE);
        // Initialize Firebase Auth


        Toast.makeText(getApplicationContext(),turista.getNombre(),Toast.LENGTH_LONG).show();

    }

    private void extraerUsuario(String id) {
        Turista aux = null;

        db1.collection("turista")
                .whereEqualTo("idTurista", id+"").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<Turista> l = queryDocumentSnapshots.toObjects(Turista.class);
                    for (int i = 0; i < l.size(); i++) {

                        turista = l.get(i);

                    }

                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navegador_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm =getFragmentManager();
        androidx.fragment.app.Fragment fragment= null;
        Fragment frag= null;
        item.setChecked(false);
        item.setCheckable(false);
        if (id == R.id.nav_camera) {
            if (opc!=0){
                opc=0;
                frag= new ListaGuiasRecicler();
                fm.beginTransaction().replace(R.id.contenedor,frag).commit();
                toolbar.setTitle("Guias");

            }

        } else if (id == R.id.nav_gallery) {
    if(opc!=1){
        opc=1;
        frag= new GmapFragment();
        fm.beginTransaction().replace(R.id.contenedor,frag).commit();
        toolbar.setTitle("Lugares");

    }


        } else if (id == R.id.nav_slideshow) {
            if(opc!=3){
                frag= new ListaGuiasRecicler();
                fm.beginTransaction().replace(R.id.contenedor,frag).commit();
                toolbar.setTitle("Guias");
                opc=3;
            }


        } else if (id == R.id.nav_manage) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setMessage("Creditos"+"\n"+
                    "Jimenez Pizaña Luis Arturo"+"\n"+
                    "Escobar Valdes Luis Angel" +"\n"+
                    "Martinez Sanchez Fernando"+"\n"+
                    "Pérez Pérez Tomás")
                    .setTitle("Acerca de")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

        }else if (id == R.id.cerrar) {
            pDialog = new ProgressDialog(NavegadorPrincipal.this);
            pDialog.setMessage("Iniciando Sesión");
            pDialog.setCancelable(false); // para  no cancealar el progress Dialog
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
            turista.setSesion("0");
            mAuth.signOut();
            Intent i= new Intent(NavegadorPrincipal.this,Login.class);
            startActivity(i);
            finish();
            Login.GUIA= new Turista();
            pDialog.dismiss();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    public static Object[] inserta(Object[] arr, ArrayList<String> obj) {
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void inicializarFirebase() {
       // FirebaseApp.initializeApp(getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
       // FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
}
