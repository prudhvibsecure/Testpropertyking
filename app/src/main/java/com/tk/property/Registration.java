package com.tk.property;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tk.property.callbacks.IItemHandler;
import com.tk.property.common.AppSettings;
import com.tk.property.tasks.HTTPTask;
import com.tk.property.utils.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends AppCompatActivity implements IItemHandler{

	private EditText et_username = null;
	private EditText et_email = null;

	private EditText et_zip = null;
	private EditText et_password = null;
	private EditText et_passwordAgain = null, et_city = null, et_cnfrmemail = null, et_phn = null, fname, lname;

	JSONObject jsonobject;
	JSONArray jsonarray;
	ArrayList<String> countrylist, statelist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.register);
		setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		fname = (EditText) findViewById(R.id.r_fname);
		lname = (EditText) findViewById(R.id.r_laname);
		et_email = (EditText) findViewById(R.id.r_email);
		et_phn = (EditText) findViewById(R.id.r_mobile);
		et_password = (EditText) findViewById(R.id.r_pass);
		et_city = (EditText) findViewById(R.id.r_city);
		et_zip = (EditText) findViewById(R.id.r_zip);

		findViewById(R.id.regiset_btn).setOnClickListener(onClick);
		findViewById(R.id.register_cancel).setOnClickListener(onClick);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:

			Registration.this.finish();

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	OnClickListener onClick = new OnClickListener() {

		@Override
		public void onClick(View view) {

			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(view.getWindowToken(), 0);

			switch (view.getId()) {

			case R.id.regiset_btn:

				registration();

				break;

			case R.id.register_cancel:
				Registration.this.finish();
				break;
			default:
				break;
			}

		}
	};

	private void registration() {

		try {

			String email = ((EditText) findViewById(R.id.r_email)).getText().toString().trim();

			if (email.length() == 0) {
				showToast(R.string.peavei);
				((EditText) findViewById(R.id.r_email)).requestFocus();
				return;
			}

			if (!emailValidation(email)) {
				showToast(R.string.peavei);
				((EditText) findViewById(R.id.r_email)).requestFocus();
				return;
			}

			String r_fname = ((EditText) findViewById(R.id.r_fname)).getText().toString().trim();

			if (r_fname.length() == 0) {
				showToast(R.string.peyfn);
				((EditText) findViewById(R.id.r_fname)).requestFocus();
				return;
			}
			String r_lname = ((EditText) findViewById(R.id.r_laname)).getText().toString().trim();

			if (r_lname.length() == 0) {
				showToast(R.string.peyln);
				((EditText) findViewById(R.id.r_laname)).requestFocus();
				return;
			}
			String password = ((EditText) findViewById(R.id.r_pass)).getText().toString().trim();

			if (password.length() == 0) {
				showToast(R.string.pePswd);
				((EditText) findViewById(R.id.r_pass)).requestFocus();
				return;
			}

			if (password.length() < 8 || password.length() > 16) {
				showToast(R.string.psmbc);
				((EditText) findViewById(R.id.r_pass)).requestFocus();
				return;
			}
			String r_mobile = ((EditText) findViewById(R.id.r_mobile)).getText().toString().trim();

			if (r_mobile.length() == 0) {
				showToast(R.string.peMsisdn);
				((EditText) findViewById(R.id.r_mobile)).requestFocus();
				return;
			}
			String r_city = ((EditText) findViewById(R.id.r_city)).getText().toString().trim();

			if (r_city.length() == 0) {
				showToast(R.string.pecn);
				((EditText) findViewById(R.id.r_city)).requestFocus();
				return;
			}

			String r_zip = ((EditText) findViewById(R.id.r_zip)).getText().toString().trim();

			if (r_zip.length() == 0) {
				showToast(R.string.pecz);
				((EditText) findViewById(R.id.r_zip)).requestFocus();
				return;
			}

			String url = AppSettings.getInstance(this).getPropertyValue("registration");
			url = url.replace("(EMAIL)", email);
			url = url.replace("(FNAME)", r_fname);
			url = url.replace("(LNAME)", r_lname);
			url = url.replace("(PWD)", password);
			url = url.replace("(PHNO)", r_mobile);
			url = url.replace("(CITY)", r_city);
			url = url.replace("(ZIP)", r_zip);

			HTTPTask get = new HTTPTask(this, this);
			get.userRequest(getString(R.string.pwait), 1, url);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onFinish(Object results, int requestType) {
		Utils.dismissProgress();

		try {

			switch (requestType) {
			case 1:
				parseRegistrationResponse((String) results, requestType);
				break;

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onError(String errorData, int requestType) {
		Utils.dismissProgress();
		showToast(errorData);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == 4) {
		}

		return super.onKeyDown(keyCode, event);
	}

	public void showToast(int text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	private void parseRegistrationResponse(String response, int requestId) throws Exception {

		if (response != null && response.length() > 0) {
			response = response.trim();
			JSONObject jsonObject = new JSONObject(response);

			if (jsonObject.has("status") && jsonObject.optString("status").equalsIgnoreCase("0")) {
				showToast(jsonObject.optString("statusdescription"));
				Registration.this.finish();
				return;
			}
			showToast(jsonObject.optString("statusdescription"));

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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

}
