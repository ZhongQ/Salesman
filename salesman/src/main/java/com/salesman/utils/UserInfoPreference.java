package com.salesman.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.salesman.common.UserConfig;

/**
 * 用户信息
 * Created by LiHuai on 2016/1/27 0027.
 */
public class UserInfoPreference {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public UserInfoPreference(Context context) {
        mPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public boolean getIsFirst() {
        return mPreferences.getBoolean(UserConfig.ISFIRST, true);
    }

    public String getUserId() {
        return mPreferences.getString(UserConfig.USREID, "");
    }

    public String getUserName() {
        return mPreferences.getString(UserConfig.USERNAME, "");
    }

    public String getUserNickName() {
        return mPreferences.getString(UserConfig.NICKNAME, "");
    }

    public String getMobile() {
        return mPreferences.getString(UserConfig.MOBILE, "");
    }

    public String getDeviceUUID() {
        return mPreferences.getString(UserConfig.DEVICEUUID, "");
    }

    public String getSessionID() {
        return mPreferences.getString(UserConfig.SESSIONID, "");
    }

    public String getToken() {
        return mPreferences.getString(UserConfig.TOKEN, "");
    }

    public int getSex() {
        return mPreferences.getInt(UserConfig.GENDER, 1);
    }

    public String getUserType() {
        return mPreferences.getString(UserConfig.USERTYPE, "0");
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
        return mPreferences.getString(UserConfig.DATE, "");
    }

    public String getPostName() {
        return mPreferences.getString(UserConfig.POSTNAME, "");
    }

    public String getLocationAddress() {
        return mPreferences.getString(UserConfig.LOCATION_ADDRESS, "");
    }

    public long getUploadTrackTime() {
        return mPreferences.getLong(UserConfig.UPLOAD_TRACK_TIME, 60000);
    }

    public String getDeptId() {
        return mPreferences.getString(UserConfig.DEPTID, "");
    }

    public String getDeptName() {
        return mPreferences.getString(UserConfig.DEPTNAME, "");
    }

    public String getAreaId() {
        return mPreferences.getString(UserConfig.AREAID, "");
    }

    public String getBDType() {
        return mPreferences.getString(UserConfig.BDTYPE, "");
    }

    public UserInfoPreference saveIsFirst(boolean isFirst) {
        mEditor.putBoolean(UserConfig.ISFIRST, isFirst);
        return this;
    }

    public UserInfoPreference saveUserId(String userId) {
        mEditor.putString(UserConfig.USREID, userId);
        return this;
    }

    public UserInfoPreference saveUserName(String userName) {
        mEditor.putString(UserConfig.USERNAME, userName);
        return this;
    }

    public UserInfoPreference saveNickName(String nickName) {
        mEditor.putString(UserConfig.NICKNAME, nickName);
        return this;
    }

    public UserInfoPreference saveMobile(String mobile) {
        mEditor.putString(UserConfig.MOBILE, mobile);
        return this;
    }

    public UserInfoPreference saveDeviceUUID(String deviceUUID) {
        mEditor.putString(UserConfig.DEVICEUUID, deviceUUID);
        return this;
    }

    public UserInfoPreference saveSessionid(String sessionid) {
        mEditor.putString(UserConfig.SESSIONID, sessionid);
        return this;
    }

    public UserInfoPreference saveToken(String token) {
        mEditor.putString(UserConfig.TOKEN, token);
        return this;
    }

    public UserInfoPreference saveSex(int gender) {
        mEditor.putInt(UserConfig.GENDER, gender);
        return this;
    }

    public UserInfoPreference saveUserType(String userType) {
        mEditor.putString(UserConfig.USERTYPE, userType);
        return this;
    }

    public UserInfoPreference saveHeadColor(int color) {
        mEditor.putInt(UserConfig.HEADCOLOR, color);
        return this;
    }

    public UserInfoPreference saveGoToWork(boolean goToWork) {
        mEditor.putBoolean(UserConfig.GOTOWORK, goToWork);
        return this;
    }

    public UserInfoPreference saveGetOffWork(boolean getOffWork) {
        mEditor.putBoolean(UserConfig.GETOFFWORK, getOffWork);
        return this;
    }

    public UserInfoPreference saveDate(String date) {
        mEditor.putString(UserConfig.DATE, date);
        return this;
    }

    public UserInfoPreference saveLocationAddress(String locationAddress) {
        mEditor.putString(UserConfig.LOCATION_ADDRESS, locationAddress);
        return this;
    }

    public UserInfoPreference savePostName(String postName) {
        mEditor.putString(UserConfig.POSTNAME, postName);
        return this;
    }

    public UserInfoPreference saveUploadTrackTime(long hz) {
        mEditor.putLong(UserConfig.UPLOAD_TRACK_TIME, hz);
        return this;
    }

    public UserInfoPreference saveDeptId(String deptId) {
        mEditor.putString(UserConfig.DEPTID, deptId);
        return this;
    }

    public UserInfoPreference saveDeptName(String deptName) {
        mEditor.putString(UserConfig.DEPTNAME, deptName);
        return this;
    }

    public UserInfoPreference saveAreaId(String areaId) {
        mEditor.putString(UserConfig.AREAID, areaId);
        return this;
    }

    public UserInfoPreference saveBDType(String bdType) {
        mEditor.putString(UserConfig.BDTYPE, bdType);
        return this;
    }

    /**
     * 最终保存方法（不调用则不保存）
     */
    public UserInfoPreference apply() {
        mEditor.apply();
        return this;
    }

    public void clear() {
        mEditor.clear();
        mEditor.apply();
    }
}
