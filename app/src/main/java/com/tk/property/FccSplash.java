package com.tk.property;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tk.property.common.AppPreferences;
import com.tk.property.common.Constants;

public class FccSplash extends Activity {
	private Runnable mUpdateTimeTask = null;
	private Handler mHandler = new Handler();
	private GoogleCloudMessaging gcm;
	private String regId;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fccsplash);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		switch (metrics.densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			break;
		case DisplayMetrics.DENSITY_HIGH:
			break;
		}
		((TextView) findViewById(R.id.version)).setText("Version : " + applicationVName());
		initiate();
	}

	public String applicationVName() {
		String versionName = "";
		PackageManager manager = getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			versionName = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
	public String registerGCM() {

		gcm = GoogleCloudMessaging.getInstance(this);
		registerInBackground();

		// Log.d("RegisterActivity", "registerGCM - successfully registered with
		// GCM server - regId: " + regId);

		return regId;
	}
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";

				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
					}
					regId = gcm.register(Constants.GOOGLE_PROJECT_ID);
					Log.d("RegisterActivity", "registerInBackground - regId: " + regId);
					msg = "Device registered, registration ID=" + regId;

				} catch (Exception ex) {
					msg = "Error :" + ex.getMessage();
					Log.d("RegisterActivity", "Error: " + msg);
				}

				Log.d("RegisterActivity", "AsyncTask completed: " + msg);
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				AppPreferences.getInstance(FccSplash.this).addToStore("regID", regId);
				launchActivty();
			}
		}.execute(null, null, null);
	}
	private void launchActivty() {
		FccSplash.this.finish();
		Intent mainIntent = new Intent(FccSplash.this, FccMainview.class);
		FccSplash.this.startActivity(mainIntent);
	}

	private void initiate() {

		try {
			mUpdateTimeTask = new Runnable() {
				public void run() {

					regId = AppPreferences.getInstance(FccSplash.this).getFromStore("regID");
					if (TextUtils.isEmpty(regId)) {
						registerGCM();
					}
					else {
						launchActivty();
					}
				}
			};
			mHandler.postDelayed(mUpdateTimeTask, 5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
