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

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.io.ByteArrayOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class Registration4PhotoActivity extends Activity implements SciGamesListener{
    private static final int CAMERA_REQUEST = 1888; 
    
    static final private int BACK_ID = Menu.FIRST;
    static final private int CLEAR_ID = Menu.FIRST + 1;

    private String firstNameIn = "FNAME";
    private String lastNameIn = "LNAME";;
    private String studentIdIn = "studentId";
    private String visitIdIn = "visitId";
    TextView or;
    Button retakeButton; 
    Button saveButton;
    Button takePhotoButton;
    ImageView avatarPhoto;
    
    Bitmap photoToSend;
    
    private String TAG = "Registration4Activity";
    
    private Bitmap photo;
    
    SciGamesHttpPoster task = new SciGamesHttpPoster(Registration4PhotoActivity.this,"http://db.scigam.es/push/upload_image.php");
    
    
    public Registration4PhotoActivity() {
    	

    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Log.d(TAG,"super.OnCreate");
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        Intent i = getIntent();
        Log.d(TAG,"getIntent");
    	firstNameIn = i.getStringExtra("fName");
    	lastNameIn = i.getStringExtra("lName");
    	studentIdIn = i.getStringExtra("studentId");
    	visitIdIn = i.getStringExtra("visitId");
    	Log.d(TAG,"...getStringExtra");
    	
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.registration4_photo);
        Log.d(TAG,"...setContentView");        
        
        avatarPhoto = (ImageView)this.findViewById(R.id.avatar_photo);
        Log.d(TAG,"...findViewById. avatar_photo");
        
        or = (TextView) findViewById(R.id.or);
        or.setVisibility(View.INVISIBLE);
        retakeButton = (Button) findViewById(R.id.retake_pic);
        retakeButton.setVisibility(View.INVISIBLE);
        retakeButton.setOnClickListener(mTakePhotoListener);
        
        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(mContinueButtonListener);
        saveButton.setVisibility(View.INVISIBLE);

        takePhotoButton = (Button) findViewById(R.id.take_pic);
        takePhotoButton.setOnClickListener(mTakePhotoListener);
        takePhotoButton.setVisibility(View.VISIBLE);
        //((Button) findViewById(R.id.take_pic)).setOnClickListener(mTakePhotoListener);
        Log.d(TAG,"...instantiateButtons");
        
        //set listener
        task.setOnResultsListener(this);
        
	    Typeface ExistenceLightOtf = Typeface.createFromAsset(getAssets(),"fonts/Existence-Light.ttf");
	    Typeface Museo300Regular = Typeface.createFromAsset(getAssets(),"fonts/Museo300-Regular.otf");
	    Typeface Museo500Regular = Typeface.createFromAsset(getAssets(),"fonts/Museo500-Regular.otf");
	    Typeface Museo700Regular = Typeface.createFromAsset(getAssets(),"fonts/Museo700-Regular.otf");
	    
//	    TextView welcome = (TextView)findViewById(R.id.welcome);
	    //instruction = (TextView)findViewById(R.id.instructions);
//	    setTextViewFont(ExistenceLightOtf, welcome);
	    setButtonFont(Museo500Regular, retakeButton);
	    setTextViewFont(Museo300Regular, or);
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
    		setProgressBarIndeterminateVisibility(true);
        	Log.d(TAG, "...mDoneListener onClick");
        	
        	// convert image to byteArray - from: http://blog.sptechnolab.com/2011/03/09/android/android-upload-image-to-server/
        	//Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.imageView1); //original
        	//Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.id.imageView1);
        	Bitmap bitmapOrg = photoToSend;
        	Log.d(TAG, "...bitmapFactory.decodeResource");
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			Log.d(TAG, "...new bytearrayoutputstream");
			bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 100, bao); 
			Log.d(TAG, "...bitmap0rd.compress");
			byte [] ba = bao.toByteArray();
			Log.d(TAG, "...toByteArray");
			
			//String ba1 = Base64.encodeBytes(ba); //original 
			String photoEncoded = Base64.encodeToString(ba, 0); //int here is "flags"
			Log.d(TAG, "photoEncoded: ");
			Log.d(TAG, photoEncoded);
        		
        	//push picture back.-- photo
 		    task.cancel(true);
		    //create a new async task for every time you hit login (each can only run once ever)
		   	task = new SciGamesHttpPoster(Registration4PhotoActivity.this,"http://db.scigam.es/push/upload_image.php");
		    //set listener
	        task.setOnResultsListener(Registration4PhotoActivity.this);
	        		
			//prepare key value pairs to send
			String[] keyVals = {"student_id", studentIdIn, "visit_id", visitIdIn, "photo", photoEncoded}; 
			
			//create AsyncTask, then execute
			@SuppressWarnings("unused")
			AsyncTask<String, Void, JSONObject> serverResponse = null;
			serverResponse = task.execute(keyVals);
			Log.d(TAG,"...task.execute(keyVals)");
    		
    	}
    };
    	

    OnClickListener mBackListener = new OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    OnClickListener mTakePhotoListener = new OnClickListener() {
    	//@Override
    	public void onClick(View v) {

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
            cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
            startActivityForResult(cameraIntent, CAMERA_REQUEST); 
        }
    };      
  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST) { 
        	or.setVisibility(View.VISIBLE);
        	retakeButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
            takePhotoButton.setVisibility(View.INVISIBLE);
            
        	if (data.hasExtra("data")){
        		//change to new view
	            photo = (Bitmap) data.getExtras().get("data"); 
	            //photoUri = data.getExtras().get("data");
	            Log.d(TAG, "photoDensity: ");
	            Log.d(TAG, String.valueOf(photo.getDensity()));
	            Log.d(TAG,"photo getHeight:");
	            Log.d(TAG,String.valueOf(photo.getHeight()));
	            Log.d(TAG,"photo getWidth");
	            Log.d(TAG,String.valueOf(photo.getWidth()));
	            Log.d(TAG,"photo config:");
	            if(photo.getHeight() < photo.getWidth()){
	            	Log.d(TAG,"height < width" );
	            	
	            	Log.d(TAG, photo.getConfig().toString());
		            photoToSend = Bitmap.createBitmap(120, 160, photo.getConfig() );
		            //photoToSend = Bitmap.createBitmap(photo);
		            Canvas canvas = new Canvas(photoToSend);
		            Log.d(TAG,"photo getScaledHeight:");
		            Log.d(TAG,String.valueOf(photo.getScaledHeight(canvas)));
		            Log.d(TAG,"photo getScaledWidth");
		            Log.d(TAG,String.valueOf(photo.getScaledWidth(canvas)));
		            Matrix matrix = new Matrix();
		            //matrix.preScale(-1.0f, 1.0f);
		            //Bitmap mirroredBitmap = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight());
		            matrix.setRotate(270, photo.getWidth()/2, photo.getHeight()/2);
		            
		            //matrix.postTranslate(photo.getWidth(),photo.getHeight());
		            Log.d(TAG, matrix.toString());
		            canvas.drawBitmap(photo, matrix, new Paint());
		        } else {
		        	Log.d(TAG, photo.getConfig().toString());
		        	photoToSend = Bitmap.createBitmap(120, 160, photo.getConfig() ); 
		        	//photoToSend = Bitmap.createBitmap(photo); 
		            Canvas canvas = new Canvas(photoToSend);
		            Log.d(TAG,"photo getScaledHeight:");
		            Log.d(TAG,String.valueOf(photo.getScaledHeight(canvas)));
		            Log.d(TAG,"photo getScaledWidth");
		            Log.d(TAG,String.valueOf(photo.getScaledWidth(canvas)));
		            Matrix matrix = new Matrix();
		            //matrix.preScale(-1.0f, 1.0f);
		            //Bitmap mirroredBitmap = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight());
		            //matrix.setRotate(0, photo.getWidth()/2, photo.getHeight()/2);
		            //matrix.setScale(0.9f, 0.9f);
		            Log.d(TAG, matrix.toString());
		            //matrix.postTranslate(photo.getWidth(),photo.getHeight());
		            
		            canvas.drawBitmap(photo, matrix, new Paint());
		            avatarPhoto.setScaleX(0.95f);
		            avatarPhoto.setScaleY(0.95f);
		            avatarPhoto.setX(180f);
		            avatarPhoto.setY(440f);
		        }
	            avatarPhoto.setImageBitmap(photoToSend);
	            
	            Log.d(TAG, "photoToSendDensity: ");
	            Log.d(TAG, String.valueOf(photoToSend.getDensity()));
	            Log.d(TAG,"photoToSend getScaledHeight:");
	            Log.d(TAG,String.valueOf(photoToSend.getHeight()));
	            Log.d(TAG,"photoToSend getScaledWidth");
	            Log.d(TAG,String.valueOf(photoToSend.getWidth()));
        	}
	         else {
	        	or.setVisibility(View.INVISIBLE);
	            saveButton.setVisibility(View.INVISIBLE);
	            retakeButton.setVisibility(View.INVISIBLE);
	            takePhotoButton.setVisibility(View.VISIBLE);
    		//instruction.setText("");
	        }
        }  
    }

	public void onResultsSucceeded(String[] serverResponseStrings, JSONObject serverResponseJSON) throws JSONException {
		Log.d(TAG,"...resultsSucceeded");
		Log.d(TAG, "QUERY SUCCEEDED: ");
		setProgressBarIndeterminateVisibility(false);
		
		for(int i=0; i<serverResponseStrings.length; i++){ //just print everything returned as a String[] for fun
			Log.d(TAG, "["+i+"] "+serverResponseStrings[i]);
		}
		
		JSONObject thisStudent;
		thisStudent = serverResponseJSON.getJSONObject("student");
		
		Log.d(TAG, "this student: ");
		Log.d(TAG, thisStudent.toString());
   		//Intent i = new Intent(Registration4PhotoActivity.this, Registration5EmailActivity.class); //THIS IS RIGHT
			Intent i = new Intent(Registration4PhotoActivity.this, ProfileActivity.class);
		Log.d(TAG,"new Intent");
		i.putExtra("fName",serverResponseStrings[2]);
		i.putExtra("lName",serverResponseStrings[3]);
		i.putExtra("studentId",serverResponseStrings[0]);
		i.putExtra("mass",thisStudent.getString("mass"));
		//i.putExtra("visitId",serverResponseStrings[1]);
		i.putExtra("photoUrl", serverResponseStrings[1]);
		Log.d(TAG,"startActivity...");
		Registration4PhotoActivity.this.startActivity(i);
		Log.d(TAG,"...startActivity");
		
	}

	public void failedQuery(String failureReason) {
		
		Log.d(TAG, "LOGIN FAILED, REASON: " + failureReason);
	} 
	
    //---- methods for setting fonts!!
    public static void setTextViewFont(Typeface tf, TextView...params) {
        for (TextView tv : params) {
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
    

