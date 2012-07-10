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
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import com.scigames.slidegame.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
public class Registration4PhotoActivity extends Activity {
    private static final int CAMERA_REQUEST = 1888; 
    private ImageView imageView;
    
    static final private int BACK_ID = Menu.FIRST;
    static final private int CLEAR_ID = Menu.FIRST + 1;

    private String firstNameIn = "FNAME";
    private String lastNameIn = "LNAME";;
    //private String passwordIn = "PWORD";;
    
    private EditText firstName;
    private EditText lastName;
    //private EditText password;
    private String TAG = "Registration4Activity";
    
    private Bitmap photo;
    
    public Registration4PhotoActivity() {
    	

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
    	//passwordIn = i.getStringExtra("pword");
    	Log.d(TAG,"...getStringExtra");
    	
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.registration4_photo);
        Log.d(TAG,"...setContentView");
        
        // Find the text editor view inside the layout, because we
        // want to do various programmatic things with it.
     //   thisMass = (EditText) findViewById(R.id.mass);
        /* to hide the keyboard on launch, then open when tap in firstname field */
//        thisMass.setInputType(InputType.TYPE_NULL);
//        thisMass.setOnTouchListener(new View.OnTouchListener() {
//  			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//  			thisMass.setInputType(InputType.TYPE_CLASS_TEXT);
//  			thisMass.onTouchEvent(event); // call native handler
//	        return true; // consume touch even
//			} 
//        });
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        //password = (EditText) findViewById(R.id.password);
        Log.d(TAG,"...instantiateEditTexts");
        
        //display name in greeting sentence
       // Resources res = getResources();
       // TextView greets = (TextView)findViewById(R.id.greeting);
       // greets.setText(String.format(res.getString(R.string.greeting), firstNameIn, lastNameIn));
       // Log.d(TAG, greets.toString());
       // Log.d(TAG,"...Greetings");
        
        
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        Log.d(TAG,"...findViewById. imageView1");
        
        //set info to what we know already
        //firstName.setText(firstNameIn);
        //lastName.setText(lastNameIn);
        
        // Hook up button presses to the appropriate event handler.
        ((Button) findViewById(R.id.back)).setOnClickListener(mBackListener);
        //((Button) findViewById(R.id.continue_button)).setOnClickListener(mContinueButtonListener);
        ((Button) findViewById(R.id.continue_button)).setOnClickListener(mContinueButtonListener);//(mDoneListener);
        ((Button) findViewById(R.id.take_pic)).setOnClickListener(mTakePhotoListener);
        Log.d(TAG,"...instantiateButtons");
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
        	//firstName.setText("");
            //password.setText("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    OnClickListener mContinueButtonListener = new OnClickListener(){
    	public void onClick(View v) {
    		Log.d(TAG,"...mContinueButtonListener onClick");
       		Intent i = new Intent(Registration4PhotoActivity.this, Registration5EmailActivity.class);
    		Log.d(TAG,"new Intent");
    		i.putExtra("fName",firstNameIn);
    		i.putExtra("lName",lastNameIn);
    		//i.putExtra("pword",password.getText().toString());
    		Log.d(TAG,"startActivity...");
    		Registration4PhotoActivity.this.startActivity(i);
    		Log.d(TAG,"...startActivity");
    		
    	}
    };
    	

    OnClickListener mBackListener = new OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    OnClickListener mTakePhotoListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
            startActivityForResult(cameraIntent, CAMERA_REQUEST); 
        }
    };      
  
    OnClickListener mDoneListener = new OnClickListener() {
        public void onClick(View v) {
        	Log.d(TAG, "...mDoneListener onClick");
        	//push picture back.-- photo
        	Intent resultIntent = new Intent();
        	Log.d(TAG, "...new Intent ()");
        	
        	//resultIntent.
        	//Uri outputFileUri = Uri.fromFile(photo);
        	//resultIntent.setType("image/*");
        	//resultIntent.setAction(Intent.ACTION_GET_CONTENT);
        	//startActivityForResult(Intent.createChooser(resultIntent, "Select Picture"),0);

        	
        	ByteArrayOutputStream bs = new ByteArrayOutputStream();
        	photo.compress(Bitmap.CompressFormat.PNG, 50, bs);
        	Log.d(TAG, "...photo.compress");
        	
        	resultIntent.putExtra("byteArray", bs.toByteArray());
        	Log.d(TAG, "...resultIntent.putExtra");
        	
        	//setResult(Activity.RESULT_OK, resultIntent);
        	startActivity(resultIntent);
        	Log.d(TAG, "...startActivity");
            
        	finish();
            Log.d(TAG, "...finish");
        }
    };
 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST) {  
            photo = (Bitmap) data.getExtras().get("data"); 
            //photoUri = data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }  
    } 
}
    

