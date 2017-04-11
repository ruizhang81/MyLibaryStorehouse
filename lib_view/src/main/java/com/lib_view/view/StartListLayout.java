package com.lib_view.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lib_view.R;


/**
 * Created by zhangrui on 17/1/6.
 */

public class StartListLayout extends LinearLayout {

    private int mIndex;

    public StartListLayout(Context context) {
        super(context);
        init();
    }

    public StartListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StartListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StartListLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
    }

    public void setLevel(int max, int level) {
        removeAllViews();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        for (int i = 0; i < max; i++) {
            ImageView image = new ImageView(getContext());
            setImageSelected(image, i > level - 1);
            addView(image, params);
        }

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final int index = i;
            getChildAt(index).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectIndex(index);
                }
            });
        }
    }

    public int getLevel() {
        return mIndex;
    }

    private void setImageSelected(ImageView imageview, boolean bool) {
        if (bool) {
            imageview.setImageResource(R.drawable.star);
        } else {
            imageview.setImageResource(R.drawable.star_press);
        }
    }

    private void selectIndex(int index) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            setImageSelected((ImageView) getChildAt(i), i > index);
        }
        mIndex = index;
    }


}
