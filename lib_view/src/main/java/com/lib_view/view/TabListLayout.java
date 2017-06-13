package com.lib_view.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lib_view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangrui on 17/1/6.
 */

public class TabListLayout extends LinearLayout {


    private int mSelectIndex = 0;
    private OnItemClickListener mListener;
    private LinearLayout tab_list;
    private ImageView bottom_line;
    private int bottom_line_width;
    private AnimationSet set;

    public TabListLayout(Context context) {
        super(context);
        init();
    }

    public TabListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TabListLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_tab_list, this);

        tab_list = (LinearLayout) findViewById(R.id.tab_list);
        bottom_line = (ImageView) findViewById(R.id.bottom_line);
    }

    public void addListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void addTab(List<String> nameList,,int screenW) {
        List<Integer> resIdList = new ArrayList<>();
        for(String str:nameList){
            resIdList.add(0);
        }
        addTab(nameList, resIdList, screenW);
    }

    public void addTab(List<String> nameList, List<Integer> resIdList,int screenW) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;

        int size = nameList.size();
        for (int i = 0; i < size; i++) {
            String name = nameList.get(i);
            int res = resIdList.get(i);
            TabLayout tab = new TabLayout(getContext());
            tab.setText(name);
            tab.setTag(i);
            tab.setImage(res);
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int moToIndex = (int) v.getTag();
                    if (mSelectIndex != moToIndex) {
                        ((TabLayout) tab_list.getChildAt(mSelectIndex)).setTextenable(false);
                        playAnimation(mSelectIndex, moToIndex);
                        mSelectIndex = moToIndex;
                        mListener.onCallback(mSelectIndex);
                    }
                }
            });
            tab.setTextenable(i == 0);
            tab_list.addView(tab, params);
        }
        bottom_line_width = screenW / size;
        bottom_line.getLayoutParams().width = bottom_line_width;
    }

    private void playAnimation(int selectIndex, final int moToIndex) {
        if (set == null) {
            TabLayout startViewParent = (TabLayout) tab_list.getChildAt(selectIndex);
            TabLayout endViewParent = (TabLayout) tab_list.getChildAt(moToIndex);
            if (startViewParent != null && endViewParent != null) {
                final int start = selectIndex * bottom_line_width;
                final int end = moToIndex * bottom_line_width;

                set = new AnimationSet(true);
                TranslateAnimation translate = new TranslateAnimation(
                        0, end - start, 0, 0);
                set.addAnimation(translate);
                set.setDuration(200);
                set.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        bottom_line.clearAnimation();
                        LayoutParams params = (LayoutParams) bottom_line.getLayoutParams();
                        params.leftMargin = end;
                        bottom_line.setLayoutParams(params);
                        ((TabLayout) tab_list.getChildAt(moToIndex)).setTextenable(true);
                        set = null;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                bottom_line.startAnimation(set);
            }
        }
    }

    public int getSelectIndex() {
        return mSelectIndex;
    }


    public interface OnItemClickListener {
        void onCallback(int index);
    }


}
