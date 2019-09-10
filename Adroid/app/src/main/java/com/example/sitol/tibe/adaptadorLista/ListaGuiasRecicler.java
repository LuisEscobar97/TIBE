package com.example.sitol.tibe.adaptadorLista;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.sitol.tibe.objetos.Guia;
import com.example.sitol.tibe.R;
import com.example.sitol.tibe.ServiceHandler;
import com.example.sitol.tibe.objetos.ObjCita;
import com.example.sitol.tibe.pirncipales.NavegadorPrincipal;
import com.example.sitol.tibe.turistas_citas.EstadosDeCitas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaGuiasRecicler.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaGuiasRecicler#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaGuiasRecicler extends  android.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Guia> movies;

    private RecyclerView mRecyclerView;
    // Puede ser declarado como 'RecyclerView.Adapter' o como nuetra clase adaptador 'MyAdapter'
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseFirestore db1;
    private OnFragmentInteractionListener mListener;

    static int n;
    public static int noGuia=0;
    Guia cat;
    ProgressDialog pDialog;
    public static ArrayList<Guia> instList= new ArrayList<Guia>();


    private String URL_LISTA_HISTORIAL ="http://192.168.0.14/Tibe/Turistas/consultarGuias.php?";
    View vista;
    ListView listView;
    static ArrayList<String> dat= new ArrayList<>();

    public ListaGuiasRecicler() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaGuiasRecicler.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaGuiasRecicler newInstance(String param1, String param2) {
        ListaGuiasRecicler fragment = new ListaGuiasRecicler();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_lista_guias_recicler, container, false);
        inicializarFirebase();

        if (NavegadorPrincipal.opc==3){
            Intent i = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                i = new Intent(getContext(), EstadosDeCitas.class);
                NavegadorPrincipal.opc=0;
            }
            startActivity(i);
        }
            mRecyclerView = v.findViewById(R.id.reciclerLista);
            mLayoutManager = new LinearLayoutManager(getContext());


            listarGuias();


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void inicializarFirebase() {
        // FirebaseApp.initializeApp(getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

    }
    public List<Guia>listarGuias(){
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Iniciando Sesión");
        pDialog.setCancelable(false); // para  no cancealar el progress Dialog
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
        final List<Guia> guia=new ArrayList<>();

        databaseReference.child("Guia").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                instList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Guia p = objSnaptshot.getValue(Guia.class);
                    if(p.getEstado().equals("1")) {
                        instList.add(p);
                    }
                }
                apadtar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return guia;
    }

    public void apadtar(){
        for (int i=0;i<instList.size(); i++){

            if (instList.get(i).getNombre().trim().equals("Juan De La barrera")){
                instList.get(i).setImg(R.drawable.fernando);
            }else if (instList.get(i).getNombre().trim().equals(
                    "Gustavo")){
                instList.get(i).setImg(R.drawable.gustavo);
            }else if (instList.get(i).getNombre().trim().equals(
                    "Luis")){
                instList.get(i).setImg(R.drawable.adan);
            }else{
                instList.get(i).setImg(R.drawable.adan);
            }
            movies=instList;

                if (instList.size()!=0){
                    mAdapter=new MyAdapter(movies, R.layout.item_card_viwe, new MyAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Guia movie, int position) {
                            Intent i=new Intent(getContext(),InfoGuias.class);
                            startActivity(i);
                            InfoGuias.guia=movie;
                        }
                    });

                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    Toast.makeText(getContext(),"sin guias dados de alta",Toast.LENGTH_LONG).show();
                }
        }

        pDialog.dismiss();
    }
    }



