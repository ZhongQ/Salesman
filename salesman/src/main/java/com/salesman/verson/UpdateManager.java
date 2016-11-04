package com.salesman.verson;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.application.SalesManApplication;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.VersionUpdateBean;
import com.salesman.network.BaseHelper;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.utils.PackageUtils;
import com.studio.jframework.widget.dialog.DialogCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * 版本更新
 * Created by LiHuai on 2016/2/25 0025.
 */
public class UpdateManager {
    private final static String TAG = UpdateManager.class.getSimpleName();
    private Context mContext;


    private String downloadUrl = "";
    private String changelog = "";
    private static final String SDPATH = Environment.getExternalStorageDirectory() + "/";
    /* 文件下载保存路径 */
    private String mSavePath = SDPATH + "download";
    private static final String APKNAME = "salesman.apk";

    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private static final int DOWNLOAD = 1;
    private static final int DOWNLOAD_FINISH = 2;
    private static final int UPDATE_MSG = 3;
    private static final int UPDATE_APK = 4;

    public UpdateManager(Context mContext) {
        this.mContext = mContext;
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD:// 设置进度条位置
                    Log.i("login", progress + "");
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH: // 安装文件
                    installApk();
                    break;
                case UPDATE_MSG:
//                    ToastUtil.show(mContext, "当前已是最新版本");
                    break;
                case UPDATE_APK:
//                    showNoticeDialog(false);
                    showAppUpdataDialog();
                    break;
            }
        }
    };

    public void checkUpdate() {
        String url = Constant.moduleVersionUpdate;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("plant", "android");
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                VersionUpdateBean versionUpdateBean = GsonUtils.json2Bean(response, VersionUpdateBean.class);
                if (null != versionUpdateBean) {
                    if (versionUpdateBean.success && null != versionUpdateBean.data) {
                        if (!TextUtils.isEmpty(versionUpdateBean.data.version) && !"null".equals(versionUpdateBean.data.version)) {
                            int newCode = Integer.parseInt(versionUpdateBean.data.version);
                            int lodCode = PackageUtils.getVersionCode(mContext);
                            if (newCode > lodCode) {
                                if (!TextUtils.isEmpty(versionUpdateBean.data.installUrl)) {
                                    downloadUrl = versionUpdateBean.data.installUrl;
                                    changelog = versionUpdateBean.data.changelog;
                                    Message msg = new Message();
                                    msg.what = UPDATE_APK;
                                    msg.arg1 = 1;
                                    mHandler.sendMessage(msg);
                                }
                            } else if (newCode == lodCode) {
//                                ToastUtil.show(mContext, "当前已是最新版本");
                            }
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleyController.getInstance(mContext).addToQueue(post);
    }

    /**
     * 显示软件更新对话框(原始对话框)
     */
    private void showNoticeDialog(boolean bForceUpdate) {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("客户端有新更新，是否更新？");
        {
            String strContent = "";
            builder.setMessage(strContent);
            builder.setMessage(R.string.soft_update_info);
        }
        // 更新日志
        builder.setMessage(changelog);
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 强制更新控制
        if (!bForceUpdate) {
            // 稍后更新
            builder.setNegativeButton(R.string.soft_update_later, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setCancelable(false);
        }
        try {
            Dialog noticeDialog = builder.create();
            noticeDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示app升级提示框
     * 版本V1.3.0
     * 备注：强制更新
     */
    public void showAppUpdataDialog() {
        View unLoginView = LayoutInflater.from(mContext).inflate(R.layout.dialog_app_updata, null);
        Button btnDownload = (Button) unLoginView.findViewById(R.id.btn_download_apk);
        final Dialog appUpdataDialog = DialogCreator.createNormalDialog(mContext, unLoginView, DialogCreator.Position.CENTER);
        appUpdataDialog.setCancelable(false);
        appUpdataDialog.show();
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUpdataDialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
    }

    /**
     * 显示软件下载对话框
     */
    public void showDownloadDialog() {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.soft_updating);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // 取消更新
//        builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                // 设置取消状态
//                cancelUpdate = true;
//            }
//        });
        builder.setCancelable(false);
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得下载的路径
                    URL url = new URL(downloadUrl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();

                    if (length > 0) {
                        // 创建输入流
                        InputStream is = conn.getInputStream();
                        File file = new File(mSavePath);
                        // 判断文件目录是否存在
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        File apkFile = new File(mSavePath, APKNAME);
                        FileOutputStream fos = new FileOutputStream(apkFile);
                        int count = 0;
                        // 缓存
                        byte buf[] = new byte[1024];
                        // 写入到文件中
                        do {
                            int numread = is.read(buf);
                            count += numread;
                            // 计算进度条位置
                            progress = (int) (((float) count / length) * 100);
                            // 更新进度
                            mHandler.sendEmptyMessage(DOWNLOAD);
                            if (numread <= 0) {
                                // 下载完成
                                mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                                break;
                            }
                            // 写入文件
                            fos.write(buf, 0, numread);
                        } while (!cancelUpdate);// 点击取消就停止下载.
                        fos.close();
                        is.close();
                    }

                } else {
                    Message msg = new Message();
                    msg.what = UPDATE_MSG;
                    msg.obj = R.string.sd_error;
                    mHandler.sendMessage(msg);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, APKNAME);
        if (!apkfile.exists()) {
            return;
        }
        SalesManApplication.g_GlobalObject.getmUserInfo().clear();
        SalesManApplication.g_GlobalObject.getmUserConfig().saveIsFirst(true).apply();
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
}
