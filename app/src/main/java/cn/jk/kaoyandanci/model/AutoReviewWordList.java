package cn.jk.kaoyandanci.model;

import android.content.Context;

import java.util.Date;


/**
 * Created by Administrator on 2017/6/13.
 */

public class AutoReviewWordList extends WordList {

    Date reviewDate;


    /**
     * @param context activity 中的context
     */
    public AutoReviewWordList(Context context) {
        super(context);
        long now = new Date().getTime();
        long[][] duringSecond = new long[][]{
                {3, 8},
                {20, 40},
                {10 * 60, 14 * 60},
                {22 * 60, 26 * 60},
                {(24 * 2) * 60, (24 * 3) * 60},
                {(24 * 6) * 60, (24 * 7) * 60},
                {(24 * 14) * 60, (24 * 15) * 60}
        };
        for (int i = 0; i < duringSecond.length; i++) {
            for (int j = 0; j < duringSecond[0].length; j++) {
                duringSecond[i][j] = now - duringSecond[i][j] * 60 * 1000;
            }
        }
        boolean showAnd = false;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < duringSecond.length; i++) {

            if (showAnd) {
                stringBuffer.append(" or  ");
            } else {
                stringBuffer.append(" where ( ");
                showAnd = true;

            }
            long before = duringSecond[i][0];
            long after = duringSecond[i][1];
            String condition = " (last_learn_time>" + after + " and last_learn_time<" + before + ") ";
            stringBuffer.append(condition);

        }
        stringBuffer.append(")and never_show is null order by last_learn_time");
        words = wordDao.queryRaw(stringBuffer.toString());
        currentPosition = 0;
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
            return null;
        } else {
            return words.get(currentPosition);
        }
    }

    @Override
    public String getListName() {
        return "智能复习";
    }

    @Override
    public String getFinishMessage() {
        return "已经完成智能复习";
    }

    @Override
    public String getEmptyMessage() {
        return "没有需要智能复习的单词";
    }
}
