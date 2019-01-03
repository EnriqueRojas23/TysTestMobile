package com.mobile.tys.tysmobile.View.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mobile.tys.tysmobile.API.API;
import com.mobile.tys.tysmobile.API.APIService.EntregaService;
import com.mobile.tys.tysmobile.API.APIService.OrdenTrabajoService;
import com.mobile.tys.tysmobile.Adapter.PictureAdapterRecyclerView;
import com.mobile.tys.tysmobile.Common.ProgressRequestBody;
import com.mobile.tys.tysmobile.Database.TysDbHelper;
import com.mobile.tys.tysmobile.Model.ConfirmarEntrega;
import com.mobile.tys.tysmobile.Model.OrdenTrabajo;
import com.mobile.tys.tysmobile.R;
import com.mobile.tys.tysmobile.View.ConfirmarEntregaActivity;
import com.mobile.tys.tysmobile.View.IndexActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_LONG;

public class OtsBandejaSalidaActivity extends Fragment  implements  ProgressRequestBody.UploadCallbacks{


    private SharedPreferences userLoggedPreferences;
    private SharedPreferences.Editor userLoggedPrefsEditor;
    Integer IdUsuario = 0;
    List<OrdenTrabajo> ordenesTrabajo = null;
    PictureAdapterRecyclerView pictureAdapterRecyclerView;
    @RequiresApi(api = Build.VERSION_CODES.M)
    SearchView sv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AsyncTask asyncTask;
    View view = null;
    Bitmap mBitmap;
    byte[] byteArray;
    String imageFileName;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final ArrayList<OrdenTrabajo> ordenes = new ArrayList<>();
        final TysDbHelper usdbh = new TysDbHelper(getContext());
        final SQLiteDatabase db = usdbh.getWritableDatabase();


        view = inflater.inflate(R.layout.activity_ots_bandeja_salida, container, false);
        final RecyclerView picturesRecycler = view.findViewById(R.id.pictureRecycler);

        swipeRefreshLayout =  view.findViewById(R.id.swipeLayout);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);


        userLoggedPreferences = this.getActivity().getSharedPreferences("userLoggedPrefs", Context.MODE_PRIVATE);
        userLoggedPrefsEditor = userLoggedPreferences.edit();
        IdUsuario = userLoggedPreferences.getInt("idusuario", 0);

        userLoggedPrefsEditor.putInt("idusuario", IdUsuario);
        userLoggedPrefsEditor.commit();

        sv = view.findViewById(R.id.mSearch);
        //Toast.makeText(getActivity(),sv.getQuery(), LENGTH_LONG).show();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation((LinearLayoutManager.VERTICAL));

        picturesRecycler.setLayoutManager(linearLayoutManager);





        //validar si tiene conexion
        ConnectivityManager cm =
                (ConnectivityManager) this.getContext().getSystemService(this.getContext().CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null;

        isConnected = false;

        if (isConnected == false) {
            //Toast.makeText(getActivity(), "No tiene conexión a internet", Toast.LENGTH_LONG).show();

            String query = "SELECT  * FROM  OrdenesTrabajo WHERE idestado = 12 ";

            SQLiteDatabase db_read = usdbh.getWritableDatabase();
            Cursor cursor = db_read.rawQuery(query, null);


            OrdenTrabajo orden = null;
            if (cursor.moveToFirst()) {
                do {
                    ordenes.add(new OrdenTrabajo(cursor.getLong(0)
                            , cursor.getString(7)
                            , cursor.getString(3)
                            , cursor.getString(8)
                            , cursor.getString(5)
                            , cursor.getString(6)
                            , cursor.getString(4)
                            , cursor.getInt(7)));
                } while (cursor.moveToNext());
            }

            pictureAdapterRecyclerView = new PictureAdapterRecyclerView(
                    ordenes, R.layout.cardview_bandeja_salida, getActivity());

            picturesRecycler.setAdapter(pictureAdapterRecyclerView);


            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    cargarDatos(ordenes);
                    // swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setRefreshing(false);
                }
            });


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
        db.close();
        return view;
    }

    private void cargarDatos(final ArrayList<OrdenTrabajo> ordenes) {

        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null;

        //isConnected = false;
        if (isConnected == false) {

            Toast.makeText(getActivity() , "No tiene conexión a internet", Toast.LENGTH_LONG).show();
        }
        else
        {
            new BackgroundTask().execute();
        }



    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void uploadStart() {

    }


    private class BackgroundTask extends AsyncTask<Void , Void, ArrayList<OrdenTrabajo>> {
        final TysDbHelper usdbh = new TysDbHelper(getContext());
        final ArrayList<OrdenTrabajo> ordenes = new ArrayList<>();

        @Override
        protected ArrayList<OrdenTrabajo> doInBackground(Void... params) {
            try {






                String query = "SELECT  * FROM  OrdenesTrabajo WHERE idestado = 12 ";
                SQLiteDatabase db_read = usdbh.getWritableDatabase();
                Cursor cursor = db_read.rawQuery(query, null);
                ConfirmarEntrega confirmarEntrega ;

                if (cursor.moveToFirst()) {
                    do {
                        confirmarEntrega = new ConfirmarEntrega();



                        confirmarEntrega.setIdusuarioentrega(IdUsuario);
                        confirmarEntrega.setDescripcion(cursor.getString(14));
                        confirmarEntrega.setDocumento(cursor.getString(9));

                        String dateValue = cursor.getString(12);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //  04/02/2011 20:27:05

                        Date date = sdf.parse(dateValue); // returns date object


                        confirmarEntrega.setFechaetapa(date);
                        confirmarEntrega.setHoraetapa(cursor.getString(13));
                        confirmarEntrega.setIdestado(cursor.getInt(2));
                        confirmarEntrega.setIdmaestroetapa(cursor.getInt(11));
                        confirmarEntrega.setIdordentrabajo(cursor.getLong(1));
                        confirmarEntrega.setIdusuarioregistro(IdUsuario);
                        confirmarEntrega.setRecurso(cursor.getString(10));
                        confirmarEntrega.setFile(cursor.getString(15));

                        ordenes.add(new OrdenTrabajo(cursor.getLong(1)
                                , cursor.getString(7)
                                , cursor.getString(3)
                                , cursor.getString(8)
                                , cursor.getString(5)
                                , cursor.getString(6)
                                , cursor.getString(4)
                                , cursor.getInt(7)));

                        ServerExecute(confirmarEntrega);

                        ordenes.remove(0);


                    } while (cursor.moveToNext());
                }





                Thread.sleep(20);
                return ordenes;

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<OrdenTrabajo> result) {
            super.onPostExecute(result);

            pictureAdapterRecyclerView.clear();


             pictureAdapterRecyclerView.addAll(result);

            swipeRefreshLayout.setRefreshing(false);

        }

        @Override
        protected void onCancelled() {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    private boolean ServerExecute(final ConfirmarEntrega confirmarEntrega)
    {


        File filesDir = getContext().getApplicationContext().getFilesDir();

        String filePath = confirmarEntrega.file;
        if (filePath != null) {
            mBitmap = BitmapFactory.decodeFile(filePath);


            String imageFileName = "PHOTO_IDORDEN" + "_" + confirmarEntrega.getIdordentrabajo() + ".jpg";
            String Ruta = imageFileName + '/' + imageFileName;

            mBitmap = BitmapFactory.decodeFile(filePath);
            //getByteArrayInBackground();
            File file = new File(filePath);

//                FileOutputStream fos = new FileOutputStream(file);
//                fos.write(byteArray);
//                fos.flush();
//                fos.close();


            //textView.setTextColor(Color.BLUE);

            ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), fileBody);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");
            EntregaService apiService = API.getApi().create(EntregaService.class);
            Call<ResponseBody> req = apiService.upload(body, name);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(getContext().getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }


        // Guardar en el servidor
        final TysDbHelper usdbh = new TysDbHelper(getContext());
        EntregaService service = API.getApi().create(EntregaService.class);
        final Call<ConfirmarEntrega> EntregaCall = service.postEntregarCliente(confirmarEntrega);
        boolean resultado = false;

        EntregaCall.enqueue(new Callback<ConfirmarEntrega>() {
            @Override
            public void onResponse(Call<ConfirmarEntrega> call, Response<ConfirmarEntrega> response) {

                if (response.code() == 200) {

                    SQLiteDatabase db = usdbh.getWritableDatabase();
                    db.delete("OrdenesTrabajo", "ID" + "=" + confirmarEntrega.getIdordentrabajo(), null);
                    db.close();



                }


            }

            @Override
            public void onFailure(Call<ConfirmarEntrega> call, Throwable t) {
            }
        });
        return resultado;
    }
    private void getByteArrayInBackground() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                byteArray = bos.toByteArray();

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // frameLayout.setVisibility(View.VISIBLE);
//                    }
//                });


            }
        };
        thread.start();
    }
//    public String getImageFilePath(Intent data) {
//        return getImageFromFilePath(data);
//    }
//    private String getImageFromFilePath(Intent data) {
//        boolean isCamera = data == null || data.getData() == null;
//
//        if (isCamera) return getCaptureImageOutputUri().getPath();
//        else return getPathFromURI(data.getData());
//
//    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
//    private Uri getCaptureImageOutputUri() {
//        Uri outputFileUri = null;
//        File getImage = getContext().getExternalFilesDir("");
//
//        imageFileName =  "FILE_CONF_"  + String.valueOf(Id) + ".jpg";
//
//        if (getImage != null) {
//            outputFileUri = Uri.fromFile(new File(getImage.getPath(), imageFileName));
//            imageFileName = outputFileUri.getPath() ;
//        }
//        return outputFileUri;
//    }

}