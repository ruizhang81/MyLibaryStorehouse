package client.bluerhino.cn.lib_image.imageshow.albumgridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import client.bluerhino.cn.lib_image.R;
import client.bluerhino.cn.lib_image.imageutil.ImageLoad;

/**
 * @author LynnChurch
 */
public class AlbumGridAdapter extends BaseAdapter {

    private ArrayList<String> mThumbnailUrls = new ArrayList<String>();
    private Context mContext;
    private ViewHolder mHolder;

    public AlbumGridAdapter(Context context) {
        mContext = context;

    }

    @Override
    public int getCount() {
        return mThumbnailUrls.size();
    }

    @Override
    public String getItem(int position) {
        return mThumbnailUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.image,
                    null);
            mHolder = new ViewHolder();
            mHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.image);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        String url = getItem(position);
        ImageLoad.load(mContext, mHolder.imageView, url);
        return convertView;
    }


    /**
     * 添加缩略图并显示
     *
     * @param thumbnailUrls
     */
    public void addThumbnails(ArrayList<String> thumbnailUrls) {
        mThumbnailUrls.addAll(thumbnailUrls);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
