package com.salesman.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 发布对象列表实体
 * Created by LiHuai on 2016/2/1 0001.
 */
public class ReleaseObjectListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        /**
         * id : ff80808150ce53680150e9de2d845119
         * userName : 郑增艺
         */

        public List<ReleaseObjectBean> list;


    }

    public static class ReleaseObjectBean implements Parcelable {
        public String userId;
        public String userName;

        public int imgId;
        public boolean checked;

        public ReleaseObjectBean(Parcel in) {
            userId = in.readString();
            userName = in.readString();
            imgId = in.readInt();
            checked = in.readByte() != 0;
        }

        public static final Creator<ReleaseObjectBean> CREATOR = new Creator<ReleaseObjectBean>() {
            @Override
            public ReleaseObjectBean createFromParcel(Parcel in) {
                return new ReleaseObjectBean(in);
            }

            @Override
            public ReleaseObjectBean[] newArray(int size) {
                return new ReleaseObjectBean[size];
            }
        };

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public int getImgId() {
            return imgId;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(userId);
            dest.writeString(userName);
            dest.writeInt(imgId);
            dest.writeByte((byte) (checked ? 1 : 0));
        }
    }
}
