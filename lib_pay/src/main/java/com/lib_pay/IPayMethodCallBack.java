package com.lib_pay;

/**
 * Created by ruizhang on 12/30/15.
 */
public interface IPayMethodCallBack {

    int PAY_WAIT = 0;
    int PAY_SUCCESS = 1;
    int PAY_FAIL = 2;

    void callback(int code, String displayMessage);
}
