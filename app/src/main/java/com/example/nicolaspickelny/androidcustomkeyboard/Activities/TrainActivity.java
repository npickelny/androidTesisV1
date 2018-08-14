package com.example.nicolaspickelny.androidcustomkeyboard.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.nicolaspickelny.androidcustomkeyboard.LetterItem;
import com.example.nicolaspickelny.androidcustomkeyboard.R;
import com.example.nicolaspickelny.androidcustomkeyboard.ShowArrays;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import Network.RetrofitAPIService;
import Network.ServerInterface;
import restClases.ResponseCode;
import restClases.ResponseMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private Button btnCounter;
    private Button btnReady;
    private TextView tvCounter;

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

    private EditText etPhrase;
    protected LetterItem[] keyPressArray = new LetterItem[41];
    protected LetterItem[][] keyAirArray = new LetterItem[41][41];

    protected ArrayList<LetterItem[]> trainingData;
    private TextView textToWrite;
    private TextView intentos;
    final Context context = this;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        keyboardView.setPreviewEnabled(false);
        keyboard = new Keyboard(this, R.xml.qwerty);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(keyboardActionListener);

        alTecla = new ArrayList<String>();
        alAire = new ArrayList<String>();

        this.setRandomPhrase();
        this.loadLetterTransformer();

        inicializarArray(keyPressArray);
        inicializarArray(keyAirArray);

        etPhrase = (EditText) findViewById(R.id.editText);
        registerEditText(etPhrase.getId());

        trainingData = new ArrayList<LetterItem[]>();
        frasesArrayTest = new ArrayList<String>();

        tvCounter = (TextView) findViewById(R.id.tvCounter);
        intentos = (TextView) findViewById(R.id.intentos);

        email = getIntent().getStringExtra("email");
        btnReady = (Button) findViewById(R.id.btnReady);
        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfPhraseCompleted();
             }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setHeaderPhrasePluralized();
    }

    private void setHeaderPhrasePluralized() {
        int counter = Integer.parseInt(tvCounter.getText().toString());
        Resources res = getResources();
        String i = res.getQuantityString(R.plurals.intentos, counter);
        intentos.setText(" " + i);
    }

    private void checkIfPhraseCompleted() {
//        if(!phraseCompleted()){
//            showCustomSnackBar("La frase no esta completa");
//            return;
//        }
        trainingData.add(keyPressArray.clone());
        resetAndCount();
        setHeaderPhrasePluralized();

        if(finishTraining()){
            showCustomSnackBar("Mandando al servidor");
            sendTrainingData();
        }
    }

    private void sendTrainingData() {
        ServerInterface retrofit = RetrofitAPIService.getInstance();

        final HashMap<String, String> params = new HashMap<>(2);
        Gson gson = new Gson();

        String trainingDataJSON = gson.toJson(trainingData);

        params.put("trainingData", trainingDataJSON);
        params.put("email", email);

        Log.d(TAG, params.toString());
        Log.d(TAG, email);

        Call<ResponseMessage> sendDataCall = retrofit.sendTrainingData(params);

        sendDataCall.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                ResponseMessage responseMsg = response.body();

                Intent i = new Intent(TrainActivity.this, ShowArrays.class);
                if(responseMsg.getMsg() == "99"){ // si pasa esto es pq algo se cago zarpado
                    return;
                } else {
                    i.putExtra("name", "BUT WE COULDNT RECOGNIZE YOU");
                }
                startActivity(i);
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean finishTraining(){
        if(Integer.parseInt(tvCounter.getText().toString()) == 1)
            return true;
        return false;
    }

    public boolean phraseCompleted(){
        return etPhrase.getText().toString().equalsIgnoreCase(textToWrite.getText().toString());
    }

    private void showCustomSnackBar(String msg){
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        View snackView = snack.getView();
        snackView.setBackgroundColor(ContextCompat.getColor(TrainActivity.this, R.color.GREY));
        snack.show();
    }

    private void decrementCounter(){
        tvCounter.setText(String.valueOf(Integer.parseInt(tvCounter.getText().toString())-1));
        YoYo.with(Techniques.Landing)
                .duration(700)
                .repeat(0)
                .playOn(tvCounter);
    }

    private void resetAndCount() {
        decrementCounter();
        inicializarArray(keyPressArray);
        inicializarArray(keyAirArray);
        etPhrase.setText("");
    }

    private void inicializarArray(LetterItem[][] keyAirArray) {
        for(int i=0; i<41; i++){
            for(int j=0; j<41; j++){
                keyAirArray[i][j] = new LetterItem();
                keyAirArray[i][j].setLetter(hmap.get(i+29));
                keyAirArray[i][j].setLetra2(hmap.get(j+29));
            }
        }
    }

    private void inicializarArray(LetterItem[] keyPressArray) {
        for(int j=0; j<41; j++){
            keyPressArray[j] = new LetterItem();
            keyPressArray[j].setLetter(hmap.get(j+29));
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
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(getResources().getInteger(R.integer.vibration_length));

           if(this.checkNonLeterKeys(primaryCode, "onPress")){
               //67 code for delete
               return;
           }

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
