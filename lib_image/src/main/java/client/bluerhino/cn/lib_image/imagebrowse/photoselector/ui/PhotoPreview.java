package client.bluerhino.cn.lib_image.imagebrowse.photoselector.ui;
/**
 * @author Aizaz AZ
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.request.target.BitmapImageViewTarget;

import client.bluerhino.cn.lib_image.R;
import client.bluerhino.cn.lib_image.imagebrowse.photoselector.model.PhotoModel;
import client.bluerhino.cn.lib_image.imagebrowse.polites.GestureImageView;
import client.bluerhino.cn.lib_image.imageutil.ImageLoad;

public class PhotoPreview extends LinearLayout implements OnClickListener {

    private ProgressBar pbLoading;
    private GestureImageView ivContent;
    private OnClickListener l;

    public PhotoPreview(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_photopreview, this, true);

        pbLoading = (ProgressBar) findViewById(R.id.pb_loading_vpp);
        ivContent = (GestureImageView) findViewById(R.id.iv_content_vpp);
        ivContent.setOnClickListener(this);
    }

    public PhotoPreview(Context context, AttributeSet attrs, int defStyle) {
        this(context);
    }

    public PhotoPreview(Context context, AttributeSet attrs) {
        this(context);
    }

    public void loadImage(PhotoModel photoModel) {
        loadImage("file://" + photoModel.getOriginalPath());
    }

    private void loadImage(String path) {

        ImageLoad.loadLocalImage(getContext(), path, new BitmapImageViewTarget(ivContent) {
            @Override
            protected void setResource(Bitmap resource) {
                ivContent.setImageBitmap(resource);
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                ivContent.setImageDrawable(getResources().getDrawable(R.drawable.ic_picture_loadfailed));
                pbLoading.setVisibility(View.GONE);
            }


        });

    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.l = l;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_content_vpp && l != null)
            l.onClick(ivContent);
    }

}
