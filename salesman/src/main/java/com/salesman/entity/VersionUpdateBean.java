package com.salesman.entity;

/**
 * Created by LiHuai on 2016/2/25 0025.
 */
public class VersionUpdateBean extends BaseBean{
    /**
     * name : 阿街足迹
     * version : 1
     * changelog : First release
     * updatedTime : 1456293132
     * versionShort : 1.0
     * build : 1
     * installUrl : http://download.fir.im/v2/app/install/56cd44def2fc423a9c000032?download_token=0cf1e30ec7de896075934730d97ba1f3
     * updateUrl : http://fir.im/ez1p
     */

    public DataBean data;

    public static class DataBean {
        public String name;
        public String version;
        public String changelog;
        public String updatedTime;
        public String versionShort;
        public String build;
        public String installUrl;
        public String updateUrl;
    }
}
