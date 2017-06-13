package com.lib_view.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib_view.R;


/**
 * Created by zhangrui on 17/1/6.
 */

public class TabLayout extends LinearLayout {

    public TabLayout(Context context) {
        super(context);
        init();
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_tab_item, this);
    }

    public void setText(String displayStr) {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(displayStr);
    }

    public void setImage(int res) {
        if(res != 0){
            ImageView image = (ImageView) findViewById(R.id.image);
            image.setVisibility(VISIBLE);

            image.setImageResource(res);
        }
    }

    public void setTextenable(boolean enable) {
        TextView title = (TextView) findViewById(R.id.title);
        if (enable) {
            title.setTextColor(ContextCompat.getColor(getContext(), R.color.text_red));
        } else {
            title.setTextColor(ContextCompat.getColor(getContext(), R.color.text_sub));
        }
    }
}
