package cn.jk.kaoyandanci.model;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jk.kaoyandanci.util.Config;
import cn.jk.kaoyandanci.util.SPUtil;
import cn.jk.kaoyandanci.util.ToastUtil;

/**
 * Created by Administrator on 2017/6/13.
 */

public class LearnWordList extends WordList {
    //用户第一次学习就掌握了的单词.
    public List<Word> easyWord = new ArrayList<>();

    String haveLearnSp;
    int shouldLearn;
    boolean finish_today;

    public LearnWordList(Context context) {
        super(context);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        haveLearnSp = simpleDateFormat.format(new Date()) + "learn";
        int haveLearn = (int) SPUtil.get(context, haveLearnSp, 0);
        int planLearn = Config.getPlanShouldLearn();
        shouldLearn = planLearn - haveLearn;
        String querySql = "";
        String coreSql = "";
        String easySql = "";
        if (Config.coreModeIsOn()) {
            coreSql = " and hot=1 ";
        }
        if (Config.easyModeIsOn()) {
            easySql = " and easy is null ";
        }
        if (shouldLearn <= 0) {
            ToastUtil.showShort(context, "已经完成今日计划，请随意发挥");
            querySql = " where never_show is null " + coreSql + easySql + " ORDER BY RANDOM() LIMIT 1000";
        } else {
            querySql = " where never_show is null " + coreSql + easySql + " ORDER BY RANDOM() LIMIT " + shouldLearn;
        }
        words = wordDao.queryRaw(querySql);
    }

    public int getAll() {
        int planLearn = Config.getPlanShouldLearn();
        return planLearn;
    }

    @Override
    public Word next() {
        Word currentWord = getCurrent();
        //如果当前单词为空
        if (currentWord != null) {
            currentWord.setKnowed();
            if (currentWord.getFirstLearnTime() == null) {
                currentWord.setFirstLearnTime(new Date());
            }
            currentWord.setLastLearnTime(new Date());
            wordDao.update(currentWord);
        }

        shouldLearn--;
        currentPosition++;
        SPUtil.putAndApply(context, haveLearnSp, getAll() - shouldLearn);
        //如果当前位置超过应学单词数目
        if (overLast()) {
            return null;
        }
        return words.get(currentPosition);
    }


    @Override
    public int getNeedLearn() {
        return shouldLearn;
    }

    public int getPercent() {
        if (finish_today) {
            return 100;
        } else {
            return super.getPercent();
        }
    }

    @Override
    public String getListName() {
        return "学习单词";
    }

    @Override
    public String getFinishMessage() {
        return "已经完成今日学习计划";
    }

    @Override
    public void currentNeverShow() {
        Word currentWord = words.get(currentPosition);
        if (currentWord.getLastLearnTime() == null || currentWord.getLastLearnTime().getTime() == 0) {
            easyWord.add(currentWord);
        }
        super.currentNeverShow();
    }

    @Override
    public String getEmptyMessage() {
        return "我的天,你背完了所有的单词!";
    }
}
