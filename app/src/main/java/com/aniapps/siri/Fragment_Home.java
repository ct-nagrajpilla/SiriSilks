package com.aniapps.siri;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.aniapps.models.Movie;
import com.aniapps.utils.Image_Fetch;
import com.bumptech.glide.Priority;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Fragment_Home extends Fragment {

    private static final String TAG = Fragment_Home.class.getSimpleName();
    int page = 0;

    // url to fetch shopping items
    private static final String URL = "https://api.androidhive.info/json/movies_2017.json";
    Integer[] imageId = {R.drawable.aaa, R.drawable.bbb, R.drawable.ccc};
    private RecyclerView recyclerView, recyclerView1;
    private ViewPager imgViewPager;
    private List<Movie> itemsList;
    private MainCatAdapter mAdapter;
    private ProdcutsAdapter pAdapter;
    Context context;
    LinearLayout dot_layout;

    public Fragment_Home(Context context) {
        this.context = context;
        // Required empty public constructor
    }

   /* public static Fragment_Home newInstance(String param1, String param2) {
        Fragment_Home fragment = new Fragment_Home(context);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView1 = view.findViewById(R.id.recycler_view1);
        itemsList = new ArrayList<>();
        mAdapter = new MainCatAdapter(getActivity(), itemsList);
        pAdapter = new ProdcutsAdapter();
        imgViewPager = (ViewPager) view.findViewById(R.id.img_viewpager);
        dot_layout = (LinearLayout) view.findViewById(R.id.dot_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        //recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView1.setAdapter(pAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView1.setNestedScrollingEnabled(false);

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
        fetchStoreItems();

        return view;
    }

    /**
     * fetching shopping item by making http call
     */
    private void fetchStoreItems() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getActivity(), "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Movie> items = new Gson().fromJson(response.toString(), new TypeToken<List<Movie>>() {
                        }.getType());

                        itemsList.clear();
                        itemsList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        SiriApp.getInstance().addToRequestQueue(request);
    }


    /*  final Runnable mUpdateResults = new Runnable() {
          public void run() {
              int numPages = imgViewPager.getAdapter().getCount();
              page = (page + 1) % numPages;
              imgViewPager.setCurrentItem(page);

          }
      };
      */
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
        private List<Movie> movieList;

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


        public MainCatAdapter(Context context, List<Movie> movieList) {
            this.context = context;
            this.movieList = movieList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_products, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Movie movie = movieList.get(position);
            if (position == 0) {
                holder.name.setText("New Arrivals");
                holder.name.setTextColor(Color.parseColor("#FFFFFF"));
                holder.lay_categories.setBackgroundResource(R.drawable.border_view_red);
            } else {
                holder.lay_categories.setBackgroundResource(R.drawable.border_view);

            }
            //  holder.name.setText(movie.getTitle());


          /*  Glide.with(context)
                    .load(movie.getImage())
                    .into(holder.thumbnail);*/
        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }
    }

    class ProdcutsAdapter extends RecyclerView.Adapter<ProdcutsAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name, price_orginal, price_offer;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.title);
                price_orginal = itemView.findViewById(R.id.orinal_price);
                price_offer = itemView.findViewById(R.id.offer_price);

            }
        }

        @NonNull
        @Override
        public ProdcutsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.products, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ProdcutsAdapter.ViewHolder holder, int position) {
            holder.price_orginal.setPaintFlags(holder.price_orginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        @Override
        public int getItemCount() {
            return 5;
        }


    }

    class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
        private Context context;
        private List<Movie> movieList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, price_orginal, price_offer;

            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.title);
                //price_orginal = view.findViewById(R.id.orinal_price);
                //price_offer = view.findViewById(R.id.offer_price);


                thumbnail = view.findViewById(R.id.thumbnail);
            }
        }


        public ProductAdapter(Context context, List<Movie> movieList) {
            this.context = context;
            this.movieList = movieList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.store_item_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Movie movie = movieList.get(position);
            holder.name.setText(movie.getTitle());
            // holder.price_orginal.setPaintFlags(holder.price_orginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


          /*  Glide.with(context)
                    .load(movie.getImage())
                    .into(holder.thumbnail);*/
        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }
    }


   /* EditText searchEditText;
    FrameLayout menuView;
    private Menu menu;
    AppCompatImageView clear;
    MenuItem menuItem;

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        // MenuInflater menuInflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        menuItem = menu.findItem(R.id.action_search);
        menuItem.setVisible(true);
        menuItem.setEnabled(true);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                *//*Intent in=new Intent(MainActivity.this,SearchList.class);
                startActivity(in);*//*
                return false;
            }
        });
        searchEditText = (EditText) MenuItemCompat.getActionView(menuItem).findViewById(R.id.edittext);
        menuView = (FrameLayout) MenuItemCompat.getActionView(menuItem).findViewById(R.id.menu_view);
        clear = (AppCompatImageView) MenuItemCompat.getActionView(menuItem).findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* if (null != mHighlightArrayAdapter)
                    mHighlightArrayAdapter.getFilter().filter("");
                searchEditText.setText("");
                search_list.setVisibility(View.GONE);*//*
                //hideKeyboard();
                MenuItemCompat.collapseActionView(menuItem);

            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    searchEditText.setCompoundDrawables(null, null, null, null);
                    *//*if (null != mHighlightArrayAdapter)
                        mHighlightArrayAdapter.getFilter().filter(s);*//*
                    clear.setVisibility(View.VISIBLE);
                } else {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_search_edit, 0, 0, 0);
                    *//*if (null != mHighlightArrayAdapter)
                        mHighlightArrayAdapter.getFilter().filter("");*//*
                    clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                searchEditText.requestFocus();
                if (searchEditText.requestFocus()) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (null != imm)
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    *//*if (null != search_list) {
                        search_list.setVisibility(View.VISIBLE);
                    }*//*
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                try {
                    if (searchEditText.hasFocus()) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                    }
                    *//*if (null != mHighlightArrayAdapter)
                        mHighlightArrayAdapter.getFilter().filter("");*//*
                    searchEditText.setText("");

                   *//* if (serach_flag) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                spage = 1;
                                GetAuctionsData("", "", "Menu");

                            }
                        }, 300);
                    }
                    search_list.setVisibility(View.GONE);
*//*
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }

        });


    }
*/

   /* @Override
    public void onPrepareOptionsMenu(Menu menu,MenuInflater menuInflater) {
    *//*    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menu.getItem(0).setEnabled(false);
        } else {
            menu.getItem(0).setEnabled(true);
        }*//*
        return super.onPrepareOptionsMenu(menu,menuInflater);

    }
*/

  /*  @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }*/

}
