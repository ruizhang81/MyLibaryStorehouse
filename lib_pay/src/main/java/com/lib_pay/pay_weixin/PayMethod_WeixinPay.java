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
        if (args.length <= 0) {
            throw new Exception("args error");
        }
        Constants.WeiXinPayConfigStr = args[0]; //jsonStr
    }

    @Override
    public void pay(final Activity activity, final IPayMethodCallBack iPayMethodCallBack) {
        WeiXinPayConfig weiXinPayConfig = new WeiXinPayConfig(Constants.WeiXinPayConfigStr);
        WeiXinPay.getInstance().pay(activity, weiXinPayConfig);
        iPayMethodCallBack.callback(IPayMethodCallBack.PAY_WAIT, "");
    }


}
