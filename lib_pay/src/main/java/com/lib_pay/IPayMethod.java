package com.lib_pay;

import android.app.Activity;

/**
 * Created by ruizhang on 12/30/15.
 */
public interface IPayMethod {

    void init(String... args) throws Exception;//初始化

    void pay(Activity activity, IPayMethodCallBack payMethodCallBack);//支付及回调(微信和易宝,回调参数可为空)

}
