package com.mobile.tys.tysmobile.Adapter;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.tys.tysmobile.Model.OrdenTrabajo;
import com.mobile.tys.tysmobile.R;
import com.mobile.tys.tysmobile.View.ConfirmarEntregaActivity;

import java.util.ArrayList;

public class PictureAdapterRecyclerView extends RecyclerView.Adapter<PictureAdapterRecyclerView.PictureViewHolder>
        implements Filterable {

    ArrayList<OrdenTrabajo> Ordenes,filterList;
    private int resource;
    private Activity activity;
    CustomFilter filter;

    public PictureAdapterRecyclerView(ArrayList<OrdenTrabajo> ordenes, int resource, Activity activity) {
        Ordenes = ordenes;
        this.resource = resource;
        this.activity = activity;
        this.filterList = ordenes;
    }


    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent, false);
        return new PictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder holder, int position) {
        final OrdenTrabajo ordenTrabajo = Ordenes.get(position);
        holder.NombreCliente.setText(ordenTrabajo.getRazonsocial());
        holder.Estado.setText(ordenTrabajo.getEstado());
        holder.FechaCreacion.setText(ordenTrabajo.getFecharegistro());
        holder.Numcp.setText(ordenTrabajo.getNumcp());
        holder.Origen.setText(ordenTrabajo.getOrigen());
        holder.Destino.setText(ordenTrabajo.getDestino());
        holder.imageot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ordenTrabajo.getIdestado() == 11) {
                    Intent intent = new Intent(activity, ConfirmarEntregaActivity.class);
                    intent.putExtra("Id", Long.toString(ordenTrabajo.getIdordentrabajo()));
                    intent.putExtra("numcp", ordenTrabajo.getNumcp());
                    intent.putExtra("razonsocial", ordenTrabajo.getRazonsocial());
                    activity.startActivity(intent);
                }
            }

        });
    }
    public void clear(){
        Ordenes.clear();
        notifyDataSetChanged();
    }
    public void addAll(ArrayList<OrdenTrabajo> lista){
        Ordenes.addAll(lista);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return Ordenes.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(filterList,this);
        }

        return filter;
    }

    public class PictureViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageot;
        private ImageView imageclick;
        private TextView NombreCliente;
        private TextView FechaCreacion;
        private TextView Numcp;
        private TextView Estado;
        private TextView Origen;
        private TextView Destino;




        public PictureViewHolder(View itemView) {
            super(itemView);


            imageot = itemView.findViewById(R.id.imageCard);
            NombreCliente = itemView.findViewById(R.id.ClienteCard );
            Numcp = itemView.findViewById(R.id.OrdenTrabajoCard);
            FechaCreacion = itemView.findViewById(R.id.FechaCreacionCard);
            Estado = itemView.findViewById(R.id.EstadoCard);
            Origen = itemView.findViewById(R.id.OrigenCard);
            Destino = itemView.findViewById(R.id.DestinoCard);

        }
    }


}