package client.bluerhino.cn.lib_image.imageshow.albumgridview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * @author LynnChurch
 */
public class AlbumGridView extends GridView {
    private ArrayList<String> mLargeImageUrls = new ArrayList<String>();
    private AlbumGridAdapter mAdapter;

    public AlbumGridView(Context context) {
        this(context, null, 0);
    }

    public AlbumGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlbumGridView(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mAdapter = new AlbumGridAdapter(context);
        setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(activity, GalleryUrlActivity.class);
                intent.putExtra(GalleryUrlActivity.IMAGE_URLS, mLargeImageUrls);
                intent.putExtra(GalleryUrlActivity.CURRENT_ITEM, position);
                activity.startActivity(intent);
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
        setAdapter(mAdapter);
    }

    /**
     * 添加缩略图并显示
     *
     * @param thumbnailUrls
     */
    public void addThumbnails(ArrayList<String> thumbnailUrls) {
        mAdapter.addThumbnails(thumbnailUrls);
    }

    public void addLargeImages(ArrayList<String> largeImages) {
        mLargeImageUrls.addAll(largeImages);
    }


}
