package client.bluerhino.cn.lib_image.imageutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.InputStream;

import okhttp3.OkHttpClient;


/**
 * Created by ruizhang on 12/22/15.
 */
public class ImageLoad {


    //普通加载图片 -Context
    public static void load(Context context, ImageView ImageView, String url) {
        try {
            init(context);
            Glide.with(context)
                    .load(url)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(ImageView);
        } catch (Exception e) {

        }
    }

    //加载本地图片
    public static void loadLocalImage(Context context, ImageView ImageView, String url) {
        try {
            init(context);
            Glide.with(context)
                    .load(Uri.parse(url))
                    .fitCenter()
                    .into(ImageView);
        } catch (Exception e) {

        }
    }

    //加载本地图片-回调
    public static void loadLocalImage(Context context, String url, BitmapImageViewTarget target) {
        try {
            init(context);
            Glide.with(context)
                    .load(Uri.parse(url))
                    .asBitmap().centerCrop()
                    .into(target);
        } catch (Exception e) {

        }
    }

    //加载图片-回调
    public static void loadImage(final Context context, String url, final OnDownLoadBitmap onDownLoadBitmap) {
        try {
            init(context);
            Glide.with(context).load(url).asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            if (onDownLoadBitmap != null) {
                                onDownLoadBitmap.onDownLoadBitmap(bitmap);
                            }
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            if (onDownLoadBitmap != null) {
                                onDownLoadBitmap.onDownLoadBitmap(null);
                            }
                        }

                    });
        } catch (Exception e) {

        }
    }

    //加载本地GIF图片
    public static void loadGif(Fragment fragment, final ImageView imageView, int drawableId, int loadResId) {
        try {
            init(fragment.getContext());
            Glide.with(fragment)
                    .load(drawableId)
                    .asGif()
                    .placeholder(loadResId)
                    .error(loadResId)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new SimpleTarget<GifDrawable>() {
                        @Override
                        public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
                            if (imageView != null) {
                                imageView.setImageDrawable(resource);
                            }
                            if (resource != null) {
                                try {
                                    resource.setLoopCount(1);
                                    resource.start();
                                } catch (Exception e) {

                                }
                            }
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {

                        }


                    });
        } catch (Exception e) {

        }
    }


    //加载圆形图片
    public static void loadCircle(final Context context, final ImageView imageView, String url) {
        try {
            init(context);
            Glide.with(context).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    if (context != null && imageView != null) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        try {
                            circularBitmapDrawable.setCircular(true);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        } catch (Exception e) {

                        }
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    public static void loadCircleImage(final Context context, final ImageView imageView, String url, int radiusDp) {
        try {
            init(context);
            Glide.with(context).load(url).centerCrop().
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    transform(new GlideRoundTransform(context, radiusDp)).into(imageView);
        } catch (Exception e) {

        }
    }

    private static void init(Context context) {
        Glide.get(context).register(GlideUrl.class, InputStream.class,
                new OkHttpUrlLoader.Factory(new OkHttpClient()));
    }

    public static void clear(Context context, View view) {
        try {
            Glide.clear(view);
            Glide.get(context).clearMemory();
        } catch (Exception e) {

        }
    }

    public interface OnDownLoadBitmap {
        void onDownLoadBitmap(Bitmap bitmap);
    }
}
