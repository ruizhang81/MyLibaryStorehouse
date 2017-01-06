package client.bluerhino.cn.lib_image.imagebrowse.photoselector.ui;
/**
 * @author Aizaz AZ
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import client.bluerhino.cn.lib_image.imagebrowse.photoselector.model.AlbumModel;

public class AlbumAdapter extends MBaseAdapter<AlbumModel> {

    public AlbumAdapter(Context context, ArrayList<AlbumModel> models) {
        super(context, models);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumItem albumItem;
        if (convertView == null) {
            albumItem = new AlbumItem(context);
            convertView = albumItem;
        } else
            albumItem = (AlbumItem) convertView;
        albumItem.update(models.get(position));
        return convertView;
    }

}
