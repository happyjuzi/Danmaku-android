package com.happyjuzi.library.danmaku;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.happyjuzi.library.danmaku.model.Danmaku;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tangh on 15/5/19.
 */
public class DanmakuLayout extends FrameLayout {

    public static final int DEFAULT_MAX_COUNT = 50;
    public static final boolean DEFAULT_BULLET_CLICKABLE = true;
    public static final int DEFAULT_LINES = 5;

    private ArrayList<Danmaku> data = new ArrayList<>();

    private Handler handler = new MyHandler();

    private int currentIndex;

    private int page = 1;

    private long ts = 0;

    private int mDanmakuItemHeight;

    private boolean hasMore = true;//默认还有更多

    private DanmakuView currentSelectView;

    private ArrayList<Integer> rowList;

    private Random random = new Random();

    private boolean isRunning = false;

    private boolean mInit = false;
    private int mMaxCount = DEFAULT_MAX_COUNT;
    private boolean mClickable = DEFAULT_BULLET_CLICKABLE;
    private int mLines = DEFAULT_LINES;
    private boolean mAutoLines;
    private int height;//视图高度

    public DanmakuLayout(Context context) {
        this(context, null);
    }

    public DanmakuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DanmakuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray attributes = context.obtainStyledAttributes(
                attrs, R.styleable.DanmakuLayout);

        mClickable = attributes.getBoolean(R.styleable.DanmakuLayout_danmaku_clickable, DEFAULT_BULLET_CLICKABLE);
        mMaxCount = attributes.getInt(R.styleable.DanmakuLayout_max_count, DEFAULT_MAX_COUNT);
        mLines = attributes.getInt(R.styleable.DanmakuLayout_lines, DEFAULT_LINES);
        mAutoLines = attributes.getBoolean(R.styleable.DanmakuLayout_auto_lines, false);
        mDanmakuItemHeight = dip2px(context, 30);
        init();
    }

    private void init() {
        mInit = true;
        caculateLines(600);
        for (int i = 0; i < mMaxCount; i++) {
            DanmakuView child = new DanmakuView(getContext());
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(child, params);
            child.setVisibility(GONE);
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    //是否可以点击
                    if (!mClickable) {
                        return;
                    }

                    v.bringToFront();

                    if (currentSelectView != null) {
                        currentSelectView.setSelected(false);
                        restartAnimator(currentSelectView);
                    }

                    currentSelectView = ((DanmakuView) v);
                    currentSelectView.setSelected(true);

                    cancelAnimator(currentSelectView);

                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("DanmakuLayout.onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        System.out.println("DanmakuLayout.onLayout");
    }

    public void setIsClickable(boolean isClickable) {
        this.mClickable = isClickable;
    }

    public void addData(ArrayList<Danmaku> data) {
        if (data != null) {
            this.data.addAll(data);
        }
    }

    public void addData(Danmaku data) {
        if (this.data.size() == 0) {
            this.data.add(data);
        } else {
            int index = currentIndex % this.data.size();
            Log.i("barrage", "index = " + index);
            this.data.add(index, data);
        }
    }

    public void clear() {
        this.data.clear();
    }

    public void clearSelected() {
        if (currentSelectView != null) {
            currentSelectView.setSelected(false);
            restartAnimator(currentSelectView);
            currentSelectView = null;
        }
    }

    public void resume() {
        if (currentSelectView != null) {
            restartAnimator(currentSelectView);
        } else if (!isRunning || data.size() == 1) {
            restartAnimator();
        }
    }

    public void startAnimator() {
        //标志弹幕已经开始启动
        isRunning = true;
        //发送开始消息
        handler.sendEmptyMessage(0);
    }

    public void restartAnimator() {
        //标志弹幕已经开始启动
        isRunning = true;

        for (int i = 0; i < getChildCount(); i++) {
            DanmakuView child = (DanmakuView) getChildAt(i);

            //如果当前有正在执行并且没有执行完的动画，继续执行
            restartAnimator(child);
        }

        //发送开始消息
        handler.sendEmptyMessage(0);

    }

    public void pauseAnimator() {
        //标志弹幕停止
        isRunning = false;
        handler.removeMessages(0);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            cancelAnimator(child);
        }
    }

    public void stopAnimator() {
        //标志弹幕停止
        isRunning = false;
        handler.removeMessages(0);
        clear();
        hasMore = true;
        page = 1;
        ts = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setVisibility(GONE);
            cancelAnimator(child);
        }
    }

    private void restartAnimator(View view) {
        //如果当前有正在执行并且没有执行完的动画，继续执行
        if (view.getVisibility() == VISIBLE && view.getTranslationX() > -view.getMeasuredWidth()) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", view.getTranslationX(), -view.getMeasuredWidth());
            animator.setInterpolator(new LinearInterpolator());
            float percent = (view.getTranslationX() + view.getMeasuredWidth()) * 1.0f / (getMeasuredWidth() + view.getMeasuredWidth());
            animator.setDuration((int) (8000 * percent));
            view.setTag(animator);
            animator.start();

        }
    }

    private void cancelAnimator(View view) {
        ObjectAnimator animator = ((ObjectAnimator) view.getTag());
        if (animator != null) {
            animator.cancel();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        pauseAnimator();
    }


    //随机取得一下次弹幕出现的行数
    private int getNextRow() {
        int randomIndex = random.nextInt(2);//取[0-2)的随机数
        int row = rowList.remove(randomIndex);
        rowList.add(row);
        return row;
    }

    public ArrayList<Danmaku> getData() {
        return data;
    }

    private void startNext() {
        if (currentIndex % data.size() == 0) {
            if (hasMore) {
            } else {
                handler.sendEmptyMessageDelayed(0, 8 * 1000);
            }
        } else {
            handler.sendEmptyMessageDelayed(0, 600);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            if (data.size() == 0 || !isRunning) {
                return;
            }

            final DanmakuView child = (DanmakuView) getChildAt(currentIndex % DEFAULT_MAX_COUNT);

            if (child == null) {
                return;
            }
            ObjectAnimator a = (ObjectAnimator) child.getTag();

            //如果当前要复用的弹幕的动画没有执行完毕 跳过取下一个
            if (a != null && a.isRunning()) {
                currentIndex++;
                startNext();
                return;
            }

            //如果当前弹幕是选中状态 跳过对这个弹幕的复用
            if (child.isSelected()) {
                currentIndex++;
                startNext();
                return;
            }

            Danmaku bean = data.get(currentIndex % data.size());

            //让后来的弹幕出现在最顶层，防止弹幕飞得过快时被之前的弹幕遮住
            child.bringToFront();

            //如果当前有选中状态的弹幕 强制它在显示在最顶层
            if (currentSelectView != null) {
                currentSelectView.bringToFront();
                //如果当前选中弹幕的内容已经存在，则跳过
                if (currentSelectView.bean.equals(bean)) {
                    currentIndex++;
                    startNext();
                    return;
                }
            }

            child.setVisibility(VISIBLE);
            child.setData(bean);

            //测量宽高
            int w = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
            int h = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
            child.measure(w, h);
            int width = child.getMeasuredWidth();
            int parentWidth = getMeasuredWidth();

            child.setTranslationY(mDanmakuItemHeight * getNextRow());
            child.setTranslationX(0);
            ObjectAnimator animator = ObjectAnimator.ofFloat(child, "translationX", parentWidth, -width);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(8000);
            animator.start();

            isRunning = true;

            child.setTag(animator);

            currentIndex++;

            startNext();

        }
    }

    public static int dip2px(Context context, float dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    private void caculateLines(int height) {
        if (mAutoLines && height > 0) {
            mLines = height / mDanmakuItemHeight;
        }

        if (rowList != null) {
            rowList.clear();
        } else {
            rowList = new ArrayList<>();
        }
        for (int i = 0; i < mLines; i++) {
            rowList.add(i);
        }
    }
}
