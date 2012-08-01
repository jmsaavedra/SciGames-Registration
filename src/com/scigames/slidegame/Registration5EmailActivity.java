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

//import java.io.ByteArrayInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;

import com.scigames.slidegame.R;

import android.app.Activity;
//import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.KeyEvent;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class Registration5EmailActivity extends Activity implements SciGamesListener{

    private String TAG = "Registration5Activity";
    
    static final private int BACK_ID = Menu.FIRST;
    static final private int CLEAR_ID = Menu.FIRST + 1;

    private String firstNameIn = "FNAME";
    private String lastNameIn = "LNAME";
    private String studentIdIn = "STUDENTID";
    private String visitIdIn = "VISITID";
    private EditText email;
    //private String passwordIn = "PWORD";;
    SciGamesHttpPoster task = new SciGamesHttpPoster(Registration5EmailActivity.this,"http://mysweetwebsite.com/push/update_email.php");
    
    public Registration5EmailActivity() {
    	

    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Log.d(TAG,"super.OnCreate");
    	
        Intent i = getIntent();
        Log.d(TAG,"getIntent");
    	firstNameIn = i.getStringExtra("fName");
    	lastNameIn = i.getStringExtra("lName");
    	studentIdIn = i.getStringExtra("studentId");
    	visitIdIn = i.getStringExtra("visitId");
    	Log.d(TAG,"...getStringExtra");
    	
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.registration5_email);
        Log.d(TAG,"...setContentView");
        
        email = (EditText) findViewById(R.id.email);
        /* to hide the keyboard on launch, then open when tap in firstname field */
        email.setInputType(InputType.TYPE_NULL);
        email.setOnTouchListener(new View.OnTouchListener() {
  			public boolean onTouch(View v, MotionEvent event) {
  			email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
  			email.onTouchEvent(event); // call native handler
	        return true; // consume touch even
			} 
        });
        
        Log.d(TAG,"...instantiateEditTexts");
        
        //display name in greeting sentence
        Resources res = getResources();
        TextView greets = (TextView)findViewById(R.id.greeting);
        greets.setText(String.format(res.getString(R.string.greeting), firstNameIn, lastNameIn));
        Log.d(TAG, greets.toString());
        Log.d(TAG,"...Greetings");
               
        // Hook up button presses to the appropriate event handler.
        ((Button) findViewById(R.id.back)).setOnClickListener(mBackListener);
        ((Button) findViewById(R.id.continue_button)).setOnClickListener(mContinueButtonListener);
        Log.d(TAG,"...instantiateButtons");
        
        //set listener
        task.setOnResultsListener(this);
    }

    /**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"...super.onResume()");
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
        menu.add(0, BACK_ID, 0, R.string.back).setShortcut('0', 'b');
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
       // menu.findItem(CLEAR_ID).setVisible(firstName.getText().length() > 0);
       // menu.findItem(CLEAR_ID).setVisible(lastName.getText().length() > 0);
       // menu.findItem(CLEAR_ID).setVisible(password.getText().length() > 0);

        return true;
    }

    /**
     * Called when a menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case BACK_ID:
            finish();
            return true;
        case CLEAR_ID:
        	email.setText("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    OnClickListener mContinueButtonListener = new OnClickListener(){
    	public void onClick(View v) {
 		    task.cancel(true);
		    //create a new async task for every time you hit login (each can only run once ever)
		   	task = new SciGamesHttpPoster(Registration5EmailActivity.this,"http://mysweetwebsite.com/push/update_email.php");
		    //set listener
	        task.setOnResultsListener(Registration5EmailActivity.this);
	        		

			//prepare key value pairs to send
			String[] keyVals = {"student_id", studentIdIn, "visit_id", visitIdIn, "email", email.getText().toString()}; 
			Log.d(TAG,"keyVals passed: ");
			Log.d(TAG, "student_id:"+ studentIdIn+ " , visit_id:"+ visitIdIn+ " , email:"+ email.getText().toString());
			
			//create AsyncTask, then execute
			//AsyncTask<String, Void, JSONObject> serverResponse = null;
			//serverResponse = task.execute(keyVals);
			task.execute(keyVals);
			Log.d(TAG,"...task.execute(keyVals)");
    		
    	}
    };
    	
    /**
     * A call-back for when the user presses the back button.
     */
    OnClickListener mBackListener = new OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };
    
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

	public void onResultsSucceeded(String[] serverResponseStrings,
			JSONObject serverResponseJSON) throws JSONException {
		Log.d(TAG, "LOGIN SUCCEEDED: ");
		for(int i=0; i<serverResponseStrings.length; i++){ //just print everything returned as a String[] for fun
			Log.d(TAG, "["+i+"] "+serverResponseStrings[i]);
		}
		
		JSONObject thisStudent = serverResponseJSON.getJSONObject("student");
		
		String cartLevel = thisStudent.getString("cart_game_level");
		String slideLevel = thisStudent.getString("slide_game_level");
		String mass = thisStudent.getString("mass");
		String photoUrl = thisStudent.getString("photo");
		String firstName = thisStudent.getString("first_name");
		String lastName = thisStudent.getString("last_name");
		String email = thisStudent.getString("email");
		String pw = thisStudent.getString("pw");
		String classId = thisStudent.getString("class_id");
		String rfid = thisStudent.getString("current_rfid");
		
		Log.d(TAG, "this student: ");
		Log.d(TAG, thisStudent.toString());
		
		Log.d(TAG,"...onResultsSucceeded");
		/****** PROFILE ACTIVITY INTENT ******/
		Intent i = new Intent(Registration5EmailActivity.this, ProfileActivity.class);
		Log.d(TAG,"new Intent");
		i.putExtra("fName",firstName);
		i.putExtra("lName",lastName);
		i.putExtra("photoUrl", photoUrl);
		i.putExtra("mass", mass);
		i.putExtra("cartLevel", cartLevel);
		i.putExtra("slideLevel", cartLevel);
		i.putExtra("email", email);
		i.putExtra("rfid", rfid);
		i.putExtra("password",pw);
		i.putExtra("classId",classId);
		i.putExtra("studentId",serverResponseStrings[0]);
		i.putExtra("visitId",serverResponseStrings[1]);
		Registration5EmailActivity.this.startActivity(i);
		Log.d(TAG,"...startActivity");
		/****** PROFILE ACTIVITY INTENT ******/
		
	}

	public void failedQuery(String failureReason) {
		// TODO Auto-generated method stub
		
	}    
}
    

