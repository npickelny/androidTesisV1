package com.example.nicolaspickelny.androidcustomkeyboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import Network.ServerInterface;
import restClases.HealthCheck;
import restClases.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login extends AppCompatActivity {

    private EditText etUserMail;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ServerInterface instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        /***********************************************************************
         *********************** RETROFIT REST CLIENT **************************
         ***********************************************************************/
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://192.168.0.11:3000")//"http://portal.axiomexergy.com/")
//                .baseUrl("http://portal.axiomexergy.com")
                .baseUrl("http://192.168.0.12:3000")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        instance = retrofit.create(ServerInterface.class);
    }

    public void loginStep1(View view) {
        etUserMail = (EditText) findViewById(R.id.etEmail);
        String name = etUserMail.getText().toString();
        String email = etUserMail.getText().toString();

        final HashMap<String, String> params = new HashMap<>(2);
        params.put("username", name);
        params.put("email", email);

        final Context context = this;

        Log.d("GAGA","PASO");
        //Call<HealthCheck> hcCall = instance.getHealth();
        Call<User> loginCall = instance.login(params);

//        hcCall.enqueue(new Callback<HealthCheck>() {
//            @Override
//            public void onResponse(Call<HealthCheck> call, Response<HealthCheck> response) {
//                HealthCheck hc = response.body();
////                Log.d("RESPUESTA", hc.getApp().getVersion());
//            }
//
//            @Override
//            public void onFailure(Call<HealthCheck> call, Throwable t) {
//
//            }
//        });

        loginCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User usr = response.body();

                if (usr == null) {
                    Toast.makeText(getApplicationContext(), "The email provided is not recognized", Toast.LENGTH_SHORT);
                }

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                i.putExtra("userEmail", email);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

//        Intent i = new Intent(getApplicationContext(), MainActivity.class);
//        i.putExtra("userEmail", email);
//        startActivity(i);
    }

    private boolean validateUser(String email) {
        return true;

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
