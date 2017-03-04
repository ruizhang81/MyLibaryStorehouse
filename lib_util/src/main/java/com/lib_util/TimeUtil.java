package com.lib_util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangrui on 17/1/5.
 */

public class TimeUtil {


    public static long getTimeStemp(String time,String timeFormat){
        try{
            Date date =  new SimpleDateFormat(timeFormat).parse(time);
            return date.getTime()/1000;
        }catch (Exception e){

        }
        return 0;
    }
}
