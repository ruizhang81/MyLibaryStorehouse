package com.lib_storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

/**
 * 保存各种bean到SharedPreferences工具类
 **/
public class StorageUtil<T> {

    public final static String FILE_PRE_NAME = "BR";
    private final static String TAG = "StorageUtil";
    private SharedPreferences sharedPreferences;
    private String saveTag;
    private JsonHelp<T> jsonHelp;


    //每个bean一个文件
    public StorageUtil(Class<T> entityClass, Context context) {
        //init tool
        saveTag = entityClass.getName();
        String fileName = getFileName(saveTag);
        sharedPreferences = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        jsonHelp = new JsonHelp<>(entityClass);
    }

    public static String getFileName(String calssName) {
        return FILE_PRE_NAME + "_" + calssName;
    }


    //-------------------队列需要(注意要写equals方法)-------------------------------

    /**
     * 添加一个对象到队列中
     */
    public void add(T t) {
        List<T> list = getItems();
        if (!list.contains(t)) {
            list.add(t);
        }
        save(list);
    }

    /**
     * 从队列中删除一个对象
     */
    public void del(T t) {
        List<T> list = getItems();
        if (list.contains(t)) {
            list.remove(t);
        }
        save(list);
    }

    /**
     * 从队列中修改一个对象
     */
    public void modify(T t) {
        List<T> list = getItems();
        if (list.contains(t)) {
            list.remove(t);
            list.add(t);
        }
        save(list);
    }

    /**
     * 获取队列
     */
    public List<T> getItems() {
        String saveArr = sharedPreferences.getString(saveTag, "");
        return jsonHelp.getItemList(saveArr);
    }

    /**
     * 覆盖队列
     */
    public void save(List<T> list) {
        String listStr = jsonHelp.list2Json(list);
        sharedPreferences.edit().putString(saveTag, listStr).commit();
    }


    //-------------------单个元素需要-------------------------------

    /**
     * 保存一个对象,键值是对象class名
     */
    public void save(T t) {
        saveByTag(t, "");
    }

    /**
     * 保存一个对象,键值是参数:otherTag
     */
    public void saveByTag(T t, String otherTag) {
        String item = jsonHelp.item2Json(t);
        sharedPreferences.edit().putString(saveTag + otherTag, item).commit();
    }

    /**
     * 获取一个对象
     */
    public T getItem() {
        return getItemByTag("");
    }

    /**
     * 根据指定otherTag参数获取一个对象
     */
    public T getItemByTag(String otherTag) {
        String saveArr = sharedPreferences.getString(saveTag + otherTag, "");
        if (!"".equals(saveArr)) {
            return jsonHelp.getItem(saveArr);
        }
        return null;
    }


    //-------------------清除所有缓存-------------------------------
    public void clear() {
        sharedPreferences.edit().clear().commit();
    }

}
