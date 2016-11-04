package com.salesman.utils;

import com.salesman.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by LiHuai on 2016/1/28 0028.
 */
public class StaticData {

    public static List<Integer> getCircleIdList() {
        List<Integer> circleId = new ArrayList<>();
        circleId.add(R.drawable.circle_1);
        circleId.add(R.drawable.circle_2);
        circleId.add(R.drawable.circle_3);
        circleId.add(R.drawable.circle_4);
        circleId.add(R.drawable.circle_5);
        circleId.add(R.drawable.circle_6);
        circleId.add(R.drawable.circle_7);
        circleId.add(R.drawable.circle_8);
        circleId.add(R.drawable.circle_9);

        return circleId;
    }

    /**
     * 随机获取头像背景
     *
     * @param list
     * @return
     */
    public static int getImageId(List<Integer> list) {
        Random random = new Random();
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    public static List<Integer> getCircleColorList() {
        List<Integer> circleColor = new ArrayList<>();
        circleColor.add(R.color.color_9e8bc1);
        circleColor.add(R.color.color_78c06e);
        circleColor.add(R.color.color_7082ea);
        circleColor.add(R.color.color_7896a4);
        circleColor.add(R.color.color_c8ce67);
        circleColor.add(R.color.color_c482d7);
        circleColor.add(R.color.color_f65e8d);
        circleColor.add(R.color.color_ff8e6b);
        circleColor.add(R.color.color_ff943e);

        return circleColor;
    }
}
