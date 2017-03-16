package client.bluerhino.cn.lib_image.upload.bean;



/**
 * Created by zhangrui on 17/3/14.
 */

public class ImageInfo {

    public String localUrl;
    public String url;
    public String name;
    public int status;
    public int progress;
    public boolean canDeleteUrl; //是否可以删除远程图片
    public boolean onlyReplace; //仅能替换远程图片
    public boolean autoUpload;


}
