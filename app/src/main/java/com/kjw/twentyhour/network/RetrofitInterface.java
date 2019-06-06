package com.kjw.twentyhour.network;





import com.kjw.twentyhour.listener.StoreBranch;
import com.kjw.twentyhour.model.*;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface RetrofitInterface {


    @POST("users")
    Observable<Response> register(@Body User user);

    @POST("owners")
    Observable<Response> registerOwner(@Body Owner owner);


    @POST("orders")
    Observable<Response> order(@Body OrderSheet orderSheet);


//   @POST("products")
//    Observable<Response> productRegister(@Body Product product );

    @POST("authenticate")
    Observable<Response> login();

    @POST("status")
    Observable<Response> statusRegister(@Body Status status);

    @GET("stores/{storeName}")
    Observable<StoreBranch> getStore(@Path("storeName") String storeName);

    @GET("users/{email}")
    Observable<User> getProfile(@Path("email") String email);

    @GET("products/all")
    Observable<Product[]> getProduct();

    @PUT("users/{email}")
    Observable<Response> changePassword(@Path("email") String email, @Body User user);

    @POST("users/{email}/password")
    Observable<Response> resetPasswordInit(@Path("email") String email);

    @POST("users/{email}/password")
    Observable<Response> resetPasswordFinish(@Path("email") String email, @Body User user);
}