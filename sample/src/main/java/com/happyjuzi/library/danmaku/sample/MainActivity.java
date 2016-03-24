package com.happyjuzi.library.danmaku.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.happyjuzi.library.danmaku.DanmakuLayout;
import com.happyjuzi.library.danmaku.model.Danmaku;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DanmakuLayout danmakuLayout = (DanmakuLayout) findViewById(R.id.danmaku_layout);
        danmakuLayout.addData(createData());
        danmakuLayout.post(new Runnable() {
            @Override
            public void run() {
                danmakuLayout.startAnimator();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Danmaku> createData() {
        ArrayList<Danmaku> list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Danmaku danmaku = new Danmaku();
            danmaku.content = "我是第" + i + "个弹幕";
            if (i % 10 == 0) {
                danmaku.avatar = "http://images11.app.happyjuzi.com/usericonurl/2d2c7c0d-beff-43ee-9c18-709096c662db.jpg!up1.webp";
            } else if (i % 10 == 1) {
                danmaku.avatar = "http://images11.app.happyjuzi.com/usericonurl/2e91fd57-5a2d-47b3-897d-7f554e497143.jpg!up1.webp";
            } else if (i % 10 == 2) {
                danmaku.avatar = "http://images11.app.happyjuzi.com/usericonurl/18f17ea1-46fc-4a65-973d-99ebd2d4451b!up1.webp";
            } else if (i % 10 == 3) {
                danmaku.avatar = "http://images11.app.happyjuzi.com/usericonurl/14411472592638.jpg!up1.webp";
            } else if (i % 10 == 4) {
                danmaku.avatar = "http://images11.app.happyjuzi.com/usericonurl/14380067347819.jpg!up1.webp";
            } else if (i % 10 == 5) {
                danmaku.avatar = "http://images11.app.happyjuzi.com/usericonurl/1e6a9bbb-2b7c-4e2e-ba32-f6d796723569!up1.webp";
            } else if (i % 10 == 6) {
                danmaku.avatar = "http://images11.app.happyjuzi.com/usericonurl/866f6522-be50-4f7f-b622-fafddd8c027e!up1.webp";
            } else if (i % 10 == 7) {
                danmaku.avatar = "http://images11.app.happyjuzi.com/usericonurl/02d97f14-05a7-4561-9713-35745a4cf497.jpg!up1.webp";
            } else if (i % 10 == 8) {
                danmaku.avatar = "http://images11.app.happyjuzi.com/usericonurl/35912ab5-ba77-45f7-86f1-e43265e565f9.jpg!up1.webp";
            } else {
                danmaku.avatar = "http://images11.app.happyjuzi.com/usericonurl/866f6522-be50-4f7f-b622-fafddd8c027e!up1.webp";
            }
            list.add(danmaku);
        }
        return list;
    }
}
