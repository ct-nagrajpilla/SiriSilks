package com.aniapps.siri;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.aniapps.adapters.MainAdapter;
import com.aniapps.callbackclient.APIResponse;
import com.aniapps.callbackclient.RetrofitClient;
import com.aniapps.models.MyCategories;
import com.aniapps.models.OtherMenu;
import com.aniapps.models.SubCategory;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    ExpandableListView expandableListView;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Siri Silks");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view2);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showBadge(this, navigation, R.id.navigation_wishlist, "1");
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
//https://stackoverflow.com/questions/42682855/display-badge-on-top-of-bottom-navigation-bars-icon
    public void showBadge(Context context, BottomNavigationView
            bottomNavigationView, @IdRes int itemId, String value) {
        removeBadge(bottomNavigationView, itemId);
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.badge, bottomNavigationView, false);

        TextView text = badge.findViewById(R.id.badge_text_view);
        text.setText(value);
        itemView.addView(badge);
    }

    public void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 4) {
            itemView.removeViewAt(2);
        }
    }




    AppCompatEditText searchEditText;
    FrameLayout menuView;
    private Menu menu;
    AppCompatImageView clear;
    MenuItem menuItem;

    public boolean onCreateOptionsMenu(Menu menuu) {
        this.menu = menuu;

        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_auction_search, menu);
        menuItem = menu.findItem(
                R.id.action_search);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
               // eventTrack(Bluejack_Auctions.this, "Auctions", "Auctions|Search");
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

        return super.onCreateOptionsMenu(menu);
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
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
                    fragment = new Fragment_Home(MainActivity.this);
                    break;
                case R.id.navigation_wishlist:
                    toolbar.setTitle("Wish List");
                    fragment = new Fragment_WishList();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_cart:
                // do stuff, like showing settings fragment
                return true;

            case R.id.action_search:
                // do stuff, like showing settings fragment
                return true;
        }

        return super.onOptionsItemSelected(item); // important line
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();

        // apiCall();

    }

    Gson gson = new Gson();
    MyCategories mainCategories;

    public void apiCall() {
        Map<String, String> params = new HashMap<>();
        params.put("action", getResources().getString(R.string.loadallcategories));
        params.put("device_id", "8fa2d80f9b99dc24");
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
                       /* Pref.getIn().setMainCount(object.getInt("livecnt"));
                        Pref.getIn().setShort_link(object.getString("short_link"));*/
                        expandableListView.setAdapter((BaseExpandableListAdapter) null);
                        JSONObject mylist = object.getJSONObject("list");
                        JSONArray resultArray = mylist.getJSONArray("categories");
                        JSONArray otherArray = mylist.getJSONArray("quicklinks");
                        Log.e("####", "JSON OBJ" + resultArray);
                        ArrayList<OtherMenu> otherMenus = new ArrayList<>();
                        for (int k = 0; k < otherArray.length(); k++) {
                            otherMenus.add(new OtherMenu(otherArray.get(k).toString()));
                        }


                        ArrayList<MyCategories> mainCategory_list = new ArrayList<>();
                        for (int i = 0; i < resultArray.length(); i++) {
                            MyCategories categories = gson.fromJson(resultArray.getJSONObject(i).toString(), MyCategories.class);
                            JSONArray resultArray2 = resultArray.getJSONObject(i).getJSONArray("subcategories");
                            ArrayList<SubCategory> subCategory_list = new ArrayList<>();
                            for (int j = 0; j < resultArray2.length(); j++) {
                                SubCategory subCategory = gson.fromJson(resultArray2.getJSONObject(j).toString(), SubCategory.class);
                                subCategory_list.add(subCategory);
                            }
                            mainCategories = new MyCategories(categories.getName(), categories.getId(), subCategory_list);
                            mainCategory_list.add(mainCategories);


//                            Log.e("####", "JSON OBJ" + new JSONObject((Map) mainCategory_list));
                        }

                        for (int k = 0; k < otherArray.length(); k++) {
                            mainCategory_list.add(new MyCategories(otherArray.get(k).toString()));
                        }

                        MainAdapter mainAdapter = new MainAdapter(MainActivity.this, mainCategory_list);
                        expandableListView.setAdapter(mainAdapter);

                        loadFragment(new Fragment_Home(MainActivity.this));
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


    public void apiStatusRes(Context ctx, int status, JSONObject job) {
        if (status == 3) {
            String exceptionTitle = "", exceptionMessage = "";
            try {
                exceptionTitle = job.getString("details");
                JSONObject details = job
                        .getJSONObject("validationErrors");
                JSONArray verrors = details
                        .getJSONArray("validationerror");
                for (int i = 0; i < verrors.length(); i++) {
                    JSONObject myvalue = verrors.getJSONObject(i);
                    exceptionMessage = myvalue.getString("description");
                }
                alertMessage(ctx, exceptionTitle, exceptionMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                alertMessage(ctx, "Alert..!", job.getString("details"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void apiFailure(Context ctx, String res) {
        if (res.equals("")) {
            try {
                alertMessage(ctx, "Oops..!", "Connection failed, please try again");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                alertMessage(ctx, "Oops..!", res);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void alertMessage(Context context, String title, String msg) {
        final Dialog myDialog = new Dialog(context, R.style.ThemeDialogCustom);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialog_alert);
        AppCompatTextView alert_title = myDialog.findViewById(R.id.alert_title);
        AppCompatTextView alert_message = myDialog.findViewById(R.id.alert_text);
        alert_title.setText(title);
        alert_message.setText(msg);
        myDialog.findViewById(R.id.btn_alert_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

}
