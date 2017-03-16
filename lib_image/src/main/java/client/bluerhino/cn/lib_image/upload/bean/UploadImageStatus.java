package client.bluerhino.cn.lib_image.upload.bean;

/**
 * Created by zhangrui on 17/3/16.
 */

public class UploadImageStatus {

    public final static int image_normal = 0;
    public final static int upload_ready = 1;
    public final static int upload_working = 2;
    public final static int upload_success = 3;
    public final static int upload_fail = 4;

    public String servicePath;
    public int progress;
    public int status;

    public UploadImageStatus() {
        this.status = image_normal;//初始化的时候是普通显示
    }

    public UploadImageStatus(String servicePath, int progress) {
        this.servicePath = servicePath;
        this.progress = 100;
        this.status = upload_success;
    }

}
