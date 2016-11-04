package com.salesman.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.salesman.R;
import com.salesman.umeng.UmengAnalyticsUtil;
import com.salesman.utils.ViewUtil;
import com.studio.jframework.widget.dialog.DialogCreator;
import com.umeng.message.PushAgent;

/**
 * 基类
 *
 * @author LiHuai
 *         Created by LiHuai on 2016/1/21.
 */
public class BaseActivity extends AppCompatActivity {
    protected Dialog mProgressDialog;
    private int mThemeColor = 0xff198bfe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 统计应用启动数据(友盟推送)
        PushAgent.getInstance(this).onAppStart();
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    public void setContentLayout(int layoutId) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(layoutId, null);
        ViewUtil.scaleContentView((ViewGroup) view);
        setContentView(view);

        initView();
        initData();
    }

    protected void initView() {
    }

    protected void initData() {
    }

    /**
     * 隐藏输入法
     *
     * @param context
     * @param binder
     */
    public void hideSoftInput(Context context, IBinder binder) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binder, 0);
    }

    /**
     * 弹出输入法
     *
     * @param context
     * @param et
     */
    public void showSoftInput(Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, 0);
    }

    /**
     * 可取消的弹出框
     *
     * @param message  对话内容
     * @param isCancel 是否可取消
     */
    protected void showProgressDialog(String message, boolean isCancel) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
        mProgressDialog = DialogCreator.createProgressDialog(this, DialogCreator.Position.CENTER, mThemeColor, message, false);
        mProgressDialog.setCancelable(isCancel);
        mProgressDialog.show();
    }

    /**
     * 瞬间结束,隐藏对话框
     */
    protected void dismissProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengAnalyticsUtil.umengOnCustomResume(this, this.getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengAnalyticsUtil.umengOnCustomPause(this, this.getClass().getSimpleName());
    }

    public void initRecyclerView(EasyRecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        DividerDecoration itemDecoration = new DividerDecoration(getResources().getColor(R.color.color_e5e5e5), dip2px(this, 0.5f), 0, 0);
//        itemDecoration.setDrawLastItem(true);
//        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setRefreshingColorResources(R.color.color_0090ff, R.color.color_ff8800, R.color.color_669900);
    }
}
