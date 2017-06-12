package com.example.nicolaspickelny.androidcustomkeyboard;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class SignUp extends AppCompatActivity {

    private Button btn_signup;
    private EditText input_email;
    private EditText input_lastName;
    private EditText input_name;


    private LinearLayout mainLayout;
    private PopupWindow popUpWindow;
    private LayoutInflater layoutInflater;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        input_email = (EditText) findViewById(R.id.input_email);
        input_lastName = (EditText) findViewById(R.id.input_lastName);
        input_name = (EditText) findViewById(R.id.input_name);

        relativeLayout = (RelativeLayout) findViewById(R.id.activity_sign_up);

        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFieldsAndContinue();
            }
        });
    }

    private void checkFieldsAndContinue() {

        if(input_email.getText().toString().equals("") || input_lastName.getText().toString().equals("") || input_name.getText().toString().equals("")){
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup, null);

            popUpWindow = new PopupWindow(container, 400, 400, false);
            popUpWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, 500, 500);
            Log.e("TATA","FDSDSAFASFDSAFASDFSDAAFS");
        }


//        popUpWindow.showAtLocation(mainLayout, Gravity.BOTTOM, 10, 10);
//        popUpWindow.update(50, 50, 320, 90);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                popUpWindow.dismiss();
//            }
//        },2000);
    }
}
