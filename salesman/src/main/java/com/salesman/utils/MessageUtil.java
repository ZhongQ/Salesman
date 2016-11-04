package com.salesman.utils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.application.SalesManApplication;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.NewestMesBean;
import com.salesman.network.BaseHelper;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.Map;

/**
 * 消息工具类
 * Created by LiHuai on 2016/4/25 0025.
 */
public class MessageUtil {
    public static final String TAG = MessageUtil.class.getSimpleName();
    private MessageListener messageListener;

    public interface MessageListener {
        void messageSuccess(NewestMesBean.NewsBean newsBean, CharSequence msgsContent);
    }

    public MessageUtil(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    /**
     * 获取最新消息
     */
    public void getNewestMessage() {
        String url = Constant.moduleHomeMessageRemind;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("userId", SalesManApplication.g_GlobalObject.getmUserInfo().getUserId());
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                NewestMesBean newestMesBean = GsonUtils.json2Bean(response, NewestMesBean.class);
                if (null != newestMesBean && newestMesBean.success && null != newestMesBean.data) {
                    setNewestData(newestMesBean.data);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleyController.getInstance(SalesManApplication.getInstance()).addToQueue(post);
    }

    /**
     * 处理最新消息
     *
     * @param dataBean
     */
    private void setNewestData(NewestMesBean.NewsBean dataBean) {
        StringBuffer sb = new StringBuffer();
        if (null != dataBean.notice && null != dataBean.daily) {
            if (dataBean.notice.update || dataBean.daily.update) {
                sb.append("您有");
            }
            if (dataBean.notice.update) {// 有公告更新
                sb.append("新公告");
                if (dataBean.daily.update) {
                    sb.append("和");
                }
            }
            if (dataBean.daily.update) {// 有日报更新
                sb.append(dataBean.daily.total);
                sb.append("条日志消息");
            } else {
            }
            sb.append("!");
            sb.append("立即查看");
        }
        if (null != messageListener) {
            messageListener.messageSuccess(dataBean, StringUtil.getSpannable(sb.toString()));
        }
    }
}
