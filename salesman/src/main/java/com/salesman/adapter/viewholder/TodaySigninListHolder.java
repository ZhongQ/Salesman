package com.salesman.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.salesman.R;
import com.salesman.entity.SigninListBean;
import com.salesman.views.layoutmanager.FullyLinearLayoutManager;

/**
 * 今日签到列表
 * Created by LiHuai on 2016/10/26 0026.
 */

public class TodaySigninListHolder extends BaseViewHolder<SigninListBean.SignGroupBean> {
    private ImageView ivBg;
    private TextView tvType, tvNum;
    private EasyRecyclerView recyclerView;
    private TodaySigninListener todaySigninListener;

    public interface TodaySigninListener {
        void onInnerItemListener(SigninListBean.SignBean signBean);
    }

    public TodaySigninListHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        ivBg = $(R.id.iv_line_signin);
        tvType = $(R.id.tv_signin_type);
        tvNum = $(R.id.tv_signin_num);
        recyclerView = $(R.id.rv_inner_signin);
    }

    @Override
    public void setData(SigninListBean.SignGroupBean data) {
        super.setData(data);
        if (0 == getAdapterPosition()) {
            ivBg.setVisibility(View.GONE);
        } else {
            ivBg.setVisibility(View.VISIBLE);
        }
        tvType.setText(data.typeName);
        tvNum.setText(data.signList.size() + "/" + data.staffTotal);


        final Context context = getContext();
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(context));
        final RecyclerArrayAdapter<SigninListBean.SignBean> adapter = new RecyclerArrayAdapter<SigninListBean.SignBean>(getContext()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new SigninInnerListHolder(parent, R.layout.item_signin_today);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.addAll(data.signList);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SigninListBean.SignBean signBean = adapter.getItem(position);
                if (null != todaySigninListener) {
                    todaySigninListener.onInnerItemListener(signBean);
                }
            }
        });
    }

    public void setTodaySigninListener(TodaySigninListener todaySigninListener) {
        this.todaySigninListener = todaySigninListener;
    }
}
