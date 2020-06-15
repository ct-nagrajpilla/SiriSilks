package com.aniapps.siri;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.aniapps.adapters.SliderImageAdapter;
import com.aniapps.callbackclient.APIResponse;
import com.aniapps.callbackclient.RetrofitClient;
import com.aniapps.models.Product;
import com.aniapps.utils.ProgressDialog;
import com.aniapps.utils.ScrollingPagerIndicator;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailsPage extends AppCompatActivity {
    ViewPager viewPager;
    AppCompatButton btn_bidnow;
    ScrollingPagerIndicator pagerIndicator;
    AppCompatImageView ic_close;
    WebView myWebView;
    String auction_id = "", auction_event_id = "";
    ArrayList<String> arlImageList = new ArrayList<>();
    String from_screen;
    LinearLayoutManager HorizontalLayout;
    RecyclerView recycler_horizontal;
    HorizontalAdapter horizontalAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_page);
        from_screen = getIntent().getStringExtra("from_screen");
        auction_id = getIntent().getStringExtra("product_id");
        initViews();

    }

    public void initViews() {
        ic_close = findViewById(R.id.close_back);
        myWebView = findViewById(R.id.web_details);
        btn_bidnow = findViewById(R.id.btn_details_checkout);
        viewPager = (ViewPager) findViewById(R.id.viewpagers);
        recycler_horizontal = (RecyclerView) findViewById(R.id.recycler_horizontal);
        // Set Horizontal Layout Manager
        // for Recycler view
        HorizontalLayout = new LinearLayoutManager(DetailsPage.this, LinearLayoutManager.HORIZONTAL, false);
        recycler_horizontal.setLayoutManager(HorizontalLayout);

        pagerIndicator = (ScrollingPagerIndicator) findViewById(R.id.pagerIndicator);

        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //  GetLCAuctionsData();

        myProduct();

    }


    private void setLCImages(final Product object) {
        SliderImageAdapter adapter = new SliderImageAdapter(DetailsPage.this, object.getProduct_all_images(), new OnPagerClick() {
            @Override
            public void onClick(int position, View view) {
                Bundle bundleObject = new Bundle();
                Intent intent = new Intent(DetailsPage.this, ZoomImageAct.class);
                bundleObject.putSerializable("images", arlImageList);
                bundleObject.putString("title", "" + object.getProduct_name());
                bundleObject.putString("auction_id", "" + auction_id);
                bundleObject.putString("auction_event_id", "" + auction_event_id);
                bundleObject.putString("from_screen", from_screen);
                bundleObject.putInt("currentSelectedCar", viewPager.getCurrentItem());
                intent.putExtras(bundleObject);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        viewPager.setAdapter(adapter);
        pagerIndicator.attachToPager(viewPager);
        webPage(object.getProduct_details());
    }

    public void webPage(String url) {
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setDisplayZoomControls(false);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setSaveFormData(false);
        myWebView.clearCache(true);

        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                if (!isFinishing()) {
                    ProgressDialog.getInstance().show(DetailsPage.this);

                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                ProgressDialog.getInstance().dismiss(DetailsPage.this);

                super.onPageFinished(view, url);
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
                } else if (url.startsWith("http:") || url.startsWith("https:")) {
                    myWebView.loadUrl(url);
                }

                return true;
            }


            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                view.loadUrl("");
                if (!isFinishing()) {
                    alertMessage(DetailsPage.this, "Message..!", description);
                }
            }

        });

        try {
            if (!isConnectingToInternet()) {
                if (!isFinishing()) {
                    alertMessage(DetailsPage.this,
                            "No Internet Connection",
                            "Please check your internet connectivity and try again!"
                    );
                }
            } else {

                myWebView.loadUrl(url);

            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    public void myProduct() {
        Product product = new Product();
        ArrayList<String> myImages = new ArrayList<>();
        myImages.add("https://5.imimg.com/data5/MD/EV/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        myImages.add("https://5.imimg.com/data5/OE/PC/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        myImages.add("https://5.imimg.com/data5/QJ/GV/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        myImages.add("https://5.imimg.com/data5/GF/JS/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        myImages.add("https://5.imimg.com/data5/YH/YI/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        myImages.add("https://5.imimg.com/data5/DW/GJ/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        myImages.add("https://5.imimg.com/data5/BK/GE/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        myImages.add("https://5.imimg.com/data5/KY/AP/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
        product.setProduct_all_images(myImages);
        product.setProduct_id("123");
        product.setProduct_name("Cotton Silk Traditional Saree");
        product.setProduct_details("https://testlb.cartradeexchange.com//mob//auctionlivesingle//oKqYwR1HP7Ce8D5jfgY6lkD19biW1HjIJLjE4OlFvdk//cteappv");


        setLCImages(product);



        horizontalAdapter = new HorizontalAdapter(product.getProduct_all_images());
        recycler_horizontal.setAdapter(horizontalAdapter);

    }


    public void GetLCAuctionsData() {
        final HashMap<String, String> params = new HashMap<>();
        params.put("action", getResources().getString(R.string.loadproduct));
        params.put("product_id", "" + auction_id);
        RetrofitClient.getInstance().doBackProcess(DetailsPage.this, params, "", new APIResponse() {
            @Override
            public void onSuccess(String res) {
                try {
                    JSONObject jsonobject = new JSONObject(res);
                    int status = jsonobject.getInt("status");
                    if (status == 1) {
                        JSONArray auction_list = jsonobject.getJSONArray("auction_list");
                        Product myObject = new Gson().fromJson(auction_list
                                .getJSONObject(0).toString(), Product.class);
                        setLCImages(myObject);
                    } else {
                        apiStatusRes(DetailsPage.this, status, jsonobject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String res) {
                apiFailure(DetailsPage.this, res);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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
    private class HorizontalAdapter extends RecyclerView.Adapter<CasesHolder> {
        ArrayList<String> imagesList;

        private HorizontalAdapter(ArrayList<String> data) {
            super();
            this.imagesList = data;
        }

        @Override
        public CasesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CasesHolder(getLayoutInflater().inflate(R.layout.layout_horizontal, parent, false));
        }

        @Override
        public void onBindViewHolder(final CasesHolder holder, int position) {

            try {
                Glide.with(DetailsPage.this).load(imagesList.get(position)).placeholder(R.mipmap.fav_none2).centerCrop().into(holder.imgDisplay);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return imagesList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class CasesHolder extends RecyclerView.ViewHolder {
        ImageView imgDisplay;

        public CasesHolder(View convertView) {
            super(convertView);
            imgDisplay = (ImageView) convertView
                    .findViewById(R.id.imgDisplay);

        }
    }

}
