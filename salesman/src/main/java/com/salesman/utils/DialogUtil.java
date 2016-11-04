package com.salesman.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.adapter.NoticeReleaseObjAdapter;
import com.salesman.entity.NoticeReleaseObj;
import com.salesman.listener.OnDialogItemClickListener;
import com.studio.jframework.widget.dialog.DialogCreator;

import java.util.List;

/**
 * 对话框工具类
 * Created by LiHuai on 2016/6/2 0002.
 */
public class DialogUtil {
    private Dialog mDialog;
    private Context mContext;
    private String message;
    private boolean isCancel;
    private DialogOnClickListener mListener;
    private PickPhotoOnClickListener pickPhotoOnClickListener;
    private OnDialogItemClickListener onDialogItemClickListener;

    public DialogUtil(Context mContext, String message, boolean isCancel) {
        this.mContext = mContext;
        this.message = message;
        this.isCancel = isCancel;
    }

    public DialogUtil(Context mContext, String message) {
        this(mContext, message, true);
    }

    public DialogUtil(Context mContext) {
        this(mContext, "", true);
    }

    /**
     * 普通对话框
     */
    public void showDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
        View hintView = LayoutInflater.from(mContext).inflate(R.layout.dialog_hint_common, null);
        TextView tvMsg = (TextView) hintView.findViewById(R.id.tv_dialog_msg);
        TextView tvCancel = (TextView) hintView.findViewById(R.id.tv_dialog_cancel);
        TextView tvConfirm = (TextView) hintView.findViewById(R.id.tv_dialog_confirm);
        tvMsg.setText(message);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                if (null != mListener) {
                    mListener.confirmDialog();
                }
            }
        });
        mDialog = DialogCreator.createNormalDialog(mContext, hintView, DialogCreator.Position.CENTER);
        mDialog.setCancelable(isCancel);
        mDialog.show();
    }

    /**
     * 图片选择对话框
     */
    public void showPhotoPickDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
        View pictureView = View.inflate(mContext, R.layout.view_camera_or_photo, null);
        TextView tvTakePhoto = (TextView) pictureView.findViewById(R.id.tv_take_photo);
        TextView tvPickPhoto = (TextView) pictureView.findViewById(R.id.tv_pick_photo);
        TextView tvCancel = (TextView) pictureView.findViewById(R.id.tv_cancel_picture);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                if (null != pickPhotoOnClickListener) {
                    pickPhotoOnClickListener.onTakePhotoClick();
                }
            }
        });
        tvPickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                if (null != pickPhotoOnClickListener) {
                    pickPhotoOnClickListener.onPickPhotoClick();
                }
            }
        });
        mDialog = DialogCreator.createNormalDialog(mContext, pictureView, DialogCreator.Position.BOTTOM);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    /**
     * 列表对话框
     *
     * @param mData
     */
    public void showListDialog(@NonNull List<NoticeReleaseObj> mData) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
        View objView = View.inflate(mContext, R.layout.dialog_notice_release_obj, null);
        ListView listView = (ListView) objView.findViewById(R.id.lv_dialog);
        final NoticeReleaseObjAdapter adapter = new NoticeReleaseObjAdapter(mContext, mData);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismissDialog();
                if (null != onDialogItemClickListener) {
                    onDialogItemClickListener.onDialogItemClick(adapter.getItem(position), position);
                }
            }
        });
        mDialog = DialogCreator.createNormalDialog(mContext, objView, DialogCreator.Position.CENTER);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    /**
     * 删除对话框
     */
    public void showDeleteDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
        View pictureView = View.inflate(mContext, R.layout.dialog_del_comment, null);
        TextView tvDelete = (TextView) pictureView.findViewById(R.id.tv_delete);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                if (null != mListener) {
                    mListener.confirmDialog();
                }
            }
        });
        mDialog = DialogCreator.createNormalDialog(mContext, pictureView, DialogCreator.Position.CENTER);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    /**
     * 瞬间结束,隐藏对话框
     */
    public void dismissDialog() {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void setDialogListener(DialogOnClickListener mListener) {
        this.mListener = mListener;
    }

    public void setPickPhotoOnClickListener(PickPhotoOnClickListener pickPhotoOnClickListener) {
        this.pickPhotoOnClickListener = pickPhotoOnClickListener;
    }

    public void setOnDialogItemClickListener(OnDialogItemClickListener onDialogItemClickListener) {
        this.onDialogItemClickListener = onDialogItemClickListener;
    }

    public interface DialogOnClickListener {
        void confirmDialog();
    }

    public interface PickPhotoOnClickListener {
        void onTakePhotoClick();

        void onPickPhotoClick();
    }
}
