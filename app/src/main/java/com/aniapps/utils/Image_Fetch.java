package com.aniapps.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.aniapps.siri.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * Created by ChRajeshKumar on 21-Feb-17.
 * https://github.com/igreenwood/SimpleCropView
 */

public final class Image_Fetch {
    public static Image_Fetch instance;

    public static Image_Fetch getInstance() {
        if (null == instance) {
            instance = new Image_Fetch();
        }
        return instance;
    }

    public void LoadImage(Context context, AppCompatImageView imgview, String source) {

        Glide.with(context)
                .load(source)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image_gallery))
                .into(imgview);
        /*Glide.with(context)
               .load(source)
                .centerCrop()
                .placeholder(R.drawable.no_image_gallery)
                .error(R.drawable.no_image_gallery)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imgview);*/

        // Glide.get(context).setMemoryCategory(MemoryCategory.HIGH);
        //  Glide.with(context).load(source).into(imgview).onLoadFailed(context.getResources().getDrawable(R.drawable.no_image_gallery));
    }

    public void DisplayImagePager(Context mContext, Integer url, final AppCompatImageView imageView,
                                  int img_id, Priority priority, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(mContext)
                .load(url)
                .thumbnail(0.1f)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);

    }


}
