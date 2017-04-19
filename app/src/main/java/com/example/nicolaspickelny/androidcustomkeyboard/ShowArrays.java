package com.example.nicolaspickelny.androidcustomkeyboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.nicolaspickelny.androidcustomkeyboard.LetterItem;
import com.example.nicolaspickelny.androidcustomkeyboard.R;

import java.util.ArrayList;

public class ShowArrays extends AppCompatActivity {

    private TextView tvName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_arrays);

        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(getIntent().getStringExtra("name"));
        tvName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }
}
