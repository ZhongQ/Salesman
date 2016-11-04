package com.salesman.views.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;

import com.salesman.R;
import com.salesman.adapter.CommonAdapter;
import com.salesman.adapter.ViewHolder;
import com.salesman.utils.DeviceUtil;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户刷选PopupWindow
 * Created by LiHuai on 2016/6/14 0014.
 */
public class ClientPopup extends PopupWindow {
    private static final String TAG = ClientPopup.class.getSimpleName();
    private Context mContext;
    // 屏幕的宽度和高度
    private int mScreenWidth, mScreenHeight;
    // 屏幕的0.6高度
    private int partScreenHeight;

    // 定义列表对象
    private GridView mGridView;
    // 背景
    private View viewBg;
    // 弹窗子类项选中时的监听
    private OnItemOnClickListener mItemOnClickListener;
    // 定义弹窗子类项列表
    private ArrayList<FilterItem> mActionItems = new ArrayList<>();

    public ClientPopup(Context context, Context mContext) {
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @SuppressWarnings("deprecation")
    public ClientPopup(Context context, int width, int height) {
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
        partScreenHeight = (int) (mScreenHeight * 0.6);

        // 设置弹窗的宽度和高度
        setWidth(width);
        setHeight(height);

        setBackgroundDrawable(new BitmapDrawable());

        // 设置弹窗的布局界面
        setContentView(LayoutInflater.from(mContext).inflate(R.layout.client_popup, null));

        initUI();
    }

    /**
     * 初始化弹窗列表
     */
    private void initUI() {
        mGridView = (GridView) getContentView().findViewById(R.id.gv_popup);
        viewBg = getContentView().findViewById(R.id.view_popup);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
                if (mItemOnClickListener != null) {
                    mItemOnClickListener.onItemClick(mActionItems.get(index), index);
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
    public void addActionList(List<FilterItem> actions) {
        if (actions != null) {
            mActionItems.clear();
            mActionItems.addAll(actions);
            populateActions();
        }
    }

    /**
     * 设置弹窗列表子项
     */
    private void populateActions() {
        // 设置列表的适配器
        PopAdapter adapter = new PopAdapter(mContext, mActionItems);
        mGridView.setAdapter(adapter);
        setGridViewHeightBasedOnChildren(mGridView);
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

    private class PopAdapter extends CommonAdapter<FilterItem> {

        public PopAdapter(Context context, List<FilterItem> data) {
            super(context, data);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public void inflateContent(ViewHolder holder, int position, FilterItem actionItem) {
            holder.setTextByString(R.id.tv_popup_client, actionItem.name);
        }

        @Override
        public int setItemLayout(int type) {
            return R.layout.item_popup_client;
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
