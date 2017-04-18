package client.bluerhino.cn.lib_image.upload.http;


import client.bluerhino.cn.lib_image.upload.bean.ImageResult;
import client.bluerhino.cn.lib_image.upload.bean.ResponseBean;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by zhangrui on 17/1/2.
 */

public interface ApiInterface {

    String BASE_DOMAIN = "http://api.mijingkeji.com/";
    String BASE_PORT = "80";


//    String BASE_DOMAIN = "http://test.lanxiniu.com";
//    String BASE_PORT = "80";


    //上传图片
    @Multipart
    @POST("/upload")
//    @POST("/Infos/uploadGoodsImg")
    Call<ResponseBean<ImageResult>> upload(@Part MultipartBody.Part file);


}
