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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.scigames.slidegame.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */

public class LoginActivity extends Activity {
	    
    static final private int QUIT_ID = Menu.FIRST;
    static final private int CLEAR_ID = Menu.FIRST + 1;

    private EditText firstName;
    private EditText lastName;
    public  EditText password;
    public EditText classId;
    private static String TAG = "LoginActivity";
    
    ProgressDialog progressBar;
//    private int progressBarStatus = 0;
//    private Handler progressBarHandler = new Handler();
//    private int currProgress = 0;
//    
//    private String serverResponseReceived;
    
    public LoginActivity() {
    	
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
    }

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
   
    
   OnClickListener mLogInListener = new OnClickListener(){
	   public void onClick(View v) {
    		Log.d(TAG,"mLogInListener.onClick");
    		//final SciGamesHttpPoster poster = new SciGamesHttpPoster("http://mysweetwebsite.com/pull/auth_student.php");
    	    		//("http://requestb.in/rb1n8prb");
    		AsyncTask<String, Void, JSONObject> serverResponse = null;
			
			String thisUn = (firstName.getText().toString())+"_"+(lastName.getText().toString())+"_"+(classId.getText().toString());
			thisUn = thisUn.toLowerCase();
			String thisPw = password.getText().toString();
			//serverResponse = poster.newPostData("un", thisUn, "pw",thisPw);
			String[] keyVals = {"un", thisUn, "pw", thisPw};
			serverResponse = new SciGamesHttpPoster("http://mysweetwebsite.com/pull/auth_student.php").execute(keyVals);
			Log.d(TAG,"...created serverResponse with poster.postData");
			Log.d(TAG,"serverResponse: ");
			Log.d(TAG, serverResponse.toString());

			//jsonHandler.handleMessage(msg)
//    		
//			if((serverResponse).has("error")){
//				Log.d(TAG, "BAD LOGIN");
//
//			} else {
//				
//				Log.d(TAG,"GOOD LOGIN");
//				try {
//					Log.d(TAG,(serverResponse.get("first_name")).toString());
//					Log.d(TAG,(serverResponse.get("last_name")).toString());
//					Log.d(TAG,(serverResponse.get("mass")).toString());
//					
//		    		Intent i = new Intent(LoginActivity.this, Registration2RFIDActivity.class);
//		    		Log.d(TAG,"new Intent");
//		    		//---- ALL OF THE FOLLOWING WILL BE REPLACED WITH MONGO ID that's passed back upon successful login.
//		    		//i.putExtra("MongoId", mongoId);
//		    		i.putExtra("fName", firstName.getText().toString());
//		    		i.putExtra("lName", lastName.getText().toString());
//		    		i.putExtra("mPass", password.getText().toString());
//		    		i.putExtra("mClass", classId.getText().toString());
//		    		Log.d(TAG,"startActivity...");
//		    		LoginActivity.this.startActivity(i);
//		    		Log.d(TAG,"...startActivity");
		    		
//		    		Intent i = new Intent(LoginActivity.this, Registration2RFIDActivity.class);
//		    		Log.d(TAG,"new Intent");
//		    		//---- ALL OF THE FOLLOWING WILL BE REPLACED WITH MONGO ID that's passed back upon successful login.
//		    		//i.putExtra("MongoId", mongoId);
//		    		i.putExtra("fName", firstName.getText().toString());
//		    		i.putExtra("lName", lastName.getText().toString());
//		    		i.putExtra("mPass", password.getText().toString());
//		    		i.putExtra("mClass", classId.getText().toString());
//		    		Log.d(TAG,"startActivity...");
//		    		LoginActivity.this.startActivity(i);
//		    		Log.d(TAG,"...startActivity");
					
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}	
//			}
    	}
    };
    

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
    
//------- OLD login connection -------
   OnClickListener mLogInListenerOLD = new OnClickListener(){
    	
    	public void onClick(View v) {
    		Log.d(TAG,"mLogInListener.onClick");
    		final SciGamesHttpPoster poster = new SciGamesHttpPoster("http://mysweetwebsite.com/pull/auth_student.php");
			//("http://requestb.in/rb1n8prb");//("http://mysweetwebsite.com/pull/auth_student.php");
    			

        	// prepare for a progress bar dialog
			progressBar = new ProgressDialog(v.getContext());
			progressBar.setCancelable(true);
			progressBar.setMessage("Logging in ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressBar.setProgress(0);
			progressBar.setMax(100);
			progressBar.show();
			//reset progress bar status
			//progressBarStatus = 0;
			//reset currProgress
			//currProgress = 0;
			
			new Thread (new Runnable() {
				JSONObject serverResponse;
				public void run() {
					
					Message msg = new Message();
			        
					String thisUn = (firstName.getText().toString())+"_"+(lastName.getText().toString())+"_"+(classId.getText().toString());
					thisUn = thisUn.toLowerCase();
					String thisPw = password.getText().toString();
					try {
						serverResponse = poster.postDataOld("un", thisUn, "pw",thisPw);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Log.d(TAG,"...created serverResponse with poster.postData");
					Log.d(TAG,"serverResponse: ");
					Log.d(TAG, serverResponse.toString());

			        //jsonHandler.handleMessage(msg);
			        
					
					if(serverResponse.has("error")){
						Log.d(TAG, "BAD LOGIN");

					} else {
						
						Log.d(TAG,"GOOD LOGIN");
						try {
							Log.d(TAG,(serverResponse.get("first_name")).toString());
							Log.d(TAG,(serverResponse.get("last_name")).toString());
							Log.d(TAG,(serverResponse.get("mass")).toString());
							
				    		Intent i = new Intent(LoginActivity.this, Registration2RFIDActivity.class);
				    		Log.d(TAG,"new Intent");
				    		//---- ALL OF THE FOLLOWING WILL BE REPLACED WITH MONGO ID that's passed back upon successful login.
				    		//i.putExtra("MongoId", mongoId);
				    		i.putExtra("fName", firstName.getText().toString());
				    		i.putExtra("lName", lastName.getText().toString());
				    		i.putExtra("mPass", password.getText().toString());
				    		i.putExtra("mClass", classId.getText().toString());
				    		Log.d(TAG,"startActivity...");
				    		LoginActivity.this.startActivity(i);
				    		Log.d(TAG,"...startActivity");
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
//					while (progressBarStatus < 100) {
//					  // process some tasks
//						progressBarStatus = attemptLogin();
//						// your computer is too fast, sleep 1 second
//						try {
//							Thread.sleep(10);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						// Update the progress bar
//						progressBarHandler.post(new Runnable() {
//						
//							public void run() {
//								progressBar.setProgress(progressBarStatus);
//							}
//						});
//					}
					// ok, time is up
//					if (progressBarStatus >= 100) {
//						Log.d(TAG, "...progressBar time's up");
//						// sleep 2 seconds, so that you can see the 100%
//						try {
//							Thread.sleep(2000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						Message msg = new Message();
//				        String textTochange = serverResponseReceived;
//				        msg.obj = textTochange;
//				        jsonHandler.sendMessage(msg);
//						// close the progress bar dialog
						progressBar.dismiss();
//						Log.d(TAG, "...progressBar.dismiss()");
//        			}

				}

			}).start(); 	
    		//---- attemptLogin();
    		//onActivityResult:
    		//if incorrect{
    		//	shoot warning -- wrong pass/no name found in db;
    		//} else{
    		
    		//registration state idea:
    		//-- if 0, we have NO RFID, NO MASS, NO PHOTO,  for student
    		//-- if 1, we have NO RFID, NO MASS for student
    		//-- if 2, we have NO RFID, NO PHOTO for student
    		//-- if 3, we have NO RFID for student.  -- everyone will be this each login.
    		
//    		Intent i = new Intent(LoginActivity.this, Registration2RFIDActivity.class);
//    		Log.d(TAG,"new Intent");
//    		//---- ALL OF THE FOLLOWING WILL BE REPLACED WITH MONGO ID that's passed back upon successful login.
//    		//i.putExtra("MongoId", mongoId);
//    		i.putExtra("fName", firstName.getText().toString());
//    		i.putExtra("lName", lastName.getText().toString());
//    		i.putExtra("mPass", password.getText().toString());
//    		i.putExtra("mClass", classId.getText().toString());
//    		Log.d(TAG,"startActivity...");
//    		LoginActivity.this.startActivity(i);
//    		Log.d(TAG,"...startActivity");
    	}
    	
    };

    
	static Handler jsonHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
        	//String thisResponse = (String)msg.obj;	        
    	   	//call setText here
		try {
			Log.d(TAG,"try...");
			JSONObject data= new JSONObject(msg.toString());
			if(data.has("error")){
				Log.d(TAG, "BAD LOGIN");
			} else {
				
				Log.d(TAG,"GOOD LOGIN");
				Log.d(TAG,(data.get("first_name")).toString());
				Log.d(TAG,(data.get("last_name")).toString());
				Log.d(TAG,(data.get("mass")).toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		Log.d(TAG,msg.toString());
    	Log.d(TAG,"...handleMessage:");
        }
	};
    
    public int attemptLogin(){ 
    	return 0;
    }
}
