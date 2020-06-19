package com.aniapps.callbackclient;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.aniapps.siri.BuildConfig;
import com.aniapps.siri.MainActivity;
import com.aniapps.siri.R;
import com.aniapps.utils.Pref;
import com.aniapps.utils.ProgressDialog;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/*
 * Created by NagRaj_Pilla on 4/12/2017.
 * REST CTE Service
 * https://stackoverflow.com/questions/41805341/retrofit-dynamic-base-url-change-using-singleton-class
 * https://inthecheesefactory.com/blog/retrofit-2.0/en
 * https://www.reddit.com/r/androiddev/comments/6r8l7m/android_dagger_211_and_retrofit_dynamic_urls/
 */

public class RetrofitClient extends MainActivity {
    private static Retrofit retrofit = null;
    private static RetrofitClient uniqInstance;
    private Context context;
    private Map<String, String> params;
    private APIService apiService;
    private String TAG = "##Retrofit##", from = "";

    public static RetrofitClient getInstance() {
        if (uniqInstance == null) {
            uniqInstance = new RetrofitClient();
        }
        return uniqInstance;
    }

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = null;

            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(90, TimeUnit.SECONDS)
                    .connectTimeout(90, TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(context.getResources().getString(R.string.base))
                    .addConverterFactory(new RetrofitConverter())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    //RetrofitCallBack
    public void doBackProcess(final Context context, Map<String, String> postParams,
                              String from, APIResponse api_res) {
        this.context = context;
        this.params = postParams;
        this.from = from;
        if (from.length() == 0 && null != context) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDialog.getInstance().show(context);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        getNoCryptCoreRes(context, from, postParams, api_res);


    }


    //NoCryptCore
    public void getNoCryptCoreRes(final Context context, final String from,
                                  final Map<String, String> postParams,
                                  final APIResponse api_res) {
        this.context = context;
        apiService = RetrofitClient.getClient(context).create(APIService.class);
        postParams.put("api_id", "checklist2019v1.0");
        postParams.put("version_code", "" + BuildConfig.VERSION_CODE);
        postParams.put("device_id", Pref.getIn().getDeviceId());
        postParams.put("app_code", Pref.getIn().getApp_code());
        params.put("mobile", Pref.getIn().getMobile_number());
        params.put("user_id",Pref.getIn().getUser_id());
        Log.e("####", "POST" + postParams);
        apiService.getApiResult(context.getResources().getString(R.string.core_live), postParams).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, final Response<String> res) {
                if (res.isSuccessful()) {
                    if (from.length() == 0) {
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ProgressDialog.getInstance().dismiss((Activity) context);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        if (null != res.body() && !res.body().equals("")) {
                            Log.e("RES", "RES" + res.body());
                            api_res.onSuccess(res.body().trim());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } else {
                    api_res.onFailure(context.getResources().getString(
                            R.string.socketErrorMessage));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (from.length() == 0) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ProgressDialog.getInstance().dismiss((Activity) context);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                retrofit = null;

            }
        });
    }
}
