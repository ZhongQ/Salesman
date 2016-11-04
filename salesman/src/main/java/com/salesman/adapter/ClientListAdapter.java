package com.salesman.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.salesman.R;
import com.salesman.activity.client.ClientInfoActivity;
import com.salesman.activity.home.OutsideSignInActivity;
import com.salesman.application.SalesManApplication;
import com.salesman.entity.ClientListBean;
import com.salesman.fragment.ClientFragment;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserConfigPreference;
import com.salesman.utils.UserInfoPreference;
import com.salesman.views.CircleHeadView;

import java.util.List;

/**
 * 客户列表适配器
 * Created by LiHuai on 2016/1/28 0028.
 */
public class ClientListAdapter extends CommonAdapter<ClientListBean.ShopBean> {
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    public ClientListAdapter(Context context, List<ClientListBean.ShopBean> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, final ClientListBean.ShopBean shopBean) {
//        holder.setBackground(R.id.iv_background, shopBean.imgId);
        holder.setTextByString(R.id.tv_name_client, shopBean.shopName);
        holder.setTextByString(R.id.tv_addr_client, shopBean.shopAddress);
        CircleHeadView circleHeadView = holder.getView(R.id.hv_client);
        circleHeadView.setCircleColorResources(shopBean.imgId);
        ImageView btnState = holder.getView(R.id.btn_visit_state);
        if (mUserInfo.getUserId().equals(shopBean.salesmanId) && "1".equals(shopBean.optType)) {
            btnState.setVisibility(View.VISIBLE);
        } else {
            btnState.setVisibility(View.GONE);
        }
        switch (shopBean.status) {
            case "0":// 未拜访（签到）
                btnState.setImageResource(R.drawable.signin_client);
                break;
            case "1":// 拜访中
                btnState.setImageResource(R.drawable.visitting_client);
                break;
            case "2":// 已拜访
                btnState.setImageResource(R.drawable.visited_client);
                break;
        }
        btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserConfig.getGotoWork()) {
                    Intent intent = new Intent(mContext, OutsideSignInActivity.class);
                    intent.putExtra("shopBean", shopBean);
                    intent.putExtra("come_from", ClientFragment.TAG);
                    mContext.startActivity(intent);
                } else {
                    ToastUtil.show(mContext, mContext.getString(R.string.first_signin_please));
                }
            }
        });

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClientInfoActivity.class);
                intent.putExtra("shopId", shopBean.shopId);
                intent.putExtra("lineId", shopBean.lineId);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int setItemLayout(int type) {
//        return R.layout.item_client_list;
        return R.layout.item_client_list_new;
    }
}
