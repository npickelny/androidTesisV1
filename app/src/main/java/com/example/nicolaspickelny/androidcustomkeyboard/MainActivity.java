package com.example.nicolaspickelny.androidcustomkeyboard;

import android.app.Activity;
import android.content.res.Resources;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText eTexto;
    private TextView beforeTitle;
    private TextView textToWrite;

    private Keyboard keyboard;
    protected KeyboardView keyboardView;

    private Date dOneKeyPress;
    private long tiempoPresionDeTecla;
    private long[][] aux;
    private long tiempoDiferenciaEntreTeclas;
    private boolean primero = false;
    private int primarykeyguardada;
    private Long sueltoTecla;
    private HashMap<Integer, String> hmap;

    protected ListView lvAire, lvTecla;
    protected ArrayList<String> alAire,alTecla;
    protected ArrayAdapter<String> listAdapterAire, listAdapterTecla;

    protected LetterItem[] keyPressArray = new LetterItem[26];
    protected LetterItem[][] keyAirArray = new LetterItem[26][26];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        beforeTitle = (TextView) findViewById(R.id.before_label);
        eTexto = (EditText) findViewById(R.id.before_input);

        this.setRandomPhrase();
        this.loadLetterTransformer();

        inicializarArray(keyPressArray);
        inicializarArray(keyAirArray);

        lvAire = (ListView) findViewById(R.id.listViewAire);
        alAire = new ArrayList<String>();
        listAdapterAire = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alAire);
        TextView textView2 = new TextView(this);
        textView2.setText("Diferencia entre Teclas");
        lvAire.addHeaderView(textView2);
        lvAire.setAdapter(listAdapterAire);

        lvTecla = (ListView) findViewById(R.id.listViewTecla);
        alTecla = new ArrayList<String>();
        listAdapterTecla = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alTecla);
        TextView textView = new TextView(this);
        textView.setText("Presion de Tecla");
        lvTecla.addHeaderView(textView);
        lvTecla.setAdapter(listAdapterTecla);

        keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        keyboardView.setPreviewEnabled(false);
        keyboard = new Keyboard(this, R.xml.qwerty);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(keyboardActionListener);

        registerEditText(R.id.before_input);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void inicializarArray(LetterItem[][] keyAirArray) {
        for(int i=0; i<26; i++){
            for(int j=0; j<26; j++){
                keyAirArray[i][j] = new LetterItem();
            }
        }
    }

    private void inicializarArray(LetterItem[] keyPressArray) {
        for(int j=0; j<26; j++){
            keyPressArray[j] = new LetterItem();
        }
    }

    private void setRandomPhrase(){
        textToWrite = (TextView) findViewById(R.id.textToWrite);
        Random rand = new Random();
        int strRand = rand.nextInt(5) + 1;
        Resources res = getResources();
        String[] frases = res.getStringArray(R.array.frases);
        textToWrite.setText(frases[strRand-1]);

    }


    public KeyboardView.OnKeyboardActionListener keyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
            dOneKeyPress = new Date();
            if(primero != false) {
                Date onpress = new Date();
                tiempoDiferenciaEntreTeclas = onpress.getTime() - sueltoTecla;
                Log.d("TestAire","TestAire: tecla "+hmap.get(primarykeyguardada-29) +" "+hmap.get(primaryCode-29)+" -- "+ String.valueOf(tiempoDiferenciaEntreTeclas));

                alAire.add("tecla "+hmap.get(primarykeyguardada-29) +" "+hmap.get(primaryCode-29)+" -- "+ String.valueOf(tiempoDiferenciaEntreTeclas));

                keyAirArray[primarykeyguardada-29][primaryCode-29].addValue(tiempoDiferenciaEntreTeclas);

                listAdapterAire.notifyDataSetChanged();
            }
            else{
                primero = true;
            }
        }

        @Override
        public void onRelease(int primaryCode) {

            //eTexto.setText(eTexto.getText().toString() + (primaryCode));//getKeyForPrimaryCode

            Date dOneKeyRelease = new Date();
            primarykeyguardada=primaryCode;
            sueltoTecla = dOneKeyRelease.getTime();
            tiempoPresionDeTecla = dOneKeyRelease.getTime()- dOneKeyPress.getTime();
            Log.d("TestTecla","TestTecla: tecla "+hmap.get(primaryCode-29)+" -- "+ String.valueOf(tiempoPresionDeTecla));


            alTecla.add("tecla "+hmap.get(primaryCode)+" -- "+ String.valueOf(tiempoPresionDeTecla));
            int a = KeyEvent.KEYCODE_E;

            //menos 29 pq el abecedario esta defasado.... 29 = a = pos 0
            keyPressArray[primaryCode-29].addValue(tiempoPresionDeTecla);

            listAdapterTecla.notifyDataSetChanged();
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            long eventTime = System.currentTimeMillis();
            KeyEvent event = new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_DOWN, primaryCode, 0, 0, 0, 0, KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE);
            dispatchKeyEvent(event);

            //int start = eTexto.getSelectionStart();
            //KeyEvent.KEYCODE_BUTTON_A
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() { }

        @Override
        public void swipeRight() {  }

        @Override
        public void swipeDown() {  }

        @Override
        public void swipeUp() {  }
    };

    //protected abstract int getLayoutResourceId();

    //protected abstract void displayCalculatedResult();

    public void registerEditText(int resid) {
        // Find the EditText 'res_id'
        final EditText edittext = (EditText) findViewById(resid);

        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showCustomKeyboard(v);
                }
                else hideCustomKeyboard();
            }
        });
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomKeyboard(v);
            }
        });
        edittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);              // Restore input type
                edittext.setSelection(edittext.getText().length());
                return true; // Consume touch event
            }
        });
    }

    public void hideCustomKeyboard() {
        keyboardView.setVisibility(View.GONE);
        keyboardView.setEnabled(false);
    }

    public void showCustomKeyboard(View v) {
        keyboardView.setVisibility(View.VISIBLE);
        keyboardView.setEnabled(true);
        if (v != null) {
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public boolean isCustomKeyboardVisible() {
        return keyboardView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onBackPressed() {
        if (isCustomKeyboardVisible()) hideCustomKeyboard();
        else this.finish();
    }

    private void loadLetterTransformer(){
        hmap = new HashMap<Integer, String>();
        hmap.put(29,"a");
        hmap.put(30,"b");
        hmap.put(31,"c");
        hmap.put(32,"d");
        hmap.put(33,"e");
        hmap.put(34,"f");
        hmap.put(35,"g");
        hmap.put(36,"h");
        hmap.put(37,"i");
        hmap.put(38,"j");
        hmap.put(39,"k");
        hmap.put(40,"l");
        hmap.put(41,"m");
        hmap.put(42,"n");
        hmap.put(43,"o");
        hmap.put(44,"p");
        hmap.put(45,"q");
        hmap.put(46,"r");
        hmap.put(47,"s");
        hmap.put(48,"t");
        hmap.put(49,"u");
        hmap.put(50,"v");
        hmap.put(51,"w");
        hmap.put(52,"x");
        hmap.put(53,"y");
        hmap.put(54,"z");
    }
}