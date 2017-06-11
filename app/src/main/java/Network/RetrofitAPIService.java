package Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAPIService {

    private static ServerInterface instance;
    private static Retrofit retrofit;

    public static ServerInterface getInstance() {
        if (instance == null) {

            Gson gson = new GsonBuilder().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.12:3000")
                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(new OkHttpClient.Builder()
//                            .addInterceptor(new LoggingInterceptor())
//                            .build())
                    .build();
            instance = retrofit.create(ServerInterface.class);
        }
        return instance;

    }

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static void setRetrofit(Retrofit retrofit) {
        RetrofitAPIService.retrofit = retrofit;
    }
}
