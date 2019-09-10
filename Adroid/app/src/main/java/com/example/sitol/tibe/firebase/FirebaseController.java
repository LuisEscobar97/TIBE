package com.example.sitol.tibe.firebase;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sitol.tibe.objetos.Guia;
import com.example.sitol.tibe.objetos.ObjCita;
import com.example.sitol.tibe.objetos.Turista;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseController extends android.app.Application {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseFirestore db;
     Turista turista;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
    public void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
        db = FirebaseFirestore.getInstance();
    }

    public List<ObjCita>listarCitasRealTimePendientes(){

        final List<ObjCita> guia=new ArrayList<>();

        databaseReference.child("Cita").orderByChild("estado").equalTo("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                guia.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    ObjCita p = objSnaptshot.getValue(ObjCita.class);
                    guia.add(p);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return guia;
    }
    public List<ObjCita>listarCitasRealTimeAceptadas(){

        final List<ObjCita> guia=new ArrayList<>();

        databaseReference.child("Cita").orderByChild("estado").equalTo("2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                guia.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    ObjCita p = objSnaptshot.getValue(ObjCita.class);
                    guia.add(p);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return guia;
    }

    public List<ObjCita>listarCitasRealTimeCAnceladas(){

        final List<ObjCita> guia=new ArrayList<>();

        databaseReference.child("Cita").orderByChild("estado").equalTo("3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                guia.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    ObjCita p = objSnaptshot.getValue(ObjCita.class);
                    guia.add(p);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return guia;
    }
    public void insertarCitas(ObjCita cita){

        databaseReference.child("Cita").child(cita.getId_cita()).setValue(cita);


    }

    public void caneclarCitas(ObjCita cita){

        databaseReference.child("Cita").child(cita.getId_cita()).setValue(cita);


    }

    public Turista buscarTurista(String id) {
        Turista aux = null;

        db.collection("turista").whereEqualTo("uid", id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Turista> l = queryDocumentSnapshots.toObjects(Turista.class);
                for (int i = 0; i < l.size(); i++) {

                    turista = l.get(i);

                }

            }
        });
        aux=turista;
            return aux;
    }

    public void insertarTusita(Turista t){
        db.collection("turista").add(t).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Agregado"+documentReference.getId(), Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Agregado hubo un fallo al agergar dato", Toast.LENGTH_LONG).show();

            }
        });
    }
}
