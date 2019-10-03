package cn.jk.kaoyandanci;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDexApplication;

import com.danikula.videocache.HttpProxyCacheServer;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jk.kaoyandanci.model.DaoMaster;
import cn.jk.kaoyandanci.model.DaoSession;
import cn.jk.kaoyandanci.util.Config;
import cn.jk.kaoyandanci.util.Constant;
import cn.jk.kaoyandanci.util.MediaFileNameGenerator;
import cn.jk.kaoyandanci.util.SPUtil;
import cn.jk.kaoyandanci.util.WordDatabase;

import static android.os.Build.ID;


/**
 * Created by then24 on 2015/9/5.
 */
public class InitApplication extends MultiDexApplication {

    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    DaoMaster daoMaster;

    Context context;
    private DaoSession daoSession;
    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        InitApplication app = (InitApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        Config.setContext(context);

        boolean isFirstOpen = !SPUtil.contains(context, Constant.FIRST_OPEN);
//        isFirstOpen=false; //for init db , delete this later
        if (isFirstOpen) {
            context.deleteDatabase(Constant.DATABASE_NAME);
            new WordDatabase(context).getWritableDatabase();
            helper = new DaoMaster.DevOpenHelper(this, Constant.DATABASE_NAME, null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();

            SPUtil.putAndApply(context, Constant.FIRST_OPEN, "no");

        } else {

            db = new WordDatabase(context).getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }

        CrashReport.initCrashReport(getApplicationContext(), "11cc1a2754", false);

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    private HttpProxyCacheServer newProxy() {
        HttpProxyCacheServer proxy = (new HttpProxyCacheServer.Builder(context))
                .fileNameGenerator(new MediaFileNameGenerator())
                .build();
        return proxy;
    }
}
