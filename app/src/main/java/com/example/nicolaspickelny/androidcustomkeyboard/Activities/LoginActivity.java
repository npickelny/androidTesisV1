package com.example.nicolaspickelny.androidcustomkeyboard.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nicolaspickelny.androidcustomkeyboard.R;

import java.util.HashMap;
import java.util.function.ToLongBiFunction;

import Network.RetrofitAPIService;
import Network.ServerInterface;
import restClases.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    private EditText etUserMail;
    private CheckBox checkBox;
    private ServerInterface instance;

    private ImageButton btnSettings;
    private Button btnLogin;
    private Button btnJustTrain;
    private TextView etSignUp;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();

        btnSettings = (ImageButton) findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(LoginActivity.this, btnSettings);
                popup.getMenuInflater().inflate(R.menu.settings_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        if (item.getTitle().toString().equalsIgnoreCase("Server Url")) {
                            View view = inflater.inflate(R.layout.server_url_layout, (ViewGroup)findViewById(R.id.ll_server_url));
                            EditText x1 = (EditText) view.findViewById(R.id.et_server_url);
                            setServerUrl(x1);
                            builder.setView(view)
                                    .setTitle("Set Server Url")
                                    .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            Dialog d = (Dialog) dialog;
                                            EditText x = (EditText) d.findViewById(R.id.et_server_url);
                                            saveNewServerUrl(x.getText().toString());
                                        }
                                    })
                                    .setNegativeButton("Cancelar", null);

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });

        btnLogin = (Button) findViewById(R.id.butLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgresPopUp();
                loginStep1();
            }
        });
        etSignUp = (TextView) findViewById(R.id.link_signup);
        etSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        btnJustTrain = (Button) findViewById(R.id.btnJustTrain);
        btnJustTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, JustTrainActivity.class);
                startActivity(i);
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(progressDialog.isShowing()){
//            progressDialog.dismiss();
//        }
//    }

    private void saveNewServerUrl(String newSrvUrl) {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences_filename), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.serverUrl), newSrvUrl);
        editor.commit();
    }

    private void setServerUrl(EditText x1) {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences_filename), Context.MODE_PRIVATE);
        String defaultValue = "No Url Server";
        String serverUrl = sharedPref.getString(getString(R.string.serverUrl), defaultValue);
        x1.setHint(serverUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    private void showProgresPopUp() {
        progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }

    public void loginStep1() {

        checkBox = (CheckBox) findViewById(R.id.checkBox1);

        if(!checkBox.isChecked()){
            showCustomSnackBar("Server ByPassed");
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            return;
        }

        showCustomSnackBar("Validating against Server");

        etUserMail = (EditText) findViewById(R.id.Email);
        String email = etUserMail.getText().toString();

        if(email.equalsIgnoreCase("")){
            showCustomSnackBar("Email field is Empty");
            progressDialog.dismiss();
            return;
        }

        loginStep2(email);
    }

    public void loginStep2(final String email){
        final HashMap<String, String> params = new HashMap<>(2);
        params.put("email", email);
        Call<User> loginCall = RetrofitAPIService.getInstance().login(params);
        loginCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressDialog.dismiss();

                User usr = response.body();
                if (usr == null) {
                    showCustomSnackBar("No reconocimos el email");
                    return;
                }

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("email", email);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                showCustomSnackBar("Server Error");
            }
        });
    }

    private void showCustomSnackBar(String msg){
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        View snackView = snack.getView();
        snackView.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.GREY));
        snack.show();
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
