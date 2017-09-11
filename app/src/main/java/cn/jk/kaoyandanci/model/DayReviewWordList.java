package cn.jk.kaoyandanci.model;

import android.content.Context;

import org.greenrobot.greendao.query.WhereCondition;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jk.kaoyandanci.util.DayUtil;
import cn.jk.kaoyandanci.util.SPUtil;


/**
 * Created by Administrator on 2017/6/13.
 */

public class DayReviewWordList extends WordList {

    Date reviewDate;


    /**
     * @param context    activity 中的context
     * @param reviewDate
     */
    public DayReviewWordList(Context context, Date reviewDate) {
        super(context);
        this.reviewDate = reviewDate;

        if (reviewDate == null) {
            reviewDate = new Date();
        }
        WhereCondition afterStart = WordDao.Properties.LastLearnTime.ge(DayUtil.getStartOfDay(reviewDate));
        WhereCondition beforeEnd = WordDao.Properties.LastLearnTime.le(DayUtil.getEndOfDay(reviewDate));
        WhereCondition shouldShow = WordDao.Properties.NeverShow.isNull();
        words = wordDao.queryBuilder().where(afterStart, beforeEnd, shouldShow).orderAsc(WordDao.Properties.LastLearnTime).list();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        spName = simpleDateFormat.format(reviewDate) + "review";

        int index = (int) SPUtil.get(context, spName, 0);
        if (index != 0) {
            currentPosition = index;
        }
    }


    /**
     * 将当前单词 置入这一天的sp中
     *
     * @return
     */
    @Override
    public Word next() {
        currentPosition++;
        if (overLast()) {
            SPUtil.putAndApply(context, spName, 0);
            return null;
        } else {

            SPUtil.putAndApply(context, spName, currentPosition);
            return words.get(currentPosition);
        }
    }

    /**
     * 复习完成后重置复习进度
     */
    public void init() {

    }

    @Override
    public String getListName() {

        return "按天复习";
    }

    @Override
    public String getFinishMessage() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        String monthDay = simpleDateFormat.format(reviewDate);
        return String.format("已经完成%s的复习计划", monthDay);
    }

    @Override
    public String getEmptyMessage() {
        return "没有需要复习的单词";
    }
}
