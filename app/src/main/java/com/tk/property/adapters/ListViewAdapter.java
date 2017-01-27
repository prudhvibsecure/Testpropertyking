package com.tk.property.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import com.tk.property.FccMainview;
import com.tk.property.ProductPurchage;
import com.tk.property.R;
import com.tk.property.VideoCapture;
import com.tk.property.common.AppSettings;
import com.tk.property.common.Item;
import com.tk.property.imageloaders.ImageLoader;
import com.tk.property.models.VideosList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private Vector<Item> items;
	public List<VideosList> videolist = null;

	private ArrayList<VideosList> arraylist;
	int count = 0;
	int progress = 0;

	public ListViewAdapter(Context context, List<VideosList> arraylist) {
		this.context = context;
		videolist = arraylist;
		imageLoader = new ImageLoader(context, false);
		this.arraylist = new ArrayList<VideosList>();
		this.arraylist.addAll(videolist);
	}

	@Override
	public int getCount() {
		return videolist.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position + 1;
	}

	public static class ViewHolder {
		TextView title;
		TextView buy;
		TextView price;
		ImageView flag;

	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.list_row, parent, false);
		holder = new ViewHolder();
		holder.title = (TextView) itemView.findViewById(R.id.propertyname_title);
		holder.buy = (TextView) itemView.findViewById(R.id.tv_buy);
		holder.price = (TextView) itemView.findViewById(R.id.property_price);
		holder.flag = (ImageView) itemView.findViewById(R.id.imageview);
		String file_path = AppSettings.getInstance(context).getPropertyValue("file_download");
		final String filepath = file_path + videolist.get(position).getVideo();
		// final String mimeType = "video/mp4";

		String viddrel = videolist.get(position).getRangeval();
		if (viddrel.length() > 0) {
			holder.buy.setVisibility(View.VISIBLE);
		}

		holder.title.setText((Html.fromHtml(videolist.get(position).getVideoname())));

		String link_image = videolist.get(position).getImage();
		String path = file_path + link_image;
		imageLoader.DisplayImage(path, holder.flag);

		holder.buy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				String username = getFromStore("email");
//				if (username.length() == 0) {
//					// Toast.makeText(context, R.string.logalrt,
//					// Toast.LENGTH_LONG).show();
//					Snackbar.make(holder.title, R.string.logalrt, Snackbar.LENGTH_LONG).setActionTextColor(Color.YELLOW)
//							.setAction("Ok", new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							((FccMainview) context).showLoginDialog();
//						}
//
//					}).show();
//					return;
//				}

				Intent intent = new Intent(context, VideoCapture.class);
				intent.putExtra("filepath", filepath + "");
				// intent.putExtra("mimeType", mimeType + "");
				context.startActivity(intent);

			}
		});
//		holder.buy.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				String username = getFromStore("email");
//				if (username.length() == 0) {
//					// Toast.makeText(context, R.string.logalrt,
//					// Toast.LENGTH_LONG).show();
//					Snackbar.make(holder.title, R.string.logalrt, Snackbar.LENGTH_LONG).setActionTextColor(Color.YELLOW)
//							.setAction("Ok", new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							((FccMainview) context).showLoginDialog();
//						}
//
//					}).show();
//					return;
//				}
//				String vid = videolist.get(position).getVideoid();
//				// ((FccMainview) context).showCustomWebview(vid);
//				Intent mainIntent = new Intent(context, ProductPurchage.class);
//				mainIntent.putExtra("vid", vid);
//				mainIntent.putExtra("range", videolist.get(position).getRangeval());
//				context.startActivity(mainIntent);
//			}
//		});
		return itemView;
	}

	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		videolist.clear();
		if (charText.length() == 0) {
			videolist.addAll(arraylist);
		} else {
			for (VideosList vidlist : arraylist) {
				if (vidlist.getVideoname().toLowerCase(Locale.getDefault()).contains(charText)) {
					videolist.add(vidlist);
				}
			}
		}
		notifyDataSetChanged();
	}

	public String getFromStore(String key) {
		return context.getSharedPreferences("Fcc", 0).getString(key, "");
	}
}
