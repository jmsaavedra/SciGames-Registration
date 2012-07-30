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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

public class SciGamesHttpPoster extends AsyncTask <String, Void, JSONObject> {

    private String TAG = "ScGamesHttpPoster";
    private String firstKey = "";
    private String secondKey = "";
    private String firstValue = "";
    private String secondValue = "";
    private String thirdKey = "";
    private String thirdValue = "";
    private String thisPostAddress = "";
    
    public JSONObject serverResponse = null;
    static InputStream is = null;
    static String json = "";
    public String failureReason = ""; //holds any explanations for failure
    public String[] parsedLoginInfo;
    
    static public Activity MyActivity;

    SciGamesListener listener;  
    
    SciGamesHttpPoster(Activity a, String addr){
    	thisPostAddress = addr;
    	MyActivity = a;
    	
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
            //Log.d(TAG, response.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            Log.d(TAG, "...BufferedReader");
            String json = reader.readLine();
            String line = null;
            Log.d(TAG, "raw response: ");
            while ((line = reader.readLine()) != null) {
            	Log.d(TAG,line);
            }
            //Log.d(TAG, reader.toString());
            //String json = reader.readLine();
            Log.d(TAG, "incoming json:");
            Log.d(TAG, json);
            thisResponse = new JSONObject(json);
            //Log.d(TAG, thisResponse.toString());
            Log.d(TAG, "...jsonObject");
            //Log.d(TAG, "first return...");
            return thisResponse;
            
    	} catch (Exception e) {
    		Log.e(TAG, "--- failed at doInBackground");
    		e.printStackTrace();
            return null;
        }
	}
	
    protected void onPostExecute(JSONObject response){
//    	this.serverResponse = response;  	
    	
    	//check which activity called this task
    	//login activity
    	if (MyActivity.toString().startsWith("com.scigames.slidegame.LoginActivity")){ 
    		if(checkLoginFailed(response)){
    			listener.failedQuery(failureReason);
    		} else {
    			
    			Log.d(TAG, "GOOD LOGIN: ");
				try {
					parsedLoginInfo = parseThisLogin(response);
					Log.d(TAG, "..set parsedLoginInfo:");
					for(int i=0; i<parsedLoginInfo.length; i++){
						Log.d(TAG, parsedLoginInfo[i]);
					}
					Log.d(TAG, "full response: ");
					Log.d(TAG, response.toString());
					listener.onResultsSucceeded(parsedLoginInfo, response);//send both String[] and raw JSON
					Log.d(TAG, "listener.onResultsSucceeded");
				} catch (JSONException e) {
					Log.d(TAG, "failed at parsedThisLogin");
					e.printStackTrace();
				}
    		}
    	}
    	
    	//username activity
    	else if (MyActivity.toString().startsWith("com.scigames.slidegame.Registration1UserNameActivity")){
    		if(checkLoginFailed(response)){
    			listener.failedQuery(failureReason);
    		} else {
    			
    			Log.d(TAG, "GOOD LOGIN: ");
				try {
					parsedLoginInfo = parseThisLogin(response);
					Log.d(TAG, "..set parsedLoginInfo:");
					for(int i=0; i<parsedLoginInfo.length; i++){
						Log.d(TAG, parsedLoginInfo[i]);
					}
					Log.d(TAG, "full response: ");
					Log.d(TAG, response.toString());
					listener.onResultsSucceeded(parsedLoginInfo, response);//send both String[] and raw JSON
					Log.d(TAG, "listener.onResultsSucceeded");
				} catch (JSONException e) {
					Log.d(TAG, "failed at parsedThisLogin");
					e.printStackTrace();
				}
    		}
    	}
    	
    	//RFID activity
    	else if (MyActivity.toString().startsWith("com.scigames.slidegame.Registration2RFIDActivity")){
    		if(checkLoginFailed(response)){
    			listener.failedQuery(failureReason);
    		} else {
    			
    			Log.d(TAG, "GOOD LOGIN: ");
				try {
					parsedLoginInfo = parseThisLogin(response);
					Log.d(TAG, "..set parsedLoginInfo:");
					for(int i=0; i<parsedLoginInfo.length; i++){
						Log.d(TAG, parsedLoginInfo[i]);
					}
					Log.d(TAG, "full response: ");
					Log.d(TAG, response.toString());
					listener.onResultsSucceeded(parsedLoginInfo, response);//send both String[] and raw JSON
					Log.d(TAG, "listener.onResultsSucceeded");
				} catch (JSONException e) {
					Log.d(TAG, "failed at parsedThisLogin");
					e.printStackTrace();
				}
    		}
    	}
    	
    	//mass activity
    	else if (MyActivity.toString().startsWith("com.scigames.slidegame.Registration3MassActivity")){
    		if(checkLoginFailed(response)){
    			listener.failedQuery(failureReason);
    		} else {
    			
    			Log.d(TAG, "GOOD LOGIN: ");
				try {
					parsedLoginInfo = parseThisLogin(response);
					Log.d(TAG, "..set parsedLoginInfo:");
					for(int i=0; i<parsedLoginInfo.length; i++){
						Log.d(TAG, parsedLoginInfo[i]);
					}
					Log.d(TAG, "full response: ");
					Log.d(TAG, response.toString());
					listener.onResultsSucceeded(parsedLoginInfo, response);//send both String[] and raw JSON
					Log.d(TAG, "listener.onResultsSucceeded");
				} catch (JSONException e) {
					Log.d(TAG, "failed at parsedThisLogin");
					e.printStackTrace();
				}
    		}
    	}
    	
    	//photo activity
    	else if (MyActivity.toString().startsWith("com.scigames.slidegame.Registration4PhotoActivity")){
    		if(checkLoginFailed(response)){
    			listener.failedQuery(failureReason);
    		} else {
    			
    			Log.d(TAG, "GOOD LOGIN: ");
				try {
					parsedLoginInfo = parseThisLogin(response);
					Log.d(TAG, "..set parsedLoginInfo:");
					for(int i=0; i<parsedLoginInfo.length; i++){
						Log.d(TAG, parsedLoginInfo[i]);
					}
					Log.d(TAG, "full response: ");
					Log.d(TAG, response.toString());
					listener.onResultsSucceeded(parsedLoginInfo, response);//send both String[] and raw JSON
					Log.d(TAG, "listener.onResultsSucceeded");
				} catch (JSONException e) {
					Log.d(TAG, "failed at parsedThisLogin");
					e.printStackTrace();
				}
    		}
    	}
    	
    	//email activity
    	else if (MyActivity.toString().startsWith("com.scigames.slidegame.Registration5EmailActivity")){
    		if(checkLoginFailed(response)){
    			listener.failedQuery(failureReason);
    		} else {
    			
    			Log.d(TAG, "GOOD LOGIN: ");
				try {
					parsedLoginInfo = parseThisLogin(response);
					Log.d(TAG, "..set parsedLoginInfo:");
					for(int i=0; i<parsedLoginInfo.length; i++){
						Log.d(TAG, parsedLoginInfo[i]);
					}
					Log.d(TAG, "full response: ");
					Log.d(TAG, response.toString());
					listener.onResultsSucceeded(parsedLoginInfo, response);//send both String[] and raw JSON
					Log.d(TAG, "listener.onResultsSucceeded");
				} catch (JSONException e) {
					Log.d(TAG, "failed at parsedThisLogin");
					e.printStackTrace();
				}
    		}
    	}
    }
    
    public boolean checkLoginFailed(JSONObject response){
		if((response).has("error")){
			Log.d(TAG, "BAD LOGIN");
			try {
				failureReason = response.get("error").toString();
				Log.d(TAG, failureReason);
			} catch (JSONException e) {
				Log.e(TAG, "failed at getting failedReason string");
				e.printStackTrace();
			}
			
			return true;
		} else return false;
    }
    
    public String[] parseThisLogin(JSONObject response) throws JSONException{
    	Log.d(TAG, "parsed data:");	
		JSONObject visits = null;
		JSONObject visitId = null;
		String thisVisitId = null;
		String activeState = null;
		
		JSONObject student = null;
		JSONObject studentId = null;
		String thisStudentId = null;
		
		if(response.has("visit")){
			visits = response.getJSONObject("visit");
			visitId = visits.getJSONObject("_id");
			thisVisitId = visitId.getString("$id");
			activeState = visits.getString("active");
			
			Log.d(TAG, "thisVisit: " + thisVisitId);
			Log.d(TAG, "aciveState: " + activeState);
		}
		
		if(response.has("student")){
			student = response.getJSONObject("student");
			studentId = student.getJSONObject("_id");
			thisStudentId = studentId.getString("$id");
		} else {
			studentId = response.getJSONObject("_id");
			thisStudentId = studentId.getString("$id");
			}
		
		Log.d(TAG, "thisStudentId: " + thisStudentId);
		
		String[] infoToSend = {thisStudentId, thisVisitId, activeState};
		Log.d(TAG, "...created String[] infoToSend:");
		
		for(int i=0; i<infoToSend.length; i++){
			Log.d(TAG, infoToSend[i]);
		}
		return infoToSend;
    }
}



// see http://androidsnippets.com/executing-a-http-post-request-with-httpclient    

