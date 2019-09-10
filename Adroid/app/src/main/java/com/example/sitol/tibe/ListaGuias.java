package com.example.sitol.tibe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.sitol.tibe.objetos.Guia;
import com.example.sitol.tibe.pirncipales.NavegadorPrincipal;
import com.example.sitol.tibe.turistas_citas.EstadosDeCitas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaGuias.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaGuias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaGuias extends android.app.Fragment implements Response.Listener<JSONObject>, Response.ErrorListener, AdapterView.OnItemSelectedListener {
    static Object detalles[]= null;
    static int n;
    public static int noGuia=0;
    Guia cat;
    ProgressDialog pDialog;
    public static ArrayList<Guia> instList= new ArrayList<Guia>();


    private String URL_LISTA_HISTORIAL ="http://192.168.216.156/Tibe/Turistas/consultarGuias.php?";
    View vista;
    ListView listView;
    static ArrayList<String> dat= new ArrayList<>();
   /* static DatosLista datos[]= new DatosLista[]{
            new DatosLista("Plazas Outlet Lerma","25/10/17", R.drawable.lerma,null),
            new DatosLista("Galerias Toluca","10/09/17", R.drawable.toluca,null),
            new DatosLista("Plaza Sendero Toluca","01/09/17", R.drawable.sendero,null),
            new DatosLista("Galerias Metepec","15/08/17", R.drawable.metepc,null)
    };*/
    // cree un arreglo de tipo datos lista
    static DatosLista hist[];
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListaGuias() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Historial.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaGuias newInstance(String param1, String param2) {
        ListaGuias fragment = new ListaGuias();
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

        vista= inflater.inflate(R.layout.fragment_listaguias, container, false);
        listView=vista.findViewById(R.id.listOpc);
        if (NavegadorPrincipal.opc==3){
            Intent i = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                i = new Intent(getContext(), EstadosDeCitas.class);
            }
            startActivity(i);
        }else {
            new GetInstitucion().execute();
            // esto lo comente para ver quitar los datos por defecto que le habia metido
        /*
        AdaptadorTit adaptadorTit= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            adaptadorTit = new AdaptadorTit(getContext(),datos);
        }
        listView.setAdapter(adaptadorTit);
        */
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    n = i;
                    dat.clear();
                    dat.add(String.valueOf(hist[i].getLista().getNombre()));
                    dat.add(String.valueOf(hist[i].getLista().getApPat()));
                    dat.add(String.valueOf(hist[i].getLista().getApMat()));
                    dat.add(String.valueOf(hist[i].getLista().getAcerca()));
                    dat.add(String.valueOf(hist[i].getLista().getDesc()));
                    dat.add(String.valueOf(hist[i].getLista().getTarifa()));
                    dat.add(String.valueOf(hist[i].getLista().getZona()));
                    dat.add(String.valueOf(hist[i].getLista().getIdG()));
                    dat.add(String.valueOf(noGuia));

                    Intent intent = new Intent(getActivity().getApplicationContext(), Detalles.class);
                    startActivity(intent);
                }
            });

        }
        return vista;
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

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
    class AdaptadorTit extends ArrayAdapter<DatosLista> {
 // esta es una clase adapradora que me permite personalizar el listview
        public AdaptadorTit(Context context, DatosLista [] datos){
            super(context,R.layout.item_list,datos);
        }

        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater inflater=LayoutInflater.from(getContext());
            View item= inflater.inflate(R.layout.item_list,null);

            TextView lblT=(TextView)item.findViewById(R.id.lblT);
            lblT.setText("Nombre: "+hist[position].getTitulo());

            TextView lblst=(TextView)item.findViewById(R.id.lblst);
            lblst.setText("Descripción: "+hist[position].getSubtitulo());

            ImageView imagen=(ImageView)item.findViewById(R.id.foto);
            imagen.setImageResource(hist[position].getImagen());
            return (item);
        }
    }

    private class GetInstitucion extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pDialog = new ProgressDialog(getContext());
            }
            pDialog.setMessage("Obtencion El Guias");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_LISTA_HISTORIAL, ServiceHandler.GET);
             String nombre;
            String apPat;
            String apMat;
            String correo;
            String estado;
            String tipo;
            String desc;
            String acerca;
            String zona;
            int telefono;
            int tarifa;
            int idG;
            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray institu = jsonObj.getJSONArray("guias");

                        for (int i = 0; i < institu.length(); i++) {
                            JSONObject catObj = (JSONObject) institu.get(i);
                            nombre=catObj.getString("Nombre");
                            apPat=catObj.getString("Ape_paterno");
                            apMat=catObj.getString("Ape_materno");
                            correo=catObj.getString("Correo");
                            tipo=catObj.getString("tipoGuia");
                            estado=catObj.getString("estGuia");
                            desc=catObj.getString("descripcion");
                            telefono=catObj.getInt("Telefono");
                            tarifa=catObj.getInt("tarifa");
                            zona=catObj.getString("zona");
                            acerca=catObj.getString("acerca");
                            idG=catObj.getInt("id_guias");
                           // cat = new Guia(nombre,apPat,apMat,correo,estado,tipo,desc,telefono,tarifa,acerca,zona,idG,0);
                            instList.add(cat);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "¿No ha recibido ningún dato desde el servidor!");

            }

            return null;
        }
///////////////////////////////////////////////////aqui le lo que se supone va crear mis historial///////////
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.hide();
                ///con esto me llenas los datos de cada posicion en mi historial
               llenarHistorial();
               //es mi objeto daptdaor el cual me permite adaptar lo que tengo en un arreglo y mostrarlo en listview
                AdaptadorTit adaptadorTit= null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    /// aqui se mete el arreglo con los datos para poder adaptarlo al list view
                    adaptadorTit = new AdaptadorTit(getContext(),hist);
    //se adapta el listview con el objeto adaptador
                listView.setAdapter(adaptadorTit);
            }

        }
    }
public void llenarHistorial(){
        // con esto se llena los datos del hitorial
        DatosLista dato;

        hist= new DatosLista[instList.size()];
         noGuia=0;
        for (int i=0;i<instList.size(); i++){

            if (instList.get(i).getNombre().trim().equals("Juan De La barrera")){
                noGuia=R.drawable.fernando;
            }else if (instList.get(i).getNombre().trim().equals(
                    "Gustavo")){
                noGuia=R.drawable.gustavo;
            }else if (instList.get(i).getNombre().trim().equals(
                    "Luis")){
                noGuia=R.drawable.adan;
            }

        //el primer paramatro se llama titulo en el objeto y ahi va el nombre del estacionamiento, el segudo parametro se llama subtitulo y va la fecha que se hizo el servicoo, el segun parametro va la imagen
            dato =  new DatosLista(instList.get(i).getNombre(),instList.get(i).getDesc(),noGuia,instList.get(i));

            hist[i]=dato;
        }
}
}
