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

package com.scigames.registration;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.scigames.registration.SciGamesHttpPoster;
import com.scigames.registration.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
//import android.view.KeyEvent;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class Registration1UserNameActivity extends Activity implements SciGamesListener{

    static final private int BACK_ID = Menu.FIRST;
    static final private int CLEAR_ID = Menu.FIRST + 1;

    private String firstNameIn = "FNAME";
    private String lastNameIn = "LNAME";
    private String passwordIn = "STUDENTID";
    
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText password_confirm;
    private String TAG = "Registration1Activity";
    
    SciGamesHttpPoster task = new SciGamesHttpPoster(Registration1UserNameActivity.this,"http://mysweetwebsite.com/push/register_player.php");
    AlertDialog alertDialog;
    
    public Registration1UserNameActivity() {
    	

    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Log.d(TAG,"super.OnCreate");
    	
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        Intent i = getIntent();
        Log.d(TAG,"...getIntent");
    	firstNameIn = i.getStringExtra("fName");
    	lastNameIn = i.getStringExtra("lName");
    	passwordIn = i.getStringExtra("pword");

    	Log.d(TAG,"...getStringExtra");
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.registration1_username);
        Log.d(TAG,"...setContentView");

        lastName = (EditText) findViewById(R.id.last_name);
        password = (EditText) findViewById(R.id.password);
        password_confirm = (EditText) findViewById(R.id.confirm_password);
        firstName = (EditText) findViewById(R.id.first_name);
        /* to hide the keyboard on launch, then open when tap in firstname field */
        firstName.setCursorVisible(false);
        firstName.setInputType(InputType.TYPE_NULL);
        firstName.setOnTouchListener(new View.OnTouchListener() {
  			//@Override
			public boolean onTouch(View v, MotionEvent event) {
				firstName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		        firstName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		        firstName.setCursorVisible(true);
		        firstName.onTouchEvent(event); // call native handler
		        return true; // consume touch even
			} 
        });

        Log.d(TAG,"...instantiateEditTexts");
        
        // Hook up button presses to the appropriate event handler.
        ((Button) findViewById(R.id.continue_button)).setOnClickListener(mContinueButtonListener);
        Log.d(TAG,"...instantiateButtons");
        
	    Typeface ExistenceLightOtf = Typeface.createFromAsset(getAssets(),"fonts/Existence-Light.ttf");
	    Typeface Museo300Regular = Typeface.createFromAsset(getAssets(),"fonts/Museo300-Regular.otf");
	    Typeface Museo500Regular = Typeface.createFromAsset(getAssets(),"fonts/Museo500-Regular.otf");
	    Typeface Museo700Regular = Typeface.createFromAsset(getAssets(),"fonts/Museo700-Regular.otf");
	    
	    setEditTextFont(Museo500Regular, firstName, lastName, password, password_confirm);

        
        //set info to what we know already
        firstName.setText(firstNameIn);
        lastName.setText(lastNameIn);
        //password.setText(passwordIn);
        Log.d(TAG,"...setTexts with incoming name/pw");
        
        //set listener
        task.setOnResultsListener(this);
        
		alertDialog = new AlertDialog.Builder(
		        Registration1UserNameActivity.this).create();
	    // Setting Dialog Title
	    alertDialog.setTitle("Login Failed");
	    // Setting Dialog Message
	    alertDialog.setMessage("Welcome to AndroidHive.info");
	    // Setting Icon to Dialog
	    //alertDialog.setIcon(R.drawable.tick);
	
	    alertDialog.setButton(RESULT_OK,"OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        // Write your code here to execute after dialog closed
	        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
	        }
	    });
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
        menu.findItem(CLEAR_ID).setVisible(firstName.getText().length() > 0);
        menu.findItem(CLEAR_ID).setVisible(lastName.getText().length() > 0);
        //menu.findItem(CLEAR_ID).setVisible(password.getText().length() > 0);

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
        	lastName.setText("");
            firstName.setText("");
            password.setText("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
        
    OnClickListener mContinueButtonListener = new OnClickListener() {
 	   public void onClick(View v) {
   		Log.d(TAG,"mLogInListener.onClick");
	   		if (isNetworkAvailable()){
	   			if(password.getText().toString().equals(password_confirm.getText().toString())){
				    task.cancel(true);
				    //create a new async task for every time you hit login (each can only run once ever)
				   	task = new SciGamesHttpPoster(Registration1UserNameActivity.this,"http://mysweetwebsite.com/push/register_player.php");
				    //set listener
			        task.setOnResultsListener(Registration1UserNameActivity.this);	
		   			
					String[] keyVals = {"first_name", firstName.getText().toString(), "last_name", lastName.getText().toString(), "pw", password.getText().toString()};
					AsyncTask<String, Void, JSONObject> serverResponse = null;
					serverResponse = task.execute(keyVals);
		
					Log.d(TAG,"...created serverResponse with poster.postData");
					Log.d(TAG,"serverResponse: ");
					Log.d(TAG, serverResponse.toString());
	   			} else {
	   				//show error!
	   				Log.d(TAG,"PASSWORDS DO NOT MATCH");
	   				alertDialog.setMessage("Passwords do not match!");
	   				alertDialog.show();
	   			}
	   		} else {
				alertDialog.setMessage("You're not connected to the internet. Make sure this tablet is logged into a working Wifi Network.");
				alertDialog.show();
	   		}
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

    /**
     * A call-back for when the user presses the clear button.
     */
    OnClickListener mClearListener = new OnClickListener() {
        public void onClick(View v) {
        	lastName.setText("");
            firstName.setText("");
            //password.setText("");
        }
    };      
    
//    public void onActivityResult(int requestCode, Intent data){
//    //public void onActivityResult(int requestCode, int resultCode, Intent data) {     
//      super.onActivityResult(requestCode, 1, data); 
//      Log.d(TAG,"...super.onActivityResult");
//      switch(requestCode) { 
//
//      case (0) : { 
//        	Log.d(TAG,"...case(0)");
//          //if (resultCode == Activity.RESULT_OK) { 
//        	//  Log.d(TAG,"...RESULT_OK");
//        	  
//        	  ImageView previewThumbnail = new ImageView(this);
//        	  Log.d(TAG,"...newImageView");
//
//        	    Bitmap b = BitmapFactory.decodeByteArray(
//        	            getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);        
//        	  Log.d(TAG,"...BitmapFactory.decodeByteArray");
//        	  
//        	  previewThumbnail.setImageBitmap(b);
//        	  Log.d(TAG,"...setImageBitmap");
//        	  
//        	  LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
//        	  Log.d(TAG,"...new layoutparams");
//        	  
//        	  previewThumbnail.setLayoutParams(layoutParams);
//        	  Log.d(TAG,"...setLayoutParams");
//          	//}
//          	break;
//        	}
//      	} 
//    }
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
		// TODO Auto-generated method stub
		Log.d(TAG, "REGISTER SUCCEEDED: ");
		for(int i=0; i<serverResponseStrings.length; i++){ //just print everything returned as a String[] for fun
			Log.d(TAG, "["+i+"] "+serverResponseStrings[i]);
		}
//		JSONObject thisStudent = serverResponseJSON.getJSONObject("student");
//		String firstName = thisStudent.getString("first_name");
//		String lastName = thisStudent.getString("last_name");
//		Log.d(TAG, "this student: ");
//		Log.d(TAG, thisStudent.toString());
		
		/****** RFID ACTIVITY INTENT ******/
		Intent i = new Intent(Registration1UserNameActivity.this, Registration2RfidMass_AdkServiceActivity.class); //THIS IS THE CORRECT PAGE
		Log.d(TAG,"new Intent");
		i.putExtra("fName",serverResponseStrings[2]);
		i.putExtra("lName",serverResponseStrings[3]);
		i.putExtra("studentId",serverResponseStrings[0]);
		i.putExtra("visitId",serverResponseStrings[1]);
		i.putExtra("needsRfid","yes");
		Registration1UserNameActivity.this.startActivity(i);
		Log.d(TAG,"...startActivity");
		/**********************************/
	}

	public void failedQuery(String failureReason) {
		// TODO Auto-generated method stub
		Log.d(TAG, "LOGIN FAILED, REASON: " + failureReason);
		alertDialog.setMessage(failureReason);
		alertDialog.show();
	}
	
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
	
    //---- methods for setting fonts!!
    public static void setTextViewFont(Typeface tf, TextView...params) {
        for (TextView tv : params) {
            tv.setTypeface(tf);
        }
    } 
    public static void setEditTextFont(Typeface tf, EditText...params) {
        for (EditText tv : params) {
            tv.setTypeface(tf);
        }
    }  
    public static void setButtonFont(Typeface tf, Button...params) {
        for (Button tv : params) {
            tv.setTypeface(tf);
        }
    }
}
    

