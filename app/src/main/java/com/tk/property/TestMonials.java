package com.tk.property;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tk.property.adapters.PaidListAdapter;
import com.tk.property.callbacks.IItemHandler;
import com.tk.property.common.AppSettings;
import com.tk.property.models.VideosList;
import com.tk.property.tasks.HTTPTask;
import com.tk.property.utils.Utils;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class TestMonials extends AppCompatActivity implements IItemHandler {

	private PaidListAdapter adapter;
	private ListView listview;
	private List<VideosList> videolist = null;
	private LinearLayout ll_test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fcc_home);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("TestMonials");
		toolbar.setVisibility(View.VISIBLE);
		setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		ll_test = (LinearLayout) findViewById(R.id.ll_home);
		ll_test.setVisibility(View.GONE);
		String url = AppSettings.getInstance(this).getPropertyValue("test_monials");
		HTTPTask task = new HTTPTask(this, this);
		task.userRequest(getString(R.string.pleasewait), 1, url);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:

			TestMonials.this.finish();

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onFinish(Object results, int requestType) {
		videolist = new ArrayList<VideosList>();
		try {
			switch (requestType) {
			case 1:

				if (results != null) {
					JSONObject jsonobject = new JSONObject(results.toString());
					if (jsonobject.has("status") && jsonobject.optString("status").equalsIgnoreCase("0")) {
						JSONArray jsonarray = jsonobject.getJSONArray("testimonials");

						for (int i = 0; i < jsonarray.length(); i++) {
							VideosList map = new VideosList();
							jsonobject = jsonarray.getJSONObject(i);

							String vname = jsonobject.getString("videoname");
							String imagename = jsonobject.getString("image");
							String videoid = jsonobject.getString("videoid");
							String video_path = jsonobject.getString("video");
							String rval = jsonobject.getString("range");
							map.setVideoname(vname);
							map.setImage(imagename);
							map.setVideoid(videoid);
							map.setVideo(video_path);
							map.setRangeval(rval);
							videolist.add(map);
						}

						listview = (ListView) findViewById(R.id.list);
						adapter = new PaidListAdapter(this, videolist);
						listview.setAdapter(adapter);
					}
				}
				break;

			default:
				break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void showToast(String value) {
		Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onError(String errorCode, int requestType) {
		showToast(errorCode);
		Utils.dismissProgress();

	}

}
