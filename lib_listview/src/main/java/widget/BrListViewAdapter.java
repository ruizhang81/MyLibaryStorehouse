package widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * Created by neo on 16/2/24.
 */
public abstract class BrListViewAdapter {

    protected Context mContext;
    private LayoutInflater mLayoutInflater;
    private RecyclerView.Adapter<MyViewHolder> adapter;
    private OnItemClickListener mListener;
    private int mRes;

    public BrListViewAdapter(Context context, int res) {
        mContext = context;
        mRes = res;
        mLayoutInflater = LayoutInflater.from(mContext);
        adapter = new RecyclerView.Adapter<MyViewHolder>() {
            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = mLayoutInflater.inflate(mRes, parent, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null)
                            mListener.onItemClick(null, null, (int) v.getTag(), 0);//为了不改变原来的代码
                    }
                });
                return new MyViewHolder(view);
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, int position) {
                getView(position, holder.itemView, null);
                holder.itemView.setTag(position);
            }

            @Override
            public int getItemCount() {
                return getCount();
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener Listener) {
        mListener = Listener;
    }

    public RecyclerView.Adapter<MyViewHolder> getAdapter() {
        return adapter;
    }

    public void notifyDataSetChanged() {
        if (null != adapter)
            adapter.notifyDataSetChanged();
    }

    public abstract long getItemId(int position);

    public abstract int getCount();

    public abstract Object getItem(int position);

    public abstract View getView(int position, View convertView, ViewGroup parent);

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
