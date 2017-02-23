package com.lib_pay.pay_weixin;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class WeiXinPayConfig implements Parcelable {
    public static final Creator<WeiXinPayConfig> CREATOR = new Creator<WeiXinPayConfig>() {
        @Override
        public WeiXinPayConfig createFromParcel(Parcel in) {
            return new WeiXinPayConfig(in);
        }

        @Override
        public WeiXinPayConfig[] newArray(int size) {
            return new WeiXinPayConfig[size];
        }
    };
    private String sign;
    private String package_weixinpay;
    private String timestamp;
    private String noncestr;
    private String partnerid;
    private String appid;
    private String prepayid;

    protected WeiXinPayConfig(String jsonStr) {
        try {
            JSONObject json = new JSONObject(jsonStr);
            sign = json.getString("sign");
            package_weixinpay = json.getString("package");
            timestamp = json.getString("timestamp");
            noncestr = json.getString("noncestr");
            partnerid = json.getString("partnerid");
            appid = json.getString("appid");
            prepayid = json.getString("prepayid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected WeiXinPayConfig(Parcel in) {
        sign = in.readString();
        package_weixinpay = in.readString();
        timestamp = in.readString();
        noncestr = in.readString();
        partnerid = in.readString();
        appid = in.readString();
        prepayid = in.readString();
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPackage_weixinpay() {
        return package_weixinpay;
    }

    public void setPackage_weixinpay(String package_weixinpay) {
        this.package_weixinpay = package_weixinpay;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    @Override
    public String toString() {
        return "WeiXinPayConfig [sign=" + sign + ", package_weixinpay="
                + package_weixinpay + ", timestamp=" + timestamp
                + ", noncestr=" + noncestr + ", partnerid=" + partnerid
                + ", appid=" + appid + ", prepayid=" + prepayid + "]";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sign);
        parcel.writeString(package_weixinpay);
        parcel.writeString(timestamp);
        parcel.writeString(noncestr);
        parcel.writeString(partnerid);
        parcel.writeString(appid);
        parcel.writeString(prepayid);
    }
}
