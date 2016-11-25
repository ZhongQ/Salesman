package com.salesman.entity;

import java.util.List;

/**
 * 签到记录列表实体
 * Created by LiHuai on 2016/1/27 0027.
 */
public class SigninRecordListBean extends BaseBean {

    /**
     * pageNo : 1
     * pageSize : 10
     * count : 19
     * list : [{"id":"6a2e1e7e524646d0b8df34d561e4d946","remarks":"8888888888","createBy":"ff80808151e6527a01521f64daf80ea7","createTime":"2016-01-27 16:09:36.0","groupId":"1","address":"445566","signTime":"134667","type":2},{"id":"d82fb06320d94763aebdf8af1f8c0843","remarks":"8888888888","createBy":"ff80808151e6527a01521f64daf80ea7","createTime":"2016-01-27 15:56:20.0","groupId":"1","address":"445566","signTime":"134667","type":2},{"id":"bde12706e5ee4a649d95f68d9f5a73a1","remarks":"8888888888","createBy":"ff80808151e6527a01521f64daf80ea7","createTime":"2016-01-27 15:15:13.0","groupId":"1","address":"445566","signTime":"134667","type":2},{"id":"f14019c18270496e9dd2aff6064e275c","remarks":"8888888888","createBy":"ff80808151e6527a01521f64daf80ea7","createTime":"2016-01-27 15:15:11.0","groupId":"1","address":"445566","signTime":"134667","type":2},{"id":"78a7aa80f36842f0aff72b059b3d2a08","remarks":"8888888888","createBy":"ff80808151e6527a01521f64daf80ea7","createTime":"2016-01-27 15:14:55.0","groupId":"1","address":"445566","signTime":"134667","type":2},{"id":"21ba279adf0d4980aa048dc4bec0c7b0","remarks":"8888888888","createBy":"ff80808151e6527a01521f64daf80ea7","createTime":"2016-01-27 15:14:27.0","groupId":"1","address":"445566","signTime":"134667","type":2},{"id":"667278e53ee04de398d4d24ecbc8ffac","createBy":"ff80808151e6527a01521f64daf80ea7","createTime":"2016-01-26 18:11:49.0","groupId":"1","picUrl":"http://www.izjjf.cn/group1/M00/00/27/oYYBAFWtDWOAWbGMAAGG1rlIDgc838.jpg","address":"深圳武汉大学大楼A座701","signTime":"2016年1月19日11:47:17","type":2},{"id":"127455eeca8b48ad8e98524d87fd4e2d","createBy":"ff80808151e6527a01521f64daf80ea7","createTime":"2016-01-26 17:57:27.0","groupId":"1","picUrl":"http://www.izjjf.cn/group1/M00/00/27/oYYBAFWtDWOAWbGMAAGG1rlIDgc838.jpg","address":"深圳武汉大学大楼A座701","signTime":"2016年1月19日11:47:17","type":2},{"id":"1389ae97988c49f899b6213a84ff3b5f","createBy":"ff80808151e6527a01521f64daf80ea7","createTime":"2016-01-26 17:57:26.0","groupId":"1","picUrl":"http://www.izjjf.cn/group1/M00/00/27/oYYBAFWtDWOAWbGMAAGG1rlIDgc838.jpg","address":"深圳武汉大学大楼A座701","signTime":"2016年1月19日11:47:17","type":2},{"id":"64f946d5bba8485a90689ecc207d14ba","createBy":"ff80808151e6527a01521f64daf80ea7","createTime":"2016-01-26 17:57:25.0","groupId":"1","picUrl":"http://www.izjjf.cn/group1/M00/00/27/oYYBAFWtDWOAWbGMAAGG1rlIDgc838.jpg","address":"深圳武汉大学大楼A座701","signTime":"2016年1月19日11:47:17","type":2}]
     * firstResult : 0
     * maxResults : 10
     */

    public SigninListBean data;

    public static class SigninListBean {
        public int pageNo;
        public int pageSize;
        public int count;
        public int firstResult;
        public int maxResults;
        /**
         * id : 6a2e1e7e524646d0b8df34d561e4d946
         * remarks : 8888888888
         * createBy : ff80808151e6527a01521f64daf80ea7
         * createTime : 2016-01-27 16:09:36.0
         * groupId : 1
         * address : 445566
         * signTime : 134667
         * type : 2
         */

        public List<SigninBean> list;

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setFirstResult(int firstResult) {
            this.firstResult = firstResult;
        }

        public void setMaxResults(int maxResults) {
            this.maxResults = maxResults;
        }

        public void setList(List<SigninBean> list) {
            this.list = list;
        }

        public int getPageNo() {
            return pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getCount() {
            return count;
        }

        public int getFirstResult() {
            return firstResult;
        }

        public int getMaxResults() {
            return maxResults;
        }

        public List<SigninBean> getList() {
            return list;
        }


    }

    public static class SigninBean {
        public String id;
        public String remarks;
        public String createBy;
        public String createTime;
        public String groupId;
        public String address;
        public String signTime;
        public int type;
        public String typeName;
        public String picUrl;// 版本V1.4.0弃用
        public String visitLine;
        public String visitCust;
        public List<String> picList;// 版本V1.4.0
        public String markType;
        public int signSeq;

        public void setId(String id) {
            this.id = id;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setSignTime(String signTime) {
            this.signTime = signTime;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public String getRemarks() {
            return remarks;
        }

        public String getCreateBy() {
            return createBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getGroupId() {
            return groupId;
        }

        public String getAddress() {
            return address;
        }

        public String getSignTime() {
            return signTime;
        }

        public int getType() {
            return type;
        }
    }
}
