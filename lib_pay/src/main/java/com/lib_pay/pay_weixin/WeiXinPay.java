package com.lib_pay.pay_weixin;

import android.content.Context;
import android.util.Log;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WeiXinPay {

    private static WeiXinPay instance = new WeiXinPay();
    private PayReq request = new PayReq();

    public static WeiXinPay getInstance() {
        return instance;
    }

    public void pay(Context context, WeiXinPayConfig weiXinPayConfig) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, null);
        request.appId = weiXinPayConfig.getAppid();
        request.partnerId = weiXinPayConfig.getPartnerid();
        request.prepayId = weiXinPayConfig.getPrepayid();
        request.packageValue = weiXinPayConfig.getPackage_weixinpay();
        request.nonceStr = weiXinPayConfig.getNoncestr();
        request.timeStamp = weiXinPayConfig.getTimestamp();
        request.sign = weiXinPayConfig.getSign();

        Log.e("xxx","appId="+request.appId);
        Log.e("xxx","partnerId="+request.partnerId);
        Log.e("xxx","prepayId="+request.prepayId);
        Log.e("xxx","packageValue="+request.packageValue);
        Log.e("xxx","nonceStr="+request.nonceStr);
        Log.e("xxx","timeStamp="+request.timeStamp);
        Log.e("xxx","sign="+request.sign);

//        api.registerApp(request.appId);
        api.sendReq(request);
    }
}
