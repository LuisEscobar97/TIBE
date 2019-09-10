package com.example.sitol.tibe.pirncipales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sitol.tibe.R;
import com.example.sitol.tibe.firebase.FirebaseController;
import com.example.sitol.tibe.objetos.Guia;
import com.example.sitol.tibe.objetos.Turista;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Registro extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    EditText nombre,apeP,apeM,telfono, correo, password;
    String no,tel, app,apm,corr,contr;
    CalendarView fecha;
    Button registrar;
    String fechaN="";
   public static String id="";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Turista t;
    Boolean b=false;
    static Turista GUIA= new Turista();
    FirebaseController firebaseController= new FirebaseController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
       inicializarFirebase();

        nombre=findViewById(R.id.reNombre);
        apeP=findViewById(R.id.reApP);
        apeM=findViewById(R.id.reApM);
        telfono=findViewById(R.id.reTel);
        correo=findViewById(R.id.reEmail);
        password=findViewById(R.id.reCon);
        fecha=findViewById(R.id.reCalendar);

        registrar=findViewById(R.id.reAceptar);

        fecha.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView,int year, int month, int dayOfMonth) {
        fechaN=dayOfMonth+"/"+month+"/"+year;
    }
});
registrar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (validar()){
            extraerDatos();
        }
    }
});
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    private void extraerDatos() {


        no=nombre.getText().toString();
        app=apeP.getText().toString();
        apm=apeM.getText().toString();
        corr=correo.getText().toString();
        contr=password.getText().toString();
        tel=telfono.getText().toString();
        crearUsuario(corr,contr);


    }

    private boolean validar() {
        if (!nombre.getText().toString().trim().equals("")){
            if (!apeM.getText().toString().trim().equals("")){
                if (!apeP.getText().toString().trim().equals("")){
                    if (!telfono.getText().toString().trim().equals("")){
                        if (!correo.getText().toString().trim().equals("")){
                            if (!password.getText().toString().trim().equals("")){
                                if (!fechaN.equals("")){

                                    return true;

                                }else{
                                    Toast.makeText(getApplicationContext(),"Seleccione otra fecha por favor",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                password.setError("requerido");
                            }

                        }else{
                            correo.setError("requerido");
                        }

                    }else{
                        telfono.setError("requerido");
                    }
                }else{
                    apeP.setError("requerido");
                }
            }else{
                apeM.setError("requerido");
            }
        }else {
            nombre.setError("requerido");
        }

        return false;
    }

    public void crearUsuario(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("firebase", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            id=user.getUid();
                            b=true;
                            t=new Turista(id,no,app,apm,corr,contr,fechaN,tel,"0");

                            databaseReference.child("turista").child(id).setValue(t);

                            Toast.makeText(getApplicationContext(), "Usuario Agregado",
                                    Toast.LENGTH_SHORT).show();
                            limpiar();
                            // Read from the database
                            databaseReference.child("turista").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    GUIA= dataSnapshot.getValue(Turista.class);
                                    Intent menuPrincipal=new Intent(getApplicationContext(), NavegadorPrincipal.class);
                                    startActivity(menuPrincipal);
                                    NavegadorPrincipal.turista= GUIA;

                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w("fire", "Failed to read value.", error.toException());
                                }
                            });
                            //updateUI(user);
                        } else {
                            b=false;
                            // If sign in fails, display a message to the user.
                            Log.w("firebase", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Ocurrio un error pruebe con otro correo",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });


    }
    public void insertarTusita(Turista t){
        db.collection("turista").add(t).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Agregado hubo un fallo al agergar dato", Toast.LENGTH_LONG).show();

            }
        });
    }

    public  void limpiar(){
        nombre.setText("");
        apeP.setText("");
        apeM.setText("");
        correo.setText("");
        password.setText("");
        telfono.setText("");

    }
    public void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

    }
}
