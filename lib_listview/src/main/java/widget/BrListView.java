package widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.core.lib.lib_listview.R;

import swipe.SubSwipeRefreshLayout;

/**
 * Created by neo on 16/2/24.
 */
public class BrListView extends LinearLayout implements IListView {

    private RecyclerView recyclerView;
    private SubSwipeRefreshLayout swipeLayout;
    private Context mContext;
    private RecyclerView.LayoutManager layoutManager;
    private int mDividerColor;
    private float mDividerHeight;


    public BrListView(Context context) {
        this(context, null, 0);
    }

    public BrListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BrListView);
        mDividerColor = ta.getColor(R.styleable.BrListView_android_divider, -1);
        mDividerHeight = ta.getDimension(R.styleable.BrListView_android_dividerHeight, 0.1f);
        ta.recycle();
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View.inflate(context, R.layout.listview_swipe, this);
        swipeLayout = (SubSwipeRefreshLayout) findViewById(R.id.swipeLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL, mDividerColor, mDividerHeight));
        setNeedLoadMore(false);
    }


    @Override
    public void setDividerHeight(int height) {
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL, mDividerColor, height));
    }

    @Override
    public void setAdapter(BrListViewAdapter mAdapter) {
        final RecyclerView.Adapter adapter = mAdapter.getAdapter();
        if (adapter != null) {

            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    dataChange(adapter);
                }
            });
        }
        recyclerView.setAdapter(adapter);
        dataChange(adapter);
    }

    private void dataChange(RecyclerView.Adapter adapter) {
        //数据改变时回调
        if (adapter != null) {
            //如果没数据
            if (adapter.getItemCount() == 0) {
                //显示mEmptyView，隐藏自身
                swipeLayout.showEmptyView(true);
            } else {
                //显示自身，隐藏mEmptyView
                swipeLayout.showEmptyView(false);
            }
        }
    }

    @Override
    public void setEmptyView(View emptyView) {
        swipeLayout.setEmptyView(emptyView);
    }

    @Override
    public void setOnRefreshStartListener(final OnStartListener onStartListener) {
        swipeLayout.setRefreshHeadListener(onStartListener);
    }

    @Override
    public void setOnLoadMoreStartListener(final OnStartListener onStartListener) {
        setNeedLoadMore(true);
        swipeLayout.setRefreshFootListener(onStartListener);
    }


    @Override
    public void setRefreshSuccess() {
        swipeLayout.setRefreshSuccess();
        if (recyclerView != null) {
            dataChange(recyclerView.getAdapter());
        }
    }

    /**
     * 是否需要加载更多功能，默认true
     *
     * @param needLoadMore
     */
    public void setNeedLoadMore(boolean needLoadMore) {
        swipeLayout.setNeedLoadMore(needLoadMore);
    }


    @Override
    public void refresh() {
        swipeLayout.refresh();
    }


    @Override
    public void setSelection(int position) {
        ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(position, 0);
    }


    public interface OnStartListener {
        void onStart();
    }


}
