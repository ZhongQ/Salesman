package com.studio.jframework.widget.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.studio.jframework.R;

/**
 * Created by Jason
 */
public class LoadingFooter {

    private ProgressBar mProgress;
    private TextView mText;

    private View mFooterView;

    public static final int FOOTER_LOADING = 1;
    public static final int FOOTER_NOMORE = 2;

    public LoadingFooter(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFooterView = inflater.inflate(R.layout.list_view_loading_footer, null);
        mProgress = (ProgressBar) mFooterView.findViewById(R.id.loading_progress);
        mText = (TextView) mFooterView.findViewById(R.id.loading_text);
    }

    public View getView() {
        return mFooterView;
    }

    public void setFooterState(int state) {
        switch (state) {
            case FOOTER_LOADING:
                mProgress.setVisibility(View.VISIBLE);
                mText.setText("正在加载中...");
                break;

            case FOOTER_NOMORE:
                mProgress.setVisibility(View.GONE);
                mText.setText("没有更多内容...");
                break;
        }
    }
}
