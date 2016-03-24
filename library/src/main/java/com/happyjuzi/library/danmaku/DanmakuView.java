package com.happyjuzi.library.danmaku;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.happyjuzi.library.danmaku.model.Danmaku;
import com.squareup.picasso.Picasso;

/**
 * Created by tangh on 14-9-20.
 */
public class DanmakuView extends FrameLayout {

    public ImageView mAvatarView;
    public TextView mContentView;
    public Danmaku bean;

    public DanmakuView(Context context) {
        super(context);
        init();
    }

    public DanmakuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DanmakuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_danmaku, this);
        mAvatarView = (ImageView) findViewById(R.id.danmu_avatar);
        mContentView = (TextView) findViewById(R.id.danmu_text);
    }

    public void setData(Danmaku bean) {
        this.bean = bean;
        if (bean != null) {
            Picasso.with(getContext()).load(bean.avatar).into(mAvatarView);
        }
        mContentView.setText(bean.content);
    }

}
