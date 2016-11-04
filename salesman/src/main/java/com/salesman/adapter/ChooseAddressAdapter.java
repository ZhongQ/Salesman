package com.salesman.adapter;

import android.content.Context;

import com.baidu.mapapi.search.core.PoiInfo;
import com.salesman.R;

import java.util.List;

/**
 * 选择地址适配器
 * Created by LiHuai on 2016/5/23 0023.
 */
public class ChooseAddressAdapter extends CommonAdapter<PoiInfo> {

    public ChooseAddressAdapter(Context context, List<PoiInfo> data) {
        super(context, data);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void inflateContent(ViewHolder holder, int position, PoiInfo poiInfo) {
        holder.setTextByString(R.id.tv_short_address, poiInfo.name);
        holder.setTextByString(R.id.tv_detail_address, poiInfo.address);
    }

    @Override
    public int setItemLayout(int type) {
        return R.layout.item_choose_address;
    }
}
