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

import com.scigames.registration.R;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements SciGamesListener{
	    
    static final private int QUIT_ID = Menu.FIRST;
    static final private int CLEAR_ID = Menu.FIRST + 1;

    private EditText firstName;
    private EditText lastName;
    public  EditText password;
    public EditText classId;
    
    public String fNameGlobal;
    public String lNameGlobal;
    private static String TAG = "LoginActivity";
    Button login;
    Button register;
    String token = null;
    
    ProgressDialog progressBar;
    AlertDialog alertDialog;
    SciGamesHttpPoster task = new SciGamesHttpPoster(LoginActivity.this,"http://mysweetwebsite.com/pull/auth_student.php");
	
//
//	protected void hideControls() {
//		super.hideControls();
//		mOutputController = null;
//	}
//
//	protected void showControls() {
//		super.showControls();
//		mOutputController = new Adk_OutputController(this, true);
//		mOutputController.accessoryAttached();
//	}
    public LoginActivity() {
    	
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION );
        // View v = findViewById(R.layout.login_page);
        // v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); 
        //Window w = this.getWindow(); // in Activity's onCreate() for instance
        //w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_page);
        Log.d(TAG,"super.OnCreate");
        // Inflate our UI from its XML layout description.       
//        Intent i = getIntent();
//        if (i.hasExtra("token")){
//        	setContentView(R.layout.login_page);
//        } else {
//        	setContentView(R.layout.no_device);
//        }
        lastName = (EditText) findViewById(R.id.last_name);
        password = (EditText) findViewById(R.id.password);
        classId = (EditText) findViewById(R.id.class_id);
        firstName = (EditText) findViewById(R.id.first_name);
        /* to hide the keyboard on launch, then open when tap in firstname field */
        //firstName.setActivated(false);
        //firstName.setSelected(false);
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

        
        //for faster testing:
//        firstName.setText("joseph7");
//        lastName.setText("christopher");
//        password.setText("qweasd");
//        classId.setText("66");
//        
        // Hook up button presses to the appropriate event handler.
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
	
	    alertDialog.setButton(RESULT_OK,"OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        // Write your code here to execute after dialog closed
	        Toast.makeText(getApplicationContext(), "Check your login info!", Toast.LENGTH_SHORT).show();
	        }
	    });
	    
	    Typeface ExistenceLightOtf = Typeface.createFromAsset(getAssets(),"fonts/Existence-Light.ttf");
	    Typeface Museo300Regular = Typeface.createFromAsset(getAssets(),"fonts/Museo300-Regular.otf");
	    Typeface Museo500Regular = Typeface.createFromAsset(getAssets(),"fonts/Museo500-Regular.otf");
	    Typeface Museo700Regular = Typeface.createFromAsset(getAssets(),"fonts/Museo700-Regular.otf");
	    
//	    TextView welcome = (TextView)findViewById(R.id.welcome);
	    TextView notascigamersentence = (TextView)findViewById(R.id.notascigamersentence);
//	    setTextViewFont(ExistenceLightOtf, welcome);
	    setTextViewFont(Museo500Regular, notascigamersentence);
	    setEditTextFont(Museo500Regular, firstName, lastName, password, classId);
	    
        login = (Button) findViewById(R.id.login_button);
        register = (Button) findViewById(R.id.register); /* out for now */
        setButtonFont(ExistenceLightOtf, login, register);
        setButtonFont(Museo500Regular, register);
        
//        if (getIntent().getBooleanExtra("EXIT", false)) {
//            finish();
//        }
	      
    }
    
    //---- runs when user clicks "LOG IN"
    OnClickListener mLogInListener = new OnClickListener(){
	   public void onClick(View v) {
		    Log.d(TAG,"mLogInListener.onClick");
		    setProgressBarIndeterminateVisibility(true);
		    
		    if (isNetworkAvailable()){
			    task.cancel(true);
			    //create a new async task for every time you hit login (each can only run once ever)
			   	task = new SciGamesHttpPoster(LoginActivity.this,"http://mysweetwebsite.com/pull/auth_student.php");
			    //set listener
		        task.setOnResultsListener(LoginActivity.this);
		        
				//prepare login strings
		        if(classId.getText().toString().equals("")){
		        	classId.setText("00");
		        }
				String thisUn = (firstName.getText().toString())+"_"+(lastName.getText().toString())+"_"+(classId.getText().toString()).toLowerCase();
				String thisPw = password.getText().toString();
				
				//prepare key value pairs to send
				String[] keyVals = {"un", thisUn, "pw", thisPw}; 
				
				//create AsyncTask, then execute
				AsyncTask<String, Void, JSONObject> serverResponse = null;
				serverResponse = task.execute(keyVals);
				Log.d(TAG,"mLogInListener server response:");
				Log.d(TAG,serverResponse.toString());
		    } else {
				alertDialog.setMessage("You're not connected to the internet. Make sure this tablet is logged into a working Wifi Network.");
				alertDialog.show();
		    	
		    }
    	}
    };
    
    //---- this is from the results listener interface (SciGamesListener)
	public void onResultsSucceeded(String[] serverResponseStrings, JSONObject serverResponseJSON) throws JSONException {
		setProgressBarIndeterminateVisibility(false);
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
		fNameGlobal = firstName;
		String lastName = thisStudent.getString("last_name");
		lNameGlobal = lastName;
		String email = thisStudent.getString("email");
		String pw = thisStudent.getString("pw");
		String classId = thisStudent.getString("class_id");
		String rfid = thisStudent.getString("current_rfid");
		
		Log.d(TAG, "this student: ");
		Log.d(TAG, thisStudent.toString());
		
		//if(debug){
//		alertDialog.setTitle("incoming:");
//		alertDialog.setMessage("active: " + serverResponseStrings[2] +
//				"      cartLevel: " + cartLevel + 
//				"      slideLevel: " + slideLevel + 
//				"      mass: " + mass + 
//				"      firstName: " + firstName + 
//				"      lastName: " + lastName + 
//				"      pw: " + pw + 
//				"      classId: " + classId + 
//				"      rfid: " + rfid
//				);
//		alertDialog.show();
		//{
		
		if(serverResponseStrings[2].equals("false")){ //check active state. if inactive for today, get RFID
			Log.d(TAG, "off to RFID page!");
			
			/****** RFID ACTIVITY INTENT ******/

    		Intent i = new Intent(LoginActivity.this, Registration2RfidMass_AdkServiceActivity.class); 
    		Log.d(TAG,"new Intent");
    		Log.d(TAG,"sending:");
    		Log.d(TAG, firstName);
    		Log.d(TAG, lastName);
    		i.putExtra("needsRfid","yes");
    		i.putExtra("rfid","");
    		i.putExtra("mass","");
    		i.putExtra("fName",firstName);
    		i.putExtra("lName",lastName);
    		i.putExtra("studentId",serverResponseStrings[0]);
    		i.putExtra("visitId",serverResponseStrings[1]);
    		LoginActivity.this.startActivity(i);
    		Log.d(TAG,"...startActivity");
    		i.removeExtra("fName");
    		i.removeExtra("lName");
    		i.removeExtra("rfid");
    		i.removeExtra("studentId");
    		i.removeExtra("visitId");
    		i.removeExtra("needsRfid");
    		i.removeExtra("mass");
    		Log.d(TAG,"...removeExtras");
    		/**********************************/
		}
		
		else if(thisStudent.get("mass").equals(null)){ //no mass recorded
			Log.d(TAG, "off to mass page!");
	   		
			/****** MASS ACTIVITY INTENT ******/
			//Intent i = new Intent(LoginActivity.this, Registration3MassActivity.class); //THIS IS CORRECT
			Intent i = new Intent(LoginActivity.this, Registration2RfidMass_AdkServiceActivity.class);
			Log.d(TAG,"new Intent");
			i.putExtra("needsRfid","no");
    		i.putExtra("fName",firstName);
    		i.putExtra("lName",lastName);
    		i.putExtra("rfid","");
    		i.putExtra("mass","");
    		i.putExtra("studentId",serverResponseStrings[0]);
    		i.putExtra("visitId",serverResponseStrings[1]);
    		LoginActivity.this.startActivity(i);
    		Log.d(TAG,"...startActivity");
    		/**********************************/

		}
		
		else if(thisStudent.get("photo").equals(null)){ //no photo recorded
			//send to photo activity
			Log.d(TAG, "off to photo page!");
			
			/****** PHOTO ACTIVITY INTENT ******/
			Intent i = new Intent(LoginActivity.this, Registration4PhotoActivity.class);
    		Log.d(TAG,"new Intent");
    		i.putExtra("fName",firstName);
    		i.putExtra("lName",lastName);
    		i.putExtra("studentId",serverResponseStrings[0]);
    		i.putExtra("visitId",serverResponseStrings[1]);
    		LoginActivity.this.startActivity(i);
    		Log.d(TAG,"...startActivity");
    		/**********************************/
		}
	
// **no email for now** //		
//		else if(thisStudent.get("email").equals(null)){ //no email recorded
//			//send to email activity
//			Log.d(TAG, "off to email page!");
//			
//			/****** EMAIL ACTIVITY INTENT ******/
//			Intent i = new Intent(LoginActivity.this, Registration5EmailActivity.class);
//    		Log.d(TAG,"new Intent");
//    		i.putExtra("fName",firstName);
//    		i.putExtra("lName",lastName);
//    		i.putExtra("studentId",serverResponseStrings[0]);
//    		i.putExtra("visitId",serverResponseStrings[1]);
//    		LoginActivity.this.startActivity(i);
//    		Log.d(TAG,"...startActivity");
//    		/**********************************/
//		}
		else {
			//send directly to their profile, they're all registered.
			/****** PROFILE ACTIVITY INTENT ******/
			Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
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
    		LoginActivity.this.startActivity(i);
    		Log.d(TAG,"...startActivity");
    		/****** PROFILE ACTIVITY INTENT ******/

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
	public void onResume() {
        super.onResume();
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION );
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
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    
    //---- this function hides the keyboard when the user clicks outside of keyboard when it's open!
    @Override 
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        Log.d(TAG, "CLICK DETECTED");
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION );
//        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        Window win = this.getWindow(); // in Activity's onCreate() for instance
//        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
	
	@Override
	public void onBackPressed() {
		//do nothing
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        
	}
//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);           
//    }
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//	   if(keyCode == KeyEvent.KEYCODE_HOME)
//	    {
//	     Log.i("Home Button","Clicked");
//	    }
//	   if(keyCode==KeyEvent.KEYCODE_BACK)
//	   {
//		   Log.i("back Button","Clicked");
//	   }
//	   if(keyCode==KeyEvent.KEYCODE_APP_SWITCH){
//		   Log.i("app switch Button","Clicked");
//	}
//	 return false;
//	}
//	
	
}
