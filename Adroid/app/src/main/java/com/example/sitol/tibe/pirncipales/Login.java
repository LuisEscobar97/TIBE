package com.example.sitol.tibe.pirncipales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sitol.tibe.R;
import com.example.sitol.tibe.objetos.Turista;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static com.example.sitol.tibe.pirncipales.NavegadorPrincipal.turista;

public class Login extends AppCompatActivity {

    EditText txtEmail, txtPass;
    Button btnIniciarSesion,btonRegsitro;
    private FirebaseAuth mAuth;
    FirebaseFirestore db1;
    List<Turista> l;
    ProgressDialog pDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    static Turista GUIA= new Turista();
    androidx.appcompat.app.AlertDialog.Builder builder;
    AlertDialog alert;
   public static Boolean bandera=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail= findViewById(R.id.txtEmail);
        txtPass= findViewById(R.id.txtPass);
        btnIniciarSesion= findViewById(R.id.btnIniciarSesion);

        inicializarFirebase();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db1=FirebaseFirestore.getInstance();
        btonRegsitro=findViewById(R.id.buttonIrRegistro);
        btonRegsitro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(),Registro.class);
                startActivity(i);
            }
        });
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             iniciarSesion(txtEmail.getText().toString(),txtPass.getText().toString());
             txtEmail.setText("");
             txtPass.setText("");
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        /*
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){

            //tibe d

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

        }



    //    updateUI(currentUser);
    */
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
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("firebase", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void iniciarSesion (String email, String password){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Iniciando Sesión");
        pDialog.setCancelable(false); // para  no cancealar el progress Dialog
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
        if (bandera){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                final FirebaseUser user = mAuth.getCurrentUser();

                                Log.w("usuario",user.getUid());


                                // Read from the database
                                databaseReference.child("turista").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        GUIA= dataSnapshot.getValue(Turista.class);
                                        turista=GUIA;
                                        databaseReference.child("turista").child(turista.getIdTurista()).setValue(turista).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){

                                                    Intent menuPrincipal=new Intent(getApplicationContext(), NavegadorPrincipal.class);
                                                    startActivity(menuPrincipal);

                                                    finish();

                                                }
                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Failed to read value
                                        Log.w("fire", "Failed to read value.", error.toException());
                                    }
                                });

                                pDialog.dismiss();

                                // updateUI(user);

                            } else {
                                pDialog.dismiss();
                                // If sign in fails, display a message to the user.
                                Log.w("firebase", "signInWithEmail:failure", task.getException());
                                mensaje("No se pudo inicar sesion");

                                // updateUI(null);
                            }

                            // ...
                        }
                    });
        }else{
            bandera=true;
        }

    }


    public void mensaje(String s){

         builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage(s)
                .setTitle("Error")
                .setCancelable(false)
                .setNeutralButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        alert = builder.create();
        alert.show();



    }

    //#####################   VALIDACIONES
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length()>4 &&  !password.isEmpty();
    }
    //####################################


    private void inicializarFirebase() {
        //FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }


}
