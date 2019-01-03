package com.mobile.tys.tysmobile.Adapter;

import android.widget.Filter;

import com.mobile.tys.tysmobile.Model.OrdenTrabajo;

import java.util.ArrayList;

public class CustomFilter extends Filter {

    PictureAdapterRecyclerView adapter;
    ArrayList<OrdenTrabajo> filterList;

    public CustomFilter(ArrayList<OrdenTrabajo> filterList,PictureAdapterRecyclerView adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;

    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<OrdenTrabajo> filteredPlayers=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getNumcp().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }

            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;

        }

        return results;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.Ordenes = (ArrayList<OrdenTrabajo>) results.values;


        //REFRESH
        adapter.notifyDataSetChanged();
    }
}