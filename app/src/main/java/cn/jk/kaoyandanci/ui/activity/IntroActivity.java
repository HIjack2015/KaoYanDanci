package cn.jk.kaoyandanci.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import com.github.paolorotolo.appintro.AppIntro;

import cn.jk.kaoyandanci.ui.fragment.Intro1Fragment;
import cn.jk.kaoyandanci.ui.fragment.Intro2Fragment;
import cn.jk.kaoyandanci.ui.fragment.Intro3Fragment;
import cn.jk.kaoyandanci.util.Config;
import cn.jk.kaoyandanci.util.Constant;

/**
 * Created by Administrator on 2017/6/30.
 */

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFadeAnimation();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // Note here that we DO NOT use setContentView();
        Fragment intro1 = new Intro1Fragment();
        Fragment intro2 = new Intro2Fragment();
        Fragment intro3 = new Intro3Fragment();

        addSlide(intro1);
        addSlide(intro2);
        addSlide(intro3);


        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);
        setSkipText("跳过");
        setDoneText("开始");

        if (Config.getGUID().equals(Constant.DEFAULT_GUID)) {
            Config.setGuid(java.util.UUID.randomUUID().toString());
        }

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}