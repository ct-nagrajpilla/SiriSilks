package com.aniapps.siri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.aniapps.adapters.MenuAdapter;
import com.aniapps.callbackclient.APIResponse;
import com.aniapps.callbackclient.RetrofitClient;
import com.aniapps.models.MyAreas;
import com.aniapps.models.MyCategories;
import com.aniapps.models.MyProduct;
import com.aniapps.models.NewArrivals;
import com.aniapps.models.OtherMenu;
import com.aniapps.models.SubCategory;
import com.aniapps.models.Trending;
import com.aniapps.utils.Pref;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppConstants implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    ExpandableListView expandableListView;
    NavigationView navigationView;
    public static DrawerLayout drawer;
    public static BottomNavigationView bottomNavigationView;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Siri Silks");
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view2);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        expandableListView = findViewById(R.id.expandable_menu);
        apiCall();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if (Pref.getIn().getCart_count() > 0) {
            ((AppCompatTextView) menu_cart.findViewById(R.id.filter_count)).setVisibility(View.VISIBLE);
            ((AppCompatTextView) menu_cart.findViewById(R.id.filter_count)).setText("" + Pref.getIn().getCart_count());
        } else {
            ((AppCompatTextView) menu_cart.findViewById(R.id.filter_count)).setVisibility(View.GONE);
        }*/
    }

    //https://stackoverflow.com/questions/42682855/display-badge-on-top-of-bottom-navigation-bars-icon
    @SuppressLint("SetTextI18n")
    public static void showBadge(Context context, BottomNavigationView
            bottomNavigationView, @IdRes int itemId, int value) {
        Log.e("####", "Value" + value);
        removeBadge(bottomNavigationView, itemId,value);
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.badge, bottomNavigationView, false);
        TextView text = badge.findViewById(R.id.badge_text_view);
        text.setText("" + value);
        /* if (value != 0) {*/
        itemView.addView(badge);
       /* } else {
            badge.setVisibility(View.GONE);
            //      bottomNavigationView.removeView(bottomNavigationView.getChildAt(2));

            //  itemView.removeView(badge);
        }
*/
    }


    /*public static void removeBadge(BottomNavigationView navigationView, int index) {
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(index);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        itemView.removeViewAt(itemView.getChildCount() - 1);
    }*/
    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId, int count) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 4) {
            itemView.removeViewAt(2);
            if (count == 0) {
                itemView.setVisibility(View.INVISIBLE);
            }
        }
    }


    AppCompatEditText searchEditText;
    FrameLayout menuView;
    private Menu menu;
    AppCompatImageView clear;
    MenuItem menuItem;
    View menu_cart;
    public static AppCompatTextView filter_count;
    public static int mNotifCount = 0;

    public boolean onCreateOptionsMenu(Menu menuu) {
        this.menu = menuu;
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_auction_search, menu);
        menuItem = menu.findItem(
                R.id.action_search);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        searchEditText = (AppCompatEditText) MenuItemCompat.getActionView(menuItem).findViewById(R.id.edittext);
        menuView = (FrameLayout) MenuItemCompat.getActionView(menuItem).findViewById(R.id.menu_view);
        clear = (AppCompatImageView) MenuItemCompat.getActionView(menuItem).findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (null != mHighlightArrayAdapter)
                    mHighlightArrayAdapter.getFilter().filter("");*/
                searchEditText.setText("");
                //    search_list.setVisibility(View.GONE);
                hideKeyboard();
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
                    //searchEditText.setCompoundDrawables(null, null, null, null);
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_search_edit, 0, 0, 0);
                 /*   if (null != mHighlightArrayAdapter)
                        mHighlightArrayAdapter.getFilter().filter(s);*/
                    clear.setVisibility(View.VISIBLE);
                } else {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_search_edit, 0, 0, 0);
                  /*  if (null != mHighlightArrayAdapter)
                        mHighlightArrayAdapter.getFilter().filter("");*/
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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (null != imm)
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                   /* if (null != search_list) {
                        search_list.setVisibility(View.VISIBLE);
                    }*/
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                try {
                    if (searchEditText.hasFocus()) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                    }
                  /*  if (null != mHighlightArrayAdapter)
                        mHighlightArrayAdapter.getFilter().filter("");*/
                    searchEditText.setText("");
                    //  eventTrack(Bluejack_Auctions.this, "Auctions", "Auctions|Search|Close");

                    /*if (serach_flag) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                spage = 1;
                                GetAuctionsData("", "", "Menu");

                            }
                        }, 300);
                    }
                    search_list.setVisibility(View.GONE);*/

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }

        });

        MenuItem item_done = menu.findItem(R.id.action_cart);
        item_done.setVisible(true);
        menu_cart = item_done.getActionView();
        filter_count = menu_cart.findViewById(R.id.filter_count);
        if (Pref.getIn().getCart_count() > 0) {
            filter_count.setVisibility(View.VISIBLE);
            filter_count.setText("" + Pref.getIn().getCart_count());
        }
        menu_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginPage.
                        class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void setNotifCount(int count) {
        mNotifCount = count;
        invalidateOptionsMenu();
    }

    public static void refreshMenu(int count) {
        filter_count.setVisibility(View.VISIBLE);
        filter_count.setText("" + count);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(true);
        /*if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menu.getItem(0).setEnabled(false);
        } else {
            menu.getItem(0).setEnabled(true);
        }*/
        return super.onPrepareOptionsMenu(menu);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Home");
                    fragment = new Fragment_Home(MainActivity.this, areas, parr_newarrivals, parr_trendings);
                    break;
                case R.id.navigation_wishlist:
                    toolbar.setTitle("Wish List");
                    fragment = new Fragment_WishList(menu_cart);
                    break;
                case R.id.navigation_orders:
                    toolbar.setTitle("My Oders");
                    fragment = new Fragment_Orders();
                    break;
                case R.id.navigation_account:
                    toolbar.setTitle("My Profile");
                    fragment = new Fragment_Profile();
                    break;
            }


            return loadFragment(fragment);
        }
    };


    //https://github.com/aurelhubert/ahbottomnavigation
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    Gson gson = new Gson();
    MyCategories mainCategories;
    NewArrivals homeArray;
    ArrayList<NewArrivals> arr_newarrivals = new ArrayList<>();
    ArrayList<MyProduct> parr_newarrivals = new ArrayList<>();
    ArrayList<Trending> arr_trendings = new ArrayList<>();
    ArrayList<MyProduct> parr_trendings = new ArrayList<>();
    ArrayList<MyAreas> areas = new ArrayList<>();

    public void apiCall() {
        Map<String, String> params = new HashMap<>();
        params.put("action", getResources().getString(R.string.homeapi));
        params.put("mobile", "9441349123");
        Log.e("####", "JSON OBJ" + params);
        RetrofitClient.getInstance().doBackProcess(MainActivity.this, params, "", new APIResponse() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSuccess(String Response) {
                try {
                    int status;
                    JSONObject object = new JSONObject(Response);
                    status = object.getInt("status");
                    if (status == 1) {
                        expandableListView.setAdapter((BaseExpandableListAdapter) null);
                        JSONArray resultArray = object.getJSONArray("areas");
                        JSONArray otherArray = object.getJSONArray("quicklinks");
                        JSONArray new_arrival = object.getJSONArray("new_arrival");
                        JSONArray trend_arrival = object.getJSONArray("trend_arrival");

                        ArrayList<OtherMenu> otherMenus = new ArrayList<>();
                        for (int k = 0; k < otherArray.length(); k++) {
                            otherMenus.add(new OtherMenu(otherArray.get(k).toString()));
                        }
                        ArrayList<MyCategories> mainCategory_list = new ArrayList<>();
                        areas = new ArrayList<>();
                        for (int i = 0; i < resultArray.length(); i++) {
                            MyCategories categories = gson.fromJson(resultArray.getJSONObject(i).toString(), MyCategories.class);
                            MyAreas areas1 = new MyAreas();
                            areas1.setName(categories.getName());
                            areas1.setId("" + categories.getId());
                            areas.add(areas1);
                            JSONArray resultArray2 = resultArray.getJSONObject(i).getJSONArray("categories");
                            ArrayList<SubCategory> subCategory_list = new ArrayList<>();
                            if (resultArray2.length() > 1)
                                for (int j = 0; j < resultArray2.length(); j++) {
                                    SubCategory subCategory = gson.fromJson(resultArray2.getJSONObject(j).toString(), SubCategory.class);
                                    subCategory_list.add(subCategory);
                                }
                            mainCategories = new MyCategories(categories.getName(), categories.getId(), subCategory_list);
                            mainCategory_list.add(mainCategories);
                        }
                        for (int k = 0; k < otherArray.length(); k++) {
                            mainCategory_list.add(new MyCategories(otherArray.get(k).toString()));
                        }
                        MenuAdapter mainAdapter = new MenuAdapter(MainActivity.this, mainCategory_list, expandableListView);
                        expandableListView.setAdapter(mainAdapter);

                        for (int i = 0; i < new_arrival.length(); i++) {
                            NewArrivals newArrivals = gson.fromJson(new_arrival.getJSONObject(i).toString(), NewArrivals.class);
                            arr_newarrivals.add(newArrivals);
                            ArrayList<MyProduct> productsArrayList = new ArrayList<>();
                            JSONArray resultArray2 = new_arrival.getJSONObject(i).getJSONArray("products");
                            for (int j = 0; j < resultArray2.length(); j++) {
                                MyProduct subCategory = gson.fromJson(resultArray2.getJSONObject(j).toString(), MyProduct.class);
                                parr_newarrivals.add(subCategory);
                            }
                        }
                        for (int i = 0; i < trend_arrival.length(); i++) {
                            Trending newArrivals = gson.fromJson(trend_arrival.getJSONObject(i).toString(), Trending.class);
                            arr_trendings.add(newArrivals);
                            JSONArray resultArray2 = trend_arrival.getJSONObject(i).getJSONArray("products");
                            for (int j = 0; j < resultArray2.length(); j++) {
                                MyProduct subCategory = gson.fromJson(resultArray2.getJSONObject(j).toString(), MyProduct.class);
                                parr_trendings.add(subCategory);
                            }
                        }

                        Pref.getIn().setCart_count(object.getInt("cart_count"));
                        showBadge(MainActivity.this, bottomNavigationView, R.id.navigation_wishlist, object.getInt("wishlist_count"));
                        loadFragment(new Fragment_Home(MainActivity.this, areas, parr_newarrivals, parr_trendings));
                    } else {
                        apiStatusRes(MainActivity.this, status, object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String res) {
                apiFailure(MainActivity.this, res);
            }
        });
    }
}
