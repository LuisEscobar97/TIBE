package com.example.sitol.tibe.pirncipales;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.sitol.tibe.R;
import com.example.sitol.tibe.objetos.Turista;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.sitol.tibe.pirncipales.NavegadorPrincipal.turista;

public class Splash extends AppCompatActivity {
    ProgressDialog pDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    static Turista GUIA= new Turista();
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        inicializarFirebase();
        mAuth = FirebaseAuth.getInstance();

    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            databaseReference.child("turista").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GUIA= dataSnapshot.getValue(Turista.class);

                    Intent menuPrincipal=new Intent(getApplicationContext(), NavegadorPrincipal.class);
                    startActivity(menuPrincipal);
                    turista=GUIA;
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("fire", "Failed to read value.", error.toException());
                }
            });

        }else{
            Intent menuPrincipal=new Intent(getApplicationContext(), Login.class);
            startActivity(menuPrincipal);
        }
    }
}
