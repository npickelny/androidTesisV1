package Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.nicolaspickelny.androidcustomkeyboard.R;
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
            String serverUrl = getServerUrl();
            Gson gson = new GsonBuilder().create();
            retrofit = new Retrofit.Builder()
//                    .baseUrl("http://192.168.0.14:3000")
                    .baseUrl("http://b32f3503.ngrok.io")
                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(new OkHttpClient.Builder()
//                            .addInterceptor(new LoggingInterceptor())
//                            .build())
                    .build();
            instance = retrofit.create(ServerInterface.class);
        }
        return instance;

    }

    private String getServerUrl() {
        get
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences_filename), Context.MODE_PRIVATE);
        String defaultValue = "No Url Server";
        String serverUrl = sharedPref.getString(getString(R.string.serverUrl), defaultValue);
        x1.setHint(serverUrl);
        return null;
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
