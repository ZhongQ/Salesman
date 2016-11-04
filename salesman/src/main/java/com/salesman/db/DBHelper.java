package com.salesman.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.salesman.entity.AppLogDBBean;
import com.salesman.entity.TrackDBBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiHuai on 2016/3/17 0017.
 */
public class DBHelper {
    private static DBHelper mInstance;
    private SQLiteDatabase db;
    private SqliteHelper dbHelper;

    public static DBHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBHelper.class) {
                if (mInstance == null) {
                    mInstance = new DBHelper(context);
                }
            }
        }
        return mInstance;
    }

    public DBHelper(Context context) {
        dbHelper = new SqliteHelper(context, SqliteHelper.DATABASE_NAME, null, SqliteHelper.DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    /*往足迹表中添加数据 ，返回-1表示添加失败 */
    public long insertTrackToDB(TrackDBBean bean) {
        if (null != bean) {
            ContentValues values = new ContentValues();
            values.put(TrackDBBean.TRACK_DATE, bean.getTrack_date());
            values.put(TrackDBBean.LONGITUDE, bean.getLongitude());
            values.put(TrackDBBean.LATITUDE, bean.getLatitude());
            values.put(TrackDBBean.CREATE_TIME, bean.getCreate_time());
            if (!TextUtils.isEmpty(bean.getAddress())) {
                values.put(TrackDBBean.ADDRESS, bean.getAddress());
            }
            return db.insert(SqliteHelper.TRACK_TRABLE_NAME, null, values);
        }
        return -1;
    }

    /*查询足迹, 根据日期*/
    public List<TrackDBBean> queryTrack() {
        List<TrackDBBean> list = new ArrayList<>();
        Cursor cursor = db.query(SqliteHelper.TRACK_TRABLE_NAME, null, null, null, null, null, TrackDBBean.CREATE_TIME + " asc", "100");
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            TrackDBBean bean = new TrackDBBean();
            bean.set_id(cursor.getString(0));
            bean.setTrack_date(cursor.getString(1));
            bean.setLongitude(cursor.getDouble(2));
            bean.setLatitude(cursor.getDouble(3));
            bean.setAddress(cursor.getString(4));
            bean.setCreate_time(cursor.getString(5));
            list.add(bean);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /*删除足迹，根据日期*/
    public void deleteTrack(String date, int type) {
        switch (type) {
            case 1:
                db.delete(SqliteHelper.TRACK_TRABLE_NAME, TrackDBBean.TRACK_DATE + "=?", new String[]{date});
                break;
            case 2:
                db.delete(SqliteHelper.TRACK_TRABLE_NAME, TrackDBBean.TRACK_DATE + "!=?", new String[]{date});
                break;
        }
    }

    /*删除足迹，根据创建时间*/
    public void deleteTrackByTime(List<TrackDBBean> list) {
        StringBuffer sql = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sql.append(list.get(i).getCreate_time());
            } else {
                sql.append(list.get(i).getCreate_time() + ",");
            }
        }
        if (!TextUtils.isEmpty(sql)) {
            db.execSQL("delete from " + SqliteHelper.TRACK_TRABLE_NAME + " where " + TrackDBBean.CREATE_TIME + " in (" + sql.toString() + ")");
        }
    }

    /*删除所有足迹*/
    public void deleteAllTrack() {
        db.execSQL("delete from " + SqliteHelper.TRACK_TRABLE_NAME);
    }

//    /*往足迹记录表中添加数据 ，返回-1表示添加失败 */
//    public long insertToTrackRecordDB(TrackDBBean bean) {
//        if (null != bean) {
//            ContentValues values = new ContentValues();
//            values.put(TrackDBBean.TRACK_DATE, bean.getTrack_date());
//            values.put(TrackDBBean.LONGITUDE, bean.getLongitude());
//            values.put(TrackDBBean.LATITUDE, bean.getLatitude());
//            values.put(TrackDBBean.CREATE_TIME, bean.getCreate_time());
//            if (!TextUtils.isEmpty(bean.getAddress())) {
//                values.put(TrackDBBean.ADDRESS, bean.getAddress());
//            }
//            return db.insert(SqliteHelper.TRACK_RECORD_TRABLE_NAME, null, values);
//        }
//        return -1;
//    }
//
//    /*查询足迹记录表, 根据日期*/
//    public List<TrackDBBean> queryTrackRecord() {
//        List<TrackDBBean> list = new ArrayList<>();
//        Cursor cursor = db.query(SqliteHelper.TRACK_RECORD_TRABLE_NAME, null, null, null, null, null, TrackDBBean.CREATE_TIME + " desc");
//        cursor.moveToFirst();
//        while (cursor.isAfterLast() == false) {
//            TrackDBBean bean = new TrackDBBean();
//            bean.set_id(cursor.getString(0));
//            bean.setTrack_date(cursor.getString(1));
//            bean.setLongitude(cursor.getDouble(2));
//            bean.setLatitude(cursor.getDouble(3));
//            bean.setAddress(cursor.getString(4));
//            bean.setCreate_time(cursor.getString(5));
//            list.add(bean);
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return list;
//    }
//
//    /*往日志记录表中添加数据 ，返回-1表示添加失败 */
//    public long insertToLogDB(LogDBBean bean) {
//        if (null != bean) {
//            ContentValues values = new ContentValues();
//            values.put(LogDBBean.LOG_DATE, bean.getLog_date());
//            values.put(LogDBBean.NETWORK, bean.getNetwork());
//            values.put(LogDBBean.GPS, bean.getGps());
//            values.put(LogDBBean.APP_ALIVE, bean.getApp_alive());
//            values.put(LogDBBean.UPLOAD_STATE, bean.getUpload_state());
//            values.put(LogDBBean.CREATE_TIME, bean.getCreate_time());
//            return db.insert(SqliteHelper.LOG_TRABLE_NAME, null, values);
//        }
//        return -1;
//    }
//
//    /*查询日志记录表, 根据日期*/
//    public List<LogDBBean> queryLogRecord() {
//        List<LogDBBean> list = new ArrayList<>();
//        Cursor cursor = db.query(SqliteHelper.LOG_TRABLE_NAME, null, null, null, null, null, LogDBBean.CREATE_TIME + " desc");
//        cursor.moveToFirst();
//        while (cursor.isAfterLast() == false) {
//            LogDBBean bean = new LogDBBean();
//            bean.set_id(cursor.getString(0));
//            bean.setLog_date(cursor.getString(1));
//            bean.setNetwork(cursor.getString(2));
//            bean.setGps(cursor.getString(3));
//            bean.setApp_alive(cursor.getString(4));
//            bean.setUpload_state(cursor.getString(5));
//            bean.setCreate_time(cursor.getString(6));
//            list.add(bean);
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return list;
//    }

    /*往日志记录表中添加数据 ，返回-1表示添加失败 */
    public long insertToAppLogDB(AppLogDBBean bean) {
        if (null != bean) {
            ContentValues values = new ContentValues();
            values.put(AppLogDBBean.KEEPALIVE, bean.getKeepAlive());
            values.put(AppLogDBBean.NETWORK, bean.getNetwork());
            values.put(AppLogDBBean.GPS, bean.getGps());
            values.put(AppLogDBBean.LNG, bean.getLng());
            values.put(AppLogDBBean.LAT, bean.getLat());
            values.put(AppLogDBBean.ADDRESS, bean.getAddress());
            values.put(AppLogDBBean.UPLOADSTATE, bean.getUploadState());
            values.put(AppLogDBBean.CREATETIME, bean.getCreateTime());
            values.put(AppLogDBBean.LOGDATE, bean.getLogDate());

            db.insert(SqliteHelper.APP_LOG_TRABLE_NAME, null, values);
            db.insert(SqliteHelper.APP_LOG_COPY_TRABLE_NAME, null, values);
            return 1;
        }
        return -1;
    }

    /*查询日志记录表, 根据日期*/
    public List<AppLogDBBean> queryAppLogRecord() {
        List<AppLogDBBean> list = new ArrayList<>();
        Cursor cursor = db.query(SqliteHelper.APP_LOG_TRABLE_NAME, null, null, null, null, null, AppLogDBBean.CREATETIME + " desc");
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            AppLogDBBean bean = new AppLogDBBean();
            bean.set_id(cursor.getString(0));
            bean.setKeepAlive(cursor.getString(1));
            bean.setNetwork(cursor.getString(2));
            bean.setGps(cursor.getString(3));
            bean.setLng(cursor.getString(4));
            bean.setLat(cursor.getString(5));
            bean.setAddress(cursor.getString(6));
            bean.setUploadState(cursor.getString(7));
            bean.setCreateTime(cursor.getString(8));
            bean.setLogDate(cursor.getString(9));
            list.add(bean);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /*查询日志备份记录表, 根据日期*/
    public List<AppLogDBBean> queryAppLogByDate(String date) {
        List<AppLogDBBean> list = new ArrayList<>();
        if (TextUtils.isEmpty(date)) {
            return list;
        }
        Cursor cursor = db.query(SqliteHelper.APP_LOG_COPY_TRABLE_NAME, null, AppLogDBBean.LOGDATE + "=?", new String[]{date}, null, null, AppLogDBBean.CREATETIME + " desc");
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            AppLogDBBean bean = new AppLogDBBean();
            bean.set_id(cursor.getString(0));
            bean.setKeepAlive(cursor.getString(1));
            bean.setNetwork(cursor.getString(2));
            bean.setGps(cursor.getString(3));
            bean.setLng(cursor.getString(4));
            bean.setLat(cursor.getString(5));
            bean.setAddress(cursor.getString(6));
            bean.setUploadState(cursor.getString(7));
            bean.setCreateTime(cursor.getString(8));
            bean.setLogDate(cursor.getString(9));
            list.add(bean);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /*删除日志，根据创建时间*/
    public void deleteLogByTime(List<AppLogDBBean> list) {
        StringBuffer sql = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sql.append(list.get(i).getCreateTime());
            } else {
                sql.append(list.get(i).getCreateTime() + ",");
            }
        }
        if (!TextUtils.isEmpty(sql)) {
            db.execSQL("delete from " + SqliteHelper.APP_LOG_TRABLE_NAME + " where " + AppLogDBBean.CREATETIME + " in (" + sql.toString() + ")");
        }
    }
}
