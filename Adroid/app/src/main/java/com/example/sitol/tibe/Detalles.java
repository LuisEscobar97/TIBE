package com.example.sitol.tibe;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sitol.tibe.turistas_AgendarCitas.AgendarCitas;

public class Detalles extends AppCompatActivity implements View.OnClickListener{
    TextView nombre;
    TextView acerca;
    TextView idiomas;
    TextView actividades;
    TextView descripcion;
    TextView tarifa;
    TextView zona;
    Button agendar;
    ImageView fotoPerfil;

   public static int idG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        idG=0;
        nombre= findViewById(R.id.nombre);
        acerca= findViewById(R.id.acerca);
        idiomas=findViewById(R.id.idiomas);
        actividades= findViewById(R.id.actividades);
        descripcion= findViewById(R.id.descripcion);
        tarifa=findViewById(R.id.tarifa);
        zona=findViewById(R.id.zona);
        agendar=findViewById(R.id.btnAgendar);
        fotoPerfil= findViewById(R.id.fotoPerfil);

        nombre.setText(ListaGuias.dat.get(0)+" "+ ListaGuias.dat.get(1) +" "+ListaGuias.dat.get(2));
        acerca.setText(ListaGuias.dat.get(3));
        idiomas.setText("Español");
        actividades.setText("Comida, " + "Museos, " +"Entretenimiento");
        descripcion.setText(ListaGuias.dat.get(4));
        tarifa.setText(ListaGuias.dat.get(5)+"/h");
        zona.setText(ListaGuias.dat.get(6));
        idG=Integer.parseInt(ListaGuias.dat.get(7));
        agendar.setOnClickListener(this);
        fotoPerfil.setImageResource(Integer.parseInt(ListaGuias.dat.get(8)));
        /*
        tarifaBase=(TextView)findViewById(R.id.pb);
        lugar=(TextView)findViewById(R.id.lu);
        tiempo= (TextView)findViewById(R.id.t);
        tarifaFrac= (TextView)findViewById(R.id.pp);
        fecha= (TextView)findViewById(R.id.fe);
        total=(TextView)findViewById(R.id.to);
        sbTotal=(TextView)findViewById(R.id.st);
        descuento= (TextView)findViewById(R.id.des);
        pago=(TextView)findViewById(R.id.pa);

        tarifaBase.setText("$"+ ListaGuias.dat.get(1));
        tiempo.setText( ListaGuias.dat.get(2));
        sbTotal.setText("$"+ ListaGuias.dat.get(3));
        descuento.setText( ListaGuias.dat.get(4));
        total.setText("$"+ ListaGuias.dat.get(5));
        pago.setText("$"+ ListaGuias.dat.get(6));
        lugar.setText(ListaGuias.dat.get(7));
        fecha.setText(ListaGuias.dat.get(8));
        */
    }

    @Override
    public void onClick(View v) {
        Intent intent= new Intent(getApplicationContext(), AgendarCitas.class);
        startActivity(intent);
    }
}
