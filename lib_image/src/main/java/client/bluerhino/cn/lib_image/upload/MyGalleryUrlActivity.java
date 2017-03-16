package client.bluerhino.cn.lib_image.upload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;


import java.util.ArrayList;

import client.bluerhino.cn.lib_image.R;
import client.bluerhino.cn.lib_image.imageshow.gallerywidget.BasePagerAdapter;
import client.bluerhino.cn.lib_image.imageshow.gallerywidget.GalleryViewPager;
import client.bluerhino.cn.lib_image.imageshow.gallerywidget.UrlPagerAdapter;


public class MyGalleryUrlActivity extends Activity {

    public static final String IMAGE_URLS = "image_urls";
    public static final String CURRENT_ITEM = "current_item";
    private GalleryViewPager mViewPager;
    private ArrayList<String> mLargeImageUrls;
    private int mCurrentItem;

    public static void start(Context context, ArrayList<String> mLargeImageUrls) {
        Intent intent = new Intent(context, MyGalleryUrlActivity.class);
        intent.putStringArrayListExtra(IMAGE_URLS, mLargeImageUrls);
        intent.putExtra(CURRENT_ITEM, 0);
        context.startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gallery);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        mLargeImageUrls = intent.getStringArrayListExtra(IMAGE_URLS);
        mCurrentItem = intent.getIntExtra(CURRENT_ITEM, 0);
        initView();
    }

    private void initView() {
        setTitle("查看图片");
//        ImageView right_img = (ImageView) findViewById(R.id.right_img);
//        right_img.setImageResource(R.drawable.icon_back);
//        right_img.setVisibility(View.VISIBLE);
//        right_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setResult(Activity.RESULT_OK);
//                finish();
//            }
//        });
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