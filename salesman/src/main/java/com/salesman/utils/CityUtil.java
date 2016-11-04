package com.salesman.utils;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.application.SalesManApplication;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.AreaListBean;
import com.salesman.entity.BaseBean;
import com.salesman.network.BaseHelper;
import com.salesman.views.citypicker.CityPicker;
import com.salesman.views.citypicker.Cityinfo;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省、市、区工具类
 * Created by LiHuai on 2016/6/30 0030.
 */
public class CityUtil {
    private static final String TAG = CityUtil.class.getSimpleName();
    private OnAreaListener mListener;

    public void getAllCityUtil() {
        String url = Constant.moduleProvinceCityDistrict;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean && baseBean.success) {
                    UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
                    mUserConfig.saveShengShiQu(response).apply();
                    if (null != mListener) {
                        mListener.onAreaSuccess();
                    }
                } else {
                    if (null != mListener) {
                        mListener.onAreaFail();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != mListener) {
                    mListener.onAreaFail();
                }
            }
        });
        VolleyController.getInstance(SalesManApplication.getInstance()).addToQueue(post);
    }

    /**
     * 获取省份
     *
     * @return
     */
    public static List<Cityinfo> getProvinceList() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        AreaListBean areaListBean = GsonUtils.json2Bean(mUserConfig.getShengShiQu(), AreaListBean.class);
        List<Cityinfo> data = new ArrayList<>();
        if (null != areaListBean) {
            if (null != areaListBean.data) {
                List<AreaListBean.ProvinceBean> list = areaListBean.data;
                for (AreaListBean.ProvinceBean provinceBean : list) {
                    data.add(new Cityinfo(String.valueOf(provinceBean.id), provinceBean.name));
                }
            }
        }
        return data;
    }

    /**
     * 获取城市
     *
     * @return
     */
    public static HashMap<String, List<Cityinfo>> getCityData() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        AreaListBean areaListBean = GsonUtils.json2Bean(mUserConfig.getShengShiQu(), AreaListBean.class);
        HashMap<String, List<Cityinfo>> cityMap = new HashMap<>();
        if (null != areaListBean) {
            if (null != areaListBean.data) {
                List<AreaListBean.ProvinceBean> province = areaListBean.data;
                for (AreaListBean.ProvinceBean provinceBean : province) {
                    List<Cityinfo> cityList = new ArrayList<>();
                    String provinceId = String.valueOf(provinceBean.id);
                    if (null != provinceBean.regionList) {
                        List<AreaListBean.CityBean> list = provinceBean.regionList;
                        for (AreaListBean.CityBean cityBean : list) {
                            cityList.add(new Cityinfo(String.valueOf(cityBean.id), cityBean.name));
                        }
                        cityMap.put(provinceId, cityList);
                    }

                }
            }
        }
        return cityMap;
    }

    /**
     * 获取县区
     *
     * @return
     */
    public static HashMap<String, List<Cityinfo>> getCountyData() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        AreaListBean areaListBean = GsonUtils.json2Bean(mUserConfig.getShengShiQu(), AreaListBean.class);
        HashMap<String, List<Cityinfo>> countyMap = new HashMap<>();
        if (null != areaListBean) {
            if (null != areaListBean.data) {
                List<AreaListBean.ProvinceBean> province = areaListBean.data;
                for (AreaListBean.ProvinceBean provinceBean : province) {
                    if (null != provinceBean.regionList) {
                        List<AreaListBean.CityBean> cityList = provinceBean.regionList;
                        for (AreaListBean.CityBean cityBean : cityList) {
                            String cityId = String.valueOf(cityBean.id);
                            List<Cityinfo> countyList = new ArrayList<>();
                            if (null != cityBean.regionList) {
                                List<AreaListBean.AreaBean> areaList = cityBean.regionList;
                                for (AreaListBean.AreaBean areaBean : areaList) {
                                    countyList.add(new Cityinfo(String.valueOf(areaBean.id), areaBean.name));
                                }
                                countyMap.put(cityId, countyList);
                            }

                        }

                    }
                }
            }
        }
        return countyMap;
    }

    /**
     * 是否二次请求数据
     *
     * @return
     */
    public static boolean isSecondRequest() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        String shengShiQu = mUserConfig.getShengShiQu();
        if (TextUtils.isEmpty(shengShiQu)) {
            return true;
        }
        return false;
    }

    /**
     * 初始化城市选择器数据
     *
     * @param cityPicker
     */
    public static void initCityPicker(CityPicker cityPicker) {
        List<Cityinfo> province = getProvinceList();
        HashMap<String, List<Cityinfo>> city = getCityData();
        HashMap<String, List<Cityinfo>> county = getCountyData();
        if (null != province && !province.isEmpty() && null != city && !city.isEmpty() && null != county && !county.isEmpty()) {
            cityPicker.setDatas(province, city, county);
        }
    }

    public static String initShengShiQu(String province, String city, String county, String shengShiQu) {
        if (!TextUtils.isEmpty(shengShiQu)) {
            String[] diquArray = shengShiQu.split(";");
            for (int i = 0; i < diquArray.length; i++) {
                switch (i) {
                    case 0:
                        province = diquArray[0];
                        break;
                    case 1:
                        city = diquArray[1];
                        break;
                    case 2:
                        county = diquArray[2];
                        break;
                }
            }
        }
        return province + city + county;
    }

    public static void initShengShiQuCode(int provinceId, int cityId, int countyId, String shengShiQuCode) {
        if (!TextUtils.isEmpty(shengShiQuCode)) {
            String[] diquArray = shengShiQuCode.split(";");
            for (int i = 0; i < diquArray.length; i++) {
                switch (i) {
                    case 0:
                        provinceId = TextUtils.isEmpty(diquArray[0]) ? 0 : Integer.parseInt(diquArray[0]);
                        break;
                    case 1:
                        cityId = TextUtils.isEmpty(diquArray[1]) ? 0 : Integer.parseInt(diquArray[1]);
                        break;
                    case 2:
                        countyId = TextUtils.isEmpty(diquArray[2]) ? 0 : Integer.parseInt(diquArray[2]);
                        break;
                }
            }
        }
    }

    public void setOnAreaListener(OnAreaListener mListener) {
        this.mListener = mListener;
    }

    public interface OnAreaListener {
        void onAreaSuccess();

        void onAreaFail();

    }
}
