package widget;

import android.view.View;

/**
 * Created by neo on 16/2/24.
 */
public interface IListView {

    void setDividerHeight(int height);

    void setAdapter(BrListViewAdapter adapter);

    void setEmptyView(View emptyView);

    void setOnRefreshStartListener(BrListView.OnStartListener onStart);

    void setOnLoadMoreStartListener(BrListView.OnStartListener onStart);

    void setRefreshSuccess();

    void refresh();

    void setSelection(int position);
}
