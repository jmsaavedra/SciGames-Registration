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

import java.io.ByteArrayOutputStream;

import com.scigames.slidegame.R;

import android.app.Activity;
import android.content.Intent;
//import android.net.Uri;
import android.os.Bundle;
//import android.view.KeyEvent;
//import android.text.InputType;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
//import android.view.View.OnClickListener;
import android.widget.Button;
//import android.widget.EditText;


import android.graphics.Bitmap;
import android.widget.ImageView;


public class CameraActivity extends Activity {
    private static final int CAMERA_REQUEST = 1888; 
    private ImageView imageView;
    private String TAG = "CameraActivity";
    private Bitmap photo;
   // private String photoUri;

    public CameraActivity(){
    	
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "...super.onCreate");
        setContentView(R.layout.camera);
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.button1);
        photoButton.setOnClickListener(new View.OnClickListener() {

           // @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
            }
        });
        
    ((Button) findViewById(R.id.done)).setOnClickListener(mDoneListener);    
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST) {  
            photo = (Bitmap) data.getExtras().get("data"); 
            //photoUri = data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }  
    } 
    
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
}