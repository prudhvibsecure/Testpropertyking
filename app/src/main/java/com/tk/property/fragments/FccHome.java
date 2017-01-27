package com.tk.property.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tk.property.FccMainview;
import com.tk.property.R;
import com.tk.property.adapters.PaidListAdapter;
import com.tk.property.callbacks.IItemHandler;
import com.tk.property.common.AppSettings;
import com.tk.property.models.VideosList;
import com.tk.property.tasks.HTTPTask;
import com.tk.property.utils.Utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FccHome extends ParentFragment implements IItemHandler {

	private View layout = null;
	private FccMainview fccmainview = null;
	private ListView listview;
	private PaidListAdapter adapter;

	private List<VideosList> videolist = null;
	private SearchView searchView = null;
	private TextView login;
	TextView textview_advanced, textview_inter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.fccmainview = (FccMainview) activity;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		layout = inflater.inflate(R.layout.fcc_home, container, false);
		login = (TextView) layout.findViewById(R.id.no_video);

		textview_advanced = (TextView) layout.findViewById(R.id.advanced_txt);
		textview_advanced.setEnabled(false);

		textview_advanced.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String email = fccmainview.getFromStore("email");
				if (email.length() == 0) {
					fccmainview.showLoginDialog();
				} else {
					VideosList();
				}

				textview_inter.setEnabled(true);

				textview_advanced.setBackgroundColor(Color.parseColor("#001abb"));
				textview_inter.setBackgroundColor(Color.parseColor("#A19EA9"));

				textview_inter.setTextColor(Color.parseColor("#FFFFFF"));
				textview_advanced.setTextColor(Color.parseColor("#FFFEEF"));

			}
		});

		textview_inter = (TextView) layout.findViewById(R.id.intermediate_txt);

		textview_inter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email = fccmainview.getFromStore("email");
				if (email.length() == 0) {
					fccmainview.showLoginDialog();
				} else {
					VideosList_inter();
				}

				textview_advanced.setEnabled(true);
				textview_inter.setBackgroundColor(Color.parseColor("#001abb"));
				textview_advanced.setBackgroundColor(Color.parseColor("#A19EA9"));

				textview_inter.setTextColor(Color.parseColor("#FFFEEF"));
				textview_advanced.setTextColor(Color.parseColor("#FFFFFF"));

			}
		});

		// VideosList();
		String email = fccmainview.getFromStore("email");
		if (email.length() == 0) {
			fccmainview.showLoginDialog();
		} else {
			VideosList();
		}

		return layout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setHasOptionsMenu(true);

		getActivity().supportInvalidateOptionsMenu();

	}

	private void VideosList() {
		String url = AppSettings.getInstance(getActivity()).getPropertyValue("paids_list");
		url = url.replace("(EMAIL)", fccmainview.getFromStore("email"));
		HTTPTask task = new HTTPTask(getActivity(), this);
		task.disableProgress();
		task.userRequest(getString(R.string.pleasewait), 1, url);

	}

	private void VideosList_inter() {
		String url = AppSettings.getInstance(getActivity()).getPropertyValue("paids_list");
		url = url.replace("(EMAIL)", fccmainview.getFromStore("email"));
		HTTPTask task = new HTTPTask(getActivity(), this);
		task.userRequest(getString(R.string.pleasewait), 2, url);

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
						JSONArray jsonarray = jsonobject.getJSONArray("paid_advance_detail");

						for (int i = 0; i < jsonarray.length(); i++) {
							VideosList map = new VideosList();
							jsonobject = jsonarray.getJSONObject(i);

							String vname = jsonobject.getString("videoname");
							String imagename = jsonobject.getString("image");
							String videoid = jsonobject.getString("videoid");
							String video_path = jsonobject.getString("video");

							map.setVideoname(vname);
							map.setImage(imagename);
							map.setVideoid(videoid);
							map.setVideo(video_path);

							videolist.add(map);
						}

						listview = (ListView) layout.findViewById(R.id.list);
						adapter = new PaidListAdapter(getActivity(), videolist);
						listview.setAdapter(adapter);
					}
					if (jsonobject.has("status") && jsonobject.optString("status").equalsIgnoreCase("1")) {

						String email = fccmainview.getFromStore("email");
						if (email.length() == 0) {
							login.setVisibility(View.VISIBLE);
							// login.setText(R.string.logalrt);
							fccmainview.showLoginDialog();
						}

					}
				}
				break;
			case 2:

				if (results != null) {
					JSONObject jsonobject = new JSONObject(results.toString());
					if (jsonobject.has("status") && jsonobject.optString("status").equalsIgnoreCase("0")) {
						JSONArray jsonarray = jsonobject.getJSONArray("paid_intermediate_detail");

						for (int i = 0; i < jsonarray.length(); i++) {
							VideosList map = new VideosList();
							jsonobject = jsonarray.getJSONObject(i);

							String vname = jsonobject.getString("videoname");
							String imagename = jsonobject.getString("image");
							String videoid = jsonobject.getString("videoid");
							String video_path = jsonobject.getString("video");

							map.setVideoname(vname);
							map.setImage(imagename);
							map.setVideoid(videoid);
							map.setVideo(video_path);

							videolist.add(map);
						}

						listview = (ListView) layout.findViewById(R.id.list);
						adapter = new PaidListAdapter(getActivity(), videolist);
						listview.setAdapter(adapter);
					}
					if (jsonobject.has("status") && jsonobject.optString("status").equalsIgnoreCase("1")) {

						String email = fccmainview.getFromStore("email");
						if (email.length() == 0) {
							login.setVisibility(View.VISIBLE);
							// login.setText(R.string.logalrt);
							fccmainview.showLoginDialog();
						}

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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.searchview, menu);
		searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub

				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				adapter.filter(newText);
				return false;
			}
		});

		super.onCreateOptionsMenu(menu, inflater);
	}

	public void showToast(String value) {
		Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int text) {
		Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onError(String errorCode, int requestType) {
		showToast(errorCode);
		Utils.dismissProgress();

	}

}
