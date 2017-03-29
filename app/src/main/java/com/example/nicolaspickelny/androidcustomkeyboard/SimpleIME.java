package com.example.nicolaspickelny.androidcustomkeyboard;
import java.util.Date;


import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class SimpleIME extends InputMethodService implements OnKeyboardActionListener {

	private KeyboardView kv;
    private Keyboard keyboard;
    private boolean caps = false;
	private Date dOneKeyPress;
    private long diferencia;
    private  long[][] aux;
    private long resta;
    private boolean primero = false;
    private Long sueltoTecla;


    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }
        
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {        
        InputConnection ic = getCurrentInputConnection();

        switch(primaryCode){
        case Keyboard.KEYCODE_DELETE :
            ic.deleteSurroundingText(1, 0);
            break;
        case Keyboard.KEYCODE_SHIFT:
            caps = !caps;
            keyboard.setShifted(caps);
            kv.invalidateAllKeys();
            break;
        case Keyboard.KEYCODE_DONE:
            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            break;
        default:
            char code = (char)primaryCode;
            if(Character.isLetter(code) && caps){
                code = Character.toUpperCase(code);
            }
            ic.commitText(String.valueOf(code),1);                  
        }
    }
 
    @Override
    public void onPress(int primaryCode) {
    	dOneKeyPress = new Date();
        Log.d("blbl",String.valueOf(primero));
        if(primero != false) {
            Date onpress = new Date();
            resta = onpress.getTime() - sueltoTecla;
            Log.d("TestAire","TestAire: tecla "+String.valueOf(primaryCode)+" -- "+ String.valueOf(resta));
        }
        else{
            primero = true;
        }
    }
 
    @Override
    public void onRelease(int primaryCode) { 
    	//long diferencia;
    	Date dOneKeyRelease = new Date();
        sueltoTecla = dOneKeyRelease.getTime();
        diferencia = dOneKeyRelease.getTime()- dOneKeyPress.getTime();
        Log.d("TestTecla","TestTecla: tecla "+String.valueOf(primaryCode)+" -- "+ String.valueOf(diferencia));
    	// Toast.makeText(getBaseContext() ,"Tiempo presionado: " + String.valueOf(diferencia), Toast.LENGTH_LONG).show();

    }



    @Override
    public void onText(CharSequence text) {     
    }
 
    @Override
    public void swipeDown() {   
    }
 
    @Override
    public void swipeLeft() {
    }
 
    @Override
    public void swipeRight() {
    }
 
    @Override
    public void swipeUp() {
    }

}
