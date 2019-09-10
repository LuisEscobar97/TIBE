package com.example.sitol.tibe.adaptadorLista;

import com.example.sitol.tibe.objetos.Guia;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sitol.tibe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

//import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Guia> movies;
    private int layout;
    private OnItemClickListener itemClickListener;

    private Context context;

    private StorageReference mStorageRef;
    StorageReference referenciaDirectorio;
    ProgressDialog pDialog;

    public MyAdapter(List<Guia> movies, int layout, OnItemClickListener listener) {
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
        public TextView textViewActividades;
        public ImageView imageViewPoster;

        public ViewHolder(View itemView) {
            // Recibe la View completa. La pasa al constructor padre y enlazamos referencias UI
            // con nuestras propiedades ViewHolder declaradas justo arriba.
            super(itemView);
            textViewName = itemView.findViewById(R.id.infoNombre);

            textViewActividades =  itemView.findViewById(R.id.actiInfo);

            imageViewPoster =  itemView.findViewById(R.id.imageInfo);
        }

        public void bind(final Guia movie, final OnItemClickListener listener) {
            // Procesamos los datos a renderizar
            textViewName.setText(movie.getNombre()+" "+movie.getApPat()+" "+movie.getApMat());
            textViewActividades.setText(movie.getAcerca());

           bajarFoto(movie);

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
        public void bajarFoto(Guia guia){

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
                            Picasso.with(context).load(finalLocalFile).into(imageViewPoster);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, "ocurrio un error al bajar la imagen", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Declaramos nuestra interfaz con el/los método/s a implementar
    public interface OnItemClickListener {
        void onItemClick(Guia movie, int position);
    }


}