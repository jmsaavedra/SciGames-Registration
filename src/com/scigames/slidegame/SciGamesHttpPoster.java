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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.scigames.slidegame.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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


public class SciGamesHttpPoster extends AsyncTask <String, Void, JSONObject> {

    private String TAG = "ScGamesHttpPoster";
    private String firstKey = "";
    private String secondKey = "";
    private String firstValue = "";
    private String secondValue = "";
    private String thirdKey = "";
    private String thirdValue = "";
    private String thisPostAddress = "";
   // private String photoUri;
    public JSONObject serverResponse = null;
    private Exception exception;
    
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    
    static public Activity MyActivity;
    SciGamesListener listener;
    
    public String failureReason = ""; //holds any explanations for failure
    
    SciGamesHttpPoster(Activity a, String addr){
    	thisPostAddress = addr;
    	MyActivity = a;
    	Log.d(TAG, "----"+MyActivity);
    }
    
	public void run() {
		// TODO Auto-generated method stub
	}
	
    public void setOnResultsListener(SciGamesListener listener) {
        this.listener = listener;
    }
	
    protected void onPreExecute (){
//    	ProgressDialog dialog ;
//    	dialog = ProgressDialog.show(Activity.activity ,"title","message");
   }
	
	@Override
	protected JSONObject doInBackground (String... keyVals){
		
		firstKey 	= keyVals[0]; 
    	firstValue 	= keyVals[1];
    	secondKey	= keyVals[2]; 
    	secondValue = keyVals[3];
    	if(keyVals.length > 4){
	    	thirdKey	= keyVals[4]; 
	    	thirdValue  = keyVals[5];
    	}
    	
    	Log.d(TAG, "keyVals: ");
    	Log.d(TAG, firstKey +":"+ firstValue +" , "+ secondKey+":"+ secondValue);
    	
    	try{
        	JSONObject thisResponse=null;
        	Log.d(TAG, "...doInBackground (String... keyVals)");
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(thisPostAddress);//("http://requestb.in/xurt8kxu");
            Log.d(TAG, "...create POST");
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(firstKey, firstValue));
            nameValuePairs.add(new BasicNameValuePair(secondKey, secondValue));
            if(keyVals.length > 4){
            	nameValuePairs.add(new BasicNameValuePair(thirdKey, thirdValue));
            }
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            Log.d(TAG, "...setEntity");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            Log.d(TAG, "...executed");
            Log.d(TAG, response.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            Log.d(TAG, "...BufferedReader");
            //Log.d(TAG, reader.toString());
            String json = reader.readLine();
            Log.d(TAG, "incoming json:");
            Log.d(TAG, json);
            thisResponse = new JSONObject(json);
            //Log.d(TAG, thisResponse.toString());
            Log.d(TAG, "...jsonObject");
            //Log.d(TAG, "first return...");
            return thisResponse;
            
    	} catch (Exception e) {
            this.exception = e;
            return null;
        }
	}
	
    protected void onPostExecute(JSONObject response){
    
    	this.serverResponse = response;
    	
    	if (MyActivity.toString().startsWith("com.scigames.slidegame.LoginActivity")){
    		if(checkLoginFailed(response)){
    			Log.d(TAG, "BAD LOGIN: ");
    			listener.failedQuery(failureReason);
    			Log.d(TAG, failureReason);
    		} else {
    			String[] parsedLoginInfo;
    			Log.d(TAG, "GOOD LOGIN: ");
				try {
					parsedLoginInfo = parseThisLogin(response);
					listener.onResultsSucceeded(parsedLoginInfo);
				} catch (JSONException e) {
					Log.d(TAG, "failed at parsedThisLogin");
					e.printStackTrace();
				}
    		}
    	}
    	
//		if((serverResponse).has("error")){
//			Log.d(TAG, "BAD LOGIN");
//
//		} else {
//			Log.d(TAG,"GOOD LOGIN");
//			Activity thisActivity;
			//thisActivity = SciGamesHttpPoster.MyActivity.getParent();
			
			//((Object) thisActivity).parseJson(response);
			
//			try {
//				parseResponse(serverResponse);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			
//			try {
//				Log.d(TAG,(serverResponse.get("first_name")).toString());
//				Log.d(TAG,(serverResponse.get("last_name")).toString());
//				Log.d(TAG,(serverResponse.get("mass")).toString());
//				
//	    		Intent i = new Intent(LoginActivity.this, Registration2RFIDActivity.class);
//	    		Log.d(TAG,"new Intent");
//	    		//---- ALL OF THE FOLLOWING WILL BE REPLACED WITH MONGO ID that's passed back upon successful login.
//	    		//i.putExtra("MongoId", mongoId);
//	    		i.putExtra("fName", firstName.getText().toString());
//	    		i.putExtra("lName", lastName.getText().toString());
//	    		i.putExtra("mPass", password.getText().toString());
//	    		i.putExtra("mClass", classId.getText().toString());
//	    		Log.d(TAG,"startActivity...");
//	    		LoginActivity.this.startActivity(i);
//	    		Log.d(TAG,"...startActivity");
			//}
		//}
    }
    
    public boolean checkLoginFailed(JSONObject response){
		if((response).has("error")){
			Log.d(TAG, "BAD LOGIN");
			//TODO: find the reason login failed
			failureReason = "either bad name or bad password";
			return true;
		} else return false;
    }
    
    public String[] parseThisLogin(JSONObject response) throws JSONException{
    	
    	Log.d(TAG, "parsed data:");	
		JSONObject visits = null;
		JSONObject visitId = null;
		String thisVisitId = null;
		
		JSONObject student = null;
		JSONObject studentId = null;
		String thisStudentId = null;
		
		if(serverResponse.has("visit")){
			visits = serverResponse.getJSONObject("visit");
			visitId = visits.getJSONObject("_id");
			thisVisitId = visitId.getString("$id");
			
			Log.d(TAG, "thisVisit: " + thisVisitId);
		}
		
		student = serverResponse.getJSONObject("student");
		studentId = student.getJSONObject("_id");
		thisStudentId = studentId.getString("$id");
		
		Log.d(TAG, "thisStudentId: " + thisStudentId);
		
		String[] infoToSend = {thisStudentId, thisVisitId};
		return infoToSend;
    }
    
//	public JSONObject getServerResponse(){
//        return this.serverResponse;
//    }
}



//  public JSONObject newPostData(String key1, String val1, String key2, String val2){
//	
//	firstKey = key1; 
//	firstValue = val1;
//	secondKey = key2; 
//	secondValue = val2;
//    // Making HTTP request
//    try {
//        // defaultHttpClient
//        DefaultHttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(thisPostAddress);
//        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//        //String thisUn = (firstName.getText().toString())+"_"+(lastName.getText().toString())+"_"+(classId.getText().toString());
//        //thisUn = thisUn.toLowerCase();
//        //String thisPw = password.getText().toString();
//        Log.d(TAG, "values: ");
//        Log.d(TAG, firstKey + ": " +firstValue + "   "+secondKey+": "+secondValue);
//        Log.d(TAG, secondValue);
//        nameValuePairs.add(new BasicNameValuePair(firstKey, firstValue));
//        nameValuePairs.add(new BasicNameValuePair(secondKey, secondValue));
//        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//        Log.d(TAG, "...setEntity: ");
//        
//        HttpResponse httpResponse = httpClient.execute(httpPost);
//        HttpEntity httpEntity = httpResponse.getEntity();
//        is = httpEntity.getContent();           
//
//    } catch (UnsupportedEncodingException e) {
//        e.printStackTrace();
//    } catch (ClientProtocolException e) {
//        e.printStackTrace();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//
//    try {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(
//                is, "iso-8859-1"), 8);
//        StringBuilder sb = new StringBuilder();
//        String line = null;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line + "n");
//        }
//        is.close();
//        json = sb.toString();
//    } catch (Exception e) {
//        Log.e("Buffer Error", "Error converting result " + e.toString());
//    }
//
//    // try parse the string to a JSON object
//    try {
//        jObj = new JSONObject(json);
//    } catch (JSONException e) {
//        Log.e("JSON Parser", "Error parsing data " + e.toString());
//    }
//    // return JSON String
//    return jObj;
//}
	


// see http://androidsnippets.com/executing-a-http-post-request-with-httpclient    

