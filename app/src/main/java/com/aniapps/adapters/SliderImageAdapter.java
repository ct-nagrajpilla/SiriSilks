package com.aniapps.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aniapps.siri.OnPagerClick;
import com.aniapps.siri.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class SliderImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<String> imagesList;
    private LayoutInflater inflater;
    private OnPagerClick pagerClick;


    // constructor
    public SliderImageAdapter(Activity activity, ArrayList<String> imagesList, OnPagerClick pagerClick) {
        this._activity = activity;
        this.imagesList = imagesList;
        this.pagerClick = pagerClick;
    }

    @Override
    public int getCount() {
        return this.imagesList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;

        final int pos = position;
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = viewLayout.findViewById(R.id.imgDisplay);

        try {
            Glide.with(_activity).load(imagesList.get(pos)).placeholder(R.mipmap.fav_none2).centerCrop().into(imgDisplay);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((ViewPager) container).addView(viewLayout);

        viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pagerClick.onClick(pos, view);
            }
        });

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}
