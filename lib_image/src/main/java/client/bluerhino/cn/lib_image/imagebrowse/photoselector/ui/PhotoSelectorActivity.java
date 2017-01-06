package client.bluerhino.cn.lib_image.imagebrowse.photoselector.ui;
/**
 * @author Aizaz AZ
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import client.bluerhino.cn.lib_image.R;
import client.bluerhino.cn.lib_image.imagebrowse.photoselector.domain.PhotoSelectorDomain;
import client.bluerhino.cn.lib_image.imagebrowse.photoselector.model.AlbumModel;
import client.bluerhino.cn.lib_image.imagebrowse.photoselector.model.PhotoModel;
import client.bluerhino.cn.lib_image.imagebrowse.photoselector.ui.PhotoItem.onItemClickListener;
import client.bluerhino.cn.lib_image.imagebrowse.photoselector.ui.PhotoItem.onPhotoItemCheckedListener;
import client.bluerhino.cn.lib_image.imagebrowse.photoselector.util.AnimationUtil;
import client.bluerhino.cn.lib_image.imagebrowse.photoselector.util.CommonUtils;
import client.bluerhino.cn.lib_image.imageutil.ImageUtil;

/**
 * @author Aizaz AZ
 */
public class PhotoSelectorActivity extends Activity implements
        onItemClickListener, onPhotoItemCheckedListener, OnItemClickListener,
        OnClickListener {

    public final static String pic_type = "pic_type";
    public final static String pic_index = "pic_index";
    public static final int SINGLE_IMAGE = 1;
    public static final String KEY_MAX = "key_max";
    public static final int REQUEST_PHOTO = 0;
    private static final int REQUEST_CAMERA = 1;
    public static String RECCENT_PHOTO = null;
    private int MAX_IMAGE;
    private GridView gvPhotos;
    private ListView lvAblum;
    private Button btnOk;
    private TextView tvAlbum, tvPreview, tvTitle;
    private PhotoSelectorDomain photoSelectorDomain;
    private PhotoSelectorAdapter photoAdapter;
    private AlbumAdapter albumAdapter;
    private RelativeLayout layoutAlbum;
    private ArrayList<PhotoModel> selected;
    private boolean stopSelectPic;
    private OnLocalAlbumListener albumListener = new OnLocalAlbumListener() {
        @Override
        public void onAlbumLoaded(List<AlbumModel> albums) {
            albumAdapter.update(albums);
        }
    };
    private OnLocalReccentListener reccentListener = new OnLocalReccentListener() {
        @Override
        public void onPhotoLoaded(List<PhotoModel> photos) {
            for (PhotoModel model : photos) {
                if (selected.contains(model)) {
                    model.setChecked(true);
                }
            }
            photos.add(0, new PhotoModel());
            photoAdapter.update(photos);
            gvPhotos.smoothScrollToPosition(0); // 滚动到顶端
            // reset(); //--keep selected photos

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RECCENT_PHOTO = getResources().getString(R.string.recent_photos);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.activity_photoselector);

        if (getIntent().getExtras() != null) {
            MAX_IMAGE = getIntent().getIntExtra(KEY_MAX, 1);
        }


        photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());

        selected = new ArrayList<PhotoModel>();

        tvTitle = (TextView) findViewById(R.id.tv_title_lh);
        gvPhotos = (GridView) findViewById(R.id.gv_photos_ar);
        lvAblum = (ListView) findViewById(R.id.lv_ablum_ar);
        btnOk = (Button) findViewById(R.id.btn_right_lh);
        tvAlbum = (TextView) findViewById(R.id.tv_album_ar);
        tvPreview = (TextView) findViewById(R.id.tv_preview_ar);
        layoutAlbum = (RelativeLayout) findViewById(R.id.layout_album_ar);

        btnOk.setOnClickListener(this);
        tvAlbum.setOnClickListener(this);
        tvPreview.setOnClickListener(this);

        photoAdapter = new PhotoSelectorAdapter(getApplicationContext(),
                new ArrayList<PhotoModel>(), CommonUtils.getWidthPixels(this),
                this, this, this);
        gvPhotos.setAdapter(photoAdapter);

        albumAdapter = new AlbumAdapter(getApplicationContext(),
                new ArrayList<AlbumModel>());
        lvAblum.setAdapter(albumAdapter);
        lvAblum.setOnItemClickListener(this);

        findViewById(R.id.bv_back_lh).setOnClickListener(this); // 返回

        photoSelectorDomain.getReccent(reccentListener); // 更新最近照片
        photoSelectorDomain.updateAlbum(albumListener); // 跟新相册信息

        stopSelectPic = false;
        gvPhotos.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                stopSelectPic = scrollState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_right_lh) {
            ok(); // 选完照片
            return;
        } else if (v.getId() == R.id.tv_album_ar) {
            album();
            return;
        } else if (v.getId() == R.id.tv_preview_ar) {
            priview();
            return;
        } else if (v.getId() == R.id.tv_camera_vc) {
            catchPicture();
            return;
        } else if (v.getId() == R.id.bv_back_lh) {
            finish();
            return;
        }
    }

    private void takePhoto() {
        File cameraOutputImage = ImageUtil.GetPic.getFile(this, ImageUtil.GetPic.cameraImageName, false);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        intent.putExtra(MediaStore.Images.Media.ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraOutputImage));
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * 拍照
     */
    private void catchPicture() {
        if (CameraUtil.check(this)) {
            takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (CameraUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            takePhoto();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {

            selected.clear();
            ArrayList<PhotoModel> items = photoAdapter.getItems();
            for (PhotoModel item : items) {
                item.setChecked(false);
            }
            photoAdapter.update(items);
            photoSelectorDomain.getReccent(reccentListener); // 更新最近照片

            File cameraOutputImage = ImageUtil.GetPic.getFile(this, ImageUtil.GetPic.cameraImageName, false);
            PhotoModel photoModel = new PhotoModel(cameraOutputImage.getAbsolutePath());
            if (selected.size() > MAX_IMAGE) {
                Toast.makeText(
                        this,
                        String.format(
                                getString(R.string.max_img_limit_reached),
                                MAX_IMAGE), Toast.LENGTH_SHORT).show();
                photoModel.setChecked(false);
                photoAdapter.notifyDataSetChanged();
            } else {
                if (!selected.contains(photoModel)) {
                    selected.add(photoModel);
                }
            }
            ok();
        }
    }

    /**
     * 完成
     */
    private void ok() {
        if (selected != null && selected.size() > MAX_IMAGE) {
            Toast.makeText(
                    this,
                    String.format(
                            getString(R.string.max_img_limit_reached),
                            MAX_IMAGE), Toast.LENGTH_SHORT).show();
            return;
        }
        if (selected.isEmpty()) {
            setResult(RESULT_CANCELED);
        } else {
            Intent data = new Intent();
            data.putParcelableArrayListExtra("photos", selected);
            data.putExtra(pic_type,getIntent().getIntExtra(pic_type,-1));
            data.putExtra(pic_index,getIntent().getIntExtra(pic_index,-1));
            setResult(RESULT_OK, data);
        }
        finish();
    }

    /**
     * 预览照片
     */
    private void priview() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("photos", selected);
        CommonUtils.launchActivity(this, PhotoPreviewActivity.class, bundle);
    }

    private void album() {
        if (layoutAlbum.getVisibility() == View.GONE) {
            popAlbum();
        } else {
            hideAlbum();
        }
    }

    /**
     * 弹出相册列表
     */
    private void popAlbum() {
        layoutAlbum.setVisibility(View.VISIBLE);
        new AnimationUtil(getApplicationContext(), R.anim.translate_up_current)
                .setLinearInterpolator().startAnimation(layoutAlbum);
    }

    /**
     * 隐藏相册列表
     */
    private void hideAlbum() {
        new AnimationUtil(getApplicationContext(), R.anim.translate_down)
                .setLinearInterpolator().startAnimation(layoutAlbum);
        layoutAlbum.setVisibility(View.GONE);
    }

    /**
     * 清空选中的图片
     */
    private void reset() {
        selected.clear();
        btnOk.setText(R.string.sure);
        tvPreview.setEnabled(false);
    }

    @Override
    /** 点击查看照片 */
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        if (tvAlbum.getText().toString().equals(RECCENT_PHOTO))
            bundle.putInt("position", position - 1);
        else
            bundle.putInt("position", position);
        bundle.putString("album", tvAlbum.getText().toString());
        CommonUtils.launchActivity(this, PhotoPreviewActivity.class, bundle);
    }

    @Override
    /** 照片选中状态改变之后 */
    public boolean onCheckedChanged(PhotoModel photoModel,
                                    PhotoItem photoItem, boolean isChecked) {
        if (stopSelectPic) {
            return false;
        }
        if (isChecked) {
            if (selected.size() >= MAX_IMAGE) {
                selected.remove(photoModel);
                return false;
            }
            if (!selected.contains(photoModel)) {
                selected.add(photoModel);
            }
            tvPreview.setEnabled(true);
        } else {
            selected.remove(photoModel);
        }

        btnOk.setText(getResources().getString(R.string.sure) + "(" + selected.size() + "/" + MAX_IMAGE + ")");

        if (selected.isEmpty()) {
            tvPreview.setEnabled(false);
            tvPreview.setText(getString(R.string.preview));
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (layoutAlbum.getVisibility() == View.VISIBLE) {
            hideAlbum();
        } else
            super.onBackPressed();
    }

    @Override
    /** 相册列表点击事件 */
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        AlbumModel current = (AlbumModel) parent.getItemAtPosition(position);
        for (int i = 0; i < parent.getCount(); i++) {
            AlbumModel album = (AlbumModel) parent.getItemAtPosition(i);
            if (i == position)
                album.setCheck(true);
            else
                album.setCheck(false);
        }
        albumAdapter.notifyDataSetChanged();
        hideAlbum();
        tvAlbum.setText(current.getName());
        // tvTitle.setText(current.getName());

        // 更新照片列表
        if (current.getName().equals(RECCENT_PHOTO))
            photoSelectorDomain.getReccent(reccentListener);
        else
            photoSelectorDomain.getAlbum(current.getName(), reccentListener); // 获取选中相册的照片
    }

    /**
     * 获取本地图库照片回调
     */
    public interface OnLocalReccentListener {
        void onPhotoLoaded(List<PhotoModel> photos);
    }

    /**
     * 获取本地相册信息回调
     */
    public interface OnLocalAlbumListener {
        void onAlbumLoaded(List<AlbumModel> albums);
    }
}
