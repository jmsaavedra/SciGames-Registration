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

//import java.io.ByteArrayInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.net.URI;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.scigames.registration.R;
import com.scigames.registration.ProfileActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
//import android.net.Uri;
import android.os.Bundle;
//import android.view.KeyEvent;
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
public class ProfileActivity extends Activity implements SciGamesListener{
    private String TAG = "ProfileActivity";
    
	static final private int QUIT_ID = Menu.FIRST;
    static final private int BACK_ID = Menu.FIRST + 1;

    private String firstNameIn = "FNAME";
    private String lastNameIn = "LNAME";
    private String passwordIn = "PWORD";
    private String massIn = "MASS";
    private String emailIn = "EMAIL";
    private String classIdIn = "CLASSID";
    private String studentIdIn = "STUDENTID";
    private String visitIdIn = "VISITID";
    private String rfidIn = "RFID";
    private String slideLevel = "SLIDELEVEL";
    private String cartLevel = "CARTLEVEL";
    private String photoUrl = "none";
    
    TextView greets;
    TextView fname;
    TextView lname;
    TextView schoolname;
    TextView teachername;
    TextView mpass;
    TextView classid;
    TextView classname;
    
    Typeface Museo500Regular;
    Typeface Museo700Regular;
    Typeface ExistenceLightOtf;
    
    Button Done;
    Button editPhoto;
    
    AlertDialog alertDialog;
    
    DownloadProfilePhoto photoTask = new DownloadProfilePhoto(ProfileActivity.this, "sUrl");
    SciGamesHttpPoster task = new SciGamesHttpPoster(ProfileActivity.this,"http://db.scigam.es/pull/auth_student.php");
    
    public ProfileActivity() {
    	

    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    	Log.d(TAG,"super.OnCreate");
        Intent i = getIntent();
        Log.d(TAG,"getIntent");
    	firstNameIn = i.getStringExtra("fName");
    	lastNameIn = i.getStringExtra("lName");
//    	passwordIn = i.getStringExtra("password");
//    	massIn = i.getStringExtra("mass");
//    	emailIn = i.getStringExtra("email");
//    	classIdIn = i.getStringExtra("classId");
    	studentIdIn = i.getStringExtra("studentId");
    	visitIdIn = i.getStringExtra("visitId");
//    	rfidIn = i.getStringExtra("rfid");
    	photoUrl = i.getStringExtra("photoUrl");
    	photoUrl = "http://db.scigam.es/" + photoUrl;
//    	slideLevel = i.getStringExtra("slideLevel");
//    	cartLevel = i.getStringExtra("cartLevel");
    	Log.d(TAG,"...getStringExtra");
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.profile_page);
        Log.d(TAG,"...setContentView");
       
        
        //display name and profile info
        Resources res = getResources();
        greets = (TextView)findViewById(R.id.student_name);
        greets.setText(String.format(res.getString(R.string.profile_name), firstNameIn, lastNameIn));
        setTextViewFont(Museo700Regular, greets);
        
        //schoolname = (TextView)findViewById(R.id.school_name);
        //schoolname.setText(String.format(res.getString(R.string.profile_school_name), "from DB"));
        
        teachername = (TextView)findViewById(R.id.teacher_name);
        //teachername.setText(String.format(res.getString(R.string.profile_teacher_name), "from DB"));
        
        classname = (TextView)findViewById(R.id.class_name);
        
        classid = (TextView)findViewById(R.id.class_id);
        //classid.setText(String.format(res.getString(R.string.profile_classid), classIdIn));
        
        mpass = (TextView)findViewById(R.id.password);
        //mpass.setText(String.format(res.getString(R.string.profile_password), passwordIn));
        

        Log.d(TAG,"...Profile Info");
	    ExistenceLightOtf = Typeface.createFromAsset(getAssets(),"fonts/Existence-Light.ttf");
	    Typeface Museo300Regular = Typeface.createFromAsset(getAssets(),"fonts/Museo300-Regular.otf");
	    Museo500Regular = Typeface.createFromAsset(getAssets(),"fonts/Museo500-Regular.otf");
	    Museo700Regular = Typeface.createFromAsset(getAssets(),"fonts/Museo700-Regular.otf");
	    
//	    TextView welcome = (TextView)findViewById(R.id.welcome);
//	    TextView notascigamersentence = (TextView)findViewById(R.id.notascigamersentence);
   
        // Hook up button presses to the appropriate event handler.
        //((Button) findViewById(R.id.back)).setOnClickListener(mBackListener);
        //((Button) findViewById(R.id.clear)).setOnClickListener(mClearListener);
        
        Log.d(TAG,"...instantiateButtons");
        Done = (Button) findViewById(R.id.done);
        Done.setOnClickListener(mDone);
        setButtonFont(ExistenceLightOtf, Done);
	    
		alertDialog = new AlertDialog.Builder(
		        ProfileActivity.this).create();
	    // Setting Dialog Title
	    alertDialog.setTitle("alert title");
	    // Setting Dialog Message
	    alertDialog.setMessage("alert message");
	    // Setting Icon to Dialog
	    //alertDialog.setIcon(R.drawable.tick);
	
	    alertDialog.setButton(RESULT_OK,"OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        // Write your code here to execute after dialog closed
	        //Toast.makeText(getApplicationContext(), "Check your login info!", Toast.LENGTH_SHORT).show();
	        }
	    });
        
      //set listener
        task.setOnResultsListener(this);
        
    	//push picture back.-- photo
		task.cancel(true);

	    //create a new async task for every time you hit login (each can only run once ever)
	   	task = new SciGamesHttpPoster(ProfileActivity.this,"http://db.scigam.es/pull/return_profile.php");
	    //set listener
        task.setOnResultsListener(ProfileActivity.this);
        		
		//prepare key value pairs to send
		String[] keyVals = {"student_id", studentIdIn, "visit_id", visitIdIn}; 
		
		//create AsyncTask, then execute
		@SuppressWarnings("unused")
		AsyncTask<String, Void, JSONObject> serverResponse = null;
		serverResponse = task.execute(keyVals);
		Log.d(TAG,"...task.execute(keyVals)");
		
    }
        

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        Log.d(TAG,"...super.onResume()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //menu.add(0, BACK_ID, 0, R.string.back).setShortcut('0', 'b');
        menu.add(0, QUIT_ID, 0, R.string.quit).setShortcut('0', 'b');

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

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
        case QUIT_ID:
        	finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    OnClickListener mTakePictureListener = new OnClickListener(){
    	public void onClick(View v) {
    	
    	}
    };
    	
    OnClickListener mBackListener = new OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

	public void onResultsSucceeded(String[] serverResponseStrings, JSONObject serverResponseJSON) throws JSONException {
		
		//for debugging:
//		alertDialog.setTitle("incoming:");
//		alertDialog.setMessage("active: " + serverResponseStrings[2] +
//				"      mass: " + serverResponseStrings[5] + 
//				"      firstName: " + serverResponseStrings[2] + 
//				"      lastName: " + serverResponseStrings[3] + 
//				"      pw: " + serverResponseStrings[8] + 
//				"      classId: " + serverResponseStrings[14] + 
//				"      rfid: " + serverResponseStrings[9]
//				);
//		alertDialog.show();
		
		//download photo
        ImageView profilePhoto = (ImageView) findViewById(R.id.imageView1);
        profilePhoto.setTag(photoUrl);
        profilePhoto.setScaleX(1.4f);
        profilePhoto.setScaleY(1.4f);
        profilePhoto.setX(120f);
        profilePhoto.setY(123f);
        photoTask.cancel(true);
        photoTask = new DownloadProfilePhoto(ProfileActivity.this, photoUrl);
        //AsyncTask<ImageView, Void, Bitmap> pPhoto = 
     	photoTask.execute(profilePhoto);
     	
     	//update all text fields
     	Resources res = getResources();
     	
        //TextView greets = (TextView)findViewById(R.id.greeting);
        greets.setText(String.format(res.getString(R.string.profile_name), serverResponseStrings[2], serverResponseStrings[3]));
        
        //TextView fname = (TextView)findViewById(R.id.first_name);
//        fname.setText(String.format(res.getString(R.string.profile_first_name), serverResponseStrings[2]));
//        
//        //TextView lname = (TextView)findViewById(R.id.last_name);
//        lname.setText(String.format(res.getString(R.string.profile_last_name), serverResponseStrings[3]));
        
        //TextView school = (TextView)findViewById(R.id.school_name);
        //schoolname.setText(String.format(res.getString(R.string.profile_school_name), serverResponseStrings[14]));
        
        //TextView teacher = (TextView)findViewById(R.id.teacher_name);
        teachername.setText(String.format(res.getString(R.string.profile_teacher_name), serverResponseStrings[13]));
        
        //TextView classid = (TextView)findViewById(R.id.class_id);
        classid.setText(String.format(res.getString(R.string.profile_classid), serverResponseStrings[14]));
        
//        TextView mmass = (TextView)findViewById(R.id.mass);
        classname.setText(String.format(res.getString(R.string.profile_classname), serverResponseStrings[12]));
        
        //TextView mpass = (TextView)findViewById(R.id.password);
        mpass.setText(String.format(res.getString(R.string.profile_password), serverResponseStrings[8]));
        
//        TextView mRfid = (TextView)findViewById(R.id.rfid);
//        mRfid.setText(String.format(res.getString(R.string.profile_rfid), serverResponseStrings[9]));
	    
        setTextViewFont(Museo500Regular, teachername, classid, mpass, classname);
        setTextViewFont(Museo700Regular, greets);
     	
        Log.d(TAG,"...Profile Info");
        
	}

	public void failedQuery(String failureReason) {

		Log.d(TAG, "failedQuery in Profile Activity");
		alertDialog.setTitle("failed query: ");
		alertDialog.setMessage(failureReason);
		alertDialog.show();
	}      
    
	//---- send to 'one-off' registration page
    OnClickListener mDone = new OnClickListener(){
    	public void onClick(View v) {
    		Log.d(TAG,"mDone.onClick");
    		//startActivity(new Intent(ProfileActivity.this, Registration2RfidMass_AdkServiceActivity.class));
    		Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
    		Log.d(TAG,"new Intent");
    		i.putExtra("fName","First Name");
    		i.putExtra("lName","Last Name");
    		i.putExtra("pword","Class Password");
    		Log.d(TAG,"startActivity...");
    		ProfileActivity.this.startActivity(i);
    		Log.d(TAG,"...startActivity");
    	}
    };
    
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
	@Override
	public void onBackPressed() {
		//do nothing
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        
	}
}



