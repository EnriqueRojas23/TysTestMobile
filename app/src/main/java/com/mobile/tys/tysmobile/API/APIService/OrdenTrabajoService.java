package com.mobile.tys.tysmobile.API.APIService;

import com.mobile.tys.tysmobile.Model.ConfirmarEntrega;
import com.mobile.tys.tysmobile.Model.OrdenTrabajo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OrdenTrabajoService {
    @GET("OrdenTrabajo/GetOrdenes")
    Call<List<OrdenTrabajo>> getOrdenes(@Query("idestado") String idestado, @Query("iddestino") String iddestino);

    @GET("OrdenTrabajo/GetOrden")
    Call<ConfirmarEntrega> getOrden(@Query("id") String idorden);

    @GET("OrdenTrabajo/GetGuiaRechazada")
    Call<OrdenTrabajo> getGuiaRechazada(@Query("num") String numguia);
}
