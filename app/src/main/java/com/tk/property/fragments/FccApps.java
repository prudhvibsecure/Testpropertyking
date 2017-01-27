package com.tk.property.fragments;

import java.util.List;

import com.tk.property.FccMainview;
import com.tk.property.R;
import com.tk.property.adapters.ListViewAdapter;
import com.tk.property.callbacks.IItemHandler;
import com.tk.property.models.VideosList;
import com.tk.property.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class FccApps extends ParentFragment {

	private View layout = null;
	private FccMainview fccmainview = null;

	private ListView listview;
	private ListViewAdapter adapter;
	private List<VideosList> videolist = null;

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

		layout = inflater.inflate(R.layout.fcc_apps, container, false);
		layout.findViewById(R.id.app_dpa).setOnClickListener(fccmainview);

		return layout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setHasOptionsMenu(true);
		getActivity().supportInvalidateOptionsMenu();

	}

	public void showToast(String value) {
		Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int text) {
		Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
	}

}