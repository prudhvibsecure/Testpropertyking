package com.tk.property.fragments;

import com.tk.property.FccMainview;
import com.tk.property.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FccMore extends ParentFragment {

	private View layout = null;

	private FccMainview fccmainview = null;

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

		layout = inflater.inflate(R.layout.fcc_more, container, false);
		layout.findViewById(R.id.tv_monials).setOnClickListener(fccmainview);
		layout.findViewById(R.id.tv_rateapp).setOnClickListener(fccmainview);
		layout.findViewById(R.id.tv_share).setOnClickListener(fccmainview);
		layout.findViewById(R.id.tv_news).setOnClickListener(fccmainview);
		return layout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setHasOptionsMenu(true);

		getActivity().supportInvalidateOptionsMenu();
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {

		layout = null;

		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onFragmentChildClick(View view) {
		super.onFragmentChildClick(view);
	}

}
