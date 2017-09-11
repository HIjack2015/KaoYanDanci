package cn.jk.kaoyandanci.model;

import android.content.Context;

import java.util.List;

/**
 * Created by jack on 2017/9/11.
 * wordList 的默认实现.
 */

public class DefaultWordList extends WordList {
    /**
     * @param context
     * @param querySql dao.queryRaw()的参数.
     * @param title    显示在actionbar上的标题.
     */
    public DefaultWordList(Context context, String querySql, String title) {
        super(context);
        words = wordDao.queryRaw(querySql);
        this.title = title;
    }

    /**
     * @param context
     * @param words
     * @param title   显示在actionbar上的标题.
     */
    public DefaultWordList(Context context, List<Word> words, String title) {
        super(context);
        this.words = words;
        this.title = title;
    }

    /**
     * @return 下一个单词.
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
}
