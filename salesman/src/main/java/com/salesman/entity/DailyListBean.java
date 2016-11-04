package com.salesman.entity;

import com.salesman.adapter.DailyCommentListAdapter;

import java.util.List;

/**
 * 日报列表实体
 * Created by LiHuai on 2016/3/30 0030.
 */
public class DailyListBean extends BaseBean {
    /**
     * pageNo : 1
     * pageSize : 2
     * count : 12
     * list : [{"userId":"ff80808151e6527a01521f64daf80ea2","userName":"十四郎","deptId":"YFB001","deptName":"研发部001","replyList":[{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"你们的话","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:01"},{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"这样才华还","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:06"},{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"这样才华还","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:06"},{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"这样","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:20"},{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"焊接机","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:22"},{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"给公告和","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:34"},{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"骨灰盒","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:39"},{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"刚回家就好","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:39"}],"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","visitLine":"100","lineStoreNum":"5555","visitStoreNum":"223","createTime":"2016-03-29","week":"星期二","timePoint":"14:32:44","postName":"财务总监"},{"userId":"ff80808151e6527a01521f64daf80ea2","userName":"十四郎","deptId":"YFB001","deptName":"研发部001","replyList":[],"reportId":"773ec724e0fe4f15832c74d2fa1f043f","visitLine":"5555","lineStoreNum":"555","visitStoreNum":"555","createTime":"2016-03-29","week":"星期二","timePoint":"14:20:48","postName":"财务总监"}]
     * firstResult : 0
     * maxResults : 2
     */

    public DataBean data;

    public static class DataBean {
        public int pageNo;
        public int pageSize;
        public int count;
        public int firstResult;
        public int maxResults;
        /**
         * userId : ff80808151e6527a01521f64daf80ea2
         * userName : 十四郎
         * deptId : YFB001
         * deptName : 研发部001
         * replyList : [{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"你们的话","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:01"},{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"这样才华还","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:06"},{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"这样才华还","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:06"},{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"这样","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:20"},{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"焊接机","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:22"},{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"给公告和","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:34"},{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"骨灰盒","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:39"},{"reportId":"a821a93f0b3a4d4ca7cc1e49fad997db","comment":"刚回家就好","replyBy":"十四郎","createBy":"ff80808151e6527a01521f64daf80ea2","createTime":"03月30日 10:39"}]
         * reportId : a821a93f0b3a4d4ca7cc1e49fad997db
         * visitLine : 100
         * lineStoreNum : 5555
         * visitStoreNum : 223
         * createTime : 2016-03-29
         * week : 星期二
         * timePoint : 14:32:44
         * postName : 财务总监
         */

        public List<DailyBean> list;

    }

    public class DailyBean {
        public String userId;
        public String userName;
        public String deptId;
        public String deptName;
        public String reportId;
        public String visitLine;
        public String lineStoreNum;
        public String visitStoreNum;
        public String createTime;
        public String week;
        public String timePoint;
        public String postName;
        public String tmplName;
        public String participant;
        /**
         * reportId : a821a93f0b3a4d4ca7cc1e49fad997db
         * comment : 你们的话
         * replyBy : 十四郎
         * createBy : ff80808151e6527a01521f64daf80ea2
         * createTime : 03月30日 10:01
         */

        public List<DailyCommentListBean.CommentBean> replyList;
        public List<DailyDetailsBean.FieldBean> fieldList;
        public List<String> picList;

        public int imgId;// 头像Id
        public DailyCommentListAdapter commentListAdapter;// 评论或回复列表适配器

        public int getImgId() {
            return imgId;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }

        public DailyCommentListAdapter getCommentListAdapter() {
            return commentListAdapter;
        }

        public void setCommentListAdapter(DailyCommentListAdapter commentListAdapter) {
            this.commentListAdapter = commentListAdapter;
        }
    }
}
