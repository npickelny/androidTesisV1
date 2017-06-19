package com.example.nicolaspickelny.androidcustomkeyboard.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.nicolaspickelny.androidcustomkeyboard.R;

import java.util.Date;

public class TrainActivity2 extends AppCompatActivity {

    String TAG = this.getClass().getSimpleName();
    private EditText postingET;
    private Date dOneKeyPress;
    private Date dOneKeyPress2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        postingET = (EditText) findViewById(R.id.editText);

        postingET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("ANTES", s.toString());
                dOneKeyPress = new Date();
                Log.i("FECHAAAA", String.valueOf(dOneKeyPress.getTime()));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("DSP", s.toString());
                dOneKeyPress2 = new Date();
                Log.i("FECHAAAA", String.valueOf(dOneKeyPress2.getTime()));

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, s.toString());
            }
        });
    }

}
