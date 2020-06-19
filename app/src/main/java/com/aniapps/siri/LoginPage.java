package com.aniapps.siri;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.aniapps.callbackclient.APIResponse;
import com.aniapps.callbackclient.RetrofitClient;
import com.aniapps.utils.Pref;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class LoginPage extends AppConstants {
    public static boolean dialog_falg;
    Dialog otp_dialog;
    String otp_val = "";
    AppCompatTextView txt_resend_verification_code;
    AppCompatEditText et_mobile, edttxt_1, edttxt_2, edttxt_3, edttxt_4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.transparent_layout);
        alertOtp();
        dialog_falg = otp_dialog.isShowing();
    }

    @Override
    protected void onDestroy() {
        dialog_falg = false;
        otp_dialog.dismiss();
        LoginPage.this.finish();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        dialog_falg = false;
        otp_dialog.dismiss();
        LoginPage.this.finish();
        super.onBackPressed();
    }

    LinearLayout linear_otp;
    AppCompatTextView btn_submit;

    public void alertOtp() {
        otp_val = "";
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
                Toast.makeText(LoginPage.this, "Verification code sent to your mobile " + et_mobile.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        listen();
        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!mobileNumberValidation(et_mobile.getText().toString().trim())) {
                    LoginApi(et_mobile,linear_otp,btn_submit);
                }else{
                   Toast.makeText(LoginPage.this,"Enter valid mobile number",Toast.LENGTH_SHORT).show();
                }

            }
        });
        image_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog_falg = false;
                otp_dialog.dismiss();
                LoginPage.this.finish();
            }
        });

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        otp_dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);

        otp_dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog_falg = false;
                    otp_dialog.dismiss();
                    LoginPage.this.finish();
                    return true;

                }
                return false;
            }
        });
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
                                otpCheck(otp_val);
                            } else {
                                Toast.makeText(LoginPage.this, "Please fill valid otp", Toast.LENGTH_SHORT).show();
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


    public void LoginApi(final AppCompatEditText mobile_number, final LinearLayout linear_otp, final AppCompatTextView btn_submit) {
        Map<String, String> params = new HashMap<>();
        params.put("action", getResources().getString(R.string.authenticate));
        params.put("mobile", mobile_number.getText().toString());
        RetrofitClient.getInstance().doBackProcess(LoginPage.this, params, "", new APIResponse() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSuccess(String Response) {
                try {
                    int status;
                    JSONObject object = new JSONObject(Response);
                    status = object.getInt("status");
                    if (status == 1) {
                        mobile_number.setVisibility(GONE);
                        btn_submit.setVisibility(GONE);
                        linear_otp.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginPage.this, "OTP Sent to "+mobile_number.getText().toString(), Toast.LENGTH_SHORT).show();
                        Pref.getIn().setMobile_number(mobile_number.getText().toString());
                        Pref.getIn().setUser_id(object.getString("user_id"));

                    } else {
                        apiStatusRes(LoginPage.this, status, object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String res) {
                apiFailure(LoginPage.this, res);
            }
        });
    }


    public void otpCheck(String otp_val) {
        Map<String, String> params = new HashMap<>();
        params.put("action", getResources().getString(R.string.authenticate));
        params.put("mobile", Pref.getIn().getMobile_number());
        params.put("otp", otp_val);
        params.put("user_id",Pref.getIn().getUser_id());
        RetrofitClient.getInstance().doBackProcess(LoginPage.this, params, "online", new APIResponse() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSuccess(String Response) {
                try {
                    int status;
                    JSONObject object = new JSONObject(Response);
                    status = object.getInt("status");
                    if (status == 1) {
                        Toast.makeText(LoginPage.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                        dialog_falg = false;
                        otp_dialog.dismiss();
                        LoginPage.this.finish();
                    } else {
                        apiStatusRes(LoginPage.this, status, object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String res) {
                apiFailure(LoginPage.this, res);
            }
        });
    }

    public boolean mobileNumberValidation(String mobileno) {
        return mobileno.length() < 10;
    }

}
