package com.tk.property.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tk.property.R;
import com.tk.property.common.AppSettings;
import com.tk.property.models.NewsItem;

import java.util.List;

/**
 * Created by w7u on 10/5/2016.
 */

public class NewsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    public List<NewsItem> newslist = null;

    public NewsAdapter(Context context, List<NewsItem> arraylist) {
        this.context = context;
        newslist = arraylist;
    }

    @Override
    public int getCount() {
        return newslist.size();
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
        TextView description;
        TextView date;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final NewsAdapter.ViewHolder holder;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.news_item, parent, false);
        holder = new NewsAdapter.ViewHolder();
        holder.title = (TextView) itemView.findViewById(R.id.nw_title);
        holder.description = (TextView) itemView.findViewById(R.id.nw_desription);
        holder.date = (TextView) itemView.findViewById(R.id.nw_date);
        holder.title.setText((Html.fromHtml(newslist.get(position).getTitle())));
        holder.description.setText((Html.fromHtml(newslist.get(position).getDesc())));
        holder.date.setText((Html.fromHtml(newslist.get(position).getDate())));
        return itemView;
    }
}
