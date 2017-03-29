package Interfaces;

import java.util.List;

import restClases.HealthCheck;
import restClases.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServerInterface {

    //"api/{owner}/{repo}/contributors")
    // @Path("owner") String owner,@Path("repo") String repo)
    @GET("/api/health")
    Call<HealthCheck> getHealth();

    @FormUrlEncoded
    @POST("/api/user/login")
    Call<User> login(@Field("name") String name);
}


