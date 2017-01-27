package com.tk.property;

import org.json.JSONException;

import com.tk.property.common.AppSettings;
import com.tk.property.dialogs.MessageDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ProductPurchage extends AppCompatActivity implements OnClickListener {

	private WebView webview = null;
	private WebSettings webSettings = null;

	private Toolbar toolbar = null;

	private ContainerScriptInterface jsInterface = null;

	private String vtype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		vtype = getIntent().getStringExtra("range");

		toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		String title = getString(R.string.app_name) + " " + getString(R.string.payment);
		toolbar.setTitleTextColor(Color.WHITE);
		toolbar.setTitle(title);
		getSupportActionBar().setTitle(title);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				close();
			}
		});

		String link = "";
		if (vtype.equalsIgnoreCase("79")) {

			link = AppSettings.getInstance(this).getPropertyValue("payment");
			link = link.replace("(EMAIL)", getFromStore("email"));
			link = link.replace("(VTYPE)", "Intermediate");

		}
		if (vtype.equalsIgnoreCase("129")) {
			link = AppSettings.getInstance(this).getPropertyValue("payment");
			link = link.replace("(EMAIL)", getFromStore("email"));
			link = link.replace("(VTYPE)", "Advanced");
		}
		init(link);
	}

	private void init(String url) {

		jsInterface = new ContainerScriptInterface();

		webview = (WebView) findViewById(R.id.wv_payment);

		webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webview.addJavascriptInterface(jsInterface, "jsInterface");
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);

		webview.setWebViewClient(new MyWebViewClient());
		webview.setWebChromeClient(new MyWebChromeClient());

		webview.requestFocus(View.FOCUS_DOWN);
		webview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_UP:
					if (!v.hasFocus()) {
						v.requestFocus();
					}
					break;
				}
				return false;
			}
		});

		webview.loadUrl(url);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.webkit.WebView#onKeyDown(int, android.view.KeyEvent)
	 */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// return false;
	// }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == 4) {

		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * MyWebChromeClient - class which extend WebChromeClient for handle page
	 * progress.
	 * 
	 * @author shadab
	 * 
	 */
	private class MyWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {

			// Log.e("-=-=-=-=-=-", newProgress + "");

			if (newProgress == 5)
				findViewById(R.id.pb_payment).setVisibility(View.VISIBLE);

			if (newProgress >= 95) {
				findViewById(R.id.pb_payment).setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			return super.onJsAlert(view, url, message, result);
		}
	}

	/**
	 * MyWebViewClient - class which extends WebViewClient to handle internal
	 * URL.
	 * 
	 * @author shadab
	 * 
	 */
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			findViewById(R.id.pb_payment).setVisibility(View.GONE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
	}

	private void paymentStatus(int id) {
		switch (id) {
		case 0:
			Toast.makeText(this, "PAYMENT PENDING", Toast.LENGTH_LONG).show();
			// setResult(Constants.PAYMENT_PENDING);
			close();
			break;
		case 1:
			Toast.makeText(this, "PAYMENT SUCCESSFULLY DONE", Toast.LENGTH_LONG).show();
			// setResult(Constants.PAYMENT_SUCCESS);
			close();
			break;
		case 2:
			Toast.makeText(this, "PAYMENT FAILED", Toast.LENGTH_LONG).show();
			// setResult(Constants.PAYMENT_FAILED);
			close();
			break;
		}
	}

	/**
	 * close - When payment is done this method is called to release the
	 * resource.
	 */
	private void close() {
		if (webview != null) {
			webview.stopLoading();
			webview.setBackgroundDrawable(null);
			webview.clearFormData();
			webview.clearHistory();
			webview.clearMatches();
			webview.clearCache(true);
			webview.clearSslPreferences();
			webview = null;
		}

		finish();
	}

	public class ContainerScriptInterface {

		public ContainerScriptInterface() {
		}

		@JavascriptInterface
		public void exec(String service, String action, String arg) throws JSONException {

			try {
				showToast(service + "-----" + action + " -------- " + arg);
				// Log.e("--------------", "service : " + service + " :::
				// action:" + action + " ::::: arg:" + arg);

				showOkMessage(getString(R.string.payment), arg + "", action);

			} catch (Throwable e) {

			}
		}

	}

	public void showToast(String value) {
		// Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.tv_buy:

			Intent intent = new Intent();
			intent.putExtra("action", view.getTag() + "");
			setResult(RESULT_OK, intent);
			ProductPurchage.this.finish();

			break;

		default:
			break;
		}
	}

	public void showOkMessage(String title, String message, String action) {

		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("message", message);
		bundle.putString("action", action);

		MessageDialog.newInstance(bundle).show(this.getSupportFragmentManager(), "dialog");

	}

	public String getFromStore(String key) {
		return this.getSharedPreferences("Fcc", 0).getString(key, "");
	}
}
