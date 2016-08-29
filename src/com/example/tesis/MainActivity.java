package com.example.tesis;

import java.sql.Date;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	public int cont;
	public EditText myEditText1;
	public String mKeyString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		cont = 0;	
		
		myEditText1 = (EditText)findViewById(R.id.editText1);	
		myEditText1.addTextChangedListener(generalTextWatcher);
		
	}
			
	private TextWatcher generalTextWatcher = new TextWatcher() {    
	    @Override
	    public void onTextChanged(CharSequence s, int start, int before,int count) {
	    	mKeyString = s.toString ();
	    	Log.d("test:",mKeyString+" onTextChanged");
	    	
	        char currentChar = s.charAt(before); // currently typed character
	        Log.d("test:",currentChar+" ESTE CHARACTER");
	        Log.d("test",String.valueOf(System.currentTimeMillis()/1000));
        

	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,int after) {
	    	mKeyString = s.toString ();
	    	Log.d("test:",mKeyString + " beforeTextChanged");

	    	
	    }
	    @Override
	    public void afterTextChanged(Editable s) {
	    	
	    }
	};

	
		
	
	public void incrementa(View vista) {
		cont++;
		mostrar();
	}

	public void restar(View vista) {
		cont--;
		mostrar();
	}

	public void resetear(View vista) {
		EditText et = (EditText) findViewById(R.id.editText1);
		int num = Integer.parseInt(et.getText().toString());
		cont = num;

		mostrar();
	}

	public void mostrar() {
		TextView tv = (TextView) findViewById(R.id.textView2);
		tv.setText("Contador: " + cont);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		Log.d("hello", String.valueOf(event.getKeyCode()));
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		char c = (char) event.getUnicodeChar();
		System.out.println(c);
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		char c = (char) event.getUnicodeChar();
		Log.d("hello", String.valueOf(c));
		Log.v("hello", String.valueOf(c));
		Log.e("hello", String.valueOf(c));
		Log.w("myApp", "no network");

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyMultiple(keyCode, repeatCount, event);
	}

}
