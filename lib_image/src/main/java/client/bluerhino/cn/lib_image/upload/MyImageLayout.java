package client.bluerhino.cn.lib_image.upload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import client.bluerhino.cn.lib_image.R;
import client.bluerhino.cn.lib_image.imagebrowse.photoselector.ui.PhotoSelectorActivity;
import client.bluerhino.cn.lib_image.imageutil.ImageLoad;
import client.bluerhino.cn.lib_image.imageutil.ImageUtil;
import client.bluerhino.cn.lib_image.upload.bean.FileRequestBody;
import client.bluerhino.cn.lib_image.upload.bean.ImageInfo;
import client.bluerhino.cn.lib_image.upload.bean.ImageResult;
import client.bluerhino.cn.lib_image.upload.bean.ResponseBean;
import client.bluerhino.cn.lib_image.upload.bean.UploadImageStatus;
import client.bluerhino.cn.lib_image.upload.http.HttpAction;
import client.bluerhino.cn.lib_image.upload.http.RetrofitCallback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by zhangrui on 17/1/4.
 */

public class MyImageLayout extends RelativeLayout {

    private long clickTime;
    private NewClickLinstener mListener;
    private OnDelClickLinstener mDelListener;
    private OnUploadSuccessListener onUploadSuccessListener;
    private ImageProgressView pic;
    private TextView name;
    private ImageView del;
    public ImageInfo mImageInfo;


    public interface NewClickLinstener {
        void onClickLinstener();
    }
    public interface OnDelClickLinstener {
        void onDelClickLinstener(ImageInfo mImageInfo);
    }
    public interface OnUploadSuccessListener {
        void onUploadSuccess();
    }

    public void setNewClickLinstener(NewClickLinstener listener) {
        mListener = listener;
    }
    public void setDelClickLinstener(OnDelClickLinstener listener) {
        mDelListener = listener;
    }
    public void setOnUploadSuccessListener(OnUploadSuccessListener listener) {
        onUploadSuccessListener = listener;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            uploadUpdate(UploadImageStatus.upload_working, msg.arg1);
        }
    };

    public MyImageLayout(Context context) {
        super(context);
        initView();
    }

    public MyImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyImageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.layout_image, this);
    }

    public void init(final Activity activity, ImageInfo imageInfo) {
        pic = (ImageProgressView) findViewById(R.id.pic);
        name = (TextView) findViewById(R.id.name);
        del = (ImageView) findViewById(R.id.del);
        pic.setImageResource(R.drawable.icon_pic_add);

        mImageInfo = imageInfo;

        name.setText(mImageInfo.name);
        del.setImageResource(R.drawable.icon_image_delete);
        //初始条件下有远程图的情况下
        if (!TextUtils.isEmpty(mImageInfo.url)) {
            del.setVisibility(VISIBLE);

            if (mImageInfo.onlyReplace) {
                //只能替换
                del.setImageResource(R.drawable.icon_image_setting);
            } else {
                //可以删除
                del.setImageResource(R.drawable.icon_image_delete);
            }
        }
        update();

        del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageInfo.onlyReplace) {
                    getLocalPic(activity);
                } else {
                    deleteImage();
                }
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickLinstener();
                } else {
                    if (TextUtils.isEmpty(mImageInfo.url)) {
                        if (TextUtils.isEmpty(mImageInfo.localUrl)) {
                            //本地图和远程图都没，就去取图
                            getLocalPic(activity);
                        } else {
                            if(mImageInfo.status == UploadImageStatus.upload_fail){
                                //远程图没，本地有，而且上传失败，就重新上传
                                upload();
                            }else{
                                //远程图没，本地有，不是上传失败，就查看大图
                                browserBigPic(activity);
                            }
                        }
                    } else {
                        if (TextUtils.isEmpty(mImageInfo.localUrl)) {
                            browserBigPic(activity);
                        } else {
                            browserBigPic(activity);
                        }
                    }
                }
            }
        });
    }


    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, final Intent data) {
        long pic_index = data.getLongExtra(PhotoSelectorActivity.pic_index, -1);
        if (pic_index == clickTime) {//判断点击的是不是自己
            ImageUtil.GetPic.onGetPicByResult(activity, requestCode, resultCode, data, new ImageUtil.GetPic.OnImageGetListener() {
                @Override
                public void onImageGetListener(List<String> path) {
                    if (path != null && path.size() > 0) {
                        mImageInfo.localUrl = path.get(0);
                        mImageInfo.url = "";
                        update();
//                        if (mImageInfo.autoUpload) {
//
//                        }
                        upload();
                    }
                }
            });
            return true;
        }
        return false;
    }

    public void upload() {
        RetrofitCallback<ResponseBean<ImageResult>> callback = new RetrofitCallback<ResponseBean<ImageResult>>() {
            @Override
            public void onFailure(Call<ResponseBean<ImageResult>> call, Throwable t) {
                uploadUpdate(UploadImageStatus.upload_fail, 0);
                t.printStackTrace();
            }

            @Override
            public void onSuccess(Call<ResponseBean<ImageResult>> call, Response<ResponseBean<ImageResult>> response) {
                uploadUpdate(UploadImageStatus.upload_success, 100);
                mImageInfo.url = response.body().data.name;
                if(onUploadSuccessListener!=null){
                    onUploadSuccessListener.onUploadSuccess();
                }
            }

            @Override
            public void onLoading(final int percent, final boolean finish) {
                Message message = Message.obtain();
                message.arg1 = percent;
                handler.sendMessage(message);
            }

        };
        File file = new File(mImageInfo.localUrl);
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
        FileRequestBody body = new FileRequestBody(photoRequestBody, callback);
        MultipartBody.Part part = MultipartBody.Part.createFormData("uploads", file.getName(), body);

        Call<ResponseBean<ImageResult>> call = HttpAction.getRetrofitUploadImage().upload(part);
        call.enqueue(callback);

        uploadUpdate(UploadImageStatus.upload_ready, 0);
    }


    //取本地图
    private void getLocalPic(Activity activity) {
        clickTime = System.nanoTime();
        ImageUtil.GetPic.actionByIndex(activity, clickTime);
    }

    //查看大图
    private void browserBigPic(Activity activity) {
        ArrayList<String> list = new ArrayList<>();
        if (!TextUtils.isEmpty(mImageInfo.localUrl)) {
            list.add(mImageInfo.localUrl);
        } else {
            if (!TextUtils.isEmpty(mImageInfo.url)) {
                list.add(mImageInfo.url);
            }
        }
        if (list.size() > 0) {
            MyGalleryUrlActivity.start(activity, list);
        } else {
            Toast.makeText(activity, "没有可显示的图", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteImage() {
        pic.setImageResource(R.drawable.icon_pic_add);
        pic.uploadImageStatus.progress = 0;
        pic.uploadImageStatus.status = pic.uploadImageStatus.image_normal;
        pic.update();
        mImageInfo.url = "";
        mImageInfo.localUrl = "";
        del.setVisibility(GONE);
        if(mDelListener!=null){
            mDelListener.onDelClickLinstener(mImageInfo);
        }
    }

    private void update() {
        uploadUpdate(mImageInfo.status, mImageInfo.progress);
        if (!TextUtils.isEmpty(mImageInfo.localUrl)) {
            ImageLoad.loadLocalImage(getContext(), pic, "file://" + mImageInfo.localUrl);
            del.setVisibility(VISIBLE);
        } else {
            if (!TextUtils.isEmpty(mImageInfo.url)) {
                ImageLoad.load(getContext(), pic, mImageInfo.url);
                del.setVisibility(VISIBLE);
            }else{
                pic.setImageResource(R.drawable.icon_pic_add);
                pic.uploadImageStatus.progress = 0;
                pic.uploadImageStatus.status = pic.uploadImageStatus.image_normal;
                pic.update();
                mImageInfo.url = "";
                mImageInfo.localUrl = "";
                del.setVisibility(GONE);
            }
        }
        if(mImageInfo.alwaysHidenDel){
            del.setVisibility(GONE);
        }
    }

    private void uploadUpdate(int status, int progress) {
        mImageInfo.status = status;
        mImageInfo.progress = progress;
        pic.uploadImageStatus.status = status;
        pic.uploadImageStatus.progress = progress;
        pic.update();
        if (status == UploadImageStatus.upload_ready ||
                status == UploadImageStatus.upload_working) {
            del.setVisibility(GONE);
        } else {
            del.setVisibility(VISIBLE);
        }
        if(mImageInfo.alwaysHidenDel){
            del.setVisibility(GONE);
        }
        if (mImageInfo.onlyReplace) {
            //只能替换
            del.setImageResource(R.drawable.icon_image_setting);
        } else {
            //可以删除
            del.setImageResource(R.drawable.icon_image_delete);
        }
    }


}
