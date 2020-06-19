package com.aniapps.siri;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.aniapps.adapters.ZoomSliderAdapter;
import com.aniapps.utils.CustomViewPager;
import com.aniapps.utils.OnLoadMoreListener;
import com.aniapps.utils.discrete.DiscreteScrollView;
import com.aniapps.utils.discrete.ImagesAdapter;
import com.aniapps.utils.discrete.transform.ScaleTransformer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;


public class ZoomImageAct extends AppCompatActivity implements
        DiscreteScrollView.ScrollStateChangeListener<ImagesAdapter.ViewHolder>,
        DiscreteScrollView.OnItemChangedListener<ImagesAdapter.ViewHolder> {
    RecyclerView carThumbnails;
    static CustomViewPager viewpager;
    private ZoomSliderAdapter adapter;
    private ArrayList<String> list = new ArrayList<>();

    private int currentSelectedCar;
    private CarThumbnailsAdapter carThumbnailsAdapter;
    AppCompatImageView ic_close;
    AppCompatTextView txt_title;
    String auction_id = "", auction_event_id = "", from_screen = "";
    private DiscreteScrollView _picker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoomimageact);
        list.add("https://5.imimg.com/data5/MD/EV/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        list.add("https://5.imimg.com/data5/OE/PC/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        list.add("https://5.imimg.com/data5/QJ/GV/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        list.add("https://5.imimg.com/data5/GF/JS/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        list.add("https://5.imimg.com/data5/YH/YI/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        list.add("https://5.imimg.com/data5/DW/GJ/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        list.add("https://5.imimg.com/data5/BK/GE/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        list.add("https://5.imimg.com/data5/KY/AP/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");

        ic_close = findViewById(R.id.close_back);
        txt_title = findViewById(R.id.txt_title);
        txt_title.setText(getIntent().getStringExtra("title"));
        from_screen = getIntent().getStringExtra("from_screen");
        auction_id = getIntent().getStringExtra("auction_id");
        auction_event_id = getIntent().getStringExtra("auction_event_id");
        currentSelectedCar = getIntent().getIntExtra("currentSelectedCar", 0);
        viewpager = findViewById(R.id.viewpager);
        carThumbnails = findViewById(R.id.carThumbnails);

        adapter = new ZoomSliderAdapter(ZoomImageAct.this, list);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(currentSelectedCar);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                currentSelectedCar = i1;
                _picker.smoothScrollToPosition(i);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        setHorizontalList();

        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        _picker = (DiscreteScrollView) findViewById(R.id._picker);
        _picker.setSlideOnFling(true);
        _picker.setAdapter(new ImagesAdapter(list));
        _picker.addOnItemChangedListener(this);
        _picker.addScrollStateChangeListener(this);
        _picker.scrollToPosition(2);
        _picker.setItemTransitionTimeMillis(100);
        _picker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

    }

    private void setHorizontalList() {
        View overlay = findViewById(R.id.llMainLayout);
        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                /*| View.SYSTEM_UI_FLAG_FULLSCREEN*/);
        carThumbnails.setHasFixedSize(true);
        carThumbnails.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        carThumbnailsAdapter = new CarThumbnailsAdapter(ZoomImageAct.this, list, carThumbnails);
        carThumbnails.setAdapter(carThumbnailsAdapter);

        carThumbnails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public void onScrollEnd(@NonNull ImagesAdapter.ViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScrollStart(@NonNull ImagesAdapter.ViewHolder holder, int position) {
        holder.hideText();
    }

    @Override
    public void onScroll(
            float position,
            int currentIndex, int newIndex,
            @Nullable ImagesAdapter.ViewHolder currentHolder,
            @Nullable ImagesAdapter.ViewHolder newHolder) {
        String current = list.get(currentIndex);
        if (newIndex >= 0 && newIndex < _picker.getAdapter().getItemCount()) {
            String next = list.get(newIndex);
//            forecastView.onScroll(1f - Math.abs(position), current, next);
            viewpager.setCurrentItem(newIndex);
        }
    }

    @Override
    public void onCurrentItemChanged(@Nullable ImagesAdapter.ViewHolder viewHolder, int adapterPosition) {

    }


    public static class CarThumbnailsAdapter extends RecyclerView.Adapter {
        private final int VIEW_ITEM = 1;
        private final int VIEW_PROG = 0;
        private ArrayList<String> imagesList;
        private int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;
        private boolean loading;
        private OnLoadMoreListener onLoadMoreListener;
        public Context context;
        public RecyclerView recyclerView;

        public CarThumbnailsAdapter(Context context, ArrayList<String> imagesList, RecyclerView recyclerView) {
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


                try {
                    Glide.with(context)
                            .load(imagesList.get(position))
                            .signature(new ObjectKey("1"))
                            .placeholder(R.drawable.no_image_gallery)
                            .fitCenter()
                            .into(((CarThumbnailsViewHolder) holder).carThumbnails);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ((CarThumbnailsViewHolder) holder).carThumbnails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewpager.setCurrentItem(position);
                    }
                });

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
            return imagesList.size();
        }

        public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
            this.onLoadMoreListener = onLoadMoreListener;
        }

        public class CarThumbnailsViewHolder extends RecyclerView.ViewHolder {
            protected ImageView carThumbnails;

            public CarThumbnailsViewHolder(View view) {
                super(view);
                this.carThumbnails = view.findViewById(R.id.carThumbnails);

            }
        }

        public class ProgressViewHolder extends RecyclerView.ViewHolder {
            public ProgressBar progressBar;

            public ProgressViewHolder(View v) {
                super(v);
                progressBar = v.findViewById(R.id.progressBar1);
            }
        }

    }


}
