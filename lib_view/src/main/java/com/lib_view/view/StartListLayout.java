package com.lib_view.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lib_view.R;


/**
 * Created by zhangrui on 17/1/6.
 */

public class StartListLayout extends LinearLayout {


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
        ImageView image;
        for (int i = 0; i < max; i++) {
            image = new ImageView(getContext());
            if (i < level - 1) {
                image.setImageResource(R.drawable.star_press);
            } else {
                image.setImageResource(R.drawable.star);
            }
            addView(image, params);
        }
    }


}
