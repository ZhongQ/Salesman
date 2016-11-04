package com.salesman.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.salesman.R;
import com.salesman.activity.manage.NoticeListActivity;
import com.salesman.activity.personal.ChangePasswordActivity;
import com.salesman.activity.personal.DailyListActivity;
import com.salesman.activity.personal.HistorySigninActivity;
import com.salesman.activity.personal.HistoryTrackActivity;
import com.salesman.activity.personal.MyMessageActivity;
import com.salesman.activity.personal.MySigninActivity;
import com.salesman.activity.personal.MySubordinateActivity11;
import com.salesman.activity.personal.MyTrackActivity;
import com.salesman.activity.personal.SettingActivity;
import com.salesman.adapter.PersonalListAdapter;
import com.salesman.application.SalesManApplication;
import com.salesman.common.BaseFragment;
import com.salesman.entity.NewestMesBean;
import com.salesman.entity.PersonalListBean;
import com.salesman.global.BeanListHolder;
import com.salesman.umeng.UmengAnalyticsUtil;
import com.salesman.umeng.UmengConfig;
import com.salesman.utils.StaticData;
import com.salesman.utils.StringUtil;
import com.salesman.utils.UserInfoPreference;
import com.salesman.utils.UserInfoUtil;
import com.studio.jframework.widget.listview.InnerListView;

import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;

/**
 * 个人界面
 * Created by LiHuai on 2016/1/25.
 */
public class PersonalFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static final String TAG = PersonalFragment.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();

    private InnerListView listView;
    private PersonalListAdapter personalListAdapter;
    private ImageView ivHead;
    private TextView tvUserName;
    private TextView tvUserName2;
    private TextView tvDept;
    private TextView tvPost;
    private List<Integer> circleList = StaticData.getCircleIdList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void findViews(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_top_title);
        tvTitle.setText(R.string.tab4);
        ImageView ivSetting = (ImageView) view.findViewById(R.id.iv_top_right2);
        ivSetting.setImageResource(R.drawable.setting_icon);

        listView = (InnerListView) view.findViewById(R.id.lv_personal);
        if ("1".equals(mUserInfo.getUserType())) {
            personalListAdapter = new PersonalListAdapter(mContext, BeanListHolder.getPersonalBeanList());
        } else {
            personalListAdapter = new PersonalListAdapter(mContext, BeanListHolder.getSalesmanPersonalBeanList());
        }
        listView.setAdapter(personalListAdapter);

        ivHead = (ImageView) view.findViewById(R.id.iv_head_bg);
        tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        tvUserName2 = (TextView) view.findViewById(R.id.tv_user_name2);
        tvDept = (TextView) view.findViewById(R.id.tv_dept);
        tvPost = (TextView) view.findViewById(R.id.tv_post);

        ivSetting.setOnClickListener(this);
    }

    @Override
    protected void initialization() {
        String userName = mUserInfo.getUserName();
        tvUserName.setText(userName);
        if (userName.length() > 2) {
            tvUserName2.setText(userName.substring(userName.length() - 2, userName.length()));
        } else {
            tvUserName2.setText(mUserInfo.getUserName());
        }
        int headColor = mUserInfo.getHeadColor();
        if (0 == headColor) {
            Random random = new Random();
            int index = random.nextInt(circleList.size());
            mUserInfo.saveHeadColor(circleList.get(index)).apply();
        }
//        ivHead.setImageResource(mUserInfo.getHeadColor());// 头像背景颜色
        if (UserInfoUtil.isAdministrator()) {
            ivHead.setImageResource(R.drawable.administrator_icon);
        } else {
            ivHead.setImageResource(R.drawable.salesman_icon);
        }
        tvDept.setText(mUserInfo.getDeptName());
        tvPost.setText(mUserInfo.getPostName());
    }

    @Override
    protected void bindEvent() {
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onCreateView() {
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengAnalyticsUtil.umengOnPageStart(UmengConfig.MY_PAGE);
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengAnalyticsUtil.umengOnPageEnd(UmengConfig.MY_PAGE);
    }

    public void onEventMainThread(String action) {
    }

    public void onEventMainThread(NewestMesBean.NewsBean newsBean) {
        if (null != newsBean) {
            String total = StringUtil.getMsgsNum(String.valueOf(newsBean.msgTotal));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PersonalListBean bean = personalListAdapter.getItem(position);
        switch (bean.id) {
            case 1:// 签到记录
                UmengAnalyticsUtil.onEvent(mContext, UmengConfig.MY_SIGNIN);
                startActivity(new Intent(mContext, MySigninActivity.class));
                break;
            case 2:// 我的足迹
                UmengAnalyticsUtil.onEvent(mContext, UmengConfig.MY_TRACK);
                startActivity(new Intent(mContext, MyTrackActivity.class));
                break;
            case 3:
                break;
            case 4:// 我的下属
                UmengAnalyticsUtil.onEvent(mContext, UmengConfig.MY_XIASHU);
                startActivity(new Intent(mContext, MySubordinateActivity11.class));
                break;
            case 5:
                break;
            case 6:// 历史足迹
                startActivity(new Intent(mContext, HistoryTrackActivity.class));
                break;
            case 7:// 历史签到
                startActivity(new Intent(mContext, HistorySigninActivity.class));
                break;
            case 8:// 修改密码
                Intent changeIntent = new Intent(mContext, ChangePasswordActivity.class);
                startActivity(changeIntent);
                break;
            case 9:// 我的日报
                UmengAnalyticsUtil.onEvent(mContext, UmengConfig.MY_LOG);
                personalListAdapter.getItem(0).setIsShowRedDot(false);// 去掉小红点
                personalListAdapter.notifyDataSetChanged();
                Intent intent = new Intent(mContext, DailyListActivity.class);
                intent.putExtra("come_from", TAG);
                startActivity(intent);
                break;
            case 10:// 我的公告
                UmengAnalyticsUtil.onEvent(mContext, UmengConfig.MY_NOTICE);
                Intent noticeIntent = new Intent(mContext, NoticeListActivity.class);
                noticeIntent.putExtra("come_from", TAG);
                startActivity(noticeIntent);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_right1:// 我的消息(V2.0.0弃用)
                startActivity(new Intent(mContext, MyMessageActivity.class));
                break;
            case R.id.iv_top_right2:// 设置
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
        }
    }
}
