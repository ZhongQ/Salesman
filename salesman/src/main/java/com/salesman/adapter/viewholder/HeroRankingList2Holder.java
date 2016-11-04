package com.salesman.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.salesman.R;
import com.salesman.entity.HeroRankListBean;

/**
 * 全部英雄榜
 * Created by LiHuai on 2016/10/13 0011.
 */

public class HeroRankingList2Holder extends BaseViewHolder<HeroRankListBean.DataBean.RankBean> {
    private ImageView ivMedal, ivArrow;
    private TextView tvRank, tvName, tvRandAbs;

    public HeroRankingList2Holder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        ivMedal = $(R.id.iv_medal);
        ivArrow = $(R.id.iv_arrow_rank);
        tvRank = $(R.id.tv_rank);
        tvName = $(R.id.tv_name);
        tvRandAbs = $(R.id.tv_rank_abs);
    }

    @Override
    public void setData(HeroRankListBean.DataBean.RankBean data) {
        super.setData(data);
        tvName.setText(data.salemanName);
        tvRank.setText(String.valueOf(data.seqNo));
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
            tvRandAbs.setVisibility(View.VISIBLE);
            tvRandAbs.setText("排名下降：" + Math.abs(data.raseNum));
            ivArrow.setImageResource(R.drawable.arrow_down_green);
        } else if (data.raseNum > 0) {
            tvRandAbs.setVisibility(View.VISIBLE);
            tvRandAbs.setText("排名上升：" + Math.abs(data.raseNum));
            ivArrow.setImageResource(R.drawable.arrow_up_red);
        } else {
            tvRandAbs.setVisibility(View.GONE);
            ivArrow.setImageResource(R.drawable.arrow_orange);
        }
    }
}
