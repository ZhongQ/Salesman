package com.salesman.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.salesman.common.UserConfig;

/**
 * 用户配置
 * Created by LiHuai on 2016/02/23 0027.
 */
public class UserConfigPreference {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public UserConfigPreference(Context context) {
        mPreferences = context.getSharedPreferences("UserConfig", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public boolean getIsFirst() {
        return mPreferences.getBoolean(UserConfig.ISFIRST, true);
    }

    public int getHeadColor() {
        return mPreferences.getInt(UserConfig.HEADCOLOR, 0);
    }

    public boolean getGotoWork() {
        return mPreferences.getBoolean(UserConfig.GOTOWORK, false);
    }

    public boolean getGetOffWork() {
        return mPreferences.getBoolean(UserConfig.GETOFFWORK, false);
    }

    public String getDate() {
        return mPreferences.getString(UserConfig.DATE, "");// 签到时间
    }

    public String getLocationDate() {
        return mPreferences.getString(UserConfig.LOACATION_DATE, DateUtils.getYMD(System.currentTimeMillis()));
    }

    public String getLocationAddress() {
        return mPreferences.getString(UserConfig.LOCATION_ADDRESS, "");
    }

    public String getLatitude() {
        return mPreferences.getString(UserConfig.LATITUDE, "");
    }

    public String getLongitude() {
        return mPreferences.getString(UserConfig.LONGITUDE, "");
    }

    public long getUploadTime() {
        return mPreferences.getLong(UserConfig.UPLOAD_TIME, 0);
    }

    public boolean getTrackSet() {
        return mPreferences.getBoolean(UserConfig.TRACK_SET, true);
    }

    public boolean getHandExit() {
        return mPreferences.getBoolean(UserConfig.HAND_EXIT, false);
    }

    public String getDingGeLine() {
        return mPreferences.getString(UserConfig.DINGGE_LINE, "");
    }

    public String getClientType() {
        return mPreferences.getString(UserConfig.CLIENT_TYPE, "");
    }

    public String getShengShiQu() {
        return mPreferences.getString(UserConfig.SHENG_SHI_QU, "");
    }

    public String getZhanJiFilter() {
        return mPreferences.getString(UserConfig.ZHANJI_FILTER, "");
    }

    public String getSalesmanLine() {
        return mPreferences.getString(UserConfig.SALESMAN_LINE, "");
    }

    public String getOutLineClient() {
        return mPreferences.getString(UserConfig.LINE_CLIENT, "");
    }

    public String getNewActionGuide() {
        return mPreferences.getString(UserConfig.NEW_ACTION_GUIDE, "");
    }

    public UserConfigPreference saveIsFirst(boolean isFirst) {
        mEditor.putBoolean(UserConfig.ISFIRST, isFirst);
        return this;
    }

    public UserConfigPreference saveHeadColor(int color) {
        mEditor.putInt(UserConfig.HEADCOLOR, color);
        return this;
    }

    public UserConfigPreference saveGoToWork(boolean goToWork) {
        mEditor.putBoolean(UserConfig.GOTOWORK, goToWork);
        return this;
    }

    public UserConfigPreference saveGetOffWork(boolean getOffWork) {
        mEditor.putBoolean(UserConfig.GETOFFWORK, getOffWork);
        return this;
    }

    public UserConfigPreference saveDate(String date) {
        mEditor.putString(UserConfig.DATE, date);
        return this;
    }

    public UserConfigPreference saveLocationDate(String date) {
        mEditor.putString(UserConfig.LOACATION_DATE, date);
        return this;
    }

    public UserConfigPreference saveLocationAddress(String locationAddress) {
        mEditor.putString(UserConfig.LOCATION_ADDRESS, locationAddress);
        return this;
    }

    public UserConfigPreference saveLatitude(String latitude) {
        mEditor.putString(UserConfig.LATITUDE, latitude);
        return this;
    }

    public UserConfigPreference saveLongitude(String longitude) {
        mEditor.putString(UserConfig.LONGITUDE, longitude);
        return this;
    }

    public UserConfigPreference saveUploadTime(long uploadTime) {
        mEditor.putLong(UserConfig.UPLOAD_TIME, uploadTime);
        return this;
    }

    public UserConfigPreference saveTrackSet(boolean isOpen) {
        mEditor.putBoolean(UserConfig.TRACK_SET, isOpen);
        return this;
    }

    public UserConfigPreference saveHandExit(boolean exit) {
        mEditor.putBoolean(UserConfig.HAND_EXIT, exit);
        return this;
    }

    public UserConfigPreference saveDingGeLine(String dingGeLine) {
        mEditor.putString(UserConfig.DINGGE_LINE, dingGeLine);
        return this;
    }

    public UserConfigPreference saveClientType(String clientType) {
        mEditor.putString(UserConfig.CLIENT_TYPE, clientType);
        return this;
    }

    public UserConfigPreference saveShengShiQu(String shengShiQu) {
        mEditor.putString(UserConfig.SHENG_SHI_QU, shengShiQu);
        return this;
    }

    public UserConfigPreference saveZhanJiFilter(String zhanji) {
        mEditor.putString(UserConfig.ZHANJI_FILTER, zhanji);
        return this;
    }

    public UserConfigPreference saveSalesmanLine(String salesmanLine) {
        mEditor.putString(UserConfig.SALESMAN_LINE, salesmanLine);
        return this;
    }

    public UserConfigPreference saveOutLineClient(String lineClient) {
        mEditor.putString(UserConfig.LINE_CLIENT, lineClient);
        return this;
    }

    public UserConfigPreference saveNewActionGuide(String pageName) {
        mEditor.putString(UserConfig.NEW_ACTION_GUIDE, pageName);
        return this;
    }

    /**
     * 最终保存方法（不调用则不保存）
     */
    public UserConfigPreference apply() {
        mEditor.apply();
        return this;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     * @return
     */
    public UserConfigPreference remove(String key) {
        mEditor.remove(key);
        return this;
    }

    public void clear() {
        mEditor.clear();
        mEditor.apply();
    }
}
