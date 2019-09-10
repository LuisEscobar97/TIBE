package com.example.sitol.tibe.adaptadorLista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sitol.tibe.turistas_AgendarCitas.AgendarCitas;
import com.example.sitol.tibe.objetos.Guia;
import com.example.sitol.tibe.R;
import com.example.sitol.tibe.turistas_AgendarCitas.Maps;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class InfoGuias extends AppCompatActivity {

    public static Guia guia;
    CardView cabecera,acerca,actvidades,tarifa;
    ImageView imageView;
    Button agendar;
    TextView acerc,nombre,actvidad,tarif,zona,desc,idioma;

    ProgressDialog pDialog;
    private StorageReference mStorageRef;
    StorageReference referenciaDirectorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_guias);

        cabecera=findViewById(R.id.cardNombre);
        acerca=findViewById(R.id.cardAcerca);
        actvidades=findViewById(R.id.cardActividades);
        tarifa=findViewById(R.id.cardTarifa);

        imageView=cabecera.findViewById(R.id.imageView2);
        nombre=cabecera.findViewById(R.id.CNombre);

        acerc=acerca.findViewById(R.id.cAcerca);
        idioma=acerca.findViewById(R.id.cIdiomas);

        actvidad=actvidades.findViewById(R.id.cActividades);
        desc=actvidades.findViewById(R.id.cDesc);

        tarif=tarifa.findViewById(R.id.cTarifa);
        zona=tarifa.findViewById(R.id.cZona);
        agendar=tarifa.findViewById(R.id.cAgendarButton);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        referenciaDirectorio= mStorageRef.child("images");


        imageView.setImageResource(R.mipmap.ic_launcher);
        nombre.setText(guia.getNombre());
        acerc.setText(guia.getAcerca());
        idioma.setText(guia.getIdioma());
        actvidad.setText(guia.getActividades());
        desc.setText(guia.getDesc());
        tarif.setText(guia.getTarifa()+"");
        zona.setText(guia.getZona());

        bajarFoto();

        agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Maps.agendarCita=true;
                Maps.ubicacionCita=false;
                Maps.guia=guia;
                Intent intent= new Intent(getApplicationContext(), AgendarCitas.class);
                startActivity(intent);
            }
        });

    }

    public void bajarFoto(){
        pDialog = new ProgressDialog(InfoGuias.this);
        pDialog.setMessage("Iniciando Sesión");
        pDialog.setCancelable(false); // para  no cancealar el progress Dialog
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }


        StorageReference referenciaImagen =referenciaDirectorio.child(guia.getIdG());
        final File finalLocalFile = localFile;
        referenciaImagen.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        Picasso.with(InfoGuias.this).load(finalLocalFile).into(imageView);
                        pDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "ocurrio un error al bajar la imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
