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
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
//import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.view.KeyEvent;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class Registration3MassActivity extends Activity implements SciGamesListener{

    private String TAG = "Registration3Activity";
    
    static final private int BACK_ID = Menu.FIRST;
    static final private int CLEAR_ID = Menu.FIRST + 1;

    private String firstNameIn = "FNAME";
    private String lastNameIn = "LNAME";
    private String studentIdIn = "";
    private String visitIdIn = "";
    
    private EditText thisMass;
    
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private int currProgress = 0;
    
    private String myMass = "";
    
    SciGamesHttpPoster task = new SciGamesHttpPoster(Registration3MassActivity.this,"http://mysweetwebsite.com/push/new_mass.php");
    
    
    public Registration3MassActivity() {
    	

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
        setContentView(R.layout.registration3_mass);
        Log.d(TAG,"...setContentView");
        
        // Find the text editor view inside the layout, because we
        // want to do various programmatic things with it.
        thisMass = (EditText) findViewById(R.id.mass);
        /* to hide the keyboard on launch, then open when tap in firstname field */
        thisMass.setInputType(InputType.TYPE_NULL);
        thisMass.setOnTouchListener(new View.OnTouchListener() {
  			//@Override
			public boolean onTouch(View v, MotionEvent event) {
  			thisMass.setInputType(InputType.TYPE_CLASS_TEXT);
  			thisMass.onTouchEvent(event); // call native handler
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
        
        //set info to what we know already
        //firstName.setText(firstNameIn);
        //lastName.setText(lastNameIn);
        
        // Hook up button presses to the appropriate event handler.
        ((Button) findViewById(R.id.back)).setOnClickListener(mBackListener);
        ((Button) findViewById(R.id.continue_button)).setOnClickListener(mContinueButtonListener);
        ((Button) findViewById(R.id.scan)).setOnClickListener(mScanButtonListener);
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
        	thisMass.setText("");
            //firstName.setText("");
            //password.setText("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    OnClickListener mContinueButtonListener = new OnClickListener(){
    	public void onClick(View v) {
    		
 		    task.cancel(true);
		    //create a new async task for every time you hit login (each can only run once ever)
		   	task = new SciGamesHttpPoster(Registration3MassActivity.this,"http://mysweetwebsite.com/push/new_mass.php");
		    //set listener
	        task.setOnResultsListener(Registration3MassActivity.this);
	        		

			//prepare key value pairs to send
			String[] keyVals = {"student_id", studentIdIn, "visit_id", visitIdIn, "mass", thisMass.getText().toString()}; 
			Log.d(TAG,"keyVals passed: ");
			Log.d(TAG, "student_id"+ studentIdIn+ "visit_id"+ visitIdIn+ "mass"+ thisMass.getText().toString());
			
			//create AsyncTask, then execute
			AsyncTask<String, Void, JSONObject> serverResponse = null;
			serverResponse = task.execute(keyVals);
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

    /**
     * A call-back for when the user presses the clear button.
     */
    OnClickListener mScanButtonListener = new OnClickListener() {
        public void onClick(View v) {
        	Log.d(TAG,"...mScanButtonListener onClick");
        	// prepare for a progress bar dialog
			progressBar = new ProgressDialog(v.getContext());
			progressBar.setCancelable(true);
			progressBar.setMessage("Measuring Your Mass ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressBar.setProgress(0);
			progressBar.setMax(100);
			progressBar.show();
			//reset progress bar status
			progressBarStatus = 0;
			//reset currProgress
			currProgress = 0;
			
			new Thread (new Runnable() {
				public void run() {
					while (progressBarStatus < 100) {
					  // process some tasks
						progressBarStatus = measureMass();
						// your computer is too fast, sleep 1 second
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// Update the progress bar
						progressBarHandler.post(new Runnable() {
						
							public void run() {
								progressBar.setProgress(progressBarStatus);
							}
						});
					}
					// ok, time is up
					if (progressBarStatus >= 100) {
						Log.d(TAG, "...progressBar time's up");
						// sleep 2 seconds, so that you can see the 100%
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Message msg = new Message();
				        String textTochange = myMass;
				        msg.obj = textTochange;
				        mHandler.sendMessage(msg);
						// close the progress bar dialog
						progressBar.dismiss();
						Log.d(TAG, "...progressBar.dismiss()");
        			}

				}

			}).start(); 	
		}
        
		Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg){
	        	String _mass = (String)msg.obj;
	    		//check if RFID got read
	    		//	Log.d(TAG, "check if rfID == 'searching'...");
	    	   	if(_mass == "measuring"){
	    	           //EditText braceletIdResp = (EditText)findViewById(R.id.bracelet_id);
	    	            //braceletIdResp.setText("No Bracelet Found, please try again");
	    	   		thisMass = (EditText) findViewById(R.id.mass);
	    	   		Log.d(TAG, "...myMass == 'measuring'");
	    	   		thisMass.setText("No Mass Captured, please try again");
	    	    	Log.d(TAG, "...measuredMass.setText none found");
	    	  	}
	        
	    	   	//call setText here
	        }
		};

    };      
  
	// progress bar simulator... will hold ADK stuff...
	public int measureMass() {
		myMass = "measuring";
		if (currProgress <= 95) {
			currProgress++;
			return currProgress;
			// wait for Arduino here
		} else {
			return 100;
		}
	}
 
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

	public void onResultsSucceeded(String[] serverResponseStrings,
		JSONObject serverResponseJSON) throws JSONException {
		
		Log.d(TAG, "LOGIN SUCCEEDED: ");
		for(int i=0; i<serverResponseStrings.length; i++){ //just print everything returned as a String[] for fun
			Log.d(TAG, "["+i+"] "+serverResponseStrings[i]);
		}
		
		JSONObject thisStudent;
		thisStudent = serverResponseJSON.getJSONObject("student");
		
		Log.d(TAG, "this student: ");
		Log.d(TAG, thisStudent.toString());
		
		Log.d(TAG,"...onResultsSucceeded");
   		Intent i = new Intent(Registration3MassActivity.this, Registration4PhotoActivity.class);
		Log.d(TAG,"new Intent");
		i.putExtra("fName", firstNameIn);
		i.putExtra("lName", lastNameIn);
		i.putExtra("studentId",serverResponseStrings[0]);
		i.putExtra("visitId",serverResponseStrings[1]);
		Log.d(TAG,"startActivity...");
		Registration3MassActivity.this.startActivity(i);
		Log.d(TAG,"...startActivity");
		
	}

	public void failedQuery(String failureReason) {
		// TODO Auto-generated method stub
		Log.d(TAG, "QUERY FAILED, REASON: " + failureReason);
	}
}
    

