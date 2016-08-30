package com.example.shuangxiang.qdkelenqdforphone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shuangxiang.qdkelenqdforphone.bean.LoginErrorInfo;
import com.example.shuangxiang.qdkelenqdforphone.utils.CacheUtils;
import com.example.shuangxiang.qdkelenqdforphone.utils.CustomToast;
import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shuang.xiang on 2016/8/18.
 */
public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private String LOGINURL = "http://kawakp.chinclouds.com:58010/userconsle/login";
    @BindView(R.id.editText_username)
    EditText editTextUsername;
    @BindView(R.id.editText_password)
    EditText editTextPassword;
    @BindView(R.id.button_login)
    Button buttonLogin;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginact);
        ButterKnife.bind(this);
        okHttpClient = new OkHttpClient();

    }

    @OnClick(R.id.button_login)
    public void onClick() {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.show();

        final String userName = editTextUsername.getText().toString();
        final String passWord = editTextPassword.getText().toString();

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (isNetworkConnected()) {
                    RequestBody build = new FormEncodingBuilder().add("username", userName).add("password", passWord).build();
                    Request request = new Request.Builder().url(LOGINURL).post(build).build();
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        String header = response.header("set-cookie");
                        String cookie = header.substring(0, header.indexOf(";"));
                        CacheUtils.putString(LoginActivity.this, "cookie", cookie);
                        subscriber.onNext(response.body().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
            }

            @Override
            public void onNext(String s) {

                boolean isError = s.contains("\"error\"");
                if (!isError) {
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    //失败,显示失败信息
                    Gson gson = new Gson();
                    LoginErrorInfo loginErrorInfo = gson.fromJson(s, LoginErrorInfo.class);
                    String error = loginErrorInfo.getError();

                }

            }
        });


    }


    /**
     * 判断网络问题
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
}
