package com.example.nicolaspickelny.androidcustomkeyboard.Activities;

import android.app.Activity;
import android.content.res.Resources;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.nicolaspickelny.androidcustomkeyboard.LetterItem;
import com.example.nicolaspickelny.androidcustomkeyboard.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TrainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private Button btnCounter;
    private Button btnReady;
    private TextView tvCoutner;

    private ListView listViewTest;

    private Keyboard keyboard;
    protected KeyboardView keyboardView;
    private ArrayList<String> frasesArrayTest;
    private ArrayAdapter<String> listAdapterTest;


    private Date dOneKeyPress;
    private long tiempoPresionDeTecla;
    private long[][] aux;
    private long tiempoDiferenciaEntreTeclas;
    private boolean primero = false;
    private int primarykeyguardada;
    private Long sueltoTecla;
    private HashMap<Integer, String> hmap;
    protected ArrayList<String> alAire, alTecla;
    private TextView tvResult;

    private EditText editText;
    protected LetterItem[] keyPressArray = new LetterItem[41];
    protected LetterItem[][] keyAirArray = new LetterItem[41][41];

    protected ArrayList<LetterItem[]> trainingData;
    private TextView textToWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        keyboardView.setPreviewEnabled(false);
        keyboard = new Keyboard(this, R.xml.qwerty);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(keyboardActionListener);

        editText = (EditText) findViewById(R.id.editText);
        registerEditText(editText.getId());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tvResult = (TextView) findViewById(R.id.tvResult);
        trainingData = new ArrayList<LetterItem[]>();

        frasesArrayTest = new ArrayList<String>();

        alTecla = new ArrayList<String>();
        alAire = new ArrayList<String>();

        tvCoutner = (TextView) findViewById(R.id.textView4);

        btnReady = (Button) findViewById(R.id.btnReady);
        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAndCount();

            }
        });



        btnCounter = (Button) findViewById(R.id.button);
        btnCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementCounter();
            }
        });

        this.setRandomPhrase();
        this.loadLetterTransformer();

        inicializarArray(keyPressArray);
        inicializarArray(keyAirArray);
    }

    private void decrementCounter(){
        tvCoutner.setText(String.valueOf(Integer.parseInt(tvCoutner.getText().toString())-1));
        YoYo.with(Techniques.Landing)
                .duration(700)
                .repeat(0)
                .playOn(tvCoutner);
    }

    private void resetAndCount() {
        trainingData.add(keyPressArray);
        decrementCounter();
        inicializarArray(keyPressArray);
        inicializarArray(keyAirArray);
        tvResult.setText("");
        editText.setText("");
    }

    private void inicializarArray(LetterItem[][] keyAirArray) {
        for(int i=0; i<41; i++){
            for(int j=0; j<41; j++){
                keyAirArray[i][j] = new LetterItem();
                keyAirArray[i][j].setLetra1(hmap.get(i+29));
                keyAirArray[i][j].setLetra2(hmap.get(j+29));
            }
        }
    }

    private void inicializarArray(LetterItem[] keyPressArray) {
        for(int j=0; j<41; j++){
            keyPressArray[j] = new LetterItem();
            keyPressArray[j].setLetra1(hmap.get(j+29));
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
           if(this.checkNonLeterKeys(primaryCode, "onPress")){
               //67 code for delete
               return;
           }
           tvResult.setText(tvResult.getText().toString() + hmap.get(primaryCode));

           dOneKeyPress = new Date();
           if(primero != false) {
               Date onpress = new Date();
               tiempoDiferenciaEntreTeclas = onpress.getTime() - sueltoTecla;
               Log.d("TestAire","TestAire: tecla "+hmap.get(primarykeyguardada) +" "+hmap.get(primaryCode)+" -- "+ String.valueOf(tiempoDiferenciaEntreTeclas));
               alAire.add("tecla "+hmap.get(primarykeyguardada) +" "+hmap.get(primaryCode)+" -- "+ String.valueOf(tiempoDiferenciaEntreTeclas));
               keyAirArray[primarykeyguardada-29][primaryCode-29].addValue(tiempoDiferenciaEntreTeclas);
           }
           else{
               primero = true;
           }
        }

        @Override
        public void onRelease(int primaryCode) {

            //eTexto.setText(eTexto.getText().toString() + (primaryCode));//getKeyForPrimaryCode
            if(this.checkNonLeterKeys(primaryCode, "onRelease")){
                return;
            }

            Date dOneKeyRelease = new Date();
            primarykeyguardada=primaryCode;
            sueltoTecla = dOneKeyRelease.getTime();
            tiempoPresionDeTecla = dOneKeyRelease.getTime()- dOneKeyPress.getTime();
            Log.d("TestTecla","TestTecla: tecla "+hmap.get(primaryCode)+" -- "+ String.valueOf(tiempoPresionDeTecla));

            alTecla.add("tecla "+hmap.get(primaryCode)+" -- "+ String.valueOf(tiempoPresionDeTecla));

            //menos 29 pq el abecedario esta defasado.... 29 = a = pos 0
            keyPressArray[primaryCode-29].addValue(tiempoPresionDeTecla);
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            long eventTime = System.currentTimeMillis();
            KeyEvent event = new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_DOWN, primaryCode, 0, 0, 0, 0, KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE);
            dispatchKeyEvent(event);
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

        private boolean checkNonLeterKeys(int code, String trigger){
            if(code == -3){
                if (isCustomKeyboardVisible()) hideCustomKeyboard();
                return true;
            }
            if(code == 67){
//                if(trigger.equals("onRelease")){
//
//                }
                    //deleteLastLetter();
                return true;
            }
            return false;
        }
    };

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
        hmap.put(62,"espacio");
        hmap.put(55,",");
        hmap.put(56,".");
//        hmap.put(67,"delete");
    }

}
