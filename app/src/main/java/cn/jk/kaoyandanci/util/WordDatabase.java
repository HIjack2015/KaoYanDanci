package cn.jk.kaoyandanci.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Administrator on 2017/4/12.
 */

public class WordDatabase extends SQLiteAssetHelper {

    private static final int DATABASE_VERSION = 3;
    Context context;

    public WordDatabase(Context context) {
        super(context, Constant.DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        //印象中开始上架version就是2
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE word ADD COLUMN LAST_LEARN_TIME INTEGER DEFAULT 0");
        }
    }
}
