package com.mobile.tys.tysmobile.API.APIService;



import com.mobile.tys.tysmobile.Model.ValidateUser;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SeguridadService {
    @POST("auth/login")
    Call<ValidateUser> getLogin(@Query("user") String username, @Query("password") String password);
}