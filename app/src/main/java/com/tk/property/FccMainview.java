package com.tk.property;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tk.property.adapters.ViewPagerAdapter;
import com.tk.property.callbacks.IItemHandler;
import com.tk.property.common.AppPreferences;
import com.tk.property.common.AppSettings;
import com.tk.property.tasks.HTTPTask;
import com.tk.property.utils.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class FccMainview extends AppCompatActivity implements OnClickListener, IItemHandler {

	private ViewPager mViewPager;
	private Dialog dialog;
	private Toolbar toolbar;
	private Properties properties = null;
	private TabLayout tabs;
	private CharSequence Titles[] = { "My Lessons", "e-shop", "Apps", "More" };
	private int Numboftabs = 4, key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadProperties();
		tabs = (TabLayout) findViewById(R.id.tabs);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// toolbar.setNavigationIcon(R.drawable.ic_launcher);
		toolbar.setTitle(R.string.app_name);
		toolbar.setSubtitle(getFromStore("email"));
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs));

		tabs.setupWithViewPager(mViewPager);
		mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
		tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				mViewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});

		setupTabIcons();

	}

	private void setupTabIcons() {
		tabs.getTabAt(0).setIcon(R.drawable.ic_home);
		tabs.getTabAt(1).setIcon(R.drawable.ic_cart);
		tabs.getTabAt(2).setIcon(R.drawable.ic_settings);
		tabs.getTabAt(3).setIcon(R.drawable.ic_more);

	}

	protected void onDestroy() {
		closeDialog();
		addToStore("inactivity", "no");
		super.onDestroy();
	}

	public void restoreActionBar() {
	}

	protected void onStart() {
		addToStore("inactivity", "yes");
		super.onStart();
	}

	private void loadProperties() {
		try {
			InputStream rawResource = getResources().openRawResource(R.raw.settings);
			properties = new Properties();
			properties.load(rawResource);
			rawResource.close();
			rawResource = null;
		} catch (Resources.NotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Properties getProperties() {
		return properties;
	}

	public String getPropertyValue(String key) {
		return properties.getProperty(key);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		restoreActionBar();
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (menu.findItem(R.id.login_menu) != null) {
			String uid = getFromStore("email");
			if (uid.length() == 0) {
				menu.findItem(R.id.login_menu).setVisible(true);
				menu.findItem(R.id.logout_menu).setVisible(false);
			} else {
				menu.findItem(R.id.login_menu).setVisible(false);
				menu.findItem(R.id.logout_menu).setVisible(true);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.login_menu:
			showLoginDialog();
			break;
		case R.id.logout_menu:
			logoutAlert();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void logoutAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage("Do you want to Logout?");
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				addToStore("email", "");
				showToast(R.string.ylss);
				startActivity(getIntent());
				FccMainview.this.finish();

			}
		});
		alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				closeDialog();
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public String getFromStore(String key) {
		return getSharedPreferences("Fcc", 0).getString(key, "");
	}

	public void addToStore(String key, String value) {
		Editor editor = getSharedPreferences("Fcc", 0).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void showLoginDialog() {

		dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.login);
		dialog.findViewById(R.id.submit_btn).setOnClickListener(onclick);
		dialog.findViewById(R.id.cancel_btn).setOnClickListener(onclick);
		dialog.findViewById(R.id.tv_register).setOnClickListener(onclick);
		dialog.show();

	}

	OnClickListener onclick = new OnClickListener() {

		@Override
		public void onClick(View view) {

			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(view.getWindowToken(), 0);

			switch (view.getId()) {
			case R.id.submit_btn:
				makeLoginRequest();
				break;
			case R.id.cancel_btn:
				closeDialog();
				break;
			case R.id.register_cancel:
				closeDialog();
				break;
			case R.id.tv_register:
				Intent register = new Intent(getApplicationContext(), Registration.class);
				startActivity(register);
				closeDialog();
				break;

			default:
				break;
			}

		}
	};

	@Override
	public void onFinish(Object results, int requestType) {
		Utils.dismissProgress();

		try {

			switch (requestType) {
			case 1:

				parseLoginResponse((String) results, requestType);

				break;

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void parseLoginResponse(String response, int requestType) throws Exception {
		if (response != null && response.length() > 0) {

			JSONObject jsonObject = new JSONObject(response);

			if (jsonObject.optString("status").equalsIgnoreCase("0")) {
				showToast(jsonObject.optString("statusdescription"));
				JSONArray jsonarray = jsonObject.getJSONArray("session_details");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jObject = jsonarray.getJSONObject(i);
					addToStore("username", jObject.optString("user_name"));
					addToStore("email", jObject.optString("email"));
					closeDialog();
					Intent ss = new Intent(this, FccMainview.class);
					startActivity(ss);
					FccMainview.this.finish();
				}
				return;
			}
			showToast(jsonObject.optString("statusdescription"));
		}

	}

	@Override
	public void onBackPressed() {

		if (key == 1) {
			key = 0;
			finish();
		} else {
			showToast(R.string.pbaep);
			key++;
		}
	}

	@Override
	public void onError(String errorData, int requestType) {
		showToast(errorData);
		closeDialog();
	}

	private void closeDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
		dialog = null;
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.tv_rateapp:
			Uri uri = Uri.parse("market://details?id=" + getPackageName());
			Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
			try {
				startActivity(goToMarket);
			} catch (ActivityNotFoundException e) {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
			}
			break;
		case R.id.tv_monials:
			Intent monial = new Intent(this, TestMonials.class);
			startActivity(monial);
			break;

		case R.id.tv_news:
			Intent news = new Intent(this, NewsAct.class);
			startActivity(news);
			break;
		case R.id.app_dpa:
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id=com.propertyinvestment.apa&hl=en")));
			break;
		case R.id.tv_share:
			Intent in = new Intent(android.content.Intent.ACTION_SEND);
			in.setType("text/plain");
			in.putExtra(android.content.Intent.EXTRA_SUBJECT, "The Property King");
			in.putExtra(android.content.Intent.EXTRA_TEXT,
					"To download Property King app, please click here https://play.google.com/store/apps/details?id=com.tk.property");
			this.startActivity(Intent.createChooser(in, "Share Via"));
			break;

		default:
			break;

		}

	}

	private void makeLoginRequest() {
		try {

			String email = ((EditText) dialog.findViewById(R.id.l_name)).getText().toString().trim();

			String password = ((EditText) dialog.findViewById(R.id.l_pass)).getText().toString().trim();

			if (email.length() == 0) {
				showToast(R.string.peei);
				return;
			}

			if (!emailValidation(email)) {
				showToast(R.string.peavei);
				return;
			}

			if (password.length() == 0) {
				showToast(R.string.pePswd);
				return;
			}

			if (password.length() < 8) {
				showToast(R.string.psmbc123);
				return;
			}

			String strPassword = URLEncoder.encode(password);

			String url = AppSettings.getInstance(this).getPropertyValue("login");
			url = url.replace("(EMAIL)", email);
			url = url.replace("(PWD)", strPassword);
			url = url.replace("(REGID)", AppPreferences.getInstance(this).getFromStore("regID"));
			url = url.replace("(APP)", "android");

			HTTPTask get = new HTTPTask(this, this);
			get.userRequest(getString(R.string.pwait), 1, url);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == 4) {
			// moveTaskToBack(true);
		}

		return super.onKeyDown(keyCode, event);
	}

	public void showToast(int text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public boolean emailValidation(String email) {

		if (email == null || email.length() == 0 || email.indexOf("@") == -1 || email.indexOf(" ") != -1) {
			return false;
		}
		int emailLenght = email.length();
		int atPosition = email.indexOf("@");

		String beforeAt = email.substring(0, atPosition);
		String afterAt = email.substring(atPosition + 1, emailLenght);

		if (beforeAt.length() == 0 || afterAt.length() == 0) {
			return false;
		}
		if (email.charAt(atPosition - 1) == '.') {
			return false;
		}
		if (email.charAt(atPosition + 1) == '.') {
			return false;
		}
		if (afterAt.indexOf(".") == -1) {
			return false;
		}
		char dotCh = 0;
		for (int i = 0; i < afterAt.length(); i++) {
			char ch = afterAt.charAt(i);
			if ((ch == 0x2e) && (ch == dotCh)) {
				return false;
			}
			dotCh = ch;
		}
		if (afterAt.indexOf("@") != -1) {
			return false;
		}
		int ind = 0;
		do {
			int newInd = afterAt.indexOf(".", ind + 1);

			if (newInd == ind || newInd == -1) {
				String prefix = afterAt.substring(ind + 1);
				if (prefix.length() > 1 && prefix.length() < 6) {
					break;
				} else {
					return false;
				}
			} else {
				ind = newInd;
			}
		} while (true);
		dotCh = 0;
		for (int i = 0; i < beforeAt.length(); i++) {
			char ch = beforeAt.charAt(i);
			if (!((ch >= 0x30 && ch <= 0x39) || (ch >= 0x41 && ch <= 0x5a) || (ch >= 0x61 && ch <= 0x7a) || (ch == 0x2e)
					|| (ch == 0x2d) || (ch == 0x5f))) {
				return false;
			}
			if ((ch == 0x2e) && (ch == dotCh)) {
				return false;
			}
			dotCh = ch;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == 1002) {
			switch (resultCode) {

			case RESULT_OK:
				break;

			case RESULT_CANCELED:
				break;

			case 2002:

				if (resultCode == RESULT_CANCELED) {
					onKeyDown(4, null);
					return;
				}

				if (intent.getStringExtra("action").equalsIgnoreCase("0")) {

				} else if (intent.getStringExtra("action").equalsIgnoreCase("1")) {

				}

				break;
			default:
				break;
			}
		}
	}

}