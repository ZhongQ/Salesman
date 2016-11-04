package com.salesman.entity;

import android.text.TextUtils;

import com.salesman.utils.ReplaceSymbolUtil;

import java.util.List;

/**
 * 发日报实体
 * Created by LiHuai on 2016/3/25 0025.
 */
public class ReleaseDailyBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        /**
         * type : salesman_report
         * fieldCnName : 今日拜访路线
         * fieldEnName : StringType
         * fieldType : 1
         * isRequired : 1
         */

        public List<ReleaseListBean> list;
    }

    public class ReleaseListBean {
        public String type;
        public String fieldCnName;
        public String fieldEnName;
        public String fieldVal;
        public int fieldType;
        public int isRequired;
        public int length;

        public String value = "";

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            if (!TextUtils.isEmpty(this.value) && !TextUtils.isEmpty(this.value.trim())) {
                return this.fieldCnName + ReplaceSymbolUtil.DIVISION + this.value.trim() + ReplaceSymbolUtil.SEMICOLON;
            } else {
                return this.fieldCnName + ReplaceSymbolUtil.DIVISION + ReplaceSymbolUtil.EMPTY_STRING + ReplaceSymbolUtil.SEMICOLON;
            }
        }
    }
}
