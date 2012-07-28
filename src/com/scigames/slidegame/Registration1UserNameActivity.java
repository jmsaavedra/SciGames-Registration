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

import com.scigames.slidegame.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scigames.slidegame.SciGamesHttpPoster;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class Registration1UserNameActivity extends Activity {

    
    static final private int BACK_ID = Menu.FIRST;
    static final private int CLEAR_ID = Menu.FIRST + 1;

    private String firstNameIn = "FNAME";
    private String lastNameIn = "LNAME";
    private String passwordIn = "PWORD";
    private String classIdIn = "CLASSID";
    
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private String TAG = "Registration1Activity";
    
    public Registration1UserNameActivity() {
    	

    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    	Log.d(TAG,"super.OnCreate");
        Intent i = getIntent();
        Log.d(TAG,"getIntent");
    	firstNameIn = i.getStringExtra("fName");
    	lastNameIn = i.getStringExtra("lName");
    	passwordIn = i.getStringExtra("pword");
    	classIdIn = i.getStringExtra("mClass");
    	Log.d(TAG,"...getStringExtra");
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.registration1_username);
        Log.d(TAG,"...setContentView");
        // Find the text editor view inside the layout, because we
        // want to do various programmatic things with it.
        firstName = (EditText) findViewById(R.id.first_name);
        /* to hide the keyboard on launch, then open when tap in firstname field */
        firstName.setInputType(InputType.TYPE_NULL);
        firstName.setOnTouchListener(new View.OnTouchListener() {
  			//@Override
			public boolean onTouch(View v, MotionEvent event) {
	        firstName.setInputType(InputType.TYPE_CLASS_TEXT);
	        firstName.onTouchEvent(event); // call native handler
	        return true; // consume touch even
			} 
        });
        lastName = (EditText) findViewById(R.id.last_name);
        //password = (EditText) findViewById(R.id.password);
        Log.d(TAG,"...instantiateEditTexts");
        
        // Hook up button presses to the appropriate event handler.
        ((Button) findViewById(R.id.back)).setOnClickListener(mBackListener);
        ((Button) findViewById(R.id.clear)).setOnClickListener(mClearListener);
        ((Button) findViewById(R.id.continue_button)).setOnClickListener(mContinueButtonListener);
        Log.d(TAG,"...instantiateButtons");
        
        //set info to what we know already
        firstName.setText(firstNameIn);
        lastName.setText(lastNameIn);
        //password.setText(passwordIn);
        Log.d(TAG,"...setTexts with incoming name/pw");
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
    
    
    public void postData() {
    	Log.d(TAG, "...postData()");
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://mysweetwebsite.com/push/register_player.php");//("http://requestb.in/xurt8kxu");
        Log.d(TAG, "...create POST");
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("first_name", firstName.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("last_name", lastName.getText().toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            Log.d(TAG, "...setEntity");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            Log.d(TAG, "...executed");
            Log.d(TAG, response.toString());
            
        } catch (ClientProtocolException e) {
        	Log.d(TAG, e.toString());
        } catch (IOException e) {
        	Log.d(TAG, e.toString());
        }
    } 
    // see http://androidsnippets.com/executing-a-http-post-request-with-httpclient
    
    OnClickListener mContinueButtonListener = new OnClickListener() {
 	   public void onClick(View v) {
   		Log.d(TAG,"mLogInListener.onClick");
   		//final SciGamesHttpPoster poster = new SciGamesHttpPoster("http://mysweetwebsite.com/pull/auth_student.php");
   	    		//("http://requestb.in/rb1n8prb");
   		AsyncTask<String, Void, JSONObject> serverResponse = null;
			
			String[] keyVals = {"first_name", firstName.getText().toString(), "last_name", lastName.getText().toString()};
			serverResponse = new SciGamesHttpPoster(Registration1UserNameActivity.this, "http://mysweetwebsite.com/push/register_player.php").execute(keyVals);

			while(serverResponse == null){
				//wait
				int i=0;
			}
			Log.d(TAG,"...created serverResponse with poster.postData");
			Log.d(TAG,"serverResponse: ");
			Log.d(TAG, serverResponse.toString());
   	}
//    	public void onClick(View v) {
//    		JSONObject serverResponse = null;
//    		//postData();
//    		Log.d(TAG,"...mContinueButtonListener onClick");
//    		SciGamesHttpPoster poster = new SciGamesHttpPoster("http://mysweetwebsite.com/push/register_player.php");
//    		serverResponse = poster.newPostData("first_name", firstName.getText().toString(), "last_name",lastName.getText().toString());
//			Log.d(TAG,"...created serverResponse with poster.postData");
//			//Log.d(TAG,"serverResponse: ");
//			//Log.d(TAG, serverResponse.toString());
//			Log.d(TAG,"serverResponse: ");
//			Log.d(TAG, serverResponse.toString());
//       		Intent i = new Intent(Registration1UserNameActivity.this, Registration2RFIDActivity.class);
//    		Log.d(TAG,"new Intent");
//    		i.putExtra("fName",firstName.getText().toString());
//    		i.putExtra("lName",lastName.getText().toString());
//    		i.putExtra("mPass", passwordIn);
//    		i.putExtra("mClass", classIdIn);
//    		Log.d(TAG,"startActivity...");
//    		Registration1UserNameActivity.this.startActivity(i);
//    		Log.d(TAG,"...startActivity");
//    	}
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
    
    public void onActivityResult(int requestCode, Intent data){
    //public void onActivityResult(int requestCode, int resultCode, Intent data) {     
      super.onActivityResult(requestCode, 1, data); 
      Log.d(TAG,"...super.onActivityResult");
      switch(requestCode) { 

      case (0) : { 
        	Log.d(TAG,"...case(0)");
          //if (resultCode == Activity.RESULT_OK) { 
        	//  Log.d(TAG,"...RESULT_OK");
        	  
        	  ImageView previewThumbnail = new ImageView(this);
        	  Log.d(TAG,"...newImageView");

        	    Bitmap b = BitmapFactory.decodeByteArray(
        	            getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);        
        	  Log.d(TAG,"...BitmapFactory.decodeByteArray");
        	  
        	  previewThumbnail.setImageBitmap(b);
        	  Log.d(TAG,"...setImageBitmap");
        	  
        	  LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
        	  Log.d(TAG,"...new layoutparams");
        	  
        	  previewThumbnail.setLayoutParams(layoutParams);
        	  Log.d(TAG,"...setLayoutParams");
          	//}
          	break;
        	}
      	} 
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
}
    

