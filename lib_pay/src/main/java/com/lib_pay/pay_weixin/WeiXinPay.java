package com.lib_pay.pay_weixin;

import android.content.Context;

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
        api.sendReq(request);
    }
}
