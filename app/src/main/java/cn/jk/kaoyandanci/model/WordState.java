package cn.jk.kaoyandanci.model;

/**
 * <pre>
 *     author : jiakang
 *     e-mail : 1079153785@qq.com
 *     time   : 2018/07/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class WordState {
    public static final int isNeverShow = 1;


    public static boolean isNeverShow(Word word) {
        return word.getNeverShow() != null && word.getNeverShow() == isNeverShow;
    }

    public static boolean isCollect(Word word) {
        return word.getCollect() != null && word.getCollect() == 1;
    }
}
