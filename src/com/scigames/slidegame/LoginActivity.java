/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scigames.slidegame;

import com.scigames.slidegame.R;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements SciGamesListener,View.OnSystemUiVisibilityChangeListener{
	    
    static final private int QUIT_ID = Menu.FIRST;
    static final private int CLEAR_ID = Menu.FIRST + 1;

    private EditText firstName;
    private EditText lastName;
    public  EditText password;
    public EditText classId;
    private static String TAG = "LoginActivity";
    
    ProgressDialog progressBar;
    AlertDialog alertDialog;
    SciGamesHttpPoster task = new SciGamesHttpPoster(LoginActivity.this,"http://mysweetwebsite.com/pull/auth_student.php");
 
    public LoginActivity() {
    	
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        Log.d(TAG,"super.OnCreate");
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.login_page);

        firstName = (EditText) findViewById(R.id.first_name);
        /* to hide the keyboard on launch, then open when tap in firstname field */
        firstName.setInputType(InputType.TYPE_NULL);
        firstName.setOnTouchListener(new View.OnTouchListener() {
  			//@Override
			public boolean onTouch(View v, MotionEvent event) {
		        firstName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		        firstName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		        firstName.onTouchEvent(event); // call native handler
		        return true; // consume touch even
			} 
        });
        lastName = (EditText) findViewById(R.id.last_name);
        password = (EditText) findViewById(R.id.password);
        classId = (EditText) findViewById(R.id.class_id);

        // Hook up button presses to the appropriate event handler.
        ((Button) findViewById(R.id.clear)).setOnClickListener(mClearListener);
        ((Button) findViewById(R.id.login_button)).setOnClickListener(mLogInListener);
        ((Button) findViewById(R.id.register)).setOnClickListener(mRegisterListener);
        
        //set listener
        task.setOnResultsListener(this);
        
		alertDialog = new AlertDialog.Builder(
		        LoginActivity.this).create();
	    // Setting Dialog Title
	    alertDialog.setTitle("Login Failed");
	    // Setting Dialog Message
	    alertDialog.setMessage("Welcome to AndroidHive.info");
	    // Setting Icon to Dialog
	    //alertDialog.setIcon(R.drawable.tick);
	
	    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        // Write your code here to execute after dialog closed
	        Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
	        }
	    });
    }
    
    //---- runs when user clicks "Let's Go!"
    OnClickListener mLogInListener = new OnClickListener(){
	   public void onClick(View v) {
		   Log.d(TAG,"mLogInListener.onClick");
		   setProgressBarIndeterminateVisibility(true);
		   
		   task.cancel(true);
		    //create a new async task for every time you hit login (each can only run once ever)
		   	task = new SciGamesHttpPoster(LoginActivity.this,"http://mysweetwebsite.com/pull/auth_student.php");
		    //set listener
	        task.setOnResultsListener(LoginActivity.this);
	        
			//prepare login strings
			String thisUn = (firstName.getText().toString())+"_"+(lastName.getText().toString())+"_"+(classId.getText().toString()).toLowerCase();
			String thisPw = password.getText().toString();
			
			//prepare key value pairs to send
			String[] keyVals = {"un", thisUn, "pw", thisPw}; 
			
			//create AsyncTask, then execute
			AsyncTask<String, Void, JSONObject> serverResponse = null;
			serverResponse = task.execute(keyVals);
			Log.d(TAG,"mLogInListener server response:");
			Log.d(TAG,serverResponse.toString());
    	}
    };
    
    //---- this is from the results listener interface (SciGamesListener)
	public void onResultsSucceeded(String[] serverResponseStrings, JSONObject serverResponseJSON) throws JSONException {
		setProgressBarIndeterminateVisibility(false);
		Log.d(TAG, "LOGIN SUCCEEDED: ");
		for(int i=0; i<serverResponseStrings.length; i++){ //just print everything returned as a String[] for fun
			Log.d(TAG, "["+i+"] "+serverResponseStrings[i]);
		}
		JSONObject thisStudent;

		thisStudent = serverResponseJSON.getJSONObject("student");
		
		Log.d(TAG, "this student: ");
		Log.d(TAG, thisStudent.toString());
		
		if(serverResponseStrings[2].equals("false")){ //check active state
			//send to RFID activity
			Log.d(TAG, "off to RFID page!");
    		Intent i = new Intent(LoginActivity.this, Registration2RFIDActivity.class); //THIS IS THE CORRECT PAGE
    		Log.d(TAG,"new Intent");
    		i.putExtra("fName",firstName.getText().toString());
    		i.putExtra("lName",lastName.getText().toString());   		
    		i.putExtra("studentId",serverResponseStrings[0]);
    		i.putExtra("visitId",serverResponseStrings[1]);
    		Log.d(TAG,"startActivity...");
    		LoginActivity.this.startActivity(i);
    		Log.d(TAG,"...startActivity");
		}
		else if(thisStudent.get("mass").equals(null)){
			//send to mass activity
			Log.d(TAG, "off to mass page!");
	   		//Intent i = new Intent(LoginActivity.this, Registration3MassActivity.class); //THIS IS THE CORRECT PAGE
			Intent i = new Intent(LoginActivity.this, Registration4PhotoActivity.class);
    		Log.d(TAG,"new Intent");
    		i.putExtra("fName",firstName.getText().toString());
    		i.putExtra("lName",lastName.getText().toString());   		
    		i.putExtra("studentId",serverResponseStrings[0]);
    		i.putExtra("visitId",serverResponseStrings[1]);
    		Log.d(TAG,"startActivity...");
    		LoginActivity.this.startActivity(i);
    		Log.d(TAG,"...startActivity");

		}
		else if(thisStudent.get("photo").equals(null)){
			//send to photo activity
			Log.d(TAG, "off to photo page!");
		}
		else if(thisStudent.get("email").equals(null)){
			//send to email activity
			Log.d(TAG, "off to email page!");
		}
		else {
			//send directly to their profile, they're all registered.
			Log.d(TAG, "off to profile page!");
		}
	}

	//---- what to do when query is failed
	public void failedQuery(String failureReason) {

		Log.d(TAG, "LOGIN FAILED, REASON: " + failureReason);
		alertDialog.setMessage(failureReason);
		alertDialog.show();
	}
	

	//---- send to 'one-off' registration page
    OnClickListener mRegisterListener = new OnClickListener(){
    	public void onClick(View v) {
    		Log.d(TAG,"mRegisterListener.onClick");
    		Intent i = new Intent(LoginActivity.this, Registration1UserNameActivity.class);
    		Log.d(TAG,"new Intent");
    		i.putExtra("fName",firstName.getText().toString());
    		i.putExtra("lName",lastName.getText().toString());
    		i.putExtra("pword",password.getText().toString());
    		Log.d(TAG,"startActivity...");
    		LoginActivity.this.startActivity(i);
    		Log.d(TAG,"...startActivity");
    	}
    };

    OnClickListener mClearListener = new OnClickListener() {
        public void onClick(View v) {
        	lastName.setText("");
            firstName.setText("");
            password.setText("");
            classId.setText("");
        }
    };
    
    /**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Called when your activity's options menu needs to be created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // We are going to create two menus. Note that we assign them
        // unique integer IDs, labels from our string resources, and
        // given them shortcuts.
        menu.add(0, QUIT_ID, 0, R.string.quit).setShortcut('0', 'b');
        menu.add(0, CLEAR_ID, 0, R.string.clear).setShortcut('1', 'c');

        return true;
    }

    /**
     * Called right before your activity's option menu is displayed.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // Before showing the menu, we need to decide whether the clear
        // item is enabled depending on whether there is text to clear.
        menu.findItem(CLEAR_ID).setVisible(firstName.getText().length() > 0);
        menu.findItem(CLEAR_ID).setVisible(lastName.getText().length() > 0);
        menu.findItem(CLEAR_ID).setVisible(password.getText().length() > 0);
        menu.findItem(CLEAR_ID).setVisible(classId.getText().length() > 0);

        return true;
    }

    /**
     * Called when a menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case QUIT_ID:
            finish();
            return true;
        case CLEAR_ID:
        	lastName.setText("");
            firstName.setText("");
            password.setText("");
            classId.setText("");
            return true;
     //   case PROFILE_ID:
     //   	//send to profile page
     //   	return true;
        }
        return super.onOptionsItemSelected(item);
    }
   
    //---- this function hides the keyboard when the user clicks outside of keyboard when it's open!
    @Override 
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
    return ret;
    }

	public void onSystemUiVisibilityChange(int visibility) {
		// TODO Auto-generated method stub
		//setSystemUiVisibility(0);
	}
}
