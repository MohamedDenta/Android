package com.denta.etbar3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class List_Item extends BaseAdapter {

    private Context context;

    List_Item(Context context)
    {
        this.context = context;
    }
    @Override
    public int getCount() {
        return arr.length;
    }

    @Override
    public Object getItem(int position) {
        return arr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);
            imageView.setCropToPadding(true);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(arr[position]);

        return imageView;

    }

    private Integer[]arr ={R.drawable.opos,R.drawable.oneg,R.drawable.apos,
            R.drawable.aneg,R.drawable.bpos,R.drawable.bneg,
            R.drawable.abpos,R.drawable.abneg};
}
