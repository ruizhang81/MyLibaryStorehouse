package com.lib_view.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.lib_view.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ruizhang on 1/5/16.
 */
public class SelectBottomDialogTextView extends TextView {

    private List<? extends PopWindowItem> popWindowItemList = new ArrayList<>();
    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private PopWindowItem selectedItem;
    private View parent;

    public SelectBottomDialogTextView(Context context) {
        super(context);
        init();
    }

    public SelectBottomDialogTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectBottomDialogTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectBottomDialogTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public static List<PopWindowItem> convert(List<String> list) {
        class Item implements PopWindowItem {
            public String name;

            public Item(String name) {
                this.name = name;
            }

            @Override
            public String getDisplay() {
                return name;
            }

        }

        List<PopWindowItem> items = new ArrayList<>();
        for (String str : list) {
            items.add(new Item(str));
        }
        return items;
    }

    private void init() {
        inflater = LayoutInflater.from(getContext());
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parent == null || popWindowItemList.size() == 0) {
                    return;
                }
                if (!popupWindow.isShowing()) {
                    popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

//            int[] location = new int[2];
//            parent.getLocationOnScreen(location);
//            showAtLocation(context.getCurrentFocus(), Gravity.NO_GRAVITY, view.getWidth() / 2 - popupWidth / 2,
//                    location[1]);
//
                } else {
                    popupWindow.dismiss();
                }
            }
        });
    }

    public PopWindowItem getItem() {
        return selectedItem;
    }

    public void setValues(View view, String defaultValue, List<? extends PopWindowItem> list, boolean hideArror) {
        setValues(view, defaultValue, list);
        setTextColor(getResources().getColor(R.color.white));
    }

    public void setValues(View view, String defaultValue, List<? extends PopWindowItem> list) {
        if (list != null) {
            popWindowItemList = list;
        }
        setHint(defaultValue);
        parent = view;
        View popupWindow_view = inflater.inflate(R.layout.layout_bottom_dialog, null, false);
        ListView listView = (ListView) popupWindow_view.findViewById(R.id.listview);
        FloorAdapter adapter = new FloorAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && popWindowItemList.size() > position) {
                    selectedItem = popWindowItemList.get(position);
                    setText(selectedItem.getDisplay());
                }
                popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.contact_popup_anim_bottom);
        popupWindow.setFocusable(true);

        View back_bg = popupWindow_view.findViewById(R.id.back_bg);
        back_bg.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return false;
            }
        });
    }


    public interface PopWindowItem {
        String getDisplay();
    }

    class FloorAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return popWindowItemList.size();
        }

        @Override
        public PopWindowItem getItem(int position) {
            return popWindowItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View converView, ViewGroup parent) {
            ViewHolder vh;
            if (converView == null) {
                vh = new ViewHolder();
                converView = inflater.inflate(R.layout.item_bottom_dialog_item, null);
                vh.name = (TextView) converView.findViewById(R.id.name);
                converView.setTag(vh);
            } else {
                vh = (ViewHolder) converView.getTag();
            }
            vh.name.setText(getItem(position).getDisplay());
            return converView;
        }

        class ViewHolder {
            TextView name;
        }
    }
}
