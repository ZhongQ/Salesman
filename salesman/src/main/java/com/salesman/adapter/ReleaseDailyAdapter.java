package com.salesman.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.entity.ReleaseDailyBean;
import com.studio.jframework.utils.LogUtils;

import java.util.List;

/**
 * 发日报适配器
 * Created by LiHuai on 2016/3/25 0025.
 */
public class ReleaseDailyAdapter extends CommonAdapter<ReleaseDailyBean.ReleaseListBean> {
    public static final int STRING = 1;
    public static final int INTEGER = 2;
    public static final int FLOAT = 3;

    public ReleaseDailyAdapter(Context context, List<ReleaseDailyBean.ReleaseListBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, ReleaseDailyBean.ReleaseListBean releaseListBean) {
        if (!TextUtils.isEmpty(releaseListBean.fieldCnName)) {
            LinearLayout layout = holder.getView(R.id.lay_release_daily);
            ((TextView) holder.getView(R.id.tv_key_daily)).setText(releaseListBean.fieldCnName);
            EditText et = holder.getView(R.id.et_value_daily);
            // 集合中第一条为假数据
            // 遇到的问题：第一个输入框输入后会将所有输入框中的值改变为第一个输入框中的值
            // 解决方案：造一条假数据放在首位，再将第一个item隐藏
            switch (releaseListBean.fieldType) {
                case STRING:
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                    setEtHint(et, STRING, releaseListBean.isRequired);
                    break;
                case INTEGER:
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    et.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                    setEtHint(et, INTEGER, releaseListBean.isRequired);
                    break;
                case FLOAT:
                    et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);// 输入小数
                    setEtHint(et, FLOAT, releaseListBean.isRequired);
                    break;
            }
            // 设置输入框输入长度
            if (releaseListBean.length > 0) {
                InputFilter[] filters = {new InputFilter.LengthFilter(releaseListBean.length)};
                et.setFilters(filters);
            } else {
                InputFilter[] filters = {new InputFilter.LengthFilter(3)};
                et.setFilters(filters);
            }
            et.addTextChangedListener(new MyTextWatcher(releaseListBean));
        } else {
            // 隐藏第一个item
            holder.getConvertView().setVisibility(View.GONE);
        }
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_release_daily;
    }

    private void setEtHint(EditText et, int type, int isMust) {
        switch (type) {
            case STRING:
                if (1 == isMust) {
                    et.setHint(mContext.getResources().getString(R.string.daily_text_hint_must));
                } else {
                    et.setHint(mContext.getResources().getString(R.string.daily_text_hint_select));
                }
                break;
            case INTEGER:
            case FLOAT:
                if (1 == isMust) {
                    et.setHint(mContext.getResources().getString(R.string.daily_number_hint_must));
                } else {
                    et.setHint(mContext.getResources().getString(R.string.daily_number_hint_select));
                }
                break;
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private ReleaseDailyBean.ReleaseListBean releaseListBean;

        public MyTextWatcher(ReleaseDailyBean.ReleaseListBean releaseListBean) {
            this.releaseListBean = releaseListBean;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (null != s && null != releaseListBean) {
                LogUtils.d("afterTextChanged2>>>", s.toString());
                releaseListBean.setValue(s.toString());
            }
        }
    }
}
