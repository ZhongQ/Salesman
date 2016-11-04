package com.salesman.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.salesman.entity.AppLogDBBean;
import com.salesman.entity.TrackDBBean;
import com.studio.jframework.utils.LogUtils;

/**
 * Created by LiHuai on 2016/3/17 0017.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    public static final String TAG = SqliteHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "db_salesman";
    public static final int DB_VERSION = 2;
    /*离线足迹数据表名*/
    public static final String TRACK_TRABLE_NAME = "tb_track";
    /*app日志表名*/
    public static final String APP_LOG_TRABLE_NAME = "tb_app_log";
    /*app日志备份表名*/
    public static final String APP_LOG_COPY_TRABLE_NAME = "tb_app_log_copy";

//    /*所有足迹数据表名*/
//    public static final String TRACK_RECORD_TRABLE_NAME = "tb_track_record";
//    /*app日志表名*/
//    public static final String LOG_TRABLE_NAME = "tb_log_record";

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*离线足迹表*/
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TRACK_TRABLE_NAME + "("
                + TrackDBBean.ID + " integer primary key,"
                + TrackDBBean.TRACK_DATE + " varchar(100),"
                + TrackDBBean.LONGITUDE + " double,"
                + TrackDBBean.LATITUDE + " double,"
                + TrackDBBean.ADDRESS + " varchar(100),"
                + TrackDBBean.CREATE_TIME + " varchar(100)"
                + ")");

//        /*所有足迹记录表*/
//        db.execSQL("CREATE TABLE IF NOT EXISTS " + TRACK_RECORD_TRABLE_NAME + "("
//                + TrackDBBean.ID + " integer primary key,"
//                + TrackDBBean.TRACK_DATE + " varchar(100),"
//                + TrackDBBean.LONGITUDE + " double,"
//                + TrackDBBean.LATITUDE + " double,"
//                + TrackDBBean.ADDRESS + " varchar(100),"
//                + TrackDBBean.CREATE_TIME + " varchar(100)"
//                + ")");
//
//        db.execSQL("CREATE TABLE IF NOT EXISTS " + LOG_TRABLE_NAME + "("
//                + LogDBBean.ID + " integer primary key,"
//                + LogDBBean.LOG_DATE + " varchar(100),"
//                + LogDBBean.NETWORK + " varchar(100),"
//                + LogDBBean.GPS + " varchar(100),"
//                + LogDBBean.APP_ALIVE + " varchar(100),"
//                + LogDBBean.UPLOAD_STATE + " varchar(100),"
//                + LogDBBean.CREATE_TIME + " varchar(100)"
//                + ")");

        /*app日志记录表*/
        db.execSQL("CREATE TABLE IF NOT EXISTS " + APP_LOG_TRABLE_NAME + "("
                + AppLogDBBean.ID + " integer primary key,"
                + AppLogDBBean.KEEPALIVE + " varchar(100),"
                + AppLogDBBean.NETWORK + " varchar(100),"
                + AppLogDBBean.GPS + " varchar(100),"
                + AppLogDBBean.LNG + " varchar(100),"
                + AppLogDBBean.LAT + " varchar(100),"
                + AppLogDBBean.ADDRESS + " varchar(100),"
                + AppLogDBBean.UPLOADSTATE + " varchar(100),"
                + AppLogDBBean.CREATETIME + " varchar(100),"
                + AppLogDBBean.LOGDATE + " varchar(100)"
                + ")");

        /*app日志记录备份表*/
        db.execSQL("CREATE TABLE IF NOT EXISTS " + APP_LOG_COPY_TRABLE_NAME + "("
                + AppLogDBBean.ID + " integer primary key,"
                + AppLogDBBean.KEEPALIVE + " varchar(100),"
                + AppLogDBBean.NETWORK + " varchar(100),"
                + AppLogDBBean.GPS + " varchar(100),"
                + AppLogDBBean.LNG + " varchar(100),"
                + AppLogDBBean.LAT + " varchar(100),"
                + AppLogDBBean.ADDRESS + " varchar(100),"
                + AppLogDBBean.UPLOADSTATE + " varchar(100),"
                + AppLogDBBean.CREATETIME + " varchar(100),"
                + AppLogDBBean.LOGDATE + " varchar(100)"
                + ")");

        LogUtils.d(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TRACK_TRABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + APP_LOG_TRABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + APP_LOG_COPY_TRABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + TRACK_RECORD_TRABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + LOG_TRABLE_NAME);
        onCreate(db);
    }
}
