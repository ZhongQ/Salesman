package com.salesman.fragment.guide;


import android.os.Bundle;
import android.app.Fragment;
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
public class ClientGuideFragment extends Fragment implements View.OnClickListener {
    View guide1, guide2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_guide, container, false);
        ViewUtil.scaleContentView((ViewGroup) view);
        ImageView ivIKnow = (ImageView) view.findViewById(R.id.iv_i_know);
        ivIKnow.setOnClickListener(this);
        ImageView ivStep1 = (ImageView) view.findViewById(R.id.iv_i_know_step1);
        ivStep1.setOnClickListener(this);
        guide1 = view.findViewById(R.id.client_guide1_m);
        guide2 = view.findViewById(R.id.client_guide2_m);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_i_know_step1:
                guide1.setVisibility(View.GONE);
                guide2.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_i_know:
                getActivity().finish();
                break;
        }
    }

}
