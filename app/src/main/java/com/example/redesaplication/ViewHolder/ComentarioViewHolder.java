package com.example.redesaplication.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redesaplication.Interface.ItemClickListener;
import com.example.redesaplication.R;

public class ComentarioViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtComentario, txtNombre, txtRespuesta, estado;
    private ItemClickListener itemClickListener;

    public ComentarioViewHolder(@NonNull View itemView) {
        super(itemView);
        txtComentario = (TextView) itemView.findViewById(R.id.coment);
        txtNombre = (TextView) itemView.findViewById(R.id.nombre_usuario);
        txtRespuesta = (TextView) itemView.findViewById(R.id.respuesta);
        estado = (TextView) itemView.findViewById(R.id.res_admin);
    }

    @Override
    public void onClick(View v) {

    }
}
