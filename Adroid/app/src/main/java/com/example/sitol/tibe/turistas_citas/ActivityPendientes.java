package com.example.sitol.tibe.turistas_citas;

import android.os.AsyncTask;

import com.example.sitol.tibe.R;
import com.example.sitol.tibe.ServiceHandler;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityPendientes extends AppCompatActivity {
    TextView nombre;
    TextView hora;
    TextView latitid;
    TextView longitud;

    Button cancelar;
    ImageView fotoPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendientes);

        nombre= findViewById(R.id.nombreP);
        hora= findViewById(R.id.horaP);
        latitid=findViewById(R.id.latitudP);
        longitud=findViewById(R.id.longitudP);
        cancelar= findViewById(R.id.cancelarP);




    }

    }

