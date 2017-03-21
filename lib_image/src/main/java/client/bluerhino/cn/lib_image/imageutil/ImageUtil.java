package client.bluerhino.cn.lib_image.imageutil;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.bluerhino.cn.lib_image.imagebrowse.photoselector.model.PhotoModel;
import client.bluerhino.cn.lib_image.imagebrowse.photoselector.ui.PhotoSelectorActivity;

/**
 * 图片帮助类
 **/
public class ImageUtil {

    private final static String ImageUtilTAG = "ImageUtilTAG";
    private final static float cutSize = 640;
    private final static int compressSize = 2048;

    //尺寸压缩
    public static byte[] compressImage(String path) {
        Bitmap bitmap;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds设为true
        newOpts.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(path, newOpts);
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        // 设置分辨率
        float hh = cutSize;
        float ww = cutSize;
        // 缩放比。由于是固定的比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;
        if (w > h) {// 如果宽度大的话根据宽度固定大小缩放
            if (w > ww) {
                be = (int) (w / ww);
            }
        } else {// 如果宽度大的话根据宽度固定大小缩放
            if (h > hh) {
                be = (int) (h / hh);
            }
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        newOpts.inJustDecodeBounds = false;
        // 重新读入图片，注意此时已经把newOpts.inJustDecodeBounds = false
        bitmap = BitmapFactory.decodeFile(path, newOpts);

        int degree = readPictureDegree(path);
        if (degree != 0) {
            //旋转图片 动作
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            // 创建新的图片
            bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }


        try {
            return compressImage2(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            System.gc();
        }
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    //压缩质量
    private static byte[] compressImage2(Bitmap bitmap) throws Exception {
        int needSize = compressSize;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int options = 100;
            int size = baos.toByteArray().length / 1024;
            while (size > needSize && options > 0) {
                baos.reset();// 重置baos即清空baos
                options -= 10;// 每次都减少10
                // 这里压缩options%，把压缩后的数据存放到baos中
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                size = baos.toByteArray().length / 1024;
            }
            // 把压缩后的数据baos存放到ByteArrayInputStream中
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            throw e;
        }

    }


    //拍照或者单选照片 + 裁剪
    public static class GetPic {

        public final static String cameraImageName = "br_camera.jpg";
        private static final int Camera = 900;
        private static final int Camera_Cut = 901;
        private static final int Gallery = 902;
        private static final int Gallery_Cut = 903;
        private static final int Cut = 905;
        private static final int ManyPicture = 906;
        private final static String imagePath = "br_pics";
        private final static String imagePreCutName = "br_cutPre.jpg";
        private final static String imagefinalName = "br_final.jpg";
        private static String CutFilePath;

        public static File getFile(Context context, String fileName, boolean isFinalImage) {
            String time = System.currentTimeMillis() + "";
            File file;
            if (isFinalImage) {
                file = new File(FileCacheUtil.getMiLordDir(context, imagePath), time + fileName);
            } else {
                file = new File(FileCacheUtil.getMiLordDir(context, imagePath), fileName);
            }
            return file;
        }

        public static void deleteImage(final Context context) {

            new Thread() {
                @Override
                public void run() {
                    String milordDir = FileCacheUtil.getMiLordDir(context, imagePath);
                    if (milordDir != null) {
                        File dir = new File(milordDir);
                        if (dir != null && dir.exists() && dir.isDirectory()) {
                            File files[] = dir.listFiles();
                            if (files != null) {
                                for (int i = 0; i < files.length; i++) {
                                    File file = files[i];
                                    //don't delete icon_camera icon_pic
                                    if (file != null && !file.getName().contains(cameraImageName)) {
                                        file.delete();
                                    }
                                }
                            }
                        }
                        MediaScannerConnection.scanFile(context, new String[]{milordDir}, null, new MediaScannerConnection.OnScanCompletedListener() {

                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
                    }
                }
            }.start();
        }

        /***
         * 启动取图功能
         * isCamera：是启动照相机还是相册
         * isCut:是否需要裁剪
         * 无论哪种方案都需要压缩图片(如果需要裁剪则不壓縮图片尺寸，以免影响分辨率)
         */
        public static void actionByIndex(Activity activity, long index) {
            CutFilePath = null;
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
            if (!sdCardExist) {
                Toast.makeText(activity, "图片操作需要插入SD卡", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(activity, PhotoSelectorActivity.class);
            intent.putExtra(PhotoSelectorActivity.KEY_MAX, PhotoSelectorActivity.SINGLE_IMAGE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra(PhotoSelectorActivity.pic_index, index);
            activity.startActivityForResult(intent, Gallery);
        }

        /***
         * 启动取图功能
         * isCamera：是启动照相机还是相册
         * isCut:是否需要裁剪
         * 无论哪种方案都需要压缩图片(如果需要裁剪则不壓縮图片尺寸，以免影响分辨率)
         */
        public static void action(Activity activity, boolean isCamera, boolean isCut) {
            CutFilePath = null;
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
            if (!sdCardExist) {
                Toast.makeText(activity, "图片操作需要插入SD卡", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isCamera) {
                File cameraOutputImage = getFile(activity, cameraImageName, false);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraOutputImage));
                activity.startActivityForResult(intent, isCut ? Camera_Cut : Camera);
                //当使用照相机的时候，接受的activity必须要设置
                //android:configChanges="orientation|keyboardHidden|screenSize"
                //和onConfigurationChanged

            } else {
                Intent intent = new Intent(activity, PhotoSelectorActivity.class);
                intent.putExtra(PhotoSelectorActivity.KEY_MAX, PhotoSelectorActivity.SINGLE_IMAGE);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivityForResult(intent, isCut ? Gallery_Cut : Gallery);
            }
        }

        public static void action(Activity activity, int num) {
            Intent intent = new Intent(activity, PhotoSelectorActivity.class);
            intent.putExtra(PhotoSelectorActivity.KEY_MAX, num);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.startActivityForResult(intent, ManyPicture);
        }

        private static void cut(Activity activity, String path) {
            if (null == path || "".equals(path)) return;

            File finalFile = getFile(activity, imagefinalName, true);
            CutFilePath = finalFile.getAbsolutePath();

            Intent intent = new Intent();
            intent.setAction("com.android.camera.action.CROP");
            intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
            //intent.setDataAndType(mUri, "image/*");// mUri是已经选择的图片Uri
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);// 裁剪框比例
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 100);// 输出图片大小
            intent.putExtra("outputY", 100);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("return-data", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(finalFile));
            activity.startActivityForResult(intent, Cut);
        }

        public static void onGetPicByResult(Activity activity, int requestCode, int resultCode, Intent data, OnImageGetListener listner) {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == Gallery || requestCode == Gallery_Cut) {
                    List<PhotoModel> photos = data.getParcelableArrayListExtra("photos");
                    if (photos != null) {
                        if (photos.size() > 0) {
                            String path = photos.get(0).getOriginalPath();
                            if (requestCode == Gallery_Cut) {
                                cut(activity, path);
                            } else {
                                List<String> list = new ArrayList<String>();
                                list.add(path);
                                compressAndCutAndSave(activity, listner, list);
                            }
                        }
                    }
                } else if (requestCode == Camera || requestCode == Camera_Cut) {
                    File outputImage = getFile(activity, cameraImageName, false);
                    if (requestCode == Camera_Cut) {
                        cut(activity, outputImage.getAbsolutePath());
                    } else {
                        List<String> list = new ArrayList<String>();
                        list.add(outputImage.getAbsolutePath());
                        compressAndCutAndSave(activity, listner, list);
                    }
                } else if (requestCode == Cut) {
                    List<String> list = new ArrayList<String>();
                    list.add(CutFilePath);
                    listner.onImageGetListener(list);
                } else if (requestCode == ManyPicture) {
                    List<PhotoModel> photos = (List<PhotoModel>) data.getExtras()
                            .getSerializable("photos");
                    List<String> list = new ArrayList<String>();
                    if (photos != null) {
                        for (PhotoModel model : photos) {
                            list.add(model.getOriginalPath());
                        }
                    }
                    compressAndCutAndSave(activity, listner, list);
                }
            }
        }

        /**
         * 压缩,裁剪并保存图片
         **/
        private static void compressAndCutAndSave(final Activity activity, final OnImageGetListener listner, final List<String> paths) {
            final int size = paths.size();
            ImageWaitDialog.build(activity).show();
            new AsyncTask<Void, Integer, List<String>>() {
                @Override
                protected List<String> doInBackground(Void[] params) {
                    List<String> list = new ArrayList<String>();
                    for (int i = 0; i < size; i++) {
                        String path = paths.get(i);
                        File finalFile = getFile(activity, i + imagefinalName, true);
                        try {
                            byte[] images = compressImage(path);
                            if (images != null) {
                                FileOutputStream fops = new FileOutputStream(finalFile);
                                fops.write(images);
                                fops.flush();
                                fops.close();
                            }
                            list.add(finalFile.getAbsolutePath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        publishProgress(i);
                    }
                    return list;
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    ImageWaitDialog.update(values[0] + "/" + size);
                }

                @Override
                protected void onPostExecute(List<String> list) {
                    ImageWaitDialog.dis();
                    if (listner != null && list != null) {
                        listner.onImageGetListener(list);
                    }
                }
            }.execute();
        }


        public interface OnImageGetListener {
            void onImageGetListener(List<String> path);
        }

    }


}
