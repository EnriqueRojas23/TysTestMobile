package com.mobile.tys.tysmobile.View;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mobile.tys.tysmobile.API.API;
import com.mobile.tys.tysmobile.API.APIService.EntregaService;
import com.mobile.tys.tysmobile.API.APIService.OrdenTrabajoService;
import com.mobile.tys.tysmobile.Adapter.GuiasRechazadasRecyclerView;
import com.mobile.tys.tysmobile.Common.ProgressRequestBody;
import com.mobile.tys.tysmobile.Database.TysDbHelper;
import com.mobile.tys.tysmobile.Model.ConfirmarEntrega;

import com.mobile.tys.tysmobile.Model.GuiaRechazada;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.mobile.tys.tysmobile.R;

import static android.view.View.GONE;
import static android.widget.Toast.LENGTH_LONG;
import static okhttp3.MultipartBody.FORM;

public class ConfirmarEntregaActivity extends AppCompatActivity implements View.OnClickListener
                                        , ProgressRequestBody.UploadCallbacks{

    private static int REQEUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private SharedPreferences userLoggedPreferences;
    private SharedPreferences.Editor userLoggedPrefsEditor;
    Integer IdUsuario = 0;
    private FloatingActionButton fabCamera;
    private int dia, mes,ano ,hora,minuto;
    ConfirmarEntrega entrega = null;
    private String photoPathTemp = "";
    Spinner eidTipoEntrega ;
    private ImageView photo ;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    Button bfecha,bhora;
    EditText efecha,ehora  ,edni, eNombreEntrega, eObservacion, eBulto, eGuia;
    TextView eNombreCliente;
    private static final int REQUEST_CAMERA = 1 ;
    private static final int REQUEST_GALERY = 2 ;
    ArrayList<GuiaRechazada> guiasrechazadas = new ArrayList<>();
    private Button mButtonAdd;
    private String Id ;
    private final static int IMAGE_RESULT = 200;
    Bitmap mBitmap;
    byte[] byteArray;
    String imageFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        verifyStoragePermissions(ConfirmarEntregaActivity.this);

        setContentView(R.layout.activity_confirmar_entrega);
        final RecyclerView GuiasRecycler = findViewById(R.id.GuiasRecycler);

        Intent iin = getIntent();
        Bundle bundle = iin.getExtras();
        Id = bundle.getString("Id");
        final String numcp = bundle.getString("numcp");
        final String razonsocial = bundle.getString("razonsocial");

        //Obtener IdUsuario
        userLoggedPreferences = getSharedPreferences("userLoggedPrefs", Context.MODE_PRIVATE);
        IdUsuario = userLoggedPreferences.getInt("idusuario", 0 );

        mButtonAdd = (Button) findViewById(R.id.btnAgregar);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ConfirmarEntregaActivity.this);
        linearLayoutManager.setOrientation((LinearLayoutManager.VERTICAL));
        GuiasRecycler.setLayoutManager(linearLayoutManager);


        final GuiasRechazadasRecyclerView pictureAdapterRecyclerView = new GuiasRechazadasRecyclerView(
                guiasrechazadas,R.layout.cardview_guiasrechazadas, ConfirmarEntregaActivity.this);

        GuiasRecycler.setAdapter(pictureAdapterRecyclerView);

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = 0;

                eBulto = findViewById(R.id.Bultos);
                eGuia = findViewById(R.id.guia);

                guiasrechazadas.add(position,new GuiaRechazada(Integer.parseInt(Id), Integer.parseInt(eBulto.getText().toString()),  eGuia.getText().toString()));
                pictureAdapterRecyclerView.notifyItemInserted(position);

                GuiasRecycler.scrollToPosition(position);

            }
        });

        Spinner spinner = findViewById(R.id.idtipoentrega);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ConfirmarEntregaActivity.this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat HourFormat = new SimpleDateFormat("HH:mm");

        Date date = new Date();

        eNombreCliente = findViewById(R.id.txtCliente);
        bfecha = findViewById(R.id.btnFecha);
        bhora = findViewById(R.id.btnHora);
        efecha = findViewById(R.id.fecha);
        ehora = findViewById(R.id.hora);

        bfecha.setOnClickListener(ConfirmarEntregaActivity.this);
        bhora.setOnClickListener(ConfirmarEntregaActivity.this);
        eNombreCliente.setText(razonsocial);
        efecha.setText(dateFormat.format(date));
        ehora.setText(HourFormat.format(date));


        fabCamera =  findViewById(R.id.fab);

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
            }
        });


        showToolbar(numcp, true);



    }

    @Override
    public void onClick(View view) {
        if(view==bfecha){
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes= c.get(Calendar.MONTH);
            ano = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    efecha.setText(dayOfMonth + "/" + (month +1) + "/" + year);
                }
            },ano, mes,dia);

            datePickerDialog.show();
        }
        if(view==bhora){
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.HOUR_OF_DAY);
            mes= c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    ehora.setText(hourOfDay + ":" + minute);
                }
            },hora,minuto,false);
            timePickerDialog.show();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK) {

            ImageView imageView = findViewById(R.id.idphoto);

            if (requestCode == IMAGE_RESULT) {


                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    mBitmap = BitmapFactory.decodeFile(filePath);
                    getByteArrayInBackground();
                    imageView.setImageBitmap(mBitmap);

                }
            }

        }


    }
    private void getByteArrayInBackground() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                byteArray = bos.toByteArray();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // frameLayout.setVisibility(View.VISIBLE);
                    }
                });


            }
        };
        thread.start();
    }
    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }
    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }


    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    private void BuildProgressBar(View view) {
        progressBar = new ProgressDialog(ConfirmarEntregaActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Confirmando la orden ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        progressBar.show();
        progressBarStatus = 0;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Desea salir de la confirmación? ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ConfirmarEntregaActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.show();
    }

    public void EntregarTemp(View view) throws ParseException {

        final TysDbHelper usdbh =   new TysDbHelper(this);

        BuildProgressBar(view);

        boolean cancel = false;
        View focusView = null;


        efecha.setError(null);
        ehora.setError(null);

        efecha = findViewById(R.id.fecha);
        ehora = findViewById(R.id.hora);
        edni = findViewById(R.id.DNI);

        eNombreEntrega = findViewById(R.id.NombreEntrega);

        // validacion

        if (TextUtils.isEmpty(edni.getText())) {
            edni.setError(getString(R.string.error_field_required));
            focusView = edni;
            cancel = true;
        }
        if (TextUtils.isEmpty(eNombreEntrega.getText())) {
            eNombreEntrega.setError(getString(R.string.error_field_required));
            focusView = eNombreEntrega;
            cancel = true;
        }


        if (TextUtils.isEmpty(efecha.getText())) {
            efecha.setError(getString(R.string.error_field_required));
            focusView = efecha;
            cancel = true;
        }
        if (TextUtils.isEmpty(ehora.getText())) {
            ehora.setError(getString(R.string.error_field_required));
            focusView = ehora;
            cancel = true;
        }
        if(cancel)
        {
            focusView.requestFocus();
            progressBar.dismiss();
        }
        else {




                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final ConfirmarEntrega confirmarEntrega = new ConfirmarEntrega();


                Intent iin = getIntent();
                Bundle bundle = iin.getExtras();
                final String Id = bundle.getString("Id");



                confirmarEntrega.setIdusuarioentrega(IdUsuario);

                eNombreEntrega = findViewById(R.id.NombreEntrega);
                confirmarEntrega.setRecurso(eNombreEntrega.getText().toString());

                edni = findViewById(R.id.DNI);
                confirmarEntrega.setDocumento(edni.getText().toString());

                eObservacion = findViewById(R.id.Observacion);
                confirmarEntrega.setDescripcion(eObservacion.getText().toString());

                ehora = findViewById(R.id.hora);
                confirmarEntrega.setHoraetapa(ehora.getText().toString());

                efecha = findViewById(R.id.fecha);

                String dateValue = efecha.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //  04/02/2011 20:27:05

                Date date = sdf.parse(dateValue); // returns date object

                confirmarEntrega.setFechaetapa(date);

                eidTipoEntrega = findViewById(R.id.idtipoentrega);
                final String nombreMaestro = GetIdMaestroEntrega(eidTipoEntrega.getSelectedItem().toString());
                confirmarEntrega.setIdmaestroetapa(Integer.parseInt(nombreMaestro));
                confirmarEntrega.setIdordentrabajo(Integer.parseInt(Id));

                builder.setMessage("¿Está seguro que desea confirmar la entrega? ");
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ///Evaluar si tiene conexion
                        ConnectivityManager cm =
                                (ConnectivityManager)  getBaseContext().getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


                        boolean isConnected = activeNetwork != null ;

                        //isConnected = false;

                        if(isConnected==false)
                        {
                            Toast.makeText(ConfirmarEntregaActivity.this, "No tiene conexión a internet. La información se almacenará en la bandeja de salida.", Toast.LENGTH_LONG).show();

                            SQLiteDatabase db = usdbh.getWritableDatabase();

                            ContentValues cv = new ContentValues();
                            cv.put("fechaentrega",efecha.getText().toString());
                            cv.put("horaentrega",ehora.getText().toString());
                            cv.put("tipoentrega",nombreMaestro);
                            cv.put("dni",edni.getText().toString());
                            cv.put("nombreentrega",eNombreEntrega.getText().toString());
                            cv.put("tipoentrega",nombreMaestro);
                            cv.put("observacion",eObservacion.getText().toString());
                            cv.put("idestado", 12);
                            cv.put("estado", "Pend Retorno Doc");
                            cv.put("file", imageFileName);
                            db.update("OrdenesTrabajo", cv, "id=" + Id, null);


                            progressBar.dismiss();


                            Intent intent = new Intent(ConfirmarEntregaActivity.this, IndexActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            if (mBitmap != null)
                                multipartImageUpload();
                            else {
                                Toast.makeText(getApplicationContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
                            }
                            // Guardar en el servidor


                            EntregaService service = API.getApi().create(EntregaService.class);


                            Call<ConfirmarEntrega> EntregaCall = service.postEntregarCliente(confirmarEntrega);

                            EntregaCall.enqueue(new Callback<ConfirmarEntrega>() {
                                @Override
                                public void onResponse(Call<ConfirmarEntrega> call, Response<ConfirmarEntrega> response) {

                                    if (response.code() == 200) {
                                        Toast.makeText(ConfirmarEntregaActivity.this,
                                                "La orden ha sido confirmada.",
                                                LENGTH_LONG).show();
                                        Intent intent = new Intent(ConfirmarEntregaActivity.this, IndexActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }

                                @Override
                                public void onFailure(Call<ConfirmarEntrega> call, Throwable t) {
                                    Toast.makeText(ConfirmarEntregaActivity.this,
                                            t.getMessage(),
                                            LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // finish();
                    }
                });
                builder.show();
                //ConfirmarEntregaActivity.super.onBackPressed();
        }
    }
    public String GetIdMaestroEntrega(String item) {

        if(item.equals("Entrega conforme (OK)")){
            return "5";
        }
        else if(item.equals("Entrega: Con mercadería dañada")) {
            return "8";
        }
        else if(item.equals("Entrega: Con Mercaderia Faltante")) {
            return "9";
        }
        else if(item.equals("Entrega: Rechazo Total")) {
            return "10";
        }
            else if(item.equals("Entrega: Rechazo Parcial ")) {
            return "11";
        }
        else if(item.equals("No Entrega: Local Cerrado")) {
            return "12";
        }
        else if(item.equals("No Entrega: No existe la dirección de entrega")) {
            return "13";
        }
        else {
            return "0";
        }
    }
    private static void verifyStoragePermissions(Activity activity){
        int permissons = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissons != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE, REQEUEST_EXTERNAL_STORAGE);
        }

    }


    public Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");

        imageFileName =  "FILE_CONF_"  + String.valueOf(Id) + ".jpg";

        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), imageFileName));
            imageFileName = outputFileUri.getPath() ;
        }
        return outputFileUri;
    }
    private void multipartImageUpload() {

       // initRetrofitClient();


        try {

            if (byteArray != null) {
                File filesDir = getApplicationContext().getFilesDir();


                String imageFileName = "PHOTO_IDORDEN"  + "_"  + Id + ".jpg";

                String Ruta = imageFileName + '/' + imageFileName;

                //getCaptureImageOutputUri().getPath();

                File file = new File(filesDir, imageFileName);

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(byteArray);
                fos.flush();
                fos.close();

                //textView.setTextColor(Color.BLUE);

                ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), fileBody);
                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");


                EntregaService apiService = API.getApi().create(EntregaService.class);
                Call<ResponseBody> req = apiService.upload(body, name);
                req.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressBar.setProgress(percentage);
       // Toast.makeText(getApplicationContext(), String.valueOf(percentage), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {
        progressBar.dismiss();
    }

    @Override
    public void onFinish() {
        progressBar.dismiss();
    }

    @Override
    public void uploadStart() {
        progressBar.setProgress(0);
        Toast.makeText(getApplicationContext(), "Upload started", Toast.LENGTH_SHORT).show();
    }
    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

    }
}
//    private void takePicture() {
//        Intent intentTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(intentTakePicture.resolveActivity( getPackageManager()) != null) {
//
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//
//
//            }catch (Exception ex){
//                ex.printStackTrace();
//            }
//
//            if(photoFile!= null) {
//                Uri photoUri = FileProvider.getUriForFile(this,"com.mobile.tys.tysmobile",photoFile);
//
//                intentTakePicture.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
//                startActivityForResult(intentTakePicture, REQUEST_CAMERA);
//            }
//
//        }
//
//    }
//    private File createImageFile() throws IOException {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH-mm-ss").format(new Date());
//        String imageFileName = "JPEG_"  + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File  photo = File.createTempFile(imageFileName, ".jpg", storageDir );
//        photoPathTemp = "file:" + photo.getAbsolutePath();
//        return photo;
//
//    }
//Obtener Orden del servicio
//        OrdenTrabajoService service =  API.getApi().create(OrdenTrabajoService.class);
//        Call<ConfirmarEntrega> OrdenCall = service.getOrden(Id);
//        OrdenCall.enqueue(new Callback<ConfirmarEntrega>() {
//            @Override
//            public void onResponse(Call<ConfirmarEntrega> call, Response<ConfirmarEntrega> response) {
//                if (response.code() == 200) {
//                    entrega = response.body();
//                    edni = findViewById(R.id.DNI);
//                    eNombreEntrega = findViewById(R.id.NombreEntrega);
//
//                    edni.setText("41886259",TextView.BufferType.EDITABLE);
//                    eNombreEntrega.setText("Enrique Rojas",TextView.BufferType.EDITABLE);

//                }
//                else
//                {
//                    //  Toast.makeText(ConfirmarEntregaActivity.this, "xD", Toast.LENGTH_LONG).show();
//                    progressBar.dismiss();
//                }
//            }
//            @Override
//            public void onFailure(Call<ConfirmarEntrega> call, Throwable t) {
//                Toast.makeText( ConfirmarEntregaActivity.this ,t.getMessage(), LENGTH_LONG).show();
//                progressBar.dismiss();
//            }
//        });

//        //super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQUEST_CAMERA && resultCode ==  RESULT_OK){
//            Toast.makeText(ConfirmarEntregaActivity.this, photoPathTemp , Toast.LENGTH_LONG).show();
//            photo = findViewById(R.id.idphoto);
//            Picasso.get().load(photoPathTemp).fit().centerCrop().into(photo);
//
//
//        }