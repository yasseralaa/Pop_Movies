package com.example.yasseralaaeldin.phase2.helpers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Yasser Alaa Eldin on 1/2/2016.
 */
public class LocalImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] Mylist;
    public LocalImageAdapter(Context c, String[] Mylist) {
        mContext = c;
        this.Mylist = Mylist;
    }

    public int getCount() {
        return Mylist.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(305, 385));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        String file = "/sdcard/PopMovies/"+Mylist[position];
        // setting downloaded into image view
        File imgFile = new File(file);
        Picasso.with(mContext).load(imgFile).into(imageView);
        return imageView;
    }

}