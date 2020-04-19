package com.aniapps.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ProgressBar;

import com.aniapps.siri.MainActivity;
import com.aniapps.siri.R;

/**
 * Created by NagRaj_Pilla on 4/19/2017.
 * new CTE Progress Dialog
 */

public class ProgressDialog extends MainActivity {
    public static Dialog dialog;
    private static ProgressDialog mInstance;
    android.widget.ProgressBar cpb;

    public static synchronized ProgressDialog getInstance() {
        if (mInstance == null) {
            mInstance = new ProgressDialog();
        }
        return mInstance;
    }

    public void show(Context context) {
        if (dialog != null && dialog.isShowing()) return;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        cpb = (ProgressBar) dialog.findViewById(R.id.progress);
       // cpb.startAnimation();
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss(Activity act) {
        if (dialog != null && dialog.isShowing()) {
            if (!act.isFinishing()) {
                try {
                    dialog.dismiss();
                  //  cpb.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                dialog = null;
            }
        }
    }
}