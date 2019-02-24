package com.denta.android.myapi.api;

import com.denta.android.myapi.model.DefaultResponse;
import com.denta.android.myapi.model.LoginResponse;
import com.denta.android.myapi.model.UsersResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api  {
    @FormUrlEncoded
    @POST("createuser")
    Call<DefaultResponse>createUser(
      @Field("email") String email,
      @Field("password") String password,
      @Field("name") String name,
      @Field("school") String school
    );

    @FormUrlEncoded
    @POST("userlogin")
    Call<LoginResponse>userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("allusers")
    Call<UsersResponse>getUsers();

    @FormUrlEncoded
    @PUT("updateuser/{id}")
    Call<LoginResponse> updateUser(@Path("id") int id, @Field("email") String email, @Field("name") String name, @Field("school") String school);

    @FormUrlEncoded
    @PUT("updatepassword")
    Call<DefaultResponse> updatePassword(
            @Field("current_pwd") String currentPwd,
            @Field("new_pwd") String newPwd ,
            @Field("email") String email
    );

    @DELETE("deleteuser/{id}")
    Call<DefaultResponse> deleteUser(@Path("id") int id);
}
