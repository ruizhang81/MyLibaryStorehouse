package client.bluerhino.cn.lib_image.imagebrowse.photoselector.ui;
/**
 * @author Aizaz AZ
 */

import android.os.Bundle;

import java.util.List;

import client.bluerhino.cn.lib_image.imagebrowse.photoselector.domain.PhotoSelectorDomain;
import client.bluerhino.cn.lib_image.imagebrowse.photoselector.model.PhotoModel;
import client.bluerhino.cn.lib_image.imagebrowse.photoselector.ui.PhotoSelectorActivity.OnLocalReccentListener;
import client.bluerhino.cn.lib_image.imagebrowse.photoselector.util.CommonUtils;

public class PhotoPreviewActivity extends BasePhotoPreviewActivity implements OnLocalReccentListener {

    private PhotoSelectorDomain photoSelectorDomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());

        init(getIntent().getExtras());
    }

    @SuppressWarnings("unchecked")
    protected void init(Bundle extras) {
        if (extras == null)
            return;

        if (extras.containsKey("photos")) { // 预览图片
            photos = (List<PhotoModel>) extras.getSerializable("photos");
            current = extras.getInt("position", 0);
            updatePercent();
            bindData();
        } else if (extras.containsKey("album")) { // 点击图片查看
            String albumName = extras.getString("album"); // 相册
            this.current = extras.getInt("position");
            if (!CommonUtils.isNull(albumName) && albumName.equals(PhotoSelectorActivity.RECCENT_PHOTO)) {
                photoSelectorDomain.getReccent(this);
            } else {
                photoSelectorDomain.getAlbum(albumName, this);
            }
        }
    }

    @Override
    public void onPhotoLoaded(List<PhotoModel> photos) {
        this.photos = photos;
        updatePercent();
        bindData(); // 更新界面
    }

}
