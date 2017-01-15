package com.lib_view.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {


    private onSizeChangedListener mOnOnSizeChangedListener;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mOnOnSizeChangedListener != null) {
            mOnOnSizeChangedListener.onSizeChanged();
        }
    }


    public void setmOnOnSizeChangedListener(onSizeChangedListener mOnOnSizeChangedListener) {
        this.mOnOnSizeChangedListener = mOnOnSizeChangedListener;
    }

    public interface onSizeChangedListener {
        void onSizeChanged();
    }

}