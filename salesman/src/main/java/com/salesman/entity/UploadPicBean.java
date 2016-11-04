package com.salesman.entity;

import android.text.TextUtils;

import java.util.List;

/**
 * 上传图片实体
 * Created by LiHuai on 2016/2/17 0017.
 */
public class UploadPicBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        /**
         * filename : /group1/M00/00/45/cEpChlbDBjmASgSbAAvqH_kipG8428.jpg
         */

        public List<ImagePath> list;

    }

    public static class ImagePath {
        public String filename;

        public String toString(ImagePath imagePath) {
            return imagePath.filename + ",";
        }

        @Override
        public String toString() {
            if (!TextUtils.isEmpty(filename)) {
                return this.filename + ",";
            }
            return "";
        }
    }
}
