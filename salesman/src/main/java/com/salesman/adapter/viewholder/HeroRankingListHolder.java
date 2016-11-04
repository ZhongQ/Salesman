package com.salesman.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.salesman.R;
import com.salesman.application.SalesManApplication;
import com.salesman.entity.ZhanJiBean;
import com.salesman.utils.StringUtil;
import com.salesman.views.CircleHeadView;

/**
 * 英雄榜
 * Created by LiHuai on 2016/10/11 0011.
 */

public class HeroRankingListHolder extends BaseViewHolder<ZhanJiBean.DataBean.RankingBean> {
    private CircleHeadView hv;
    private ImageView ivMedal, ivArrow;
    private TextView tvRank, tvName, tvTime;
    private LinearLayout layItem;

    public HeroRankingListHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        hv = $(R.id.hv_rank_zhanji);
        ivMedal = $(R.id.iv_medal);
        ivArrow = $(R.id.iv_arrow_rank);
        tvRank = $(R.id.tv_rank);
        tvName = $(R.id.tv_name);
        tvTime = $(R.id.tv_time);
        layItem = $(R.id.lay_item_rank_zhanji);
    }

    @Override
    public void setData(ZhanJiBean.DataBean.RankingBean data) {
        super.setData(data);
        hv.setTextContent(StringUtil.cutStringLastTwo(data.salemanName));
        tvName.setText(data.salemanName);
        tvTime.setText(data.updateTime);

        switch (data.seqNo) {
            case 1:
                ivMedal.setVisibility(View.VISIBLE);
                tvRank.setVisibility(View.GONE);
                ivMedal.setImageResource(R.drawable.gold_medal_icon);
                break;
            case 2:
                ivMedal.setVisibility(View.VISIBLE);
                tvRank.setVisibility(View.GONE);
                ivMedal.setImageResource(R.drawable.silver_medal_icon);
                break;
            case 3:
                ivMedal.setVisibility(View.VISIBLE);
                tvRank.setVisibility(View.GONE);
                ivMedal.setImageResource(R.drawable.copper_medal_icon);
                break;
            default:
                ivMedal.setVisibility(View.GONE);
                tvRank.setVisibility(View.VISIBLE);
                tvRank.setText(String.valueOf(data.seqNo));
                break;
        }

        if (data.raseNum < 0) {
            ivArrow.setImageResource(R.drawable.arrow_down_green);
        } else if (data.raseNum > 0) {
            ivArrow.setImageResource(R.drawable.arrow_up_red);
        } else {
            ivArrow.setImageResource(R.drawable.arrow_orange);
        }

        if (1 == getAdapterPosition() && data.salemanId.equals(SalesManApplication.g_GlobalObject.getmUserInfo().getUserId())) {
            layItem.setBackgroundColor(getContext().getResources().getColor(R.color.color_daefff));
        } else {
            layItem.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        }

    }
}
