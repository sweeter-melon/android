package com.mryhc.calendarpro.ui.activity;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mryhc.calendarpro.R;
import com.mryhc.calendarpro.ui.fragment.BaseFragment;
import com.mryhc.calendarpro.ui.fragment.DayFragment;
import com.mryhc.calendarpro.ui.fragment.MonthFragment;
import com.mryhc.calendarpro.ui.fragment.WeekFragment;
import com.mryhc.calendarpro.ui.fragment.YearFragment;
import com.mryhc.calendarpro.utils.Common;
import com.mryhc.calendarpro.utils.MyLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mryhc on 2017/6/3.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int INDEX_TODAY = 0;
    private static final int INDEX_DAY = 1;
    private static final int INDEX_WEEK = 2;
    private static final int INDEX_MONTH = 3;
    private static final int INDEX_YEAR = 4;

    public static final int TITLE_FLAG_DAY = 1;
    public static final int TITLE_FLAG_WEEK = 2;
    public static final int TITLE_FLAG_MONTH = 3;

    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    private FrameLayout content;

    private LinearLayout llToday;
    private LinearLayout llDay;
    private LinearLayout llWeek;
    private LinearLayout llMonth;
    private LinearLayout llYear;
    private LinearLayout llPlan;

    private FrameLayout centerLayout;

    private BaseFragment centerFragment;

    private FragmentManager fragmentManager;

    private int curIndex;

    private SimpleDateFormat dayFormat;

    private Calendar todayCalendar;
    private boolean showTodayIcon;

    private DayFragment dayFragment;
    private WeekFragment weekFragment;
    private MonthFragment monthFragment;
    private YearFragment yearFragment;
    private boolean mLayoutComplete = false;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        dayFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        todayCalendar = Calendar.getInstance();
        showTodayIcon = false;
    }

    @Override
    protected void initView() {
        drawerLayout = (DrawerLayout) this.findViewById(R.id.main_drawer_layout);
        toolbar = (Toolbar) this.findViewById(R.id.main_toolbar);
        initToolbar();
        llToday = (LinearLayout) this.findViewById(R.id.main_left_today);
        llDay = (LinearLayout) this.findViewById(R.id.main_left_day);
        llWeek = (LinearLayout) this.findViewById(R.id.main_left_week);
        llMonth = (LinearLayout) this.findViewById(R.id.main_left_month);
        llYear = (LinearLayout) this.findViewById(R.id.main_left_year);
        llPlan = (LinearLayout) this.findViewById(R.id.main_left_plan);
        centerLayout = (FrameLayout) this.findViewById(R.id.main_center_layout);

        llToday.setOnClickListener(this);
        llDay.setOnClickListener(this);
        llWeek.setOnClickListener(this);
        llMonth.setOnClickListener(this);
        llYear.setOnClickListener(this);
        llPlan.setOnClickListener(this);

        curIndex = -1;
        fragmentManager = getSupportFragmentManager();
        replaceFragment(INDEX_MONTH);
        setActTitle(todayCalendar, TITLE_FLAG_MONTH);
        addGlobalListener();
    }

    private void addGlobalListener(){
        getContentResolver().registerContentObserver(Settings.System.getUriFor
                ("navigationbar_is_min"), true, mNavigationStatusObserver);
    }

    public void setActTitle(Calendar calendar, int flag){
        switch (flag){
            case TITLE_FLAG_DAY:
                this.setTitle(dayFormat.format(calendar.getTime()));
                break;
            case TITLE_FLAG_WEEK:
                this.setTitle(calendar.get(Calendar.YEAR) + "年第" + calendar.get(Calendar.WEEK_OF_YEAR) + "周");
                break;
            case TITLE_FLAG_MONTH:
                this.setTitle(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
                break;
        }

        if(checkCalendarEquals(todayCalendar, calendar)){
            showTodayIcon = false;
        }else{
            showTodayIcon = true;
        }
    }

    public boolean checkCalendarEquals(Calendar calendar1, Calendar calendar2){
        if(calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR)){
            return false;
        }

        if(calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH)){
            return false;
        }

        if(calendar1.get(Calendar.DAY_OF_MONTH) != calendar2.get(Calendar.DAY_OF_MONTH)){
            return false;
        }

        return true;
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void replaceFragment(int targetIndex){
        if(curIndex == targetIndex){
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (curIndex){
            case INDEX_TODAY:
                transaction.hide(dayFragment);
                break;
            case INDEX_DAY:
                transaction.hide(dayFragment);
                break;
            case INDEX_WEEK:
                transaction.hide(weekFragment);
                break;
            case INDEX_MONTH:
                transaction.hide(monthFragment);
                break;
            case INDEX_YEAR:
                transaction.hide(yearFragment);
                break;
        }
        switch (targetIndex){
            case INDEX_TODAY:
                if(dayFragment == null){
                    dayFragment = new DayFragment();
                }
                if(dayFragment.isAdded()){
                    transaction.show(dayFragment);
                }else{
                    transaction.add(R.id.main_center_layout, dayFragment);
                }
                setActTitle(todayCalendar, TITLE_FLAG_DAY);
                break;
            case INDEX_DAY:
                if(dayFragment == null){
                    dayFragment = new DayFragment();
                }
                if(dayFragment.isAdded()){
                    transaction.show(dayFragment);
                }else{
                    transaction.add(R.id.main_center_layout, dayFragment);
                }
                setActTitle(todayCalendar, TITLE_FLAG_DAY);
                break;
            case INDEX_WEEK:
                if(weekFragment == null){
                    weekFragment = new WeekFragment();
                }
                if(weekFragment.isAdded()){
                    transaction.show(weekFragment);
                }else{
                    transaction.add(R.id.main_center_layout, weekFragment);
                }
                setActTitle(todayCalendar, TITLE_FLAG_WEEK);
                break;
            case INDEX_MONTH:
                if(monthFragment == null){
                    monthFragment = new MonthFragment();
                }
                if(monthFragment.isAdded()){
                    transaction.show(monthFragment);
                }else{
                    transaction.add(R.id.main_center_layout, monthFragment);
                }
                setActTitle(todayCalendar, TITLE_FLAG_MONTH);
                break;
            case INDEX_YEAR:
                if(yearFragment == null){
                    yearFragment = new YearFragment();
                }
                if(yearFragment.isAdded()){
                    transaction.show(yearFragment);
                }else{
                    transaction.add(R.id.main_center_layout, yearFragment);
                }
                break;
        }
        transaction.commit();
        curIndex = targetIndex;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.main_toolbar_menu_today).setVisible(showTodayIcon);
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_today:
                replaceFragment(INDEX_TODAY);
                drawerLayout.closeDrawers();
                break;
            case R.id.main_left_day:
                replaceFragment(INDEX_DAY);
                drawerLayout.closeDrawers();
                break;
            case R.id.main_left_week:
                replaceFragment(INDEX_WEEK);
                drawerLayout.closeDrawers();
                break;
            case R.id.main_left_month:
                drawerLayout.closeDrawers();
                replaceFragment(INDEX_MONTH);
                break;
            case R.id.main_left_year:
                replaceFragment(INDEX_YEAR);
                drawerLayout.closeDrawers();
                break;
        }
    }

    private ContentObserver mNavigationStatusObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            int navigationBarIsMin = Settings.System.getInt(getContentResolver(),
                    "navigationbar_is_min", 0);
            if (navigationBarIsMin == 1) {
                MyLog.i(Common.TAG, "隐藏");
            } else {
                MyLog.i(Common.TAG, "显示");
            }
        }
    };
}
