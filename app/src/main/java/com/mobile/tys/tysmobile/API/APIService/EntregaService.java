package com.mobile.tys.tysmobile.API.APIService;

import com.mobile.tys.tysmobile.Model.ConfirmarEntrega;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface EntregaService {

    @POST("OrdenTrabajo/ConfirmarEntrega")
    Call<ConfirmarEntrega> postEntregarCliente(@Body ConfirmarEntrega body);

    @Multipart
    @POST("OrdenTrabajo/UploadFile")
    Call<ResponseBody> upload(
            @Part MultipartBody.Part photo,
            @Part("idOrden") RequestBody idorden
    );
}
