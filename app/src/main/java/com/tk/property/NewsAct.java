package com.tk.property;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.tk.property.adapters.NewsAdapter;
import com.tk.property.adapters.PaidListAdapter;
import com.tk.property.callbacks.IItemHandler;
import com.tk.property.common.AppSettings;
import com.tk.property.models.NewsItem;
import com.tk.property.models.VideosList;
import com.tk.property.tasks.HTTPTask;
import com.tk.property.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w7u on 10/5/2016.
 */

public class NewsAct extends AppCompatActivity implements IItemHandler{

    private NewsAdapter adapter;
    private ListView listview;
    private List<NewsItem> newslist = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newslayout);
        getNewsdata();
    }

    private void getNewsdata() {

        try {
            String url = AppSettings.getInstance(this).getPropertyValue("tp_news");
            HTTPTask task = new HTTPTask(this, this);
            task.userRequest(getString(R.string.pleasewait), 1, url);
        }catch(Exception e){
            e.printStackTrace();

        }

    }

    @Override
    public void onFinish(Object results, int requestType) {
        newslist = new ArrayList<NewsItem>();
        try {
            switch (requestType) {
                case 1:

                    if (results != null) {
                        JSONObject jsonobject = new JSONObject(results.toString());
                        if (jsonobject.has("status") && jsonobject.optString("status").equalsIgnoreCase("0")) {
                            JSONArray jsonarray = jsonobject.getJSONArray("news_detail");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                NewsItem map = new NewsItem();
                                jsonobject = jsonarray.getJSONObject(i);
                                String newstitle = jsonobject.getString("newstitle");
                                String newsdesc = jsonobject.getString("newsdesc");
                                String newsdate = jsonobject.getString("newsdate");
                                map.setTitle(newstitle);
                                map.setDesc(newsdesc);
                                map.setDate(newsdate);
                                newslist.add(map);
                            }

                            listview = (ListView) findViewById(R.id.news_list);
                            adapter = new NewsAdapter(this, newslist);
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
