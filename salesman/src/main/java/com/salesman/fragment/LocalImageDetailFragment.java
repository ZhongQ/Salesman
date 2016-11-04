package com.salesman.fragment;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.salesman.R;
import com.studio.jframework.base.BaseFragment;
import com.studio.jframework.utils.BitmapUtils;
import com.studio.jframework.utils.SizeUtils;
import com.studio.jframework.widget.progressbar.MaterialProgressBar;
import com.uk.co.senab.photoview.PhotoViewAttacher;

import java.lang.ref.WeakReference;


/**
 * 单张图片显示Fragment
 */
public class LocalImageDetailFragment extends BaseFragment {

    private String mImageUrl;
    private ImageView mImageView;
    private MaterialProgressBar progressBar;

    private PhotoViewAttacher mAttacher;
    private Bitmap mBitmap;

    @Override
    protected int setLayout() {
        return R.layout.fragment_photo_view;
    }

    @Override
    protected void findViews(View view) {
        mImageView = (ImageView) view.findViewById(R.id.image);
        progressBar = (MaterialProgressBar) view.findViewById(R.id.loading);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void initialization() {
        mImageUrl = getArguments().getString("url");
        mAttacher = new PhotoViewAttacher(mImageView);

        mBitmap = BitmapUtils.decodeSampledBitmapFromPath(mImageUrl,
                SizeUtils.getScreenWidth(mContext), SizeUtils.getScreenHeight(mContext));
        WeakReference<Bitmap> weakReference = new WeakReference<>(mBitmap);
        if (weakReference.get() != null) {
            mImageView.setImageBitmap(weakReference.get());
        }
    }

    @Override
    protected void bindEvent() {
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });
    }

    @Override
    protected void onCreateView() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageView.setImageBitmap(null);
        mBitmap = null;
        System.gc();
    }
}
