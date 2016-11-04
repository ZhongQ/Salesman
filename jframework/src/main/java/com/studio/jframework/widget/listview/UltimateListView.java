package com.studio.jframework.widget.listview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.studio.jframework.R;

/**
 * Created by Jason
 */
public class UltimateListView extends FrameLayout {

    public static final int NO_NETWORK = 0;
    public static final int NO_CONTENT = 1;
    public static final int NOTICE = 2;
    public static final int LOADING = 3;
    public static final int CUSTOM_IMAGE = 4;

    public static final int FOOTER_NONE = 0;
    public static final int FOOTER_LOADING = 1;
    public static final int FOOTER_NOMORE = 2;

    private Context mContext;

    private boolean mHasLoadingFooter = false;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LoadMoreListView mListView;

    private LoadingFooter mFooter;

    private OnItemClickListener mListener;
    private OnRetryClickListener mClickListener;
    private OnItemLongClickListener mLongListener;

    private View mEmptyView;
    private ImageView mEmptyImage;
    private TextView mEmptyText;
    private Button mEmptyButton;

    public UltimateListView(Context context) {
        this(context, null);
    }

    public UltimateListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UltimateListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        int dividerHeight = 1;
        int dividerDrawable = 0;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UltimateListView, defStyleAttr, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.UltimateListView_dividerHeight) {
                dividerHeight = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
            }
            if (attr == R.styleable.UltimateListView_dividerDrawable) {
                dividerDrawable = a.getResourceId(R.styleable.UltimateListView_dividerDrawable, R.drawable.divider_drawable);
            }
        }
        //array should be release
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.common_listview_layout, this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mListView = (LoadMoreListView) view.findViewById(R.id.list_view);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_dark, R.color.holo_orange_dark, R.color.holo_green_dark);

        mEmptyView = view.findViewById(R.id.empty_view);
        mEmptyImage = (ImageView) view.findViewById(R.id.empty_image);
        mEmptyText = (TextView) view.findViewById(R.id.empty_text);
        mEmptyButton = (Button) view.findViewById(R.id.empty_retry);

        mEmptyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeRefreshLayout.setVisibility(VISIBLE);
                mEmptyView.setVisibility(INVISIBLE);
                if (mClickListener != null) {
                    mClickListener.onRetryLoad();
                }
            }
        });

        if (dividerDrawable != 0) {
            mListView.setDivider(ContextCompat.getDrawable(mContext, dividerDrawable));
        }
        mListView.setDividerHeight(dividerHeight);
    }

    public void setRetryButtonText(CharSequence charSequence) {
        mEmptyButton.setText(charSequence);
    }

    public void setEnableRefreshing(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public void postRefresh(Runnable runnable) {
        mSwipeRefreshLayout.post(runnable);
    }

    public LoadMoreListView getListView() {
        return mListView;
    }

    public void setAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(listener);
        setLoadingState(FOOTER_NONE);
    }

    public void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int size = mListView.getAdapter().getCount();
                int headerViewCount = mListView.getHeaderViewsCount();
                int footerViewCount = mListView.getFooterViewsCount();
                if (position < headerViewCount || position >= size || position == size - footerViewCount) {
                    return;
                } else {
                    mListener.onItemClick(adapterView, view, position - headerViewCount, l);
                }
            }
        });
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongListener = listener;

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                int size = mListView.getAdapter().getCount();
                int headerViewCount = mListView.getHeaderViewsCount();
                int footerViewCount = mListView.getFooterViewsCount();
                if (position < headerViewCount || position >= size || position == size - footerViewCount) {
                    // 无操作
                } else {
                    mLongListener.onItemLongClick(adapterView, view, position - headerViewCount, l);
                }
                return true;// 默认返回flase,会触发OnItemClickListener
            }
        });
    }

    public void setImageClickListener(OnClickListener listener) {
        mEmptyView.setOnClickListener(listener);
    }

    public void setOnLoadMoreListener(LoadMoreListView.OnLoadMoreListener listener) {
        mListView.setOnLoadMoreListener(listener);
    }

    public void setOnRetryClickListener(OnRetryClickListener listener) {
        mClickListener = listener;
    }

    public void setOnScrollChangedListener(LoadMoreListView.OnScrollChangedListener listener) {
        mListView.setOnScrollChangedListener(listener);
    }

    public ListAdapter getAdapter() {
        return mListView.getAdapter();
    }

    public void addHeader(View view) {
        mListView.addHeaderView(view);
    }

    public void addOneOnlyHeader(View view, boolean selectable) {
        if (mListView.getHeaderViewsCount() == 0) {
            mListView.addHeaderView(view, null, selectable);
        }
    }

    public void addFooter(View view) {
        mListView.addFooterView(view);
    }

    public void addOneOnlyFooter(View view, boolean selectable) {
        if (mListView.getFooterViewsCount() == 0) {
            mListView.addFooterView(view, null, selectable);
        }
    }

    public void showEmptyView(int state, String text) {
        mSwipeRefreshLayout.setVisibility(INVISIBLE);
        mEmptyView.setVisibility(VISIBLE);
        switch (state) {
            case NO_NETWORK:
                mEmptyImage.setVisibility(View.VISIBLE);
                mEmptyText.setText(text);
                mEmptyButton.setVisibility(View.VISIBLE);
                break;
            case NO_CONTENT:
                mEmptyImage.setVisibility(VISIBLE);
                mEmptyText.setText(text);
                mEmptyButton.setVisibility(VISIBLE);
                break;
            case NOTICE:
                mEmptyImage.setVisibility(INVISIBLE);
                mEmptyText.setText(text);
                mEmptyButton.setVisibility(INVISIBLE);
                break;
            case LOADING:
                mEmptyImage.setVisibility(INVISIBLE);
                mEmptyText.setText(text);
                mEmptyButton.setVisibility(INVISIBLE);
                break;
        }
    }

    public void showEmptyImage(int resId, String text, boolean isShowBtn) {
        mSwipeRefreshLayout.setVisibility(INVISIBLE);
        mEmptyView.setVisibility(VISIBLE);
        mEmptyImage.setVisibility(VISIBLE);
        mEmptyImage.setImageResource(resId);
        mEmptyText.setText(text);
        if (isShowBtn) {
            mEmptyButton.setVisibility(VISIBLE);
        } else {
            mEmptyButton.setVisibility(INVISIBLE);
        }
    }

    public void removeEmptyView() {
        mSwipeRefreshLayout.setVisibility(VISIBLE);
        mEmptyView.setVisibility(INVISIBLE);
    }

    public void addLoadingFooter() {
        if (!mHasLoadingFooter) {
            if (mFooter == null) {
                mFooter = new LoadingFooter(mContext);
            }
            mListView.addFooterView(mFooter.getView());
            mHasLoadingFooter = true;
        }
    }

    public void setLoadingState(int state) {
        if (mHasLoadingFooter) {
            if (state == FOOTER_NONE) {
                if (mListView.getFooterViewsCount() == 0) {
                    mHasLoadingFooter = false;
                    return;
                }
                mListView.removeFooterView(mFooter.getView());
                mHasLoadingFooter = false;
                return;
            }
            mFooter.setFooterState(state);
            addLoadingFooter();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> adapterView, View view, int position, long id);
    }

    public interface OnRetryClickListener {
        void onRetryLoad();
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(AdapterView<?> adapterView, View view, int position, long id);
    }
}
