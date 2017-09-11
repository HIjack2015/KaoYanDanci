package cn.jk.kaoyandanci.util;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jk.kaoyandanci.R;

/**
 * Created by Administrator on 2017/6/20.
 */

public class Config {

    private static Context context;

    /**
     * @return 今日剩余需学单词数目
     */
    public static int getTodayShouldLearn() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String haveLearnSp = simpleDateFormat.format(new Date()) + "learn";
        int haveLearn = (int) SPUtil.get(context, haveLearnSp, 0);
        SPUtil.get(context, haveLearnSp, 0);
        int todayRemain = getPlanShouldLearn() - haveLearn;
        if (todayRemain < 0) {
            todayRemain = 0;
        }
        return todayRemain;
    }

    /**
     * @return 计划今日应学单词总数
     */
    public static int getPlanShouldLearn() {
        String planLearn = (String) SPUtil.get(context, Constant.PLAN_LEARN, "99");
        if (planLearn == null || planLearn.isEmpty()) {
            return 10;
        }
        return Integer.valueOf(planLearn);
    }

    public static void setPlanShouldLearn(String planShouldLearn) {
        SPUtil.putAndApply(context, Constant.PLAN_LEARN, planShouldLearn);
    }

    public static Date getExamTime() {
        String examTimeStr = (String) SPUtil.get(context, Constant.EXAM_DATE, "2017-12-23");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date examDate = simpleDateFormat.parse(examTimeStr);
            return examDate;
        } catch (Exception e) {
            Log.e("should never go there", e.toString());
            return Constant.beginDate;
        }
    }


    public static String getPieWord() {
        String pieWord = (String) SPUtil.get(context, Constant.PIE_WORD, "进度");
        return pieWord;
    }

    /**
     * call when init application
     *
     * @param initContext
     */
    public static void setContext(Context initContext) {
        context = initContext.getApplicationContext();
    }

    public static boolean shouldSearchTipShow() {
        return (boolean) SPUtil.get(context, Constant.searchTipShow, true);
    }

    public static void setSearchTipShow(boolean searchTipShow) {
        SPUtil.putAndApply(context, Constant.searchTipShow, searchTipShow);
    }


    public static boolean shouldShowGuide() {
        return (boolean) SPUtil.get(context, Constant.shouldShowGuide, true);
    }

    public static void dontShowGuide() {
        SPUtil.putAndApply(context, Constant.shouldShowGuide, false);
    }

    public static boolean getAutoDisplay() {
        return (boolean) SPUtil.get(context, Constant.AUTO_DISPLAY, false);
    }

    public static void setAutoDisplay(boolean autoDisplay) {
        SPUtil.putAndApply(context, Constant.AUTO_DISPLAY, autoDisplay);
    }

    public static String getGUID() {
        return (String) SPUtil.get(context, Constant.GUID, Constant.DEFAULT_GUID);
    }

    public static void setGuid(String guid) {
        SPUtil.putAndApply(context, Constant.GUID, guid);
    }

    public static boolean coreModeIsOn() {
        return (boolean) SPUtil.get(context, context.getString(R.string.core_mode), false);
    }
}
