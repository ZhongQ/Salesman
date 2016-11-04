package com.salesman.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.application.SalesManApplication;
import com.salesman.views.CustomImageSpan;

import java.text.DecimalFormat;

/**
 * 字符串工具类
 * Created by LiHuai on 2016/3/30 0030.
 */
public class StringUtil {
    public static final int BLACK = 0xFF000000;
    public static final int BLUE = 0xFF007ed2;
    public static final int GREY = 0xFFAAAAAA;

    public static DecimalFormat mNumFormat = new DecimalFormat("###,###,###,###");

    /**
     * 字符串
     * 用于xx回复xx
     *
     * @param str1 回复人
     * @param str2 被回复人
     * @return
     */
    public static SpannableString getSpannable(String str1, String str2) {
        if (!TextUtils.isEmpty(str1)) {
            if (!TextUtils.isEmpty(str2)) {
                StringBuffer sb = new StringBuffer();
                sb.append(str1);
                sb.append("回复");
                sb.append(str2);
                SpannableString spanText = new SpannableString(sb);
                spanText.setSpan(new ForegroundColorSpan(BLACK), str1.length(), str1.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return spanText;
            } else {
                return new SpannableString(str1);
            }

        } else {
            return new SpannableString("");
        }
    }

    /**
     * @param str1    回复人
     * @param str2    被回复人
     * @param content 回复内容
     * @return
     */
    public static SpannableString getSpannable(String str1, String str2, String content) {
        if (!TextUtils.isEmpty(str1)) {
            if (!TextUtils.isEmpty(str2)) {
                if (!TextUtils.isEmpty(content)) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(str1);
                    sb.append("回复");
                    sb.append(str2);
                    sb.append(":");
                    sb.append(content);
                    SpannableString spanText = new SpannableString(sb);
                    spanText.setSpan(new ForegroundColorSpan(BLACK), str1.length(), str1.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanText.setSpan(new ForegroundColorSpan(BLACK), sb.length() - content.length() - 1, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return spanText;
                } else {
                    StringBuffer sb3 = new StringBuffer();
                    sb3.append(str1);
                    sb3.append("回复");
                    sb3.append(str2);
                    sb3.append(":");
                    SpannableString spanText = new SpannableString(sb3);
                    spanText.setSpan(new ForegroundColorSpan(BLACK), str1.length(), str1.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanText.setSpan(new ForegroundColorSpan(BLACK), sb3.length() - 1, sb3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return spanText;
                }
            } else {
                if (!TextUtils.isEmpty(content)) {
                    StringBuffer sb2 = new StringBuffer();
                    sb2.append(str1);
                    sb2.append(":");
                    sb2.append(content);
                    SpannableString spanText2 = new SpannableString(sb2);
                    spanText2.setSpan(new ForegroundColorSpan(BLACK), str1.length(), sb2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return spanText2;
                } else {
                    return new SpannableString("");
                }
            }
        } else {
            return new SpannableString("");
        }
    }

    /**
     * @param message 首页消息
     * @return
     */
    public static SpannableString getSpannable(String message) {
        if (!TextUtils.isEmpty(message) && message.length() > 4) {
            SpannableString spanText = new SpannableString(message);
            spanText.setSpan(new ForegroundColorSpan(BLUE), message.length() - 4, message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spanText;
        } else {
            return new SpannableString("");
        }
    }

    /**
     * 消息列表之日志消息
     *
     * @param name    被回复人名称
     * @param message 回复内容
     * @return
     */
    public static SpannableString getSpannStrMsgs(String name, String message) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(message)) {
            StringBuffer sb = new StringBuffer();
            sb.append("回复了");
            sb.append(name);
            sb.append(":");
            sb.append(ReplaceSymbolUtil.reverseReplaceHuanHang(message));// 换行符替换
            SpannableString ss = new SpannableString(sb);
            ss.setSpan(new ForegroundColorSpan(BLUE), 3, name.length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ss;
        } else {
            if (TextUtils.isEmpty(message)) {
                return new SpannableString("");
            }
            return new SpannableString(ReplaceSymbolUtil.reverseReplaceHuanHang(message));
        }
    }

    /**
     * 消息列表之新日志消息
     *
     * @param name
     * @return
     */
    public static SpannableString getSpannStrMsgs(String name) {
        if (!TextUtils.isEmpty(name)) {
            StringBuffer sb = new StringBuffer();
            sb.append("您收到");
            sb.append(name);
            sb.append("的日志");
            SpannableString ss = new SpannableString(sb);
            ss.setSpan(new ForegroundColorSpan(BLACK), 3, name.length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ss;
        } else {
            return new SpannableString("");
        }
    }

    /**
     * 发公告指定对象字符串
     *
     * @param obj
     * @return
     */
    public static SpannableString getSpannStrNoticeReleaseObj(String obj) {
        if (!TextUtils.isEmpty(obj)) {
            StringBuffer sb = new StringBuffer();
            sb.append("发送对象：   @ ");
            sb.append(obj);
            int index = sb.indexOf("@");
            SpannableString ss = new SpannableString(sb);
            ss.setSpan(new ForegroundColorSpan(GREY), 0, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ss;
        } else {
            return new SpannableString("");
        }
    }

    /**
     * 将图片插入文字中
     *
     * @param context
     * @param start
     * @param end
     * @param imgRes
     * @param text
     * @return
     */
    public static SpannableString setImgToText(@NonNull Context context, int start, int end, int imgRes, @NonNull String text) {
        StringBuffer sb = new StringBuffer(text);
        if (end - start <= 1 && start != end) {
            sb.insert(start, "A  ");// 图片右边与文字之间有间距
        }
        SpannableString spanText = new SpannableString(sb);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);
        CustomImageSpan imageSpan = new CustomImageSpan(context, bitmap);// 自定义图片居中显示
        spanText.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        return spanText;
    }


    /**
     * 输入框输入行数控制
     *
     * @param maxLines
     * @param s
     * @param editText
     */
    public static void setEditTextMaxLines(int maxLines, Editable s, EditText editText) {
        int lines = editText.getLineCount();
        // 限制最大输入行数
        if (lines > maxLines) {
            String str = s.toString();
            int cursorStart = editText.getSelectionStart();
            int cursorEnd = editText.getSelectionEnd();
            if (cursorStart == cursorEnd && cursorStart < str.length() && cursorStart >= 1) {
                str = str.substring(0, cursorStart - 1) + str.substring(cursorStart);
            } else {
                str = str.substring(0, s.length() - 1);
            }
            // setText会触发afterTextChanged的递归
            editText.setText(str);
            // setSelection用的索引不能使用str.length()否则会越界
            editText.setSelection(editText.getText().length());
        }
    }

    /**
     * 截取小数位
     *
     * @param numStr
     * @return
     */
    public static int getStringToInt(String numStr) {
        if (!TextUtils.isEmpty(numStr)) {
            String[] num = numStr.split("\\.");
            if (num.length > 0 && !TextUtils.isEmpty(num[0])) {
                return Integer.parseInt(num[0]);
            } else {
                return Integer.parseInt(numStr);
            }
        }
        return 0;
    }

    public static String judgeStr(String str) {
        if ("1".equals(str)) {
            return "是";
        } else {
            return "否";
        }
    }

    /**
     * 是否打开新功能引导页
     *
     * @param pageName
     * @return
     */
    public static boolean isOpenGuide(String pageName) {
        String str = SalesManApplication.g_GlobalObject.getmUserConfig().getNewActionGuide();
        if (!TextUtils.isEmpty(str)) {
            return str.contains(pageName);
        } else {
            return false;
        }
    }

    /**
     * 消息数量转换
     *
     * @param total
     * @return
     */
    public static String getMsgsNum(String total) {
        if (!TextUtils.isEmpty(total)) {
            int num = Integer.parseInt(total);
            if (num > 0 && num <= 99) {
                return String.valueOf(num);
            } else if (num > 99) {
                return "99+";
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

    /**
     * 文字内容和颜色
     *
     * @param context
     * @param tv
     * @param status
     */
    public static void setTextAndColor(Context context, TextView tv, int status) {
        switch (status) {
            case 1:
                tv.setText("已下单");
                break;
            case 2:
                tv.setText("已派单");
                tv.setTextColor(context.getResources().getColor(R.color.color_0090ff));
                break;
            case 3:
                tv.setText("已提单");
                break;
            case 4:
                tv.setText("已打印");
                break;
            case 5:
                tv.setText("已送达");
                tv.setTextColor(context.getResources().getColor(R.color.color_666666));
                break;
            case 6:
                tv.setText("已取消");
                tv.setTextColor(context.getResources().getColor(R.color.color_ff3636));
                break;
        }
        tv.setTextColor(context.getResources().getColor(R.color.color_ff3636));
    }

    public static String formatNumbers(float args) {
        return mNumFormat.format(args);
    }

    public static String formatNumbers(int args) {
        return mNumFormat.format(args);
    }
}
