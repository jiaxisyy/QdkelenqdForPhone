package com.example.shuangxiang.qdkelenqdforphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.shuangxiang.qdkelenqdforphone.bean.DeviceInfo;
import com.example.shuangxiang.qdkelenqdforphone.utils.CacheUtils;
import com.example.shuangxiang.qdkelenqdforphone.view.DrawChart;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shuang.xiang on 2016/8/18.
 */
public class ShowActivity extends AppCompatActivity {

    private static final String TAG = "ShowActivity";

    @BindView(R.id.tb_show)
    Toolbar tbShow;
    @BindView(R.id.dc_show)
    DrawChart dcShow;
    @BindView(R.id.tv_showT)
    TextView tvShowT;
    @BindView(R.id.tv_showP)
    TextView tvShowP;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private boolean flag = true;
    private String mURL;
    private int mId;
//    private Handler mHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            int obj = (int) msg.obj;
//            dcShow.setY(obj);
//            dcShow.invalidate();
//            return false;
//        }
//    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);
//        getWindow().setBackgroundDrawable(null);
        mId = CacheUtils.getInt(this, "id");
        mURL = "http://58.250.204" +
                ".112:58010/userconsle/devices/deviceId00" + mId + "/elementTables/xjf_t_1/datas" +
                "?pageNum=1&pageSize=1";
        setSupportActionBar(tbShow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    while (true) {
//                        int i = new Random().nextInt(100);
//                        Thread.sleep(2000);
//                        Message message = Message.obtain();
//                        message.obj=i;
//                        mHandler.sendMessage(message);
//                    }
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();

        readData();
    }


    /**
     * 读取数据
     */
    private void readData() {
        final String cookie = CacheUtils.getString(this, "cookie");
        if (!TextUtils.isEmpty(cookie)) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(final Subscriber<? super String> subscriber) {
                    while (flag) {
                        Request request = new Request.Builder().addHeader("cookie", cookie).url
                                (mURL).get().build();
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
                            Thread.sleep(2000);
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

                    Gson gson = new Gson();
                    DeviceInfo deviceInfo = gson.fromJson(s, DeviceInfo.class);
                    List<DeviceInfo.ListBean> list = deviceInfo.getList();
                    DeviceInfo.ListBean listBean = list.get(0);
                    //压力
                    double d10 = listBean.getD10();
                    //温度
                    double d16 = listBean.getD16();
                    tvShowT.setText("温度 " + d16 + " °C");
                    tvShowP.setText("压力 " + d10 + " Mpa");
                    dcShow.setY((int) d16);
                    String s1 = String.valueOf(d10);
                    float i = Float.parseFloat(s1) / 3;
                    float v = i * 150;
                    String s2 = String.valueOf(v);
                    String substring = s2.substring(0, s2.indexOf("."));
                    dcShow.setOtherY(Integer.parseInt(substring));
                    dcShow.invalidate();
                }
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            flag = false;
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
