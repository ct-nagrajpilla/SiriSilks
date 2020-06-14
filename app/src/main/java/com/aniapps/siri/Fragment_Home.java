package com.aniapps.siri;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aniapps.models.MyAreas;
import com.aniapps.models.NewArrivals;
import com.aniapps.models.Products;
import com.aniapps.models.Trending;
import com.aniapps.utils.Image_Fetch;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Fragment_Home extends Fragment {

    private static final String TAG = Fragment_Home.class.getSimpleName();
    int page = 0;

    // url to fetch shopping items
    Integer[] imageId = {R.drawable.aaa, R.drawable.bbb, R.drawable.ccc};
    private RecyclerView recyclerView, recyclerView1, recyclerView2;
    private ViewPager imgViewPager;
    private MainCatAdapter mAdapter;
    private NewArraivalsAdapter pAdapter1;
    private TrendingAdapter pAdapter2;
    private ArrayList<MyAreas> areas = new ArrayList<>();
    private ArrayList<Products> newArrivals = new ArrayList<>();
    private ArrayList<Products> trendings = new ArrayList<>();

    Context context;
    LinearLayout dot_layout;

    public Fragment_Home(Context context, ArrayList<MyAreas> areas, ArrayList<Products> newArrivals, ArrayList<Products> trendings) {
        this.context = context;
        this.areas = areas;
        this.newArrivals = newArrivals;
        this.trendings = trendings;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView1 = view.findViewById(R.id.recycler_view1);
        recyclerView2 = view.findViewById(R.id.recycler_view2);
        mAdapter = new MainCatAdapter(getActivity(), areas);
        pAdapter1 = new NewArraivalsAdapter(newArrivals);
         pAdapter2 = new TrendingAdapter(trendings);
        imgViewPager = (ViewPager) view.findViewById(R.id.img_viewpager);
        dot_layout = (LinearLayout) view.findViewById(R.id.dot_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView1.setAdapter(pAdapter1);
         recyclerView2.setAdapter(pAdapter2);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView2.setNestedScrollingEnabled(false);

        imgViewPager.getLayoutParams().height = (int) ((float) ((getActivity()
                .getResources().getDisplayMetrics().widthPixels)) / 1.77);
        imgViewPager.setOffscreenPageLimit(0);
        imgViewPager.setAdapter(new ImageSlidePagerAdapter(
                getActivity()));

        if (imageId.length > 1) {
            dot_layout.setVisibility(View.VISIBLE);
            imgViewPager.setCurrentItem(0);
            addimage(0, imageId.length, dot_layout);
        } else {
            dot_layout.setVisibility(View.GONE);
            addimage(0, 0, dot_layout);
        }

        final Handler mHandler = new Handler();
        final Runnable mUpdateResults = new Runnable() {
            public void run() {
                int numPages = imgViewPager.getAdapter().getCount();
                page = (page + 1) % numPages;
                imgViewPager.setCurrentItem(page);

            }
        };

        int delay = 1000; // delay for 1 sec.

        int period = 4000; // repeat every 4 sec.

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                mHandler.post(mUpdateResults);

            }

        }, delay, period);


        imgViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pos) {
                addimage(pos, imageId.length, dot_layout);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addimage(int pos, int size, LinearLayout dot_layout) {
        dot_layout.removeAllViews();
        //  LayoutInflater in = Objects.requireNonNull(getActivity()).getLayoutInflater();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        for (int i = 0; i < size; i++) {
            AppCompatImageView iv;
            view = inflater.inflate(R.layout.dotedimageview, null);
            iv = (AppCompatImageView) view.findViewById(R.id.dotedimage);
            if (i == pos) {
                iv.setBackgroundResource(R.drawable.pagerindicator_red);
            } else {
                iv.setBackgroundResource(R.drawable.pagerindicator_white);
            }
            iv.setId(i);
            dot_layout.addView(view);
        }

    }

    private class ImageSlidePagerAdapter extends PagerAdapter {
        private Activity context;

        private Image_Fetch loader;
        private int pos;

        public ImageSlidePagerAdapter(Activity c) {
            context = c;
            loader = new Image_Fetch();
        }

        public int getCount() {
            return imageId.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, final int position) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.pager_image, null);
            AppCompatImageView imgurl = (AppCompatImageView) view.findViewById(R.id.vdp_img_url);
            ProgressBar pbr = (ProgressBar) view.findViewById(R.id.progressBar1);
            pbr.getIndeterminateDrawable().setColorFilter(
                    context.getResources().getColor(R.color.pbr_color),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            FrameLayout vdp_img_url_rip = (FrameLayout) view.findViewById(R.id.vdp_img_url_rip);
            loader.DisplayImagePager(getActivity(),
                    imageId[position], imgurl,
                    R.drawable.no_image_gallery, Priority.LOW, pbr);
            ((ViewPager) collection).addView(view, 0);

            vdp_img_url_rip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*if (isNotNull(car.getAuction_images())) {
                        Intent i = new Intent(MainActivity.this, Url_Activity.class);
                        i.putExtra("data", car);
                        i.putExtra("server_time", "" + server_time);
                        startActivity(i);
                       // overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);

                    }*/


                }

            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    /**
     * RecyclerView adapter class to render items
     * This class can go into another separate class, but for simplicity
     */
    class MainCatAdapter extends RecyclerView.Adapter<MainCatAdapter.MyViewHolder> {
        private Context context;
        private List<MyAreas> myAreas;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public ImageView thumbnail;
            LinearLayout lay_categories;

            public MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.tv_category);
                lay_categories = view.findViewById(R.id.lay_categories);

                thumbnail = view.findViewById(R.id.img_category);
            }
        }


        public MainCatAdapter(Context context, List<MyAreas> myAreas) {
            this.context = context;
            this.myAreas = myAreas;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_products, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final MyAreas areas = myAreas.get(position);
            holder.name.setText(areas.getName());

            holder.lay_categories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), DetailsPage.class);
                    startActivity(i);
                    Toast.makeText(context, "clicked on item" + areas.getName(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return myAreas.size();
        }
    }

    class NewArraivalsAdapter extends RecyclerView.Adapter<NewArraivalsAdapter.ViewHolder> {

        ArrayList<Products> newArrivals;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name, price_orginal, price_offer;
            public CardView cardView;
            AppCompatImageView thumbnail;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.title);
                price_orginal = itemView.findViewById(R.id.orinal_price);
                price_offer = itemView.findViewById(R.id.offer_price);
                cardView = itemView.findViewById(R.id.myCard);
                thumbnail = itemView.findViewById(R.id.thumbnail);

            }
        }

        NewArraivalsAdapter(ArrayList<Products> newArrivals) {
            this.newArrivals = newArrivals;
        }

        @NonNull
        @Override
        public NewArraivalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_products, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull NewArraivalsAdapter.ViewHolder holder, int position) {
            Products products = newArrivals.get(position);
            SetData(holder, products);

        }

        @Override
        public int getItemCount() {
            return newArrivals.size();
        }


    }


    class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.ViewHolder> {

        ArrayList<Products> trendings;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name, price_orginal, price_offer;
            public CardView cardView;
            AppCompatImageView thumbnail;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.title);
                price_orginal = itemView.findViewById(R.id.orinal_price);
                price_offer = itemView.findViewById(R.id.offer_price);
                cardView = itemView.findViewById(R.id.myCard);
                thumbnail = itemView.findViewById(R.id.thumbnail);

            }
        }

        TrendingAdapter(ArrayList<Products> trendings) {
            this.trendings = trendings;
        }

        @NonNull
        @Override
        public TrendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_products, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TrendingAdapter.ViewHolder holder, int position) {
            Products products = trendings.get(position);
            SetData2(holder, products);

        }

        @Override
        public int getItemCount() {
            return trendings.size();
        }


    }

    public void SetData(NewArraivalsAdapter.ViewHolder holder, Products products) {
        holder.name.setText(products.getProduct_name());
        holder.price_offer.setText(rupeeFormat(products.getProduct_price()));
        try {
            Glide.with(getActivity())
                    .load(products.getProduct_image())
                    .placeholder(R.drawable.no_image_gallery)
                    .fitCenter()
                    .into(holder.thumbnail);
        } catch (Exception e) {
            e.printStackTrace();
        }
       // holder.price_orginal.setPaintFlags(holder.price_orginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DetailsPage.class);
                startActivity(i);
            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DetailsPage.class);
                startActivity(i);
            }
        });
    }

    public void SetData2(TrendingAdapter.ViewHolder holder, Products products) {
        holder.name.setText(products.getProduct_name());
        holder.price_orginal.setPaintFlags(holder.price_orginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DetailsPage.class);
                startActivity(i);
            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DetailsPage.class);
                startActivity(i);
            }
        });
    }


    public static String rupeeFormat(String value) {
        value = value.replace(",", "").replace("₹ ", "").replace("₹", "");
        char lastDigit = value.charAt(value.length() - 1);
        String result = "";
        int len = value.length() - 1;
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--) {
            result = value.charAt(i) + result;
            nDigits++;
            if (((nDigits % 2) == 0) && (i > 0)) {
                result = "," + result;
            }
        }
        return ("₹ " + result + lastDigit);
    }
}
