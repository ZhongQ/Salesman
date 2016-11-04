package com.salesman.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.salesman.R;
import com.salesman.utils.ToastUtil;
import com.studio.jframework.base.BaseFragment;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.widget.dialog.DialogCreator;
import com.studio.jframework.widget.progressbar.MaterialProgressBar;
import com.uk.co.senab.photoview.PhotoViewAttacher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends BaseFragment {

    private String mImageUrl;
    private ImageView mImageView;
    private MaterialProgressBar progressBar;

    private ImageLoader mImageLoader;
    private PhotoViewAttacher mAttacher;
    private Bitmap mBitmap;

    private Dialog mOptionMenu;
    private TextView mSaveImage;

    @Override
    protected int setLayout() {
        return R.layout.fragment_photo_view;
    }

    @Override
    protected void findViews(View view) {
        mImageView = (ImageView) view.findViewById(R.id.image);
        progressBar = (MaterialProgressBar) view.findViewById(R.id.loading);

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_save_image, null);
        mSaveImage = (TextView) dialogView.findViewById(R.id.save_image);
        mOptionMenu = DialogCreator.createNormalDialog(mContext, dialogView, DialogCreator.Position.BOTTOM);
    }

    @Override
    protected void initialization() {
        mImageUrl = getArguments().getString("url");
        mImageLoader = VolleyController.getInstance(mContext).getImageLoader();
        mAttacher = new PhotoViewAttacher(mImageView);
    }

    @Override
    protected void bindEvent() {
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });
        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mBitmap != null) {
                    mOptionMenu.show();
                }
                return false;
            }
        });
        mSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionMenu.dismiss();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                File file = new File(Environment.getExternalStorageDirectory().
                        getAbsolutePath() + File.separator + ""/*FileConstant.EXTERNAL_FOLDER_NAME*/
                        , System.currentTimeMillis() + ".jpg");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                try {
                    OutputStream ops = new FileOutputStream(file);
                    ops.write(baos.toByteArray());
                    ops.flush();
                    ops.close();
                    Toast.makeText(mContext, "保存图片成功", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onCreateView() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageLoader.get(mImageUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
//                    if(mBitmap.isRecycled()){
//                        mBitmap.
//                    }
                    mBitmap = response.getBitmap();
                    mImageView.setImageBitmap(mBitmap);
                    progressBar.setVisibility(View.GONE);
                    mAttacher.update();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                ToastUtil.show(mContext, "图片加载失败");
            }
        }, 1000, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageView.setImageBitmap(null);
        mBitmap = null;
        System.gc();
    }
}
