package com.salesman.common;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.salesman.R;
import com.salesman.utils.ViewUtil;
import com.studio.jframework.widget.dialog.DialogCreator;

/**
 * Base class for the fragment
 *
 * @author LiHuai
 */
public abstract class BaseFragment extends Fragment {

    protected View mFragmentView;
    protected Context mContext;
    protected boolean mIsVisible;
    protected boolean mPrepared;

    protected Dialog mProgressDialog;
    private int mThemeColor = 0xff198bfe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(setLayout(), container, false);
        ViewUtil.scaleContentView((ViewGroup) mFragmentView);
        findViews(mFragmentView);
        initialization();
        bindEvent();
        onCreateView();
        mPrepared = true;
        return mFragmentView;
    }

    /**
     * Method to set the layout
     *
     * @return The layout resource id of the file
     */
    protected abstract int setLayout();

    /**
     * Do find views in onCreateView()
     */
    protected abstract void findViews(View view);

    /**
     * Do some initialization in onCreateView()
     */
    protected abstract void initialization();

    /**
     * Do bind event in onCreateView()
     */
    protected abstract void bindEvent();

    /**
     * The main job should be done here
     */
    protected abstract void onCreateView();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible(mPrepared);
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    /**
     * This method will be invoked when the fragment is invisible to the user
     */
    protected void onInvisible() {
    }

    /**
     * This method will be invoked when the fragment is visible to the user
     *
     * @param prepared When prepared is true, means onCreateView return;
     */
    protected void onVisible(boolean prepared) {
    }

    /**
     * Hide the soft keyboard
     *
     * @param view One of the views in the current window
     */
    protected void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Force to hide the keyboard
     */
    protected void forceHideKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * Show the soft keyboard
     *
     * @param view One of the views in the current window
     */
    protected void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED);
        imm.showSoftInput(view, 0);
    }

    /**
     * Start an activity with specify Bundle
     *
     * @param paramClass  The activity class you want to start
     * @param paramBundle The bundle you want to put in the intent
     */
    protected void openActivityWithBundle(Class<?> paramClass, Bundle paramBundle) {
        Intent intent = new Intent(mContext, paramClass);
        if (paramBundle != null) {
            intent.putExtras(paramBundle);
        }
        startActivity(intent);
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
        mProgressDialog = DialogCreator.createProgressDialog(mContext, DialogCreator.Position.CENTER, mThemeColor, message, false);
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void initRecyclerView(EasyRecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        DividerDecoration itemDecoration = new DividerDecoration(getResources().getColor(R.color.color_e5e5e5), dip2px(mContext, 0.5f), 0, 0);
//        itemDecoration.setDrawLastItem(true);
//        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setRefreshingColorResources(R.color.color_0090ff, R.color.color_ff8800, R.color.color_669900);
    }
}
