package com.example.nicolaspickelny.androidcustomkeyboard.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.nicolaspickelny.androidcustomkeyboard.R;

import java.util.HashMap;

import Network.RetrofitAPIService;
import restClases.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private Button btn_signup;
    private EditText input_email;
    private EditText input_lastName;
    private EditText input_name;

    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        input_email = (EditText) findViewById(R.id.input_email);
        input_lastName = (EditText) findViewById(R.id.input_lastName);
        input_name = (EditText) findViewById(R.id.input_name);

//        relativeLayout = (RelativeLayout) findViewById(R.id.activity_sign_up);

        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgresPopUp();
                checkFieldsAndContinue();
            }
        });
    }

    private void showProgresPopUp() {

        progressDialog = new ProgressDialog(SignUpActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

    }

    private void checkFieldsAndContinue() {

        if(input_email.getText().toString().equals("") || input_lastName.getText().toString().equals("") || input_name.getText().toString().equals("")){
           showCustomSnackBar("Hay Campos Vacios");
        }

        final HashMap<String, String> params = new HashMap<>(2);
        params.put("email", input_email.getText().toString());
        params.put("name", input_lastName.getText().toString());
        params.put("lastName", input_name.getText().toString());


        Call<User> signUpProcess = RetrofitAPIService.getInstance().signup(params);

        signUpProcess.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressDialog.dismiss();

                /*CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR
                **CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR
                **CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR CHECK FOR ERROR*/

                Intent i = new Intent(SignUpActivity.this, TrainActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                showCustomSnackBar("Server Error");

                Intent i = new Intent(SignUpActivity.this, TrainActivity.class);
                startActivity(i);
            }
        });

    }


    private void showCustomSnackBar(String msg){
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        View snackView = snack.getView();
        snackView.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.GREY));
        snack.show();
    }
}