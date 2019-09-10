package com.example.sitol.tibe.turistas_citas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sitol.tibe.R;
import com.example.sitol.tibe.objetos.ObjCita;
import com.example.sitol.tibe.pirncipales.NavegadorPrincipal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Terminadas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Terminadas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Terminadas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public static ArrayList<ObjCita> listCitasPen= new ArrayList<ObjCita>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView mRecyclerView;
    ProgressDialog pDialog;
    private List<ObjCita> movies;
    // Puede ser declarado como 'RecyclerView.Adapter' o como nuetra clase adaptador 'MyAdapter'
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public Terminadas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Terminadas.
     */
    // TODO: Rename and change types and number of parameters
    public static Terminadas newInstance(String param1, String param2) {
        Terminadas fragment = new Terminadas();
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
        View v=inflater.inflate(R.layout.fragment_terminadas, container, false);

        inicializarFirebase();
        mRecyclerView=v.findViewById(R.id.listCanceladas);
        mLayoutManager= new LinearLayoutManager(getContext());

        listarGuias();

        return v;
    }
    public List<ObjCita> listarGuias(){
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Obtenienddo Citas");
        pDialog.setCancelable(false); // para  no cancealar el progress Dialog
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
        final List<ObjCita> guia=new ArrayList<>();

        databaseReference.child("CitasCanceladas").orderByChild("id_turista").equalTo(NavegadorPrincipal.turista.getIdTurista()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCitasPen.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    ObjCita p = objSnaptshot.getValue(ObjCita.class);

                    listCitasPen.add(p);

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

        movies=listCitasPen;
        mAdapter=new MyAdapterCitas(movies, R.layout.item_card_viwe_citas, new MyAdapterCitas.OnItemClickListener() {
            @Override
            public void onItemClick(ObjCita movie, int position) {


                InfoCitas.cita=movie;
                Intent i=new Intent(getContext(), InfoCitas.class);
                startActivity(i);

            }

        });

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        pDialog.dismiss();
    }
    public void inicializarFirebase() {
        // FirebaseApp.initializeApp(getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

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
}
