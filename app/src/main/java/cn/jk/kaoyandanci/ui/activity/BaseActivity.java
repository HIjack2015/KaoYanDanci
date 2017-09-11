package cn.jk.kaoyandanci.ui.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.afollestad.aesthetic.AestheticActivity;

import cn.jk.kaoyandanci.InitApplication;
import cn.jk.kaoyandanci.model.DaoSession;
import cn.jk.kaoyandanci.model.WordDao;

/**
 * Created by Administrator on 2017/6/8.
 */

public class BaseActivity extends AestheticActivity {

    DaoSession daoSession;
    WordDao wordDao;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        daoSession = ((InitApplication) getApplication()).getDaoSession();
        wordDao = daoSession.getWordDao();
        context = getApplicationContext();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public WordDao getWordDao() {
        return wordDao;
    }
}
