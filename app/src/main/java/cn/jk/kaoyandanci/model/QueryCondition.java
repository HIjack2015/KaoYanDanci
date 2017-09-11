package cn.jk.kaoyandanci.model;

import org.greenrobot.greendao.query.WhereCondition;

/**
 * Created by Administrator on 2017/7/11.
 */

public class QueryCondition {
    static WhereCondition neverShow = WordDao.Properties.NeverShow.in(null, 0);

}
