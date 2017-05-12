package com.example.shuangxiang.qdkelenqdforphone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.shuangxiang.qdkelenqdforphone.utils.CacheUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shuang.xiang on 2016/11/18.
 */

public class ChooseIdActivity extends Activity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseid);

        mListView = (ListView) findViewById(R.id.lv_chooseId);
        Map<String, String> map1 = new HashMap<>();
        map1.put("id", "deviceId0026");
        Map<String, String> map2 = new HashMap<>();
        map2.put("id", "deviceId0028");
        List<Map<String, String>> objects = new ArrayList<>();
        objects.add(map1);
        objects.add(map2);
        String[] from = new String[]{"id"};
        int[] to = new int[]{R.id.item_chooseid};
        mListView.setAdapter(new SimpleAdapter(this, objects, R.layout.item_chooseid, from, to));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    CacheUtils.putInt(ChooseIdActivity.this, "id", 26);
                } else if (position == 1) {
                    CacheUtils.putInt(ChooseIdActivity.this, "id", 28);
                }

                Intent intent = new Intent(ChooseIdActivity.this, MainActivity.class);
                startActivity(intent);


            }
        });


    }

}
