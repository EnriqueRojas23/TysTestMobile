package com.mobile.tys.tysmobile.Adapter;


import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mobile.tys.tysmobile.Model.GuiaRechazada;
import com.mobile.tys.tysmobile.R;

import java.util.ArrayList;

public class GuiasRechazadasRecyclerView extends RecyclerView.Adapter<GuiasRechazadasRecyclerView.GuiasHolder> {




    ArrayList<GuiaRechazada> Guias;
    private int resource;
    private Activity activity;

    public GuiasRechazadasRecyclerView(ArrayList<GuiaRechazada> guias, int resource, Activity activity) {
        Guias = guias;
        this.resource = resource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public GuiasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent, false);
        return new GuiasHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuiasHolder holder, int position) {
        final GuiaRechazada guiarechazada = Guias.get(position);
        holder.numeroGuia.setText("N°: " + guiarechazada.getNumeroGuia());
        holder.Cantidad.setText("Bul.: " +String.valueOf(guiarechazada.getCantidad()));


    }
    @Override
    public int getItemCount() {
        return  Guias.size();
    }


    public class GuiasHolder extends RecyclerView.ViewHolder {

        private ImageButton imageot;
        private TextView numeroGuia;
        private TextView Cantidad;


        public GuiasHolder(View itemView) {
            super(itemView);
            imageot = itemView.findViewById(R.id.eliminarguia);
            numeroGuia = itemView.findViewById(R.id.NumGuiaCard);
            Cantidad = itemView.findViewById(R.id.CantidadCard);

            imageot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    try {
                        builder.setMessage("¿Desea eliminar la guía de remisión? ");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Guias.remove(position);
                                notifyItemRemoved(position);

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                        builder.show();
                    }catch (ArrayIndexOutOfBoundsException e){e.printStackTrace();}
                }
            });
        }
    }
}