package com.szip.smartdream.Controller.Fragment.report;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.szip.smartdream.Adapter.MyPagerAdapter;
import com.szip.smartdream.Controller.Fragment.BaseFragment;
import com.szip.smartdream.R;
import com.szip.smartdream.View.NoScrollViewPager;

import java.util.ArrayList;


/**
 * Created by Administrator on 2019/1/23.
 */

public class ReportFragment extends BaseFragment {

    ReportDayFragment reportDayFrag = ReportDayFragment.newInstance("ReportDay");
    private String[] tabs = new String[]{"day", "week", "month"};
    private TabLayout mTab;
    private NoScrollViewPager mPager;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private MyPagerAdapter myPagerAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();


    private TextView day, month, year;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.day:
                    day.setBackgroundColor(requireContext().getResources().getColor(R.color.purple));
                    day.setTextColor(requireContext().getResources().getColor(R.color.white));
                    month.setBackgroundColor(requireContext().getResources().getColor(R.color.white));
                    month.setTextColor(requireContext().getResources().getColor(R.color.black));
                    year.setBackgroundColor(requireContext().getResources().getColor(R.color.white));
                    year.setTextColor(requireContext().getResources().getColor(R.color.black));
                    mPager.setCurrentItem(0);
                    break;

                case R.id.month:
                    day.setBackgroundColor(requireContext().getResources().getColor(R.color.white));
                    day.setTextColor(requireContext().getResources().getColor(R.color.black));
                    month.setBackgroundColor(requireContext().getResources().getColor(R.color.purple));
                    month.setTextColor(requireContext().getResources().getColor(R.color.white));
                    year.setBackgroundColor(requireContext().getResources().getColor(R.color.white));
                    year.setTextColor(requireContext().getResources().getColor(R.color.black));
                    mPager.setCurrentItem(1);
                    break;

                case R.id.year:
                    day.setBackgroundColor(requireContext().getResources().getColor(R.color.white));
                    day.setTextColor(requireContext().getResources().getColor(R.color.black));
                    month.setBackgroundColor(requireContext().getResources().getColor(R.color.white));
                    month.setTextColor(requireContext().getResources().getColor(R.color.black));
                    year.setBackgroundColor(requireContext().getResources().getColor(R.color.purple));
                    year.setTextColor(requireContext().getResources().getColor(R.color.white));
                    mPager.setCurrentItem(2);
                    break;

            }
        }
    };

    /**
     * ????????????fragment?????????Activity?????????
     */
    public static ReportFragment newInstance(String param) {
        Bundle bundle = new Bundle();
        bundle.putString("param", param);
        ReportFragment fragment = new ReportFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_report;
    }

    @Override
    protected void afterOnCreated(Bundle savedInstanceState) {
        if (mPager == null && mTab == null) {
            initView();
            initPager();
        }
    }

    /**
     * ???????????????
     **/
    private void initView() {


        fm = requireFragmentManager();
        transaction = fm.beginTransaction();


        day = getView().findViewById(R.id.day);
        month = getView().findViewById(R.id.month);
        year = getView().findViewById(R.id.year);

        mTab = getView().findViewById(R.id.reportTl);
        mPager = getView().findViewById(R.id.reportVp);

        day.setOnClickListener(onClickListener);
        month.setOnClickListener(onClickListener);
        year.setOnClickListener(onClickListener);

        day.performClick();
    }

    /**
     * ?????????????????????
     */
    private void initPager() {
        // ??????????????????,??????Fragment
        ReportDayFragment dayFragment = ReportDayFragment.newInstance("szip");
        ReportWeekFragment weekFragment = ReportWeekFragment.newInstance("szip");
        ReportMonthFragment monthFragment = ReportMonthFragment.newInstance("szip");

        // ??????
        fragments.add(dayFragment);
        fragments.add(weekFragment);
        fragments.add(monthFragment);
        // ??????ViewPager?????????
        myPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        myPagerAdapter.setFragmentArrayList(fragments);
        // ???ViewPager???????????????
        mPager.setAdapter(myPagerAdapter);

        // ?????? TabLayout ??? ViewPager ?????????
        mTab.setupWithViewPager(mPager);

        // TabLayout ????????? (????????????????????????3???Fragment,????????? app?????????Fragment ?????? V4????????? Fragment)
        for (int i = 0; i < myPagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = mTab.getTabAt(i);//???????????????tab
            tab.setCustomView(R.layout.main_top_layout);//????????????tab??????view
            if (i == 0) {
                // ???????????????tab???TextView?????????????????????
                tab.getCustomView().findViewById(R.id.main_tv).setSelected(true);//?????????tab?????????
            }
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.main_tv);
            textView.setText(tabs[i]);//??????tab????????????
        }
        mTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i("SANJAY","ONE "+tab.getPosition());

                tab.getCustomView().findViewById(R.id.main_tv).setSelected(true);
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i("SANJAY","ONE "+tab.getPosition());
                tab.getCustomView().findViewById(R.id.main_tv).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i("SANJAY","ONE "+tab.getPosition());
            }
        });
    }
}
