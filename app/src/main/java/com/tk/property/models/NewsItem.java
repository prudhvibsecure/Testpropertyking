package com.tk.property.models;

/**
 * Created by w7u on 10/5/2016.
 */

public class NewsItem {

    String title,date,desc;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getDate() {
        return date;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }


}
