package com.mryhc.app.calendar.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.ui.dialog.DialogChangeView;
import com.mryhc.app.calendar.ui.dialog.NoticeDialog;
import com.mryhc.app.calendar.ui.fragment.ColumnFragment;
import com.mryhc.app.calendar.ui.fragment.DayFragment;
import com.mryhc.app.calendar.ui.fragment.MonthFragment;
import com.mryhc.app.calendar.ui.fragment.WeekFragment;
import com.mryhc.app.calendar.ui.fragment.YearFragment;
import com.mryhc.app.calendar.utils.Common;

import java.util.Calendar;

/**
 * Created by mryhc on 2017/6/27.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final int FLAG_DAY = 1;
    public static final int FLAG_WEEK = 2;
    public static final int FLAG_COLUMN_WEEK = 3;
    public static final int FLAG_MONTH = 4;
    public static final int FLAG_YEAR = 5;

    public static final int ACTIVITY_REQUEST_CODE_SETTING = 1;

    private Toolbar toolbar;
    private RelativeLayout changView;
    private RelativeLayout goTodayView;
    private RelativeLayout addEventView;
    private RelativeLayout addTaskView;
    private RelativeLayout addNoticeView;
    private RelativeLayout settingView;
    private ImageView viewImage;

    private LinearLayout bottomLayout;

    private Calendar todayCalendar;
    private int curFlag;
    private FragmentManager fragmentManager;

    private DayFragment dayFragment;
    private WeekFragment weekFragment;
    private ColumnFragment columnFragment;
    private MonthFragment monthFragment;
    private YearFragment yearFragment;

    private SharedPreferences sharedPreferences;
    private boolean showWeekNum;
    private int weekStartNum;

    @Override
    protected int initLayout() {
        return R.layout.acivity_main;
    }

    @Override
    protected void initView() {
        initData();
        initToolBar();
        setActTitle(todayCalendar, FLAG_MONTH);
        changView = (RelativeLayout) findViewById(R.id.main_view_change);
        goTodayView = (RelativeLayout) findViewById(R.id.main_today);
        addEventView = (RelativeLayout) findViewById(R.id.main_add_event);
        addTaskView = (RelativeLayout) findViewById(R.id.main_add_task);
        addNoticeView = (RelativeLayout) findViewById(R.id.main_notice);
        settingView = (RelativeLayout) findViewById(R.id.main_setting);
        viewImage = (ImageView) findViewById(R.id.main_view_icon);
        bottomLayout = (LinearLayout) findViewById(R.id.activity_main_bottom);

        changView.setOnClickListener(this);
        goTodayView.setOnClickListener(this);
        addEventView.setOnClickListener(this);
        addTaskView.setOnClickListener(this);
        addNoticeView.setOnClickListener(this);
        settingView.setOnClickListener(this);

        replaceFragment(FLAG_MONTH);
    }

    private void initData(){
        sharedPreferences = getSharedPreferences(Common.SP_NAME, MODE_APPEND);
        if(sharedPreferences.getBoolean(Common.HAS_USED, false)){
            showWeekNum = sharedPreferences.getBoolean(Common.SHOW_WEEK_NUM, false);
            weekStartNum = sharedPreferences.getInt(Common.WEEK_START, 7);
        }else{
            sharedPreferences.edit().putBoolean(Common.SHOW_WEEK_NUM, true).putInt(Common.WEEK_START, 7).putBoolean(Common.HAS_USED, true).commit();
            showWeekNum = true;
            weekStartNum = 7;
        }

        todayCalendar = Calendar.getInstance();
        fragmentManager = getSupportFragmentManager();
        curFlag = -1;
    }

    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setActTitle(Calendar calendar, int flag){
        switch (flag){
            case FLAG_DAY:
                this.setTitle(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月"+ calendar.get(Calendar.DAY_OF_MONTH)+"日");
                break;
            case FLAG_WEEK:
            case FLAG_COLUMN_WEEK:
                this.setTitle(calendar.get(Calendar.YEAR) + "年第" + calendar.get(Calendar.WEEK_OF_YEAR) + "周");
                break;
            case FLAG_MONTH:
                this.setTitle(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
                break;
            case FLAG_YEAR:
                this.setTitle(calendar.get(Calendar.YEAR) + "年");
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                openNewActivity(CheckTaskActivity.class);
                break;
        }
        return true;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_view_change:
                DialogChangeView dialogChangeView = new DialogChangeView(this, new DialogChangeView.OnItemSelectedListener() {
                    @Override
                    public void onSelect(int flag) {
                        onViewChange(flag);
                    }
                });
                dialogChangeView.show();
                break;
            case R.id.main_today:
                goBackToday();
                break;
            case R.id.main_add_event:
                openNewActivity(AddEventActivity.class);
                break;
            case R.id.main_add_task:
                openNewActivity(AddTaskActivity.class);
                break;
            case R.id.main_notice:
//                NoticeDialog noticeDialog = new NoticeDialog(this);
//                noticeDialog.show();
                openNewActivity(AddNoticeActivity.class);
                break;
            case R.id.main_setting:
                openActivityForResult(SettingActivity.class, ACTIVITY_REQUEST_CODE_SETTING);
                break;
        }
    }

    private void openNewActivity(Class<?> cla){
        Intent intent = new Intent(MainActivity.this, cla);
        startActivity(intent);
    }

    private void openActivityForResult(Class<?> cla, int requestCode){
        Intent intent = new Intent(MainActivity.this, cla);
        startActivityForResult(intent, requestCode);
    }

    private void goBackToday(){
        switch (curFlag){
            case FLAG_DAY:
                dayFragment.setTargetCalendar(Calendar.getInstance());
                break;
            case FLAG_WEEK:
                weekFragment.setTargetCalendar(Calendar.getInstance());
                break;
            case FLAG_COLUMN_WEEK:
                columnFragment.setTargetCalendar(Calendar.getInstance());
                break;
            case FLAG_MONTH:
                monthFragment.setTargetCalendar(Calendar.getInstance());
                break;
            case FLAG_YEAR:
                yearFragment.setTargetCalendar(Calendar.getInstance());
                break;
        }
    }

    private void onViewChange(int flag){
        replaceFragment(flag);
        switch (flag){
            case FLAG_DAY:
                viewImage.setImageResource(R.mipmap.ic_day_view);
                break;
            case FLAG_WEEK:
                viewImage.setImageResource(R.mipmap.ic_week_view);
                break;
            case FLAG_COLUMN_WEEK:
                viewImage.setImageResource(R.mipmap.ic_column_week);
                break;
            case FLAG_MONTH:
                viewImage.setImageResource(R.mipmap.ic_month_view);
                break;
            case FLAG_YEAR:
                viewImage.setImageResource(R.mipmap.ic_year_view);
                break;
        }
    }

    private void replaceFragment(int flag){
        replaceFragment(flag, null);
    }

    private void replaceFragment(int flag, Calendar calendar){
        if(curFlag == flag){
            return ;
        }
//        if(calendar == null){
//            calendar = Calendar.getInstance();
//        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(curFlag != -1){
            switch (curFlag){
                case FLAG_DAY:
                    transaction.hide(dayFragment);
                    break;
                case FLAG_WEEK:
                    transaction.hide(weekFragment);
                    break;
                case FLAG_COLUMN_WEEK:
                    transaction.hide(columnFragment);
                    break;
                case FLAG_MONTH:
                    transaction.hide(monthFragment);
                    break;
                case FLAG_YEAR:
                    transaction.hide(yearFragment);
                    break;
            }
        }
        switch (flag){
            case FLAG_DAY:
                if(dayFragment == null){
                    dayFragment = new DayFragment();
                    if(calendar != null){
                        dayFragment.setTargetCalendar(calendar);
                    }
                }
                if(dayFragment.isAdded()){
                    transaction.show(dayFragment);
                    if(calendar != null){
                        dayFragment.setTargetCalendar(calendar);
                    }
                    dayFragment.isVisibleInActivity();
                }else{
                    transaction.add(R.id.main_center_layout, dayFragment);
                }
                break;
            case FLAG_WEEK:
                if(weekFragment == null){
                    weekFragment = new WeekFragment();
                }
                if(weekFragment.isAdded()){
                    transaction.show(weekFragment);
                    weekFragment.isVisibleInActivity();
                }else{
                    transaction.add(R.id.main_center_layout, weekFragment);
                }
                break;
            case FLAG_COLUMN_WEEK:
                if(columnFragment == null){
                    columnFragment = new ColumnFragment();
                }
                if(columnFragment.isAdded()){
                    transaction.show(columnFragment);
                    columnFragment.isVisibleInActivity();
                }else{
                    transaction.add(R.id.main_center_layout, columnFragment);
                }
                break;
            case FLAG_MONTH:
                if(monthFragment == null){
                    monthFragment = new MonthFragment();
                    if(calendar != null){
                        monthFragment.setTargetCalendar(calendar);
                    }
                }
                if(monthFragment.isAdded()){
                    transaction.show(monthFragment);
                    if(calendar != null){
                        monthFragment.setTargetCalendar(calendar);
                    }
                    monthFragment.isVisibleInActivity();
                }else{
                    transaction.add(R.id.main_center_layout, monthFragment);
                }
                break;
            case FLAG_YEAR:
                if(yearFragment == null){
                    yearFragment = new YearFragment();
                }
                if(yearFragment.isAdded()){
                    transaction.show(yearFragment);
                    yearFragment.isVisibleInActivity();
                }else{
                    transaction.add(R.id.main_center_layout, yearFragment);
                }
                break;
        }
        transaction.commit();
        curFlag = flag;
    }

    public void jumpToMonthView(Calendar targetCalendar){
        replaceFragment(FLAG_MONTH, targetCalendar);
    }

    public void jumpToDayView(Calendar targetCalendar){
        replaceFragment(FLAG_DAY, targetCalendar);
    }

    public int getBottomLayoutHeight(){
        return bottomLayout.getMeasuredHeight();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ACTIVITY_REQUEST_CODE_SETTING:
                if(resultCode == RESULT_OK){
                    onSettingResult();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onSettingResult(){
        if(monthFragment != null){
            monthFragment.responseSettingChanged();
        }
    }
}
