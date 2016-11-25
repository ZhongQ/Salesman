package com.salesman.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.salesman.R;
import com.salesman.activity.home.OutsideSignInActivity;
import com.salesman.application.SalesManApplication;
import com.salesman.entity.ClientListBean;
import com.salesman.fragment.ClientFragment;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserConfigPreference;
import com.salesman.utils.UserInfoPreference;
import com.salesman.views.CircleHeadView;

/**
 * 客户列表
 * Created by LiHuai on 2016/10/19 0019.
 */

public class ClientListHolder extends BaseViewHolder<ClientListBean.ShopBean> {
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private CircleHeadView hv;
    private ImageView ivStatus;
    private TextView tvName, tvAddress, tvDistance;


    public ClientListHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        hv = $(R.id.hv_client);
        ivStatus = $(R.id.btn_visit_state);
        tvName = $(R.id.tv_name_client);
        tvAddress = $(R.id.tv_addr_client);
        tvDistance = $(R.id.tv_distance_client);
    }

    @Override
    public void setData(final ClientListBean.ShopBean data) {
        super.setData(data);
        tvName.setText(data.shopName);
        tvAddress.setText(data.shopAddress);
        hv.setCircleColorResources(data.imgId);
        if (mUserInfo.getUserId().equals(data.salesmanId) && "1".equals(data.optType)) {
            ivStatus.setVisibility(View.VISIBLE);
        } else {
            ivStatus.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(data.distance)) {
            tvDistance.setText("据我" + data.distance);
            tvDistance.setTextColor(getContext().getResources().getColor(R.color.color_0090ff));
            tvDistance.setCompoundDrawablesWithIntrinsicBounds(R.drawable.client_distance_icon, 0, 0, 0);
        } else {
            tvDistance.setText("暂无坐标");
            tvDistance.setTextColor(getContext().getResources().getColor(R.color.color_cccccc));
            tvDistance.setCompoundDrawablesWithIntrinsicBounds(R.drawable.client_distance_grey_icon, 0, 0, 0);
        }
        switch (data.status) {
            case "0":// 未拜访（签到）
                ivStatus.setImageResource(R.drawable.signin_client);
                break;
            case "1":// 拜访中
                ivStatus.setImageResource(R.drawable.visitting_client);
                break;
            case "2":// 已拜访
                ivStatus.setImageResource(R.drawable.visited_client);
                break;
        }
        final Context mContext = getContext();
        ivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserConfig.getGotoWork()) {
                    Intent intent = new Intent(mContext, OutsideSignInActivity.class);
                    intent.putExtra("shopBean", data);
                    intent.putExtra("come_from", ClientFragment.TAG);
                    mContext.startActivity(intent);
                } else {
                    ToastUtil.show(mContext, mContext.getString(R.string.first_signin_please));
                }
            }
        });
    }
}
