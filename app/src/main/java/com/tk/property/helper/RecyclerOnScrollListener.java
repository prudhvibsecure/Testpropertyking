package com.tk.property.helper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class RecyclerOnScrollListener extends
		RecyclerView.OnScrollListener {

	public static String TAG = RecyclerOnScrollListener.class.getSimpleName();

	private int previousTotal = 0; // The total number of items in the dataset after the last load
	private boolean loading = true; // True if we are still waiting for the last set of data to load.
	private int visibleThreshold = 2; // The minimum amount of items to have below your current scroll position before loading more.
	int firstVisibleItem, visibleItemCount, totalItemCount;

	private int current_page = 0;

	private LinearLayoutManager mLinearLayoutManager;

	public RecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
		this.mLinearLayoutManager = linearLayoutManager;
	}
	
	/*public RecyclerOnScrollListener(GridLayoutManager gridLayoutManager) {
		this.mLinearLayoutManager = gridLayoutManager;
	}*/

	@Override
	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		super.onScrolled(recyclerView, dx, dy);

		visibleItemCount = recyclerView.getChildCount();
		totalItemCount = mLinearLayoutManager.getItemCount();
		firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

		if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;
			}
		}
		if (!loading
				&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
			// End has been reached

			// Do something
			current_page++;

			onLoadMoreData(current_page);

			loading = true;
		}

	}

	@Override
	public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
		// TODO Auto-generated method stub
		super.onScrollStateChanged(recyclerView, newState);
	}

	public abstract void onLoadMoreData(int current_page);

}
