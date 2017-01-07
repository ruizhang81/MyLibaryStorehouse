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
            R.drawable.timg0,
            R.drawable.timg1,
            R.drawable.timg2,
            R.drawable.timg3,
            R.drawable.timg4,
            R.drawable.timg5,
            R.drawable.timg6,
    };
    private int index;
    private int lastDistance;
    private boolean isLoop;
    private final static long STEP = 50;//下拉步进
    private final static long SPACE = 500;//播放的时候间隔
    private final static byte LOOP = 0x01;
    private final static byte ADD = 0x02;
    private final static byte BACK = 0x03;
    private final static byte HEAD = 0x10;
    private final static byte FOOT = 0x20;
    private BrListView.OnStartListener mRefreshHeadListener;
    private BrListView.OnStartListener mRefreshFootListener;
    private ImageView footImageView;
    private ImageView headImageView;
    private View mEmptyView;
    private boolean hidenEmpty;

    public SubSwipeRefreshLayout(Context context) {
        super(context, null);
    }

    public SubSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeader();
        initFooter();
    }

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