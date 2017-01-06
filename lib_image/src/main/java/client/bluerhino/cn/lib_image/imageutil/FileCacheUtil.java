package client.bluerhino.cn.lib_image.imageutil;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.ContextCompat;

import java.io.File;

/**
 * file help class
 **/
public class FileCacheUtil {

    public static String getMiLordDir(Context context, String pathName) {
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        if (!sdCardExist) {
            return getExternalCacheDir(context).getAbsolutePath();
        } else {
            String path = Environment
                    .getExternalStorageDirectory().getAbsolutePath();
            String newPath = path + "/" + pathName;
            File file = new File(newPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
    }

    public static File getExternalCacheDir(Context context) {
        File[] dirs = ContextCompat.getExternalCacheDirs(context);
        if (dirs == null) {
            return getInternalCacheDir(context);
        }
        return dirs[0] == null ? getInternalCacheDir(context) : dirs[0];
    }

    public static File getExternalPublicDirectory(Context context, String type) {
        String path = Environment.getExternalStoragePublicDirectory(type).getAbsolutePath();
        File file = new File(path);
        file.mkdirs();
        return file;
    }

    public static File getInternalFilesDir(Context context) {
        return context.getFilesDir();
    }

    private static File getInternalCacheDir(Context context) {
        return context.getCacheDir();
    }

    public static long getFreeSpaceOfPath(String path) {
        StatFs statFs = new StatFs(path);
        long availableBlocks = statFs.getAvailableBlocks();
        long blockSize = statFs.getBlockSize();
        return availableBlocks * blockSize;
    }

    public static boolean isExternalStorageWriteable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    private static long getDirSize(File dir) {
        if (dir == null || !dir.isDirectory() || !dir.exists()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file);
            }
        }
        return dirSize;
    }

    public static String formatFileSize(long fileSize) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }

    private static boolean deleteDirectory(File dirFile) {
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } else {
                flag = deleteDirectory(files[i]);
                if (!flag) {
                    break;
                }
            }
        }
        return flag;
    }


    private static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }


}
