package com.salesman.views.popupwindow;

/**
 * 功能描述：弹窗内部子类项（绘制标题和图标）
 * Created by LiHuai on 2016/5/24 0024.
 */
public class ActionItem {
    // 定义图片对象
    public int mDrawable;
    // 定义文本对象
    public String mTitle;
    // id
    public int id;


    public ActionItem(int id, String title, int drawableId) {
        this.mTitle = title;
        this.mDrawable = drawableId;
        this.id = id;
    }

}
