package com.aniapps.siri;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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
import com.aniapps.models.NewArrivals;
import com.aniapps.models.MyAreas;
import com.aniapps.models.MyCategories;
import com.aniapps.models.OtherMenu;
import com.aniapps.models.Products;
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
import java.util.UUID;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    ExpandableListView expandableListView;
    NavigationView navigationView;
    AppCompatImageView heart;

    @SuppressLint("HardwareIds")
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
        heart = findViewById(R.id.heart);
        Pref.getIn().saveDeviceId(Settings.Secure.getString(getApplicationContext()
                .getContentResolver(), Settings.Secure.ANDROID_ID));
        if (Pref.getIn().getApp_code().length() <= 0) {
            Pref.getIn().setApp_code(getRandomNumber());
        }
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

    public String getRandomNumber() {
        return UUID.randomUUID().toString();
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

    View menu_cart;
    AppCompatTextView filter_count;
    public static int REQUEST_CODE = 999;

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

        MenuItem item_done = menu.findItem(R.id.action_cart);
        item_done.setVisible(true);
        menu_cart = item_done.getActionView();
        filter_count = menu_cart.findViewById(R.id.filter_count);

        menu_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertOtp();
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
                    fragment = new Fragment_Home(MainActivity.this, areas,parr_newarrivals,parr_trendings);
                    break;
                case R.id.navigation_wishlist:
                    toolbar.setTitle("Wish List");
                    fragment = new Fragment_WishList(menu_cart, heart);
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


   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_cart:
                alertOtp();
                // do stuff, like showing settings fragment
                return true;

            case R.id.action_search:
                // do stuff, like showing settings fragment
                return true;
        }

        return super.onOptionsItemSelected(item); // important line
    }*/

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
    NewArrivals homeArray;
    ArrayList<NewArrivals> arr_newarrivals=new ArrayList<>();
    ArrayList<Products> parr_newarrivals=new ArrayList<>();
    ArrayList<Trending> arr_trendings=new ArrayList<>();
    ArrayList<Products> parr_trendings=new ArrayList<>();
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
                        MenuAdapter mainAdapter = new MenuAdapter(MainActivity.this, mainCategory_list);
                        expandableListView.setAdapter(mainAdapter);

                        for (int i = 0; i < new_arrival.length(); i++) {
                            NewArrivals newArrivals = gson.fromJson(new_arrival.getJSONObject(i).toString(), NewArrivals.class);
                            arr_newarrivals.add(newArrivals);
                            ArrayList<Products> productsArrayList=new ArrayList<>();
                            JSONArray resultArray2 = new_arrival.getJSONObject(i).getJSONArray("products");
                            for (int j = 0; j < resultArray2.length(); j++) {
                                Products subCategory = gson.fromJson(resultArray2.getJSONObject(j).toString(), Products.class);
                                parr_newarrivals.add(subCategory);
                            }
                        }

                       /* for (int i = 0; i < trend_arrival.length(); i++) {
                            Trending newArrivals = gson.fromJson(trend_arrival.getJSONObject(i).toString(), Trending.class);
                            arr_trendings.add(newArrivals);
                            JSONArray resultArray2 = trend_arrival.getJSONObject(i).getJSONArray("products");
                            for (int j = 0; j < resultArray2.length(); j++) {
                                Products subCategory = gson.fromJson(resultArray2.getJSONObject(j).toString(), Products.class);
                                parr_trendings.add(subCategory);
                            }
                        }*/
                        loadFragment(new Fragment_Home(MainActivity.this, areas,parr_newarrivals,parr_trendings));
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

    String otp_val = "";
    AppCompatTextView txt_resend_verification_code;
    String mobile_number = "";
    Dialog otp_dialog;
    AppCompatEditText et_mobile, edttxt_1, edttxt_2, edttxt_3, edttxt_4;

    public void alertOtp() {
        otp_val = "";
        final AppCompatTextView btn_submit;
        final LinearLayout linear_otp;
        AppCompatImageView image_close;

        otp_dialog = new Dialog(this, R.style.ThemeDialogCustom);
        otp_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otp_dialog.setContentView(R.layout.custom_alert_dialogue);
        otp_dialog.setCancelable(true);
        otp_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        btn_submit = otp_dialog.findViewById(R.id.btn_submit);
        image_close = otp_dialog.findViewById(R.id.image_close);
        txt_resend_verification_code = otp_dialog.findViewById(R.id.txt_resend_verification_code);
        linear_otp = otp_dialog.findViewById(R.id.linear_otp);
        et_mobile = otp_dialog.findViewById(R.id.et_mobile);
        edttxt_1 = otp_dialog.findViewById(R.id.edttxt_1);
        edttxt_2 = otp_dialog.findViewById(R.id.edttxt_2);
        edttxt_3 = otp_dialog.findViewById(R.id.edttxt_3);
        edttxt_4 = otp_dialog.findViewById(R.id.edttxt_4);
        txt_resend_verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Verification code sent to your mobile " + mobile_number, Toast.LENGTH_SHORT).show();
            }
        });
        listen();


        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mobile_number = et_mobile.getText().toString();
                et_mobile.setVisibility(GONE);
                linear_otp.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(GONE);
                //  myDialog.dismiss();

            }
        });
        image_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                otp_dialog.dismiss();
            }
        });

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        otp_dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
        otp_dialog.show();
    }

    public void listen() {
        edttxt_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    edttxt_1.requestFocus();

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    edttxt_2.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edttxt_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    if (edttxt_1.getText().length() == 0) {
                        edttxt_1.setText(edttxt_2.getText().toString());
                        edttxt_2.setText("");
                        edttxt_2.requestFocus();
                    } else {

                        edttxt_3.requestFocus();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    int position = edttxt_1.getText().length();
                    Editable editObj = edttxt_1.getText();
                    Selection.setSelection(editObj, position);
                    edttxt_1.requestFocus();
                }
            }
        });

        edttxt_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    if (edttxt_1.getText().length() == 0) {

                        edttxt_1.setText(edttxt_3.getText().toString());
                        edttxt_3.setText("");

                        if (edttxt_2.getText().length() == 0) {
                            edttxt_2.requestFocus();
                        } else {
                            edttxt_3.requestFocus();
                        }

                    } else if (edttxt_2.getText().length() == 0) {

                        edttxt_2.setText(edttxt_3.getText().toString());
                        edttxt_3.setText("");

                        edttxt_3.requestFocus();
                    } else {
                        edttxt_4.requestFocus();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    int position = edttxt_2.getText().length();
                    Editable editObj = edttxt_2.getText();
                    Selection.setSelection(editObj, position);
                    edttxt_2.requestFocus();
                }
            }
        });

        edttxt_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    if (edttxt_1.getText().length() == 0) {
                        edttxt_1.setText(edttxt_4.getText().toString());
                        edttxt_4.setText("");
                        edttxt_2.requestFocus();
                    } else if (edttxt_2.getText().length() == 0) {
                        edttxt_2.setText(edttxt_4.getText().toString());
                        edttxt_4.setText("");
                        edttxt_3.requestFocus();
                    } else if (edttxt_3.getText().length() == 0) {
                        edttxt_3.setText(edttxt_4.getText().toString());
                        edttxt_4.setText("");
                        edttxt_4.requestFocus();
                    } else {
                        try {
                            otp_val = edttxt_1.getText().toString() + edttxt_2.getText().toString() + edttxt_3.getText().toString() + edttxt_4.getText().toString();
                            if (otp_val.length() == 4 && otp_val.equals("1234")) {
                                /*if (other_yard.getVisibility() == View.VISIBLE) {
                                    stopService(2, spn_yard_selection.getSelectedItemPosition(), _force_stop);
                                } else {
                                    stopService(1, spn_yard_selection.getSelectedItemPosition(), _force_stop);
                                }*/
                                otp_dialog.dismiss();
                                Toast.makeText(MainActivity.this, "OTP Verified ", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(MainActivity.this, "Please fill valid otp", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    int position = edttxt_3.getText().length();
                    Editable editObj = edttxt_3.getText();
                    Selection.setSelection(editObj, position);
                    edttxt_3.requestFocus();
                }
            }
        });
        edttxt_1.requestFocus();
    }

    // check net is connecting or net
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }
}
