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

import org.json.JSONObject;

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

public interface SciGamesListener {
	
	public void onResultsSucceeded(String[] serverResponse);
	public void failedQuery(String failureReason);
	
}