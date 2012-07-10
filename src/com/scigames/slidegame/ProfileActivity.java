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

import com.scigames.slidegame.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class ProfileActivity extends Activity {

    
    static final private int BACK_ID = Menu.FIRST;
    static final private int CLEAR_ID = Menu.FIRST + 1;

    private String firstNameIn = "FNAME";
    private String lastNameIn = "LNAME";;
    private String passwordIn = "PWORD";;
    
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private String TAG = "ProfileActivity";
    
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
    	passwordIn = i.getStringExtra("pword");
    	Log.d(TAG,"...getStringExtra");
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.profile_page);
        Log.d(TAG,"...setContentView");
        // Find the text editor view inside the layout, because we
        // want to do various programmatic things with it.
        firstName = (EditText) findViewById(R.id.first_name);
        /* to hide the keyboard on launch, then open when tap in firstname field */
        firstName.setInputType(InputType.TYPE_NULL);
        firstName.setOnTouchListener(new View.OnTouchListener() {
  			@Override
			public boolean onTouch(View v, MotionEvent event) {
	        firstName.setInputType(InputType.TYPE_CLASS_TEXT);
	        firstName.onTouchEvent(event); // call native handler
	        return true; // consume touch even
			} 
        });
        lastName = (EditText) findViewById(R.id.last_name);
        password = (EditText) findViewById(R.id.password);
        Log.d(TAG,"...instantiateEditTexts");
        
        // Hook up button presses to the appropriate event handler.
        ((Button) findViewById(R.id.back)).setOnClickListener(mBackListener);
        ((Button) findViewById(R.id.clear)).setOnClickListener(mClearListener);
        ((Button) findViewById(R.id.take_picture)).setOnClickListener(mTakePictureListener);
        Log.d(TAG,"...instantiateButtons");
        
        //set info to what we know already
        firstName.setText(firstNameIn);
        lastName.setText(lastNameIn);
        password.setText(passwordIn);
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
        menu.findItem(CLEAR_ID).setVisible(password.getText().length() > 0);

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
    
    OnClickListener mTakePictureListener = new OnClickListener(){
    	public void onClick(View v) {
    		Log.d(TAG,"...mTakePictureListener onClick");
    		Intent myIntent = new Intent();
    		//Intent myIntent = new Intent(ProfileActivity.this, CameraActivity.class);
    		Log.d(TAG,"...createIntent");
    		myIntent.setClass(ProfileActivity.this, CameraActivity.class);
    		//ProfileActivity.this.startActivity(myIntent);
    		startActivityForResult(myIntent, 0);
    		Log.d(TAG,"...startActivity");
    		//Intent myIntent = new Intent(LoginActivity.this, ProfileActivity.class);
    		//LoginActivity.this.startActivity(myIntent.putExtra("something","another"));
    		//	private ProfileActivity myProfile;
    		//	public void onClick(View v){
    		//		myProfile = new ProfileActivity(firstName, lastName, password);
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
            password.setText("");
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
    

