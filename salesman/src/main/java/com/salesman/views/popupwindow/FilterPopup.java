package com.salesman.views.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.salesman.R;
import com.salesman.utils.DeviceUtil;
import com.salesman.views.layoutmanager.WrapHeightGridLayoutManager;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * PopupWindow
 * Created by LiHuai on 2016/10/17 0014.
 */
public class FilterPopup extends PopupWindow {
    private static final String TAG = FilterPopup.class.getSimpleName();
    private Context mContext;
    // 屏幕的宽度和高度
    private int mScreenWidth, mScreenHeight;
    // 屏幕的0.4高度
    private int partScreenHeight;

    private RecyclerView mRecyclerView;
    private RecyclerArrayAdapter<FilterItem> mAdapter;
    // 背景
    private View viewBg;
    // 弹窗子类项选中时的监听
    private OnItemOnClickListener mItemOnClickListener;
    // 定义弹窗子类项列表
    private ArrayList<FilterItem> mActionItems = new ArrayList<>();

    public FilterPopup(Context context, Context mContext) {
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @SuppressWarnings("deprecation")
    public FilterPopup(Context context, int width, int height) {
        this.mContext = context;

        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);

        // 获得屏幕的宽度和高度
        mScreenWidth = DeviceUtil.getScreenWidth(mContext);
        mScreenHeight = DeviceUtil.getScreenHeight(mContext);
        partScreenHeight = (int) (mScreenHeight * 0.4);

        // 设置弹窗的宽度和高度
        setWidth(width);
        setHeight(height);

        setBackgroundDrawable(new BitmapDrawable());

        // 设置弹窗的布局界面
        setContentView(LayoutInflater.from(mContext).inflate(R.layout.filter_popup, null));

        initUI();
    }

    /**
     * 初始化弹窗列表
     */
    private void initUI() {
        mRecyclerView = (RecyclerView) getContentView().findViewById(R.id.rv_popup);
        viewBg = getContentView().findViewById(R.id.view_popup);

        mRecyclerView.setLayoutManager(new WrapHeightGridLayoutManager(mContext, 1));
        mRecyclerView.setHasFixedSize(true);// 说是如果item的高度固定不变，设置这个属性能提高性能
        mAdapter = new RecyclerArrayAdapter<FilterItem>(mContext) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new PopupHolder(parent, R.layout.item_single_select);
            }
        };
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mItemOnClickListener != null) {
                    mItemOnClickListener.onItemClick(mAdapter.getItem(position), position);
                }
                // 点击子类项后，弹窗消失
                dismiss();
            }
        });
        viewBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 添加子类项集合
     *
     * @param actions
     */
    public void addPopupList(List<FilterItem> actions) {
        if (actions != null && mAdapter != null) {
            mAdapter.clear();
            mAdapter.addAll(actions);
        }
    }

    /**
     * 显示弹窗列表界面
     */
    public void show(View view) {
        // 显示弹窗的位置
        showAsDropDown(view);
    }

    /**
     * 设置GridView的高度，若高度小于屏幕的0.6倍，则原高度显示，反之高度设置为屏幕的0.6倍
     *
     * @param gridView
     */
    public void setGridViewHeightBasedOnChildren(GridView gridView) {
        // 获取listview的adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 2;// gridView.getNumColumns() = -1
        int totalHeight = 0;
        int num = listAdapter.getCount();// item数量
        int line = num / col;// 行数
        if ((line * col) < num) {
            line++;
        }
        for (int i = 0; i < line; i++) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }
        // 获取GridView的布局参数
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        LogUtils.d(TAG, partScreenHeight + ">>>" + totalHeight);
        // 设置高度
        if (totalHeight > partScreenHeight) {
            params.height = partScreenHeight;
        } else {
            params.height = totalHeight;
        }
        gridView.setLayoutParams(params);
    }

    /**
     * Popup的Holder
     */
    private class PopupHolder extends BaseViewHolder<FilterItem> {
        private TextView tvName;
        private ImageView ivBiaoZhi;

        public PopupHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            tvName = $(R.id.tv_no);
            ivBiaoZhi = $(R.id.iv_biaozhi);
        }

        @Override
        public void setData(FilterItem data) {
            super.setData(data);
            tvName.setText(data.name);
            if (data.isSelect) {
                tvName.setTextColor(mContext.getResources().getColor(R.color.color_0090ff));
                ivBiaoZhi.setVisibility(View.VISIBLE);
            } else {
                tvName.setTextColor(mContext.getResources().getColor(R.color.color_666666));
                ivBiaoZhi.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置监听事件
     */
    public void setItemOnClickListener(OnItemOnClickListener onItemOnClickListener) {
        this.mItemOnClickListener = onItemOnClickListener;
    }

    /**
     * 功能描述：弹窗子类项按钮监听事件
     */
    public interface OnItemOnClickListener {
        void onItemClick(FilterItem item, int position);
    }
}
