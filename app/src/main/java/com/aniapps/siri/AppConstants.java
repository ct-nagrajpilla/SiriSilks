package com.aniapps.siri;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.aniapps.utils.Pref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

public class AppConstants extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pref.getIn().saveDeviceId(Settings.Secure.getString(getApplicationContext()
                .getContentResolver(), Settings.Secure.ANDROID_ID));
        if (Pref.getIn().getApp_code().length() <= 0) {
            Pref.getIn().setApp_code(getRandomNumber());
        }
    }

    public String getRandomNumber() {
        return UUID.randomUUID().toString();
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
    public static void apiStatusRes(Context ctx, int status, JSONObject job) {
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

    public static void apiFailure(Context ctx, String res) {
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

    public static void alertMessage(Context context, String title, String msg) {
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
    public static int dpToPx(int dp, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
