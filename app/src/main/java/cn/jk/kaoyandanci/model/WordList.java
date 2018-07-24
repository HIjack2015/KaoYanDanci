package cn.jk.kaoyandanci.model;

import android.content.Context;

import java.util.List;

import cn.jk.kaoyandanci.InitApplication;
import cn.jk.kaoyandanci.ui.activity.MainActivity;
import cn.jk.kaoyandanci.util.ToastUtil;

/**
 * Created by Administrator on 2017/6/13.
 */

public abstract class WordList {


    List<Word> words;
    Context context;
    DaoSession daoSession;
    WordDao wordDao;
    int currentPosition = 0;
    String spName;
    String title = "学习单词";

    public WordList(Context context) {
        this.context = context;
        DaoSession daoSession = ((InitApplication) context.getApplicationContext()).getDaoSession();
        wordDao = daoSession.getWordDao();
    }

    /**
     * @return 共包含多少个单词
     */
    public int getAll() {
        return words.size();
    }


    public int getPercent() {
        int all = getAll();
        if (all == 0) {
            return 100;
        }
        int learned = all - getNeedLearn();
        return (learned * 100 / all);
    }

    /**
     * @return 当前需要学习多少个
     */
    public int getNeedLearn() {
        return words.size() - currentPosition - 1;
    }

    /**
     * @return 获取下一个单词
     */
    public abstract Word next();

    /**
     * @return 当前在学单词
     */
    public Word getCurrent() {
        if (words.size() <= currentPosition) {
            return null;
        } else {
            return words.get(currentPosition);
        }
    }

    public void currentKnown() {
        if (currentPosition >= words.size()) {
            ToastUtil.showShort(context,"你背的单词太快太多了,APP出了些问题.你需要重新进一下应用.");
            return;
        }
        Word currentWord = words.get(currentPosition);

        if (currentWord.getKnowTime() == null) {
            currentWord.setKnowTime(1);
        } else {
            currentWord.setKnowTime(currentWord.getKnowTime() + 1);
        }
        wordDao.update(currentWord);
        MainActivity.DATA_CHANGED = true;

    }

    public void currentUnknown() {
        Word currentWord = words.get(currentPosition);
        if (currentWord.getUnknownTime() == null) {
            currentWord.setUnknownTime(1);
        } else {
            currentWord.setUnknownTime(currentWord.getUnknownTime() + 1);
        }
        wordDao.update(currentWord);
        MainActivity.DATA_CHANGED = true;
    }

    public void currentNeverShow() {
        Word currentWord = words.get(currentPosition);
        if (currentWord.getKnowTime() == null || currentWord.getKnowTime() == 0) {
            currentWord.setKnowTime(1);
        }
        currentWord.setNeverShow(1);
        wordDao.update(currentWord);
        MainActivity.DATA_CHANGED = true;
    }

    /**
     * 当前是否为最后一个元素
     *
     * @return
     */
    public boolean inLast() {
        return currentPosition + 1 == words.size();
    }

    /**
     * 当前下标超过list长度
     */
    public boolean overLast() {
        return currentPosition >= words.size();
    }


    public boolean isEmpty() {
        return words.isEmpty();
    }

    public Word previous() {
        if (currentPosition == 0) {
            return null;
        } else {
            currentPosition--;
        }
        return getCurrent();
    }

    public String getListName() {
        return title;
    }

    public String getFinishMessage() {
        return "完成学习单词列表";
    }

    public String getEmptyMessage() {
        return "单词列表为空";
    }
}
