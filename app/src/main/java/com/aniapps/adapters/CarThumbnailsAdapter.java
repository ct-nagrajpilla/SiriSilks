package com.aniapps.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aniapps.models.Images;
import com.aniapps.siri.R;
import com.aniapps.utils.ImageSelectionEvent;
import com.aniapps.utils.OnLoadMoreListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CarThumbnailsAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int[] imagesList;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    public Context context;
    public RecyclerView recyclerView;

    public CarThumbnailsAdapter(Context context, int[] imagesList, RecyclerView recyclerView) {
        this.imagesList = imagesList;
        this.context = context;
        this.recyclerView = recyclerView;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return imagesList != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_car_thumbs, parent, false);


            vh = new CarThumbnailsViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override

    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CarThumbnailsViewHolder) {
            //Images images = imagesList[position];

           // Picasso.with(context).load(images.getImagepath()).fit().centerCrop().into(((CarThumbnailsViewHolder) holder).carThumbnails);

            try {
                Glide.with(context).load(imagesList[position]).signature(new ObjectKey("1")).placeholder(R.mipmap.fav_none).fitCenter().into(((CarThumbnailsViewHolder) holder).carThumbnails);
            } catch (Exception e) {
                e.printStackTrace();
            }

          /*  ((CarThumbnailsViewHolder) holder).images = imagesList[position];

            ((CarThumbnailsViewHolder) holder).carThumbnails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new ImageSelectionEvent(position));
                }
            });*/
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return imagesList.length;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public class CarThumbnailsViewHolder extends RecyclerView.ViewHolder {
        protected ImageView carThumbnails;
     //   protected Images images;

        public CarThumbnailsViewHolder(View view) {
            super(view);
            this.carThumbnails = view.findViewById(R.id.carThumbnails);

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar1);
        }
    }

}