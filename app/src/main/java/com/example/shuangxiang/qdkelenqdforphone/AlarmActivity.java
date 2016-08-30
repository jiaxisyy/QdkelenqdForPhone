package com.example.shuangxiang.qdkelenqdforphone;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shuangxiang.qdkelenqdforphone.adapter.MyAlarmRecyclerViewAdapter;
import com.example.shuangxiang.qdkelenqdforphone.bean.AlarmInfo;
import com.example.shuangxiang.qdkelenqdforphone.utils.CacheUtils;
import com.example.shuangxiang.qdkelenqdforphone.utils.CustomToast;
import com.example.shuangxiang.qdkelenqdforphone.wheelViewUtil.NumericWheelAdapter;
import com.example.shuangxiang.qdkelenqdforphone.wheelViewUtil.OnWheelScrollListener;
import com.example.shuangxiang.qdkelenqdforphone.wheelViewUtil.WheelView;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

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
public class AlarmActivity extends AppCompatActivity {
    private static final String ALARM_URL = "http://kawakp.chinclouds.com:58010/userconsle/deviceAlarms?pageSize=20";
    private static final String TAG = "AlarmActivity";
    @BindView(R.id.alarm_swipeRefresh)
    SwipeRefreshLayout alarmSwipeRefresh;
    private String deviceId = "deviceId0026";
    private int pageNum;
    private int pageSize;
    private String startTime = "";
    private String endTime = "";

    @BindView(R.id.tb_Alarm)
    Toolbar tbAlarm;
    @BindView(R.id.rv_alarm)
    RecyclerView rvAlarm;
    @BindView(R.id.tv_alarm_starTime)
    TextView tvAlarmStarTime;
    @BindView(R.id.tv_alarm_endTime)
    TextView tvAlarmEndTime;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private PopupWindow menuWindow;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private WheelView hour;
    private WheelView mins;
    private boolean flag;
    private LinearLayoutManager linearLayoutManager;
    private MyAlarmRecyclerViewAdapter adapter;
    private int lastVisibleItem;
    private boolean isStartSelect = false;
    private boolean isEndSelect = false;
    private List<AlarmInfo.ListBean> list;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        setSupportActionBar(tbAlarm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        readData(ALARM_URL);
        refresh();
    }

    /**
     * 刷新界面
     */
    private void refresh() {
        //下拉刷新
        alarmSwipeRefresh.setColorSchemeColors(Color.RED, Color.YELLOW, Color.BLUE);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        alarmSwipeRefresh.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        alarmSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readData(ALARM_URL);
                Log.d(TAG, "onRefresh");
                alarmSwipeRefresh.setRefreshing(false);
            }
        });

        //上拉加载
        rvAlarm.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    Log.d(TAG, " 上拉加载");
                    i += 1;
                    Log.d(TAG, "i=" + i);
                    final String url = "http://kawakp.chinclouds.com:58010/userconsle/deviceAlarms?pageNum=" + i + "&pageSize=20";
                    final String cookie = CacheUtils.getString(AlarmActivity.this, "cookie");
                    if (!TextUtils.isEmpty(cookie)) {
                        Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(final Subscriber<? super String> subscriber) {

                                Request request = new Request.Builder().addHeader("cookie", cookie).url(url).get().build();
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
                                Log.e(TAG, e.toString());

                            }

                            @Override
                            public void onNext(String s) {
                                Gson gson = new Gson();
                                AlarmInfo alarmInfo = gson.fromJson(s, AlarmInfo.class);
                                list = alarmInfo.getList();
                                adapter.setData(list);
                            }
                        });
                    }


                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });


    }


    /**
     * 获取所有的报警记录
     */
    private void readData(final String url) {
        final String cookie = CacheUtils.getString(this, "cookie");
        if (!TextUtils.isEmpty(cookie)) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(final Subscriber<? super String> subscriber) {

                    Request request = new Request.Builder().addHeader("cookie", cookie).url(url).get().build();
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
                    Log.e(TAG, e.toString());

                }

                @Override
                public void onNext(String s) {


                    Gson gson = new Gson();
                    AlarmInfo alarmInfo = gson.fromJson(s, AlarmInfo.class);
                    list = alarmInfo.getList();
                    rvAlarm.setHasFixedSize(true);
                    linearLayoutManager = new LinearLayoutManager(AlarmActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvAlarm.setLayoutManager(linearLayoutManager);
                    adapter = new MyAlarmRecyclerViewAdapter(list, AlarmActivity.this);
                    rvAlarm.setAdapter(adapter);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.tv_alarm_starTime, R.id.tv_alarm_endTime, R.id.tv_alarm_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_alarm_starTime:
                flag = true;
                showPopwindow(getDataPick());
                break;
            case R.id.tv_alarm_endTime:
                flag = false;
                showPopwindow(getDataPick());
                break;
            case R.id.tv_alarm_search:


                if (isStartSelect && isEndSelect) {
                    search();
                    isStartSelect = false;
                    isEndSelect = false;

                } else {
                    CustomToast.showToast(this, "不能使用默认时间", Toast.LENGTH_SHORT);
                }


                break;
        }
    }

    private void search() {
        pageNum = 1;
        pageSize = 20;
        final String SEARCH_URL = "http://kawakp.chinclouds.com:58010/userconsle/deviceAlarms/" + deviceId + "/elementTables/xjf_t_1/datas?" + "&pageNum=" + pageNum + "&pageSize=" + pageSize + "&&fromDate=" + startTime + "&toDate=" + endTime;
        Log.d(TAG, SEARCH_URL);
        final String cookie = CacheUtils.getString(this, "cookie");
        if (!TextUtils.isEmpty(cookie)) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(final Subscriber<? super String> subscriber) {

                    Request request = new Request.Builder().addHeader("cookie", cookie).url(SEARCH_URL).get().build();
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

                }

                @Override
                public void onNext(String s) {
                    Gson gson = new Gson();
                    AlarmInfo alarmInfo = gson.fromJson(s, AlarmInfo.class);
                    List<AlarmInfo.ListBean> list = alarmInfo.getList();
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AlarmActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvAlarm.setLayoutManager(linearLayoutManager);
                    rvAlarm.setAdapter(new MyAlarmRecyclerViewAdapter(list, AlarmActivity.this));
                }
            });
        }
    }

    private void showPopwindow(View view) {

        menuWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        menuWindow.setFocusable(true);
        menuWindow.setOutsideTouchable(true);
        menuWindow.setBackgroundDrawable(new BitmapDrawable());
//        menuWindow.setAnimationStyle(R.style.myanimation);
        menuWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                menuWindow = null;
//                PopUtils.setBackgroundAlpha(1.0f, SearchHistoryDataActivity.this);//设置Popw消失背景为透明
            }
        });
//        PopUtils.setBackgroundAlpha(0.5f, SearchHistoryDataActivity.this);//设置popw出现时背景透明度

    }

    private View getDataPick() {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        final View view = LayoutInflater.from(AlarmActivity.this).inflate(R.layout.datapick, null);

        year = (WheelView) view.findViewById(R.id.year);
        year.setAdapter(new NumericWheelAdapter(1950, curYear));
        //year.setLabel("年");
        year.setCyclic(true);
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.month);
        month.setAdapter(new NumericWheelAdapter(1, 12));
        // month.setLabel("月");
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.day);
        initDay(curYear, curMonth);
        //day.setLabel("日");
        day.setCyclic(true);

        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);

        hour = (WheelView) view.findViewById(R.id.hour);
        hour.setAdapter(new NumericWheelAdapter(0, 23));
        // hour.setLabel("时");
        hour.setCyclic(true);
        mins = (WheelView) view.findViewById(R.id.mins);
        mins.setAdapter(new NumericWheelAdapter(0, 59));
        //mins.setLabel("分");
        mins.setCyclic(true);

        hour.setCurrentItem(8);
        mins.setCurrentItem(30);

        TextView bt = (TextView) view.findViewById(R.id.set);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = (year.getCurrentItem() + 1950) + "-" + (month.getCurrentItem() + 1) + "-" + (day.getCurrentItem() + 1 + " " + hour.getCurrentItem() + ":" + mins.getCurrentItem());

                if (flag) {
                    tvAlarmStarTime.setText(str);
                    //转换时间戳
                    startTime = getTime(str);
                    isStartSelect = true;
                } else {
                    tvAlarmEndTime.setText(str);
                    //转换时间戳
                    endTime = getTime(str);
                    isEndSelect = true;
                }


                menuWindow.dismiss();
            }
        });
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
            }
        });
        return view;
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            // TODO Auto-generated method stub
            int n_year = year.getCurrentItem() + 1950;// 楠烇拷
            int n_month = month.getCurrentItem() + 1;// 閺堬拷
            initDay(n_year, n_month);
        }
    };

    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    /**
     */
    private void initDay(int arg1, int arg2) {
        day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
    }
// 将字符串转为时间戳


    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);


        } catch (ParseException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re_time;
    }
}
