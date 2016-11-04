package com.salesman.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 我的下属实体
 * Created by LiHuai on 2016/3/25 0025.
 */
public class SubordinateListBean extends BaseBean implements Serializable{
    public DataBean data;

    public static class DataBean {
        /**
         * id : ff8080815120dac20151322466b56526
         * userName : 何丹
         */

        public List<XiaShuBean> userList;
        /**
         * deptId : CCC03
         * deptName : 三级部门
         * deptPid : BBB02
         * total : 1
         */

        public List<DepartmentBean> deptList;

        public List<GuideBean> navList;
    }

    public class DepartmentBean {
        public String deptId;
        public String deptName;
        public String deptPid;
        public int total;
    }

    public class GuideBean {

        /**
         * deptId : YFB001
         * deptName : 研发部001
         * deptPid :
         * total : 0
         */

        public String deptId;
        public String deptName;
        public String deptPid;
        public int total;
    }

    public class XiaShuBean implements Serializable {

        /**
         * userId : ff80808151e6527a01521f64daf80ea6
         * userName : 李坏
         * deptId : TEST001
         * deptName :
         * userType : 1
         */

        public String userId;
        public String userName;
        public String deptId;
        public String deptName;
        public String userType;

        public int imgId;

        public int getImgId() {
            return imgId;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }
    }
}
