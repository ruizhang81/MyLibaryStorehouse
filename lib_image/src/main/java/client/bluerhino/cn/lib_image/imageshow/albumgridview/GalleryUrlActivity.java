package client.bluerhino.cn.lib_image.imageshow.albumgridview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import java.util.ArrayList;

import client.bluerhino.cn.lib_image.R;
import client.bluerhino.cn.lib_image.imageshow.gallerywidget.BasePagerAdapter;
import client.bluerhino.cn.lib_image.imageshow.gallerywidget.GalleryViewPager;
import client.bluerhino.cn.lib_image.imageshow.gallerywidget.UrlPagerAdapter;


public class GalleryUrlActivity extends Activity {
    public static final String IMAGE_URLS = "image_urls";
    public static final String CURRENT_ITEM = "current_item";
    private GalleryViewPager mViewPager;
    private ArrayList<String> mLargeImageUrls;
    private int mCurrentItem;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


        Intent intent = getIntent();
        mLargeImageUrls = intent.getStringArrayListExtra(IMAGE_URLS);
        mCurrentItem = intent.getIntExtra(CURRENT_ITEM, 0);
        initView();
    }

    private void initView() {
        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this,
                mLargeImageUrls);
        pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
            @Override
            public void onItemChange(int currentPosition) {
                // Toast.makeText(GalleryUrlActivity.this,
                // "Current item is " + currentPosition,
                // Toast.LENGTH_SHORT).show();
            }
        });

        mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(mCurrentItem);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            finish();
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}