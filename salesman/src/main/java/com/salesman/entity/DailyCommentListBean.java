package com.salesman.entity;

import com.salesman.R;

import java.util.List;

/**
 * 评论列表实体
 * Created by LiHuai on 2016/3/30 0030.
 */
public class DailyCommentListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        /**
         * reportId : ca22b4d3cd2a452a82ab36df3dfafb28
         * comment : 我发布了评论！
         * replyBy : 李坏坏
         * createBy : ff80808151e6527a01521f64daf80ea6
         * createTime : 03月29日 18:14
         */

        public List<CommentBean> list;

    }

    public static class CommentBean {
        public String reportId;
        public String comment;
        public String postBy;
        public String replyBy;
        public String createBy;
        public String createTime;
        public String commentId;

        /**
         * @param replyBy  创建人名称
         * @param createBy 创建人id
         * @param reportId 日报id
         * @param postBy   被回复人
         * @param comment  内容
         */
        public CommentBean(String replyBy, String createBy, String reportId, String postBy, String comment) {
            this.reportId = reportId;
            this.comment = comment;
            this.postBy = postBy;
            this.replyBy = replyBy;
            this.createBy = createBy;
        }

        /**
         * @param replyBy   创建人名称
         * @param createBy  创建人id
         * @param reportId  日报id
         * @param postBy    被回复人
         * @param comment   内容
         * @param commentId 评论id
         */
        public CommentBean(String replyBy, String createBy, String reportId, String postBy, String comment, String commentId) {
            this(replyBy, createBy, reportId, postBy, comment);
            this.commentId = commentId;
        }

        public int imgId = R.drawable.circle_1;// 图标背景

        public int getImgId() {
            return imgId;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }
    }
}
