package com.salesman.fragment.guide;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.salesman.R;
import com.salesman.utils.ViewUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkGuideFragment extends Fragment implements View.OnClickListener {
    private ImageView ivIKnow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_guide, container, false);
        ViewUtil.scaleContentView((ViewGroup) view);
        ivIKnow = (ImageView) view.findViewById(R.id.iv_i_know);
        ivIKnow.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_i_know:
                getActivity().finish();
                break;
        }
    }
}
