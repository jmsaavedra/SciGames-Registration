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

import com.scigames.slidegame.R;

import android.app.Activity;
import android.content.Intent;
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
    private EditText password;
    private String TAG = "LoginActivity";
    
    public LoginActivity() {
    	
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG,"super.OnCreate");
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.login_page);

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

        // Hook up button presses to the appropriate event handler.
        //((Button) findViewById(R.id.back)).setOnClickListener(mBackListener);
        ((Button) findViewById(R.id.clear)).setOnClickListener(mClearListener);
        ((Button) findViewById(R.id.login_button)).setOnClickListener(mLogInListener);
        ((Button) findViewById(R.id.register)).setOnClickListener(mRegisterListener);
        //firstName.setText(getText(R.string.first_name));
        //lastName.setText(getText(R.string.last_name));
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
    		Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
    		Log.d(TAG,"new Intent");
    		i.putExtra("fName",firstName.getText().toString());
    		i.putExtra("lName",lastName.getText().toString());
    		i.putExtra("pword",password.getText().toString());
    		Log.d(TAG,"startActivity...");
    		LoginActivity.this.startActivity(i);
    		Log.d(TAG,"...startActivity");
    		
    		//LoginActivity.this.startActivity(i.putExtra("fName",firstName, "lName",lastName,"pword",password));
    		//	private ProfileActivity myProfile;
    		//	public void onClick(View v){
    		//		myProfile = new ProfileActivity(firstName, lastName, password);
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
    		
    		//LoginActivity.this.startActivity(i.putExtra("fName",firstName, "lName",lastName,"pword",password));
    		//	private ProfileActivity myProfile;
    		//	public void onClick(View v){
    		//		myProfile = new ProfileActivity(firstName, lastName, password);
    	}
    };
    
    /**
     * A call-back for when the user presses the back button.
     
    OnClickListener mBackListener = new OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };*/

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
    
}
