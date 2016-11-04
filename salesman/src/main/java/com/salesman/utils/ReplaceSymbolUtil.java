package com.salesman.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.studio.jframework.utils.LogUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 特殊符号替换工具类
 * Created by 20150604 on 2015/10/21.
 */
public class ReplaceSymbolUtil {
    public final static String OLD_HUANHANG_STRING = "\n";
    public final static String HUANHANG_STRING = "[:rn]";
    // 无内容时替代符号
    public final static String EMPTY_STRING = "[:no]";
    // 分割符
    public final static String DIVISION = "[:@]";
    // 分号
    public final static String SEMICOLON = "[:;]";
    // @@
    public final static String AITE = "@@";

    /**
     * 把换行符替换成"[:rn]"
     */
    public synchronized static String replaceHuanHang(String oldString) {
        if (!TextUtils.isEmpty(oldString)
                && oldString.contains(OLD_HUANHANG_STRING)) {
            String newString = oldString.replace(OLD_HUANHANG_STRING,
                    HUANHANG_STRING);
            return newString;
        }
        return oldString;
    }

    /**
     * 把"[:rn]"替换成换行符
     */
    public synchronized static String reverseReplaceHuanHang(String oldString) {
        if (!TextUtils.isEmpty(oldString)
                && oldString.contains(HUANHANG_STRING)) {
            String newString = oldString.replace(HUANHANG_STRING,
                    OLD_HUANHANG_STRING);
            return newString;
        }
        return oldString;
    }

    /**
     * 将中文URLEncoder编码
     *
     * @return
     */
    public static String transcodeToUTF8(String chString) {
        if (!TextUtils.isEmpty(chString)) {
            try {
                String strUTF8 = URLEncoder.encode(chString, "UTF-8");
                LogUtils.d("strUTF8========", strUTF8);
                return strUTF8;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 截取字符串后两位
     *
     * @param str
     * @return
     */
    public static String cutStringLastTwo(@NonNull String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.length() > 2) {
            return str.substring(str.length() - 2, str.length());
        } else {
            return str;
        }
    }

}
