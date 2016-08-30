package com.example.shuangxiang.qdkelenqdforphone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shuangxiang.qdkelenqdforphone.bean.DeviceInfo;
import com.example.shuangxiang.qdkelenqdforphone.utils.CacheUtils;
import com.example.shuangxiang.qdkelenqdforphone.view.AnimotionSGView;
import com.example.shuangxiang.qdkelenqdforphone.view.KeyboardLayout;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity implements AnimotionSGView.IsArrive {
    private static final String TAG = "MainActivity";
    private static String URL = "http://kawakp.chinclouds.com:58010/userconsle/devices/deviceId0026/elementTables/xjf_t_1/datas?pageNum=1&pageSize=1";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @BindView(R.id.sg_animation)
    AnimotionSGView sgAnimation;
    @BindView(R.id.tv_allTime)
    TextView tvAllTime;
    @BindView(R.id.cbMore)
    CheckBox cbMore;
    @BindView(R.id.et_pressure_upper)
    EditText etPressureUpper;
    @BindView(R.id.et_t_upper)
    EditText etTUpper;
    @BindView(R.id.et_rise)
    EditText etRise;
    @BindView(R.id.tv_rise_min)
    TextView tvRiseMin;
    @BindView(R.id.et_pressure_floor)
    EditText etPressureFloor;
    @BindView(R.id.et_t_floor)
    EditText etTFloor;
    @BindView(R.id.et_decline)
    EditText etDecline;
    @BindView(R.id.tv_decline_min)
    TextView tvDeclineMin;
    @BindView(R.id.cb_setting1)
    CheckBox cbSetting1;
    @BindView(R.id.cb_setting2)
    CheckBox cbSetting2;
    @BindView(R.id.cb_setting3)
    CheckBox cbSetting3;
    @BindView(R.id.cb_setting4)
    CheckBox cbSetting4;
    @BindView(R.id.cb_setting5)
    CheckBox cbSetting5;
    @BindView(R.id.cb_setting6)
    CheckBox cbSetting6;
    @BindView(R.id.cb_setting7)
    CheckBox cbSetting7;
    @BindView(R.id.cb_setting8)
    CheckBox cbSetting8;
    @BindView(R.id.cb_setting9)
    CheckBox cbSetting9;
    @BindView(R.id.cb_setting10)
    CheckBox cbSetting10;
    @BindView(R.id.dl_right)
    DrawerLayout dlRight;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.ll_more_setting)
    LinearLayout llMoreSetting;
    @BindView(R.id.tv_moreSetting)
    TextView tvMoreSetting;
    @BindView(R.id.tv_moreShow)
    TextView tvMoreShow;
    @BindView(R.id.tv_moreAlarm)
    TextView tvMoreAlarm;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.iv_bubble)
    ImageView ivBubble;
    @BindView(R.id.main_keyLayout)
    KeyboardLayout mainKeyLayout;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private AnimationDrawable animationDrawable;
    private Subscription subscription;
    private boolean et_flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sgAnimation.start();
        sgAnimation.setIsArrive(this);
        initData();
        writeData();
        readData();
        rwCheckBox();
        test();

    }

    private void test() {

        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int
                    oldLeft, int oldTop, int oldRight, int oldBottom) {

                RelativeLayout wv_discover_law_error = (RelativeLayout) MainActivity.this.getWindow()
                        .getDecorView().findViewById
                                (R.id.rl_test);

                int measuredHeight = wv_discover_law_error.getMeasuredHeight();
                int measuredWidth = wv_discover_law_error.getMeasuredWidth();

                Log.d("test", measuredHeight + "键盘弹出" + measuredWidth);
            }
        });
    }

    /**
     * 写操作
     */
    private void writeData() {
        etPressureUpper.setCursorVisible(false);
//        mainKeyLayout.setOnkbdStateListener(new KeyboardLayout.onKybdsChangeListener() {
//            @Override
//            public void onKeyBoardStateChange(int state) {
//                if (state == KeyboardLayout.KEYBOARD_STATE_SHOW) {
//                    et_flag = false;
//                    Log.d("pan", "打开");
//                } else if (state == KeyboardLayout.KEYBOARD_STATE_HIDE) {
//                    Log.d("pan", "隐藏");
//
//                }
//            }
//        });
//        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                boolean b = v.getId() == R.id.main_keyLayout;
//                if (b) {
//                    Log.d("change", "输入框出现");
//                }
//                Log.d("change", b + "");
//                Log.d("change", left + "----left");
//                Log.d("change", top + "-----top");
//                Log.d("change", right + "---right");
//                Log.d("change", bottom + "----bottom");
//                Log.d("change", oldLeft + "-----oldLeft");
//                Log.d("change", oldTop + "-----oldTop");
//                Log.d("change", oldRight + "-----oldRight");
//                Log.d("change", oldBottom + "-----oldBottom");
//
//
//            }
//        });

        etPressureUpper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    et_flag = false;
                    return false;
                }
                return false;
            }
        });
        etPressureFloor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    et_flag = false;
                    return false;
                }
                return false;
            }
        });
        etTUpper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    et_flag = false;
                    return false;
                }
                return false;
            }
        });
        etTFloor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    et_flag = false;
                    return false;
                }
                return false;
            }
        });
        etRise.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    et_flag = false;
                    return false;
                }
                return false;
            }
        });
        etDecline.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    et_flag = false;
                    return false;
                }
                return false;
            }
        });



        /*etPressureUpper.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean clickable = etPressureUpper.isClickable();

                Log.d(TAG,"clickable="+clickable);
                et_flag = false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPressureFloor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et_flag = false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etTUpper.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et_flag = false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etTFloor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et_flag = false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etRise.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et_flag = false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etDecline.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et_flag = false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/


        etPressureUpper.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                     /*判断是否是“GO”键*/
                if (i == EditorInfo.IME_ACTION_DONE) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) textView
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                textView.getApplicationWindowToken(), 0);
                    }
                    //写入数据
                    write(etPressureUpper.getText().toString(), "xjf_t_1_e_12");
                    et_flag = true;
                    readData();
                    Log.d(TAG, "writeData");
                    etPressureUpper.setCursorVisible(false);
                    return true;
                }
                return false;
            }
        });
        etPressureFloor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                     /*判断是否是“GO”键*/
                if (i == EditorInfo.IME_ACTION_DONE) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) textView
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                textView.getApplicationWindowToken(), 0);
                    }
                    //写入数据
                    write(etPressureFloor.getText().toString(), "xjf_t_1_e_13");
                    etPressureFloor.setCursorVisible(false);
                    et_flag = true;
                    readData();
                    return true;
                }
                return false;
            }
        });
        //温度上限
        etTUpper.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                     /*判断是否是“GO”键*/
                if (i == EditorInfo.IME_ACTION_DONE) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) textView
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                textView.getApplicationWindowToken(), 0);
                    }
                    //写入数据
                    write(etTUpper.getText().toString(), "xjf_t_1_e_15");
                    etTUpper.setCursorVisible(false);
                    et_flag = true;
                    readData();
                    return true;
                }
                return false;
            }
        });
        //温度下限
        etTFloor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                     /*判断是否是“GO”键*/
                if (i == EditorInfo.IME_ACTION_DONE) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) textView
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                textView.getApplicationWindowToken(), 0);
                    }
                    //写入数据
                    write(etTFloor.getText().toString(), "xjf_t_1_e_16");
                    etTFloor.setCursorVisible(false);
                    et_flag = true;
                    readData();
                    return true;
                }
                return false;
            }
        });
        //升温
        etRise.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                     /*判断是否是“GO”键*/
                if (i == EditorInfo.IME_ACTION_DONE) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) textView
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                textView.getApplicationWindowToken(), 0);
                    }
                    //写入数据
                    write(etRise.getText().toString(), "xjf_t_1_e_17");
                    etRise.setCursorVisible(false);
                    et_flag = true;
                    readData();
                    return true;
                }
                return false;
            }
        });
        //降温
        etDecline.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                     /*判断是否是“GO”键*/
                if (i == EditorInfo.IME_ACTION_DONE) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) textView
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                textView.getApplicationWindowToken(), 0);
                    }
                    //写入数据
                    write(etDecline.getText().toString(), "xjf_t_1_e_19");
                    etDecline.setCursorVisible(false);
                    et_flag = true;
                    readData();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 单独读输入框数据
     */
    private void readEtText() {
    }

    /**
     * 点击按钮操作
     */

    private void rwCheckBox() {
        final String[] ids = {"xjf_t_1_e_24", "xjf_t_1_e_25", "xjf_t_1_e_26", "xjf_t_1_e_27", "xjf_t_1_e_28", "xjf_t_1_e_29", "xjf_t_1_e_30", "xjf_t_1_e_31", "xjf_t_1_e_32", "xjf_t_1_e_33"};
        cbSetting1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_flag = false;
                if (cbSetting1.isChecked()) {
                    openOrClose(true, ids[0]);
                } else {
                    openOrClose(false, ids[0]);
                }
//                    SystemClock.sleep(500);
                et_flag = true;
                readData();
            }
        });
        cbSetting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_flag = false;
                if (cbSetting2.isChecked()) {
                    openOrClose(true, ids[1]);
                } else {
                    openOrClose(false, ids[1]);
                }
//                SystemClock.sleep(500);
                et_flag = true;
                readData();

            }
        });
        cbSetting3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_flag = false;
                if (cbSetting3.isChecked()) {
                    openOrClose(true, ids[2]);
                } else {
                    openOrClose(false, ids[2]);
                }

//                SystemClock.sleep(500);
                et_flag = true;
                readData();

            }
        });
        cbSetting4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_flag = false;
                if (cbSetting4.isChecked()) {
                    openOrClose(true, ids[3]);
                } else {
                    openOrClose(false, ids[3]);
                }

//                SystemClock.sleep(500);
                et_flag = true;
                readData();

            }
        });
        cbSetting5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_flag = false;
                if (cbSetting5.isChecked()) {
                    openOrClose(true, ids[4]);
                } else {
                    openOrClose(false, ids[4]);
                }

//                SystemClock.sleep(500);
                et_flag = true;
                readData();

            }
        });
        cbSetting6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_flag = false;
                if (cbSetting6.isChecked()) {
                    openOrClose(true, ids[5]);
                } else {
                    openOrClose(false, ids[5]);
                }

//                SystemClock.sleep(500);
                et_flag = true;
                readData();

            }
        });
        cbSetting7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_flag = false;
                if (cbSetting7.isChecked()) {
                    openOrClose(true, ids[6]);
                } else {
                    openOrClose(false, ids[6]);
                }

//                SystemClock.sleep(500);
                et_flag = true;
                readData();

            }
        });
        cbSetting8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_flag = false;
                if (cbSetting8.isChecked()) {
                    openOrClose(true, ids[7]);
                } else {
                    openOrClose(false, ids[7]);
                }

//                SystemClock.sleep(500);
                et_flag = true;
                readData();

            }
        });
        cbSetting9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_flag = false;
                if (cbSetting9.isChecked()) {
                    openOrClose(true, ids[8]);
                } else {
                    openOrClose(false, ids[8]);
                }
//                SystemClock.sleep(500);
                et_flag = true;
                readData();

            }
        });
        cbSetting10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_flag = false;
                if (cbSetting10.isChecked()) {
                    openOrClose(true, ids[9]);
                } else {
                    openOrClose(false, ids[9]);
                }
//                SystemClock.sleep(500);
                et_flag = true;
                readData();

            }
        });

    }

    /**
     * 给元件赋值
     *
     * @param value
     * @param elementId
     */
    private void write(String value, String elementId) {


        final String json = "{\"value\":" + value + "}";
        final String switch_url = "http://kawakp.chinclouds.com:58010/userconsle/devices/deviceId0026/elements/" + elementId;
        final String cookie = CacheUtils.getString(this, "cookie");
        if (!TextUtils.isEmpty(cookie)) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(final Subscriber<? super String> subscriber) {

                    RequestBody requestBody = RequestBody.create(JSON, json);
                    Request request = new Request.Builder().addHeader("cookie", cookie).url(switch_url).put(requestBody).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Log.e(TAG, request.body().toString());
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            String string = response.body().string();
                            subscriber.onNext(string);
                        }
                    });


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
                    Log.d(TAG, s);
                }
            });

        }
    }


    /**
     * 控制开关
     *
     * @param b
     * @param elementId
     */
    private void openOrClose(final boolean b, String elementId) {

        int i = b ? 1 : 0;
        final String json = "{\"value\":" + i + "}";
        final String switch_url = "http://kawakp.chinclouds.com:58010/userconsle/devices/deviceId0026/elements/" + elementId;
        final String cookie = CacheUtils.getString(this, "cookie");
        if (!TextUtils.isEmpty(cookie)) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(final Subscriber<? super String> subscriber) {

                    RequestBody requestBody = RequestBody.create(JSON, json);
                    Request request = new Request.Builder().addHeader("cookie", cookie).url(switch_url).put(requestBody).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Log.e(TAG, request.body().toString());

                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            String string = response.body().string();
                            subscriber.onNext(string);
                        }
                    });


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
                    Log.d(TAG, s);
                }
            });

        }
    }

    /**
     * 读取数据
     */
    private void readData() {

        final String cookie = CacheUtils.getString(this, "cookie");
        if (!TextUtils.isEmpty(cookie)) {

            subscription = Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(final Subscriber<? super String> subscriber) {
                    while (et_flag) {

                        Request request = new Request.Builder().addHeader("cookie", cookie).url(URL).get().build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {

                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                String string = response.body().string();
                                subscriber.onNext(string);

                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
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

                }

                @Override
                public void onNext(String s) {
                    Log.d(TAG, "read");
                    Gson gson = new Gson();
                    DeviceInfo deviceInfo = gson.fromJson(s, DeviceInfo.class);
                    List<DeviceInfo.ListBean> list = deviceInfo.getList();
                    DeviceInfo.ListBean listBean = list.get(0);
                    //压力上限
                    double d12 = listBean.getD12();
                    etPressureUpper.setText(d12 + "");
                    //压力下限
                    double d14 = listBean.getD14();
                    etPressureFloor.setText(d14 + "");
//                    温度上下限
                    double d18 = listBean.getD18();
                    double d20 = listBean.getD20();
                    etTUpper.setText(d18 + "");
                    etTFloor.setText(d20 + "");
                    //升温
                    int d22 = listBean.getD22();
                    int d23 = listBean.getD23();
                    etRise.setText(d22 + "");
                    tvRiseMin.setText(d23 + " min");
                    //降温
                    int d24 = listBean.getD24();
                    int d25 = listBean.getD25();
                    etDecline.setText(d24 + "");
                    tvDeclineMin.setText(d25 + " min");
                    //工程总用时
                    int d26 = listBean.getD26();
                    int d27 = listBean.getD27();
                    tvAllTime.setText(d26 + " h " + d27 + " m ");
                    //TODO 给开关初始化

                    int[] intsD = {listBean.getD0(), listBean.getD1(), listBean.getD2(), listBean.getD3(), listBean.getD4(), listBean.getD5(), listBean.getD6(), listBean.getD7(), listBean.getD8(), listBean.getD9()};
                    List<CheckBox> checkBoxes = new ArrayList<>();
                    checkBoxes.add(cbSetting1);
                    checkBoxes.add(cbSetting2);
                    checkBoxes.add(cbSetting3);
                    checkBoxes.add(cbSetting4);
                    checkBoxes.add(cbSetting5);
                    checkBoxes.add(cbSetting6);
                    checkBoxes.add(cbSetting7);
                    checkBoxes.add(cbSetting8);
                    checkBoxes.add(cbSetting9);
                    checkBoxes.add(cbSetting10);

                    int length = intsD.length;
                    for (int i = 0; i < length; i++) {
                        if (intsD[i] == 1) {
                            //给开关赋值
                            checkBoxes.get(i).setChecked(true);
                        } else {
                            checkBoxes.get(i).setChecked(false);
                        }
                    }


                    //TODO 初始化动画
                    boolean m1 = listBean.isM1();
                    boolean m2 = listBean.isM2();
                    boolean m3 = listBean.isM3();
                    boolean m4 = listBean.isM4();
                    boolean m5 = listBean.isM5();
                    boolean m6 = listBean.isM6();
                    boolean m7 = listBean.isM7();
                    boolean m8 = listBean.isM8();
                    boolean m9 = listBean.isM9();
                    boolean m10 = listBean.isM10();
                    String[] ss = {String.valueOf(m1), String.valueOf(m2), String.valueOf(m3), String.valueOf(m4), String.valueOf(m5), String.valueOf(m6), String.valueOf(m7), String.valueOf(m8), String.valueOf(m9), String.valueOf(m10)};


                    //M1
                    if (ss[0].equals("true")) {
                        sgAnimation.openCircle(1);
                    } else if (ss[0].equals("false")) {
                        sgAnimation.stopCircle(1);
                    }

                    //M2
                    if (ss[1].equals("true")) {
                        sgAnimation.openCircle(2);
                    } else if (ss[1].equals("false")) {
                        sgAnimation.stopCircle(2);
                    }

                    //M3
                    if (ss[2].equals("true")) {
                        sgAnimation.openCircle(3);
                    } else if (ss[2].equals("false")) {
                        sgAnimation.stopCircle(3);
                    }

                    //M5
                    if (ss[4].equals("true")) {
                        sgAnimation.openCircle(4);
                    } else if (ss[4].equals("false")) {
                        sgAnimation.stopCircle(4);
                    }

                    //M6
                    if (ss[5].equals("true")) {
                        sgAnimation.openCircle(5);
                    } else if (ss[5].equals("false")) {
                        sgAnimation.stopCircle(5);
                    }

                    //M7
                    if (ss[6].equals("true")) {
                        sgAnimation.openCircle(7);
                    } else if (ss[6].equals("false")) {
                        sgAnimation.stopCircle(7);
                    }

                    //M8
                    if (ss[7].equals("true")) {
                        sgAnimation.openCircle(6);
                    } else if (ss[7].equals("false")) {
                        sgAnimation.stopCircle(6);
                    }

                    //M9
                    if (ss[8].equals("true")) {

                        sgAnimation.openCircle(8);
                    } else if (ss[8].equals("false")) {
                        sgAnimation.stopCircle(8);
                    }

                    //M10
                    if (ss[9].equals("true")) {
                        sgAnimation.openCircle(9);
                    } else if (ss[9].equals("false")) {
                        sgAnimation.stopCircle(9);
                    }


                }
            });


        }

    }

    private void initData() {
        animationDrawable = (AnimationDrawable) ivBubble.getBackground();

        cbMore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    llMoreSetting.setVisibility(View.VISIBLE);
                } else {
                    llMoreSetting.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @OnClick({R.id.tv_moreSetting, R.id.tv_moreShow, R.id.tv_moreAlarm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_moreSetting:
                dlRight.openDrawer(llRight);
                llMoreSetting.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_moreShow:
                startActivity(new Intent(this, ShowActivity.class));
                et_flag = false;
                subscription.unsubscribe();
                finish();
                break;
            case R.id.tv_moreAlarm:
                startActivity(new Intent(this, AlarmActivity.class));
                et_flag = false;
                subscription.unsubscribe();
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        et_flag = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        et_flag = true;
        readData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void callBack(boolean b) {
        if (b) {
            animationDrawable.start();
            animationDrawable.setOneShot(false);
        } else {
            animationDrawable.stop();
        }

    }
}
