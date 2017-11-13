package com.example.nicolaspickelny.androidcustomkeyboard.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nicolaspickelny.androidcustomkeyboard.R;

import java.util.HashMap;

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

    private Button btnLogin;
    private TextView etSignUp;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(progressDialog.isShowing()){
//            progressDialog.dismiss();
//        }
//    }

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

    public void loginStep2(String email){
        final HashMap<String, String> params = new HashMap<>(2);
        params.put("email", email);
        Call<User> loginCall = RetrofitAPIService.getInstance().login(params);
        loginCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressDialog.dismiss();

                User usr = response.body();
                if (usr == null) {
                    showCustomSnackBar("The email provided is not recognized");
                }

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                i.putExtra("userEmail", email);
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
