package com.mobile.tys.tysmobile.View.Fragment;

import android.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mobile.tys.tysmobile.API.API;
import com.mobile.tys.tysmobile.API.APIService.OrdenTrabajoService;
import com.mobile.tys.tysmobile.Adapter.PictureAdapterRecyclerView;
import com.mobile.tys.tysmobile.Database.TysDbHelper;
import com.mobile.tys.tysmobile.Model.OrdenTrabajo;
import com.mobile.tys.tysmobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_LONG;

public class OtsPendientesActivity extends Fragment {

    private SharedPreferences userLoggedPreferences;
    private SharedPreferences.Editor userLoggedPrefsEditor;
    Integer IdUsuario = 0;
    List<OrdenTrabajo> ordenesTrabajo = null;
    @RequiresApi(api = Build.VERSION_CODES.M)
    SearchView sv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AsyncTask asyncTask;

    private List<Color> colors;
    private List<Color> allColors;




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final TysDbHelper usdbh =   new TysDbHelper(getContext());
        final SQLiteDatabase db = usdbh.getWritableDatabase();



        View view =  inflater.inflate(R.layout.activity_ots_pendientes, container, false);
        final RecyclerView picturesRecycler = view.findViewById(R.id.pictureRecycler);


        userLoggedPreferences = this.getActivity().getSharedPreferences("userLoggedPrefs", Context.MODE_PRIVATE);
        userLoggedPrefsEditor = userLoggedPreferences.edit();
        IdUsuario = userLoggedPreferences.getInt("idusuario", 0 );

        userLoggedPrefsEditor.putInt("idusuario",IdUsuario);
        userLoggedPrefsEditor.commit();

        sv =  view.findViewById(R.id.mSearch);
        //Toast.makeText(getActivity(),sv.getQuery(), LENGTH_LONG).show();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation((LinearLayoutManager.VERTICAL));

        picturesRecycler.setLayoutManager(linearLayoutManager);
        final ArrayList<OrdenTrabajo> ordenes = new ArrayList<>();



        //validar si tiene conexion
        ConnectivityManager cm =
                (ConnectivityManager)this.getContext().getSystemService(this.getContext().CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null ;

      //  isConnected = false;

        if(isConnected==false)
        {
            Toast.makeText(getActivity(), "No tiene conexión a internet", Toast.LENGTH_LONG).show();

            String query = "SELECT  * FROM  OrdenesTrabajo WHERE idestado = 11 " ;

            SQLiteDatabase db_read = usdbh.getWritableDatabase();
            Cursor cursor = db_read.rawQuery(query, null);

            OrdenTrabajo orden = null;
            if (cursor.moveToFirst()) {
                do {
                    ordenes.add(new OrdenTrabajo(cursor.getLong(1)
                            , cursor.getString(7)
                            , cursor.getString(3)
                            , cursor.getString(8)
                            , cursor.getString(5)
                            , cursor.getString(6)
                            , cursor.getString(4)
                            , cursor.getInt(7)));
                } while (cursor.moveToNext());
            }

            final PictureAdapterRecyclerView pictureAdapterRecyclerView = new PictureAdapterRecyclerView(
                    ordenes, R.layout.cardview_picture, getActivity());

            picturesRecycler.setAdapter(pictureAdapterRecyclerView);

            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {

                    pictureAdapterRecyclerView.getFilter().filter(query);
                    return false;
                }
            });

        }
        else {


            OrdenTrabajoService service = API.getApi().create(OrdenTrabajoService.class);
            Call<List<OrdenTrabajo>> OrdenCall = service.getOrdenes("11", "1282");


            OrdenCall.enqueue(new Callback<List<OrdenTrabajo>>() {
                @Override
                public void onResponse(Call<List<OrdenTrabajo>> call, Response<List<OrdenTrabajo>> response) {


                    if (response.code() == 200) {

                        //Eliminar data del mobile
                        SQLiteDatabase db = usdbh.getWritableDatabase();
                        db.delete("OrdenesTrabajo",
                                "idestado" + " = ?",
                                new String[] { "11" }); //selections args


                        ordenesTrabajo = response.body();

                        for (OrdenTrabajo item : ordenesTrabajo) {

                            String query = "SELECT  * FROM  OrdenesTrabajo WHERE ID = " + item.getIdordentrabajo() ;

                            SQLiteDatabase db_read = usdbh.getWritableDatabase();
                            Cursor cursor = db_read.rawQuery(query, null);

                            OrdenTrabajo orden = null;
                            if (cursor.moveToFirst()) {
                                do {




                                } while (cursor.moveToNext());
                            }
                            else
                            {

                                ordenes.add(new OrdenTrabajo(item.getIdordentrabajo()
                                        , item.getNumcp()
                                        , item.getRazonsocial()
                                        , item.getFecharegistro()
                                        , item.getEstado()
                                        , item.getOrigen()
                                        , item.getDestino()
                                        , item.getIdestado()
                                        ));



                                db.execSQL("INSERT INTO OrdenesTrabajo( id ,idestado,numcp, fecha, estado, cliente, origen,destino,file ) " +
                                        "VALUES ( '" + String.valueOf(item.getIdordentrabajo()) + "' " +
                                        ",  '" + item.getIdestado() + "' " +
                                        ",  '" + item.getNumcp() + "' " +
                                        ",  '" + item.getFecharegistro() + "' " +
                                        ",  '" + item.getEstado() + "' " +
                                        ",  '"  + item.getRazonsocial().replace("'","") + "'" +
                                        ", '" + item.getOrigen() + "'" +
                                        ", '" + item.getDestino() + "'" +
                                        ", '')");

                            }




                        }

                        final PictureAdapterRecyclerView pictureAdapterRecyclerView = new PictureAdapterRecyclerView(
                                ordenes, R.layout.cardview_picture, getActivity());

                        picturesRecycler.setAdapter(pictureAdapterRecyclerView);

                        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String query) {

                                pictureAdapterRecyclerView.getFilter().filter(query);
                                return false;
                            }
                        });


                    } else {
                        // Toast.makeText(LoginActivity.this, "No está autorizado", Toast.LENGTH_LONG).show();
                        //progressBar.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<OrdenTrabajo>> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), LENGTH_LONG).show();
                    //  progressBar.dismiss();
                }
            });
        }
        db.close();



        return view;
    }
    private void cargarDatos() {
        new BackgroundTask().execute();
    }


    private class BackgroundTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Toast.makeText(getContext(), "Recarga", Toast.LENGTH_LONG).show();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        int sizePrev = colors.size();
        super.onPostExecute(aVoid);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    protected void onCancelled() {
        swipeRefreshLayout.setRefreshing(false);
    }
}




}
