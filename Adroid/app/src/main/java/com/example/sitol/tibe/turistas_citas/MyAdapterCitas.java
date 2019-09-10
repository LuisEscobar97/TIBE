package com.example.sitol.tibe.turistas_citas;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sitol.tibe.R;
import com.example.sitol.tibe.objetos.Guia;
import com.example.sitol.tibe.objetos.ObjCita;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.squareup.picasso.Picasso;

public class MyAdapterCitas extends RecyclerView.Adapter<MyAdapterCitas.ViewHolder> {

    private List<ObjCita> movies;
    private int layout;
    private OnItemClickListener itemClickListener;

    private Context context;

    ProgressDialog pDialog;
    private StorageReference mStorageRef;
    StorageReference referenciaDirectorio;


    public MyAdapterCitas(List<ObjCita> movies, int layout, OnItemClickListener listener) {
        this.movies = movies;
        this.layout = layout;
        this.itemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos el layout y se lo pasamos al constructor del ViewHolder, donde manejaremos
        // toda la lógica como extraer los datos, referencias...
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        ViewHolder vh = new ViewHolder(v);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        referenciaDirectorio= mStorageRef.child("images");
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Llamamos al método Bind del ViewHolder pasándole objeto y listener
        holder.bind(movies.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Elementos UI a rellenar
        public TextView textViewName;
        public TextView textViewFecha;
        public TextView textViewHora;

        public ImageView imageViewPoster;

        public ViewHolder(View itemView) {
            // Recibe la View completa. La pasa al constructor padre y enlazamos referencias UI
            // con nuestras propiedades ViewHolder declaradas justo arriba.
            super(itemView);
            textViewName = itemView.findViewById(R.id.infoNombreCitas);

            textViewFecha =  itemView.findViewById(R.id.fechaCItas);

            textViewHora=itemView.findViewById(R.id.horaCitas);

            imageViewPoster =  itemView.findViewById(R.id.imageInfoCitas);
        }

        public void bind(final ObjCita movie, final OnItemClickListener listener) {
            // Procesamos los datos a renderizar
            textViewName.setText(movie.getNombreGuia()+" "+movie.getApePatGuia()+" "+movie.getApeMateGuia());
            textViewFecha.setText(movie.getFecha_reser());
            textViewHora.setText(movie.getHora());
           bajarFoto(movie.getId_guia());

            // imageViewPoster.setImageResource(movie.getPoster());
            // Definimos que por cada elemento de nuestro recycler view, tenemos un click listener
            // que se comporta de la siguiente manera...
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(movie, getAdapterPosition());
                }
            });
        }

        public void bajarFoto(String id_guia){
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Descarando imagen");
            pDialog.setCancelable(false); // para  no cancealar el progress Dialog
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
            File localFile = null;
            try {
                localFile = File.createTempFile("images", "jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }


            StorageReference referenciaImagen =referenciaDirectorio.child(id_guia);
            final File finalLocalFile = localFile;
            referenciaImagen.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            Picasso.with(context).load(finalLocalFile).into(imageViewPoster);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, "ocurrio un error al bajar la imagen", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            });
            pDialog.dismiss();
        }
    }

    // Declaramos nuestra interfaz con el/los método/s a implementar
    public interface OnItemClickListener {
        void onItemClick(ObjCita movie, int position);
    }


}