package cn.jk.kaoyandanci.ui.activity;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.BottomNavBgMode;
import com.afollestad.aesthetic.BottomNavIconTextMode;
import com.afollestad.aesthetic.NavigationViewMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.ui.fragment.HomeFragment;
import cn.jk.kaoyandanci.ui.fragment.ReviewFragment;
import cn.jk.kaoyandanci.ui.fragment.SettingFragment;
import cn.jk.kaoyandanci.util.Config;
import cn.jk.kaoyandanci.util.Constant;


public class MainActivity extends BaseActivity {

    @BindView(R.id.main_content)
    FrameLayout mainContent;
    @BindView(R.id.navigation)
    BottomNavigationView navigationView;

    HomeFragment homeFragment;
    ReviewFragment reviewFragment;
    SettingFragment settingFragment;
    Fragment currentFragment;
    Unbinder unbind;
    boolean shouldDraw = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (Config.shouldShowGuide()) {
            startActivity(new Intent(this, IntroActivity.class));
            Config.dontShowGuide();
        }
        unbind = ButterKnife.bind(this);


        if (savedInstanceState == null) {
            homeFragment = new HomeFragment();
            reviewFragment = new ReviewFragment();
            settingFragment = new SettingFragment();
            getFragmentManager().beginTransaction().add(R.id.main_content, homeFragment, "homeFragment").commit();
            getFragmentManager().beginTransaction().add(R.id.main_content, reviewFragment, "reviewFragment").commit();
            getFragmentManager().beginTransaction().add(R.id.main_content, settingFragment, "settingFragment").commit();

        } else {
            homeFragment = (HomeFragment) getFragmentManager().findFragmentByTag("homeFragment");
            reviewFragment = (ReviewFragment) getFragmentManager().findFragmentByTag("reviewFragment");
            settingFragment = (SettingFragment) getFragmentManager().findFragmentByTag("settingFragment");


        }
        getFragmentManager().beginTransaction().hide(homeFragment).hide(reviewFragment).hide(settingFragment).commit();
        if (savedInstanceState == null) {
            showFragment(homeFragment);
        } else {
            Boolean shouldShowSettingFragment = savedInstanceState.getBoolean(Constant.SHOULD_SHOW_SETTING_FRAGMENT);
            if (shouldShowSettingFragment) {
                showFragment(settingFragment);
            } else {
                showFragment(homeFragment);
            }
        }
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment showFragment = homeFragment;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        showFragment = homeFragment;
                        break;
                    case R.id.navigation_review:
                        showFragment = reviewFragment;
                        break;
                    case R.id.navigation_setting:
                        showFragment = settingFragment;
                        break;
                }
                showFragment(showFragment);
                return true;
            }
        });
        if (Aesthetic.isFirstTime()) {
            Aesthetic.get()
                    .activityTheme(R.style.AppTheme_ActionBar)
                    .textColorPrimaryRes(R.color.colorPrimary)
                    .textColorSecondaryRes(R.color.text_color_secondary)
                    .colorPrimaryRes(R.color.colorPrimary)
                    .colorAccentRes(R.color.colorAccent)
                    .colorStatusBarAuto()
                    .colorNavigationBarAuto()
                    .navigationViewMode(NavigationViewMode.SELECTED_ACCENT)
                    .bottomNavigationIconTextMode(BottomNavIconTextMode.SELECTED_ACCENT)
                    .bottomNavigationBackgroundMode(BottomNavBgMode.BLACK_WHITE_AUTO)
                    .apply();
        }
        Aesthetic.get().
                bottomNavigationBackgroundMode(BottomNavBgMode.BLACK_WHITE_AUTO).
                bottomNavigationIconTextMode(NavigationViewMode.SELECTED_ACCENT).
                apply();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constant.DATA_CHANGED) {
            homeFragment.refresh();
            reviewFragment.refresh();
        }
        if (shouldDraw) {
            shouldDraw = false;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(context, EmptyActivity.class));
                }
            }).start();
        }

    }

    @Override
    protected void onDestroy() {
        unbind.unbind();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchWordView:
                startActivity(new Intent(this, SearchWordActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    public void showFragment(Fragment fragment) {
        if (currentFragment == fragment) {
            return;
        }
        String title = getString(R.string.app_name);
        if (currentFragment != null) {
            getFragmentManager().beginTransaction().hide(currentFragment).commit();
        }
        getFragmentManager().beginTransaction().show(fragment).commit();
        currentFragment = fragment;
        if (fragment == homeFragment) {
            title = getString(R.string.app_name);
        } else if (fragment == reviewFragment) {
            title = getString(R.string.title_review);
        } else if (fragment == settingFragment) {
            title = getString(R.string.title_setting);
        }
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (currentFragment == settingFragment) {
            outState.putBoolean(Constant.SHOULD_SHOW_SETTING_FRAGMENT, true);
        }
        super.onSaveInstanceState(outState);
    }

}
