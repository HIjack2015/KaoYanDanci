package cn.jk.kaoyandanci.model;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import cn.jk.kaoyandanci.util.Constant;

/**
 * Created by Administrator on 2017/8/3.
 */

public class Queries {
    WordDao wordDao;

    private static Queries ourInstance = null;

    public static Queries getInstance(DaoSession daoSession) {
        if (ourInstance == null) {
            ourInstance = new Queries(daoSession);
        }
        return ourInstance;
    }

    private Queries(DaoSession daoSession) {
        wordDao = daoSession.getWordDao();

    }

    public List<Word> getList(String state, boolean coreMode, boolean easyMode) {
        QueryBuilder<Word> queryBuilder = wordDao.queryBuilder();
        switch (state) {
            case Constant.NEVER_SHOW:
                queryBuilder.where(WordDao.Properties.NeverShow.eq("1"));
                break;
            case Constant.UNKNOWN:
                queryBuilder.where(WordDao.Properties.KnowTime.isNull()).
                        where(WordDao.Properties.UnknownTime.gt(0)).where(WordDao.Properties.NeverShow.isNull());
                break;
            case Constant.NOT_LEARNED:
                queryBuilder.where(WordDao.Properties.KnowTime.isNull()).
                        where(WordDao.Properties.UnknownTime.isNull()).where(WordDao.Properties.NeverShow.isNull());
                break;
            case Constant.KNOWED:
                queryBuilder.where(WordDao.Properties.KnowTime.gt("0")).where(WordDao.Properties.NeverShow.isNull());
                break;
            case Constant.EASY:
                return queryBuilder.where(WordDao.Properties.Easy.eq(true)).list();
        }
        if (coreMode) {
            queryBuilder.where(WordDao.Properties.Hot.eq(1));
        }
        if (easyMode) {
            //TODO 数据库得加字段.
        }
        return queryBuilder.list();
    }
}
