package client.bluerhino.cn.lib_image.upload.bean;


import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;

/**
 * Created by zhangrui on 17/3/14.
 */

public class ImageInfo implements Parcelable{

    private long id;
    public String localUrl;
    public String url;
    public String name;
    public int type;
    public int status;
    public int progress;
    public boolean onlyReplace; //仅能替换远程图片
    public boolean alwaysHidenDel; //一直隐藏删除按钮
    //其他信息
    public String image_name;
    public String image_level;
    public String image_cerauth;


    public ImageInfo() {
        id = System.nanoTime();
        onlyReplace = false;
    }


    protected ImageInfo(Parcel in) {
        id = in.readLong();
        localUrl = in.readString();
        url = in.readString();
        name = in.readString();
        type = in.readInt();
        status = in.readInt();
        progress = in.readInt();
        onlyReplace = in.readByte() != 0;
//        autoUpload = in.readByte() != 0;
        image_name = in.readString();
        image_level = in.readString();
        image_cerauth = in.readString();
        alwaysHidenDel= in.readByte() != 0;
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel in) {
            return new ImageInfo(in);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(localUrl);
        dest.writeString(url);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeInt(status);
        dest.writeInt(progress);
        dest.writeByte((byte) (onlyReplace ? 1 : 0));
//        dest.writeByte((byte) (autoUpload ? 1 : 0));
        dest.writeString(image_name);
        dest.writeString(image_level);
        dest.writeString(image_cerauth);
        dest.writeByte((byte) (alwaysHidenDel ? 1 : 0));
    }

    @Override
    public boolean equals(Object obj) {
        if(this!=null && obj!=null){
            if(obj instanceof ImageInfo){
                ImageInfo that = (ImageInfo)obj;
                return id==that.id;
            }
        }
        return false;
    }
}
