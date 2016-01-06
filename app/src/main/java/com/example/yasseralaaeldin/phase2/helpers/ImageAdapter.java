package com.example.yasseralaaeldin.phase2.helpers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.yasseralaaeldin.phase2.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Yasser Alaa Eldin on 11/30/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] Mylist;
    public ImageAdapter(Context c, String[] Mylist) {
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

        Picasso.with(mContext) //
                .load("http://image.tmdb.org/t/p/w185/" + Mylist[position])
               // .placeholder(R.drawable.sample_0)
                .error(R.drawable.android)
                .fit()
                .tag(mContext)
                .into(imageView);
        return imageView;
    }

}