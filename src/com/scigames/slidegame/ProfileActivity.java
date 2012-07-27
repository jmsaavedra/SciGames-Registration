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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import com.scigames.slidegame.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
public class ProfileActivity extends Activity {
    private String TAG = "ProfileActivity";
    
	static final private int QUIT_ID = Menu.FIRST;
    static final private int BACK_ID = Menu.FIRST + 1;

    private String firstNameIn = "FNAME";
    private String lastNameIn = "LNAME";
    private String passwordIn = "PWORD";
    private String massIn = "MASS";
    private String emailIn = "EMAIL";
    private String classIdIn = "CLASSID";
    private String rfidIn = "RFID";
    
    public ProfileActivity() {
    	

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
    	passwordIn = i.getStringExtra("mPass");
    	massIn = i.getStringExtra("mMass");
    	emailIn = i.getStringExtra("mEmail");
    	classIdIn = i.getStringExtra("mClass");
    	rfidIn = i.getStringExtra("mRfid");
    	Log.d(TAG,"...getStringExtra");
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.profile_page);
        Log.d(TAG,"...setContentView");
        
        // Hook up button presses to the appropriate event handler.
        //((Button) findViewById(R.id.back)).setOnClickListener(mBackListener);
        //((Button) findViewById(R.id.clear)).setOnClickListener(mClearListener);
        //((Button) findViewById(R.id.take_picture)).setOnClickListener(mTakePictureListener);
        //Log.d(TAG,"...instantiateButtons");
        
        //display name and profile info
        Resources res = getResources();
        TextView greets = (TextView)findViewById(R.id.greeting);
        greets.setText(String.format(res.getString(R.string.greeting), firstNameIn, lastNameIn));
        
        TextView fname = (TextView)findViewById(R.id.first_name);
        fname.setText(String.format(res.getString(R.string.profile_first_name), firstNameIn));
        
        TextView lname = (TextView)findViewById(R.id.last_name);
        lname.setText(String.format(res.getString(R.string.profile_last_name), lastNameIn));
        
        TextView school = (TextView)findViewById(R.id.school_name);
        school.setText(String.format(res.getString(R.string.profile_school_name), "from DB"));
        
        TextView teacher = (TextView)findViewById(R.id.teacher_name);
        teacher.setText(String.format(res.getString(R.string.profile_teacher_name), "from DB"));
        
        TextView classid = (TextView)findViewById(R.id.class_id);
        classid.setText(String.format(res.getString(R.string.profile_classid), classIdIn));
        
        TextView mmass = (TextView)findViewById(R.id.mass);
        mmass.setText(String.format(res.getString(R.string.profile_mass), massIn));
        
        TextView memail = (TextView)findViewById(R.id.email);
        memail.setText(String.format(res.getString(R.string.profile_email), emailIn));
        
        TextView mpass = (TextView)findViewById(R.id.password);
        mpass.setText(String.format(res.getString(R.string.profile_password), passwordIn));
        
        TextView mRfid = (TextView)findViewById(R.id.rfid);
        mRfid.setText(String.format(res.getString(R.string.profile_rfid), rfidIn));

        Log.d(TAG,"...Profile Info");
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        // Before showing the menu, we need to decide whether the clear
        // item is enabled depending on whether there is text to clear.
        //menu.findItem(CLEAR_ID).setVisible(firstName.getText().length() > 0);
        //menu.findItem(CLEAR_ID).setVisible(lastName.getText().length() > 0);
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

    OnClickListener mClearListener = new OnClickListener() {
        public void onClick(View v) {
        	//lastName.setText("");
           // firstName.setText("");
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
}
    


