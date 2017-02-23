package com.lib_pay.pay_weixin;

import android.app.Activity;

import com.lib_pay.IPayMethod;
import com.lib_pay.IPayMethodCallBack;


/**
 * Created by ruizhang on 12/30/15.
 */
public class PayMethod_WeixinPay implements IPayMethod {

    @Override
    public void init(String... args) throws Exception {
        if (args.length != 4) {
            throw new Exception("args error");
        }
        Constants.APP_ID = args[0];//wx3d706f05dda1a140
        Constants.MCH_ID = args[1];//1237893502
        Constants.API_KEY = args[2];//e5031c7b30603fdbe7cb051eb27600ae
        Constants.WeiXinPayConfigStr = args[3]; //jsonStr
    }

    @Override
    public void pay(final Activity activity, final IPayMethodCallBack iPayMethodCallBack) {
        WeiXinPayConfig weiXinPayConfig = new WeiXinPayConfig(Constants.WeiXinPayConfigStr);
        WeiXinPay.getInstance().pay(activity, weiXinPayConfig);
        iPayMethodCallBack.callback(IPayMethodCallBack.PAY_WAIT, "");
    }


}
