package com.tk.property.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tk.property.FccMainview;
import com.tk.property.ProductPurchage;
import com.tk.property.R;
import com.tk.property.adapters.ListViewAdapter;
import com.tk.property.callbacks.IItemHandler;
import com.tk.property.common.AppSettings;
import com.tk.property.models.VideosList;
import com.tk.property.tasks.HTTPTask;
import com.tk.property.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FccShop extends ParentFragment implements IItemHandler {
	private View layout = null;
	private FccMainview fccmainview = null;

	private ListView listview;
	private ListViewAdapter adapter;
	private List<VideosList> videolist = null;
	private TextView nocontent;
	private Menu optionsMenu;
	TextView textview_advanced, textview_inter, tv_buy;

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

		layout = inflater.inflate(R.layout.fcc_eshop, container, false);
		nocontent = (TextView) layout.findViewById(R.id.no_video_paid);

		tv_buy = (TextView) layout.findViewById(R.id.tv_buy);
		//tv_buy.setVisibility(View.GONE);

		textview_advanced = (TextView) layout.findViewById(R.id.advanced_txt_shop);
		textview_advanced.setEnabled(false);
		textview_advanced.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// memberNoLogin();
				String username = fccmainview.getFromStore("email");
				if (username.length() == 0) {
					memberNoLogin();
				} else {
					// memberNoLogin_video();
					paidVideosList();
				}
				textview_inter.setEnabled(true);

				textview_advanced.setBackgroundColor(Color.parseColor("#001abb"));
				textview_inter.setBackgroundColor(Color.parseColor("#A19EA9"));

				textview_inter.setTextColor(Color.parseColor("#FFFFFF"));
				textview_advanced.setTextColor(Color.parseColor("#FFFEEF"));

			}
		});

		textview_inter = (TextView) layout.findViewById(R.id.intermediate_txt_shop);
		textview_inter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String username = fccmainview.getFromStore("email");
				if (username.length() == 0) {
					intermediate();
				} else {
					memberNoLogin_video();
					// paidVideosList();
				}

				textview_advanced.setEnabled(true);
				textview_inter.setBackgroundColor(Color.parseColor("#001abb"));
				textview_advanced.setBackgroundColor(Color.parseColor("#A19EA9"));

				textview_inter.setTextColor(Color.parseColor("#FFFEEF"));
				textview_advanced.setTextColor(Color.parseColor("#FFFFFF"));

			}
		});
		String username = fccmainview.getFromStore("email");
		if (username.length() == 0) {
			memberNoLogin();
		} else {
			paidVideosList();
		}

		return layout;
	}

	protected void intermediate() {
		String url = AppSettings.getInstance(getActivity()).getPropertyValue("eshop_nlog");
		HTTPTask task = new HTTPTask(getActivity(), this);

		task.userRequest(getString(R.string.pleasewait), 5, url);

	}

	private void memberNoLogin() {

		String url = AppSettings.getInstance(getActivity()).getPropertyValue("eshop_nlog");
		HTTPTask task = new HTTPTask(getActivity(), this);
		task.disableProgress();
		task.userRequest(getString(R.string.pleasewait), 4, url);
	}

	private void memberNoLogin_video() {

		String url = AppSettings.getInstance(getActivity()).getPropertyValue("nopaids_list");
		url = url.replace("(EMAIL)", fccmainview.getFromStore("email"));
		HTTPTask task = new HTTPTask(getActivity(), this);
		task.disableProgress();
		task.userRequest(getString(R.string.pleasewait), 6, url);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setHasOptionsMenu(true);

		getActivity().supportInvalidateOptionsMenu();

	}

	private void paidVideosList() {
		String url = AppSettings.getInstance(getActivity()).getPropertyValue("nopaids_list");
		url = url.replace("(EMAIL)", fccmainview.getFromStore("email"));
		HTTPTask task = new HTTPTask(getActivity(), this);
		task.userRequest(getString(R.string.pleasewait), 2, url);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		this.optionsMenu = menu;
		inflater.inflate(R.menu.refresh, menu);
		menu.findItem(R.id.action_refresh);

		super.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			String username = fccmainview.getFromStore("email");
			if (username.length() == 0) {
				memberNoLogin();
			} else {
				paidVideosList();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void setRefreshActionButtonState(final boolean refreshing) {

		if (optionsMenu != null) {
			final MenuItem refreshItem = optionsMenu.findItem(R.id.action_refresh);
			if (refreshItem != null) {
				if (refreshing) {
					refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
				} else {
					refreshItem.setActionView(null);
				}
			}
		}
	}

	@Override
	public void onFinish(Object results, int requestType) {
		videolist = new ArrayList<VideosList>();
		try {
			switch (requestType) {
			case 2:

				if (results != null) {
					JSONObject jsonobject = new JSONObject(results.toString());
					if (jsonobject.has("status") && jsonobject.optString("status").equalsIgnoreCase("0")) {

						JSONArray jsonarray = jsonobject.getJSONArray("nopaid_advance_detail");

						for (int i = 0; i < jsonarray.length(); i++) {
							VideosList map = new VideosList();
							jsonobject = jsonarray.getJSONObject(i);

							String vname = jsonobject.getString("videoname");
							String imagename = jsonobject.getString("image");
							String videoid = jsonobject.getString("videoid");
							String video_path = jsonobject.getString("video");
							final String rval = jsonobject.getString("range");

							map.setVideoname(vname);
							map.setImage(imagename);
							map.setVideoid(videoid);
							map.setVideo(video_path);
							map.setRangeval(rval);

							tv_buy.setText("Subscribe for 3 Months- $" + rval);
							tv_buy.setVisibility(View.VISIBLE);
							tv_buy.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View view) {

									Intent mainIntent = new Intent(getActivity(), ProductPurchage.class);
									mainIntent.putExtra("range", rval);
									startActivity(mainIntent);
								}
							});

							videolist.add(map);
						}

						listview = (ListView) layout.findViewById(R.id.list_paid);
						adapter = new ListViewAdapter(getActivity(), videolist);
						listview.setAdapter(adapter);
					}
					if (jsonobject.has("status") && jsonobject.optString("status").equalsIgnoreCase("1")) {
						// showToast(jsonobject.optString("statusdescription"));intermediate_detail
						nocontent.setVisibility(View.VISIBLE);
						nocontent.setText(jsonobject.optString("statusdescription"));

						// tv_buy.setVisibility(View.GONE);
					}
				}
				break;
			case 4:

				if (results != null) {
					JSONObject jsonobject = new JSONObject(results.toString());
					if (jsonobject.has("status") && jsonobject.optString("status").equalsIgnoreCase("0")) {

						JSONArray jsonarray = jsonobject.getJSONArray("advance_detail");

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

							tv_buy.setText("Subscribe for 3 Months- $" + rval);
							tv_buy.setVisibility(View.VISIBLE);
							tv_buy.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View view) {
									if (fccmainview.getFromStore("email").length() == 0) {
										Snackbar.make(view, R.string.logalrt, Snackbar.LENGTH_LONG)
												.setActionTextColor(Color.YELLOW)
												.setAction("Ok", new OnClickListener() {

											@Override
											public void onClick(View v) {
												fccmainview.showLoginDialog();
											}

										}).show();
									}

								}
							});
							videolist.add(map);
						}

						listview = (ListView) layout.findViewById(R.id.list_paid);
						adapter = new ListViewAdapter(getActivity(), videolist);
						listview.setAdapter(adapter);
					}
					if (jsonobject.has("status") && jsonobject.optString("status").equalsIgnoreCase("1")) {
						// showToast(jsonobject.optString("statusdescription"));
						nocontent.setVisibility(View.VISIBLE);
						nocontent.setText(jsonobject.optString("statusdescription"));

						// nocontent.setText(jsonobject.optString("statusdescription"));
					}
				}
				break;
			case 5:

				if (results != null) {
					JSONObject jsonobject = new JSONObject(results.toString());
					if (jsonobject.has("status") && jsonobject.optString("status").equalsIgnoreCase("0")) {

						JSONArray jsonarray = jsonobject.getJSONArray("intermediate_detail");

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
							tv_buy.setText("Subscribe for 3 Months- $" + rval);
							tv_buy.setVisibility(View.VISIBLE);
							tv_buy.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View view) {
									if (fccmainview.getFromStore("email").length() == 0) {
										Snackbar.make(view, R.string.logalrt, Snackbar.LENGTH_LONG)
												.setActionTextColor(Color.YELLOW)
												.setAction("Ok", new OnClickListener() {

											@Override
											public void onClick(View v) {
												fccmainview.showLoginDialog();
											}

										}).show();
									}

								}
							});
							videolist.add(map);
						}

						listview = (ListView) layout.findViewById(R.id.list_paid);
						adapter = new ListViewAdapter(getActivity(), videolist);
						listview.setAdapter(adapter);
					}
					if (jsonobject.has("status") && jsonobject.optString("status").equalsIgnoreCase("1")) {
						// showToast(jsonobject.optString("statusdescription"));
						nocontent.setVisibility(View.VISIBLE);
						nocontent.setText(jsonobject.optString("statusdescription"));
					}
				}
				break;
			case 6:

				if (results != null) {
					JSONObject jsonobject = new JSONObject(results.toString());
					if (jsonobject.has("status") && jsonobject.optString("status").equalsIgnoreCase("0")) {

						JSONArray jsonarray = jsonobject.getJSONArray("nopaid_intermediate_detail");

						for (int i = 0; i < jsonarray.length(); i++) {
							VideosList map = new VideosList();
							jsonobject = jsonarray.getJSONObject(i);

							String vname = jsonobject.getString("videoname");
							String imagename = jsonobject.getString("image");
							String videoid = jsonobject.getString("videoid");
							String video_path = jsonobject.getString("video");
							final String rval = jsonobject.getString("range");

							map.setVideoname(vname);
							map.setImage(imagename);
							map.setVideoid(videoid);
							map.setVideo(video_path);
							map.setRangeval(rval);
							
							tv_buy.setText("Subscribe for 3 Months- $" + rval);
							tv_buy.setVisibility(View.VISIBLE);
							tv_buy.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View view) {
									Intent mainIntent = new Intent(getActivity(), ProductPurchage.class);
									mainIntent.putExtra("range", rval);
									startActivity(mainIntent);

								}
							});
							videolist.add(map);
						}

						listview = (ListView) layout.findViewById(R.id.list_paid);
						adapter = new ListViewAdapter(getActivity(), videolist);
						listview.setAdapter(adapter);
					}
					if (jsonobject.has("status") && jsonobject.optString("status").equalsIgnoreCase("1")) {
						// showToast(jsonobject.optString("statusdescription"));
						nocontent.setVisibility(View.VISIBLE);
						nocontent.setText(jsonobject.optString("statusdescription"));

						// nocontent.setText(jsonobject.optString("statusdescription"));
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