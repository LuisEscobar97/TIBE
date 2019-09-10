package com.example.sitol.tibe.turistas_citas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sitol.tibe.R;
import com.example.sitol.tibe.adaptadorLista.InfoGuias;
import com.example.sitol.tibe.objetos.ObjCita;
import com.example.sitol.tibe.turistas_AgendarCitas.Maps;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class InfoCitas extends AppCompatActivity {
public static ObjCita cita;
CardView guia,citaCard,cancelarCard;
TextView nombre,fecha, hora, estado;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView imageView;
Button ubicacion, cancelar;

    ProgressDialog pDialog;
    private StorageReference mStorageRef;
    StorageReference referenciaDirectorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_citas);
        inicializarFirebase();
        guia=findViewById(R.id.infocitasGuias);
        citaCard=findViewById(R.id.infoCitasCItas);
        cancelarCard=findViewById(R.id.infoCitasCancelar);

        nombre=guia.findViewById(R.id.nomCItas);

        fecha=citaCard.findViewById(R.id.fechaCitas);
        hora=citaCard.findViewById(R.id.horaCita);
        estado=citaCard.findViewById(R.id.estadoCita);
        ubicacion=citaCard.findViewById(R.id.ubicacionCita);
        imageView=guia.findViewById(R.id.fotoPerfilCitas);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        referenciaDirectorio= mStorageRef.child("images");
        cancelar=cancelarCard.findViewById(R.id.cancelraCitas);
        llenar();
        bajarFoto();

        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Maps.agendarCita=false;
                Maps.ubicacionCita=true;
                Maps.latitud=cita.getLatitud();
                Maps.longitud=cita.getLongitud();
                Maps.cita=cita;
                Intent i= new Intent(getApplicationContext(),Maps.class);
                startActivity(i);

            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cita.setEstado("3");
                databaseReference.child("CitasCanceladas").child(cita.getId_cita()).setValue(cita);

                databaseReference.child("Citas").child(cita.getId_cita()).removeValue();

                Snackbar.make(findViewById(android.R.id.content), "Cita Cancelada", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                finish();
            }
        });
    }

    private void llenar() {

        nombre.setText(cita.getNombreGuia()+" "+cita.getApePatGuia()+" "+cita.getApeMateGuia());
        fecha.setText(cita.getFecha_reser());
        hora.setText(cita.getHora());

        if (cita.getEstado().equals("1")){
            estado.setText("Pendiente");
        }else  if (cita.getEstado().equals("2")){
            estado.setText("Acpetada");
        }else   if (cita.getEstado().equals("3")){
            estado.setText("Cancelada");
            cancelar.setVisibility(View.INVISIBLE);
            cancelarCard.setVisibility(View.INVISIBLE);
        }
    }
    public void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

    }
    public void bajarFoto(){
        pDialog = new ProgressDialog(InfoCitas.this);
        pDialog.setMessage("Cargando");
        pDialog.setCancelable(false); // para  no cancealar el progress Dialog
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }


        StorageReference referenciaImagen =referenciaDirectorio.child(cita.getId_guia());
        final File finalLocalFile = localFile;
        referenciaImagen.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        Picasso.with(InfoCitas.this).load(finalLocalFile).into(imageView);
                        pDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "ocurrio un error al bajar la imagen", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
    }
}
