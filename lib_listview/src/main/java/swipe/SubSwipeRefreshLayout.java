package swipe;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.core.lib.lib_listview.R;

import widget.BrListView;

/**
 * Created by xingliuhua on 2016/8/5 0005.
 */
public class SubSwipeRefreshLayout extends BaseSwipeRefreshLayout {


    //动画
    private final static int[] resId = {
            R.drawable.time163,
            R.drawable.time162,
            R.drawable.time161,
            R.drawable.time160,
            R.drawable.time159,
            R.drawable.time158,
            R.drawable.time157,
            R.drawable.time156,
            R.drawable.time155,
            R.drawable.time154,
            R.drawable.time153,
            R.drawable.time152,
            R.drawable.time151,
            R.drawable.time150,
            R.drawable.time149,
            R.drawable.time148,
            R.drawable.time147,
            R.drawable.time146,
            R.drawable.time145,
            R.drawable.time144,
            R.drawable.time143,
            R.drawable.time142,
            R.drawable.time141,
            R.drawable.time140,
            R.drawable.time139,
            R.drawable.time138,
            R.drawable.time137,
            R.drawable.time136,
            R.drawable.time135,
            R.drawable.time134,
            R.drawable.time133,
            R.drawable.time132,
            R.drawable.time131,
            R.drawable.time130,
            R.drawable.time129,
            R.drawable.time128,
            R.drawable.time127,
            R.drawable.time126,
            R.drawable.time125,
            R.drawable.time124,
            R.drawable.time123,
            R.drawable.time122,
            R.drawable.time121,
            R.drawable.time120,
            R.drawable.time119,
            R.drawable.time118,
            R.drawable.time117,
            R.drawable.time116,
            R.drawable.time115,
            R.drawable.time114,
            R.drawable.time113,
            R.drawable.time112,
            R.drawable.time111,
            R.drawable.time110,
            R.drawable.time109,
            R.drawable.time108,
            R.drawable.time107,
            R.drawable.time106,
            R.drawable.time105,
            R.drawable.time104,
            R.drawable.time103,
            R.drawable.time102,
            R.drawable.time101,
            R.drawable.time100,
            R.drawable.time99,
            R.drawable.time98,
            R.drawable.time97,
            R.drawable.time96,
            R.drawable.time95,
            R.drawable.time94,
            R.drawable.time93,
            R.drawable.time92,
            R.drawable.time91,
            R.drawable.time90,
            R.drawable.time89,
            R.drawable.time88,
            R.drawable.time87,
            R.drawable.time86,
            R.drawable.time85,
            R.drawable.time84,
            R.drawable.time83,
            R.drawable.time82,
            R.drawable.time81,
            R.drawable.time80,
            R.drawable.time79,
            R.drawable.time78,
            R.drawable.time77,
            R.drawable.time76,
            R.drawable.time75,
            R.drawable.time74,
            R.drawable.time73,
            R.drawable.time72,
            R.drawable.time71,
            R.drawable.time70,
            R.drawable.time69,
            R.drawable.time68,
            R.drawable.time67,
            R.drawable.time66,
            R.drawable.time65,
            R.drawable.time64,
            R.drawable.time63,
            R.drawable.time62,
            R.drawable.time61,
            R.drawable.time60,
            R.drawable.time59,
            R.drawable.time58,
            R.drawable.time57,
            R.drawable.time56,
            R.drawable.time55,
            R.drawable.time54,
            R.drawable.time53,
            R.drawable.time52,
            R.drawable.time51,
            R.drawable.time50,
            R.drawable.time49,
            R.drawable.time48,
            R.drawable.time47,
            R.drawable.time46,
            R.drawable.time45,
            R.drawable.time44,
            R.drawable.time43,
            R.drawable.time42,
            R.drawable.time41,
            R.drawable.time40,
            R.drawable.time39,
            R.drawable.time38,
            R.drawable.time37,
            R.drawable.time36,
            R.drawable.time35,
            R.drawable.time34,
            R.drawable.time33,
            R.drawable.time32,
            R.drawable.time31,
            R.drawable.time30,
            R.drawable.time29,
            R.drawable.time28,
            R.drawable.time27,
            R.drawable.time26,
            R.drawable.time25,
            R.drawable.time24,
            R.drawable.time23,
            R.drawable.time22,
            R.drawable.time21,
            R.drawable.time20,
            R.drawable.time19,
            R.drawable.time18,
            R.drawable.time17,
            R.drawable.time16,
            R.drawable.time15,
            R.drawable.time14,
            R.drawable.time13,
            R.drawable.time12,
            R.drawable.time11,
            R.drawable.time10,
            R.drawable.time9,
            R.drawable.time8,
            R.drawable.time7,
            R.drawable.time6,
            R.drawable.time5,
            R.drawable.time4,
            R.drawable.time3,
            R.drawable.time2,
            R.drawable.time1
    };
    private final static long STEP = 2;//下拉步进
    private final static long SPACE = 20;//播放的时候间隔
    private final static byte LOOP = 0x01;
    private final static byte ADD = 0x02;
    private final static byte BACK = 0x03;
    private final static byte HEAD = 0x10;
    private final static byte FOOT = 0x20;
    private int index;
    private int lastDistance;
    private boolean isLoop;
    private BrListView.OnStartListener mRefreshHeadListener;
    private BrListView.OnStartListener mRefreshFootListener;
    private ImageView footImageView;
    private ImageView headImageView;
    private View mEmptyView;
    private boolean hidenEmpty;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            removeMessages(FOOT | LOOP);
            removeMessages(FOOT | ADD);
            removeMessages(FOOT | BACK);
            removeMessages(HEAD | LOOP);
            removeMessages(HEAD | ADD);
            removeMessages(HEAD | BACK);

            int length = resId.length;
            int status = msg.what & 0x0f;
            int headOrFoot = msg.what & 0xf0;

//            Log.e("xxxx","status="+status+" headOrFoot="+headOrFoot);
            if (status == ADD || status == LOOP) {
                index++;
            } else if (status == BACK) {
                index--;
            }

            if (index >= length) {
                index = 0;
            } else if (index < 0) {
                index = length - 1;
            }

            if (headOrFoot == HEAD) {
                if (headImageView != null) {
                    headImageView.setImageResource(resId[index]);
                }
            } else if (headOrFoot == FOOT) {
                if (footImageView != null) {
                    footImageView.setImageResource(resId[index]);
                }
            }
            if (status == LOOP) {
                //循环
                sendEmptyMessageDelayed(msg.what, SPACE);
            }
        }
    };

    public SubSwipeRefreshLayout(Context context) {
        super(context, null);
    }

    public SubSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeader();
        initFooter();
    }

    private void initHeader() {
        View headerView = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_head, null);
        headImageView = (ImageView) headerView.findViewById(R.id.image_view);
        setHeaderView(headerView);
        setTargetScrollWithLayout(true);
        setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                loadRefreshHeadData();
            }

            @Override
            public void onPullEnable(boolean enable) {
            }

            @Override
            public void onPullDistance(int distance) {
                if (!isLoop) {
                    if (distance - lastDistance > STEP) {
                        lastDistance = distance;
                        handler.sendEmptyMessage(HEAD | ADD);
                    } else if (lastDistance - distance > STEP) {
                        lastDistance = distance;
                        handler.sendEmptyMessage(HEAD | BACK);
                    }
                }
            }
        });
    }

    private void initFooter() {
        View footerView = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_head, null);
        footImageView = (ImageView) footerView.findViewById(R.id.image_view);
        setFooterView(footerView);
        setTargetScrollWithLayout(true);

        setOnPushLoadMoreListener(new BaseSwipeRefreshLayout.OnPushLoadMoreListener() {

            @Override
            public void onLoadMore() {
                loadRefreshFootData();
            }

            @Override
            public void onPushEnable(boolean enable) {
            }

            @Override
            public void onPushDistance(int distance) {
                if (!isLoop) {
                    if (distance - lastDistance > STEP) {
                        lastDistance = distance;
                        handler.sendEmptyMessage(FOOT | ADD);
                    } else if (lastDistance - distance > STEP) {
                        lastDistance = distance;
                        handler.sendEmptyMessage(FOOT | BACK);
                    }
                }
            }

        });
    }


    private void loadRefreshHeadData() {
        if (mRefreshHeadListener != null) {
            lastDistance = 0;
            isLoop = true;
            handler.sendEmptyMessage(HEAD | LOOP);
            mRefreshHeadListener.onStart();
        }
    }

    private void loadRefreshFootData() {
        if (mRefreshFootListener != null) {
            lastDistance = 0;
            isLoop = true;
            handler.sendEmptyMessage(FOOT | LOOP);
            mRefreshFootListener.onStart();
        }
    }

    public void refresh() {
        if (hidenEmpty) {
            hidenEmpty = false;
        }
        post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
            }
        });
        loadRefreshHeadData();

    }

    public void setRefreshHeadListener(BrListView.OnStartListener refreshHeadListener) {
        mRefreshHeadListener = refreshHeadListener;
    }

    public void setRefreshFootListener(BrListView.OnStartListener refreshFootListener) {
        mRefreshFootListener = refreshFootListener;
    }

    public void setRefreshSuccess() {
        post(new Runnable() {
            @Override
            public void run() {
                if (handler != null) {
                    handler.removeMessages(FOOT | LOOP);
                    handler.removeMessages(FOOT | ADD);
                    handler.removeMessages(FOOT | BACK);
                    handler.removeMessages(HEAD | LOOP);
                    handler.removeMessages(HEAD | ADD);
                    handler.removeMessages(HEAD | BACK);
                }
                lastDistance = 0;
                isLoop = false;
                setRefreshing(false);
                setLoadMore(false);
            }
        });
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mEmptyView != null) {
            mEmptyView.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mEmptyView != null) {
            mEmptyView.layout(left, top, right, bottom);
        }
    }

    public void setEmptyView(View emptyView) {
        if (emptyView == null) {
            return;
        }
        if (this.mEmptyView != null) {
            removeView(mEmptyView);
        }
        this.mEmptyView = emptyView;
        addView(this.mEmptyView, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.mEmptyView.setVisibility(GONE);
        requestLayout();
        hidenEmpty = true;
    }


    public void showEmptyView(boolean showEmptyView) {
        if (mEmptyView != null && !hidenEmpty) {
            mEmptyView.setVisibility(showEmptyView ? View.VISIBLE : View.GONE);
        }
    }
}


