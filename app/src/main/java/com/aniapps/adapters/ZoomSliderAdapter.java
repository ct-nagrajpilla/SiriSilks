package com.aniapps.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aniapps.siri.R;
import com.aniapps.utils.photoview.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;

//https://github.com/davemorrissey/subsampling-scale-image-view/issues/178
public class ZoomSliderAdapter extends PagerAdapter {

    private Activity _activity;
    //    private int[] imagesList;
    private ArrayList<String> imagesList;
    private LayoutInflater inflater;

    // constructor
    public ZoomSliderAdapter(Activity activity,
                             ArrayList<String> imagesList) {
        this._activity = activity;
        this.imagesList = imagesList;
    }

    @Override
    public int getCount() {
        return this.imagesList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((PhotoView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final PhotoView imgDisplay;
        final int pos = position;
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_zoom_image, container,
                false);
        imgDisplay = (PhotoView) viewLayout.findViewById(R.id.imgDisplay);
        try {
            Glide.with(_activity).load(imagesList.get(pos))
                    .signature(new ObjectKey(imagesList.get(pos)))
                    .placeholder(R.drawable.no_image_gallery)
                    .fitCenter()
                    .centerInside()
                    .into(imgDisplay);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((PhotoView) object);

    }

}
