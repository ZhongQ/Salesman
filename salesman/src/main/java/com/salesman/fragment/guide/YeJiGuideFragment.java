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
public class YeJiGuideFragment extends Fragment implements View.OnClickListener {
    View guide1, guide2, guide3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yeji_guide, container, false);
        ViewUtil.scaleContentView((ViewGroup) view);
        ImageView ivIKnow = (ImageView) view.findViewById(R.id.iv_i_know);
        ivIKnow.setOnClickListener(this);
        ImageView ivStep1 = (ImageView) view.findViewById(R.id.iv_i_know_step1);
        ivStep1.setOnClickListener(this);
        ImageView ivStep2 = (ImageView) view.findViewById(R.id.iv_i_know_step2);
        ivStep2.setOnClickListener(this);
        guide1 = view.findViewById(R.id.yeji_guide1);
        guide2 = view.findViewById(R.id.yeji_guide2);
        guide3 = view.findViewById(R.id.yeji_guide3);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_i_know_step1:
                guide1.setVisibility(View.GONE);
                guide2.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_i_know_step2:
                guide1.setVisibility(View.GONE);
                guide2.setVisibility(View.GONE);
                guide3.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_i_know:
                getActivity().finish();
                break;
        }
    }

}
