package cn.jk.kaoyandanci.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/3.
 */

@Entity
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    String english;

    String chinese;
    Integer neverShow;
    Integer knowed;
    Integer hot;
    Integer collect;
    String phonetic;
    Date firstLearnTime;
    Date neverShowTime;
    Date needLearnTime;
    Integer unknownTime;
    Integer knowTime;
    boolean easy;
    Date lastLearnTime;


    @Generated(hash = 629362271)
    public Word(String english, String chinese, Integer neverShow, Integer knowed,
                Integer hot, Integer collect, String phonetic, Date firstLearnTime,
                Date neverShowTime, Date needLearnTime, Integer unknownTime,
                Integer knowTime, boolean easy, Date lastLearnTime) {
        this.english = english;
        this.chinese = chinese;
        this.neverShow = neverShow;
        this.knowed = knowed;
        this.hot = hot;
        this.collect = collect;
        this.phonetic = phonetic;
        this.firstLearnTime = firstLearnTime;
        this.neverShowTime = neverShowTime;
        this.needLearnTime = needLearnTime;
        this.unknownTime = unknownTime;
        this.knowTime = knowTime;
        this.easy = easy;
        this.lastLearnTime = lastLearnTime;
    }

    @Generated(hash = 3342184)
    public Word() {
    }


    public String getPhoneticFormat() {
        return "/" + phonetic + "/";
    }

    public String getLastLearnFormat() {
        if (lastLearnTime == null) {
            return "不曾学过";
        }
        if (lastLearnTime.getTime() == 0) {
            return "不曾学过";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        return formatter.format(lastLearnTime);
    }

    public boolean isNeverShow() {
        return neverShow != null;
    }

    public String getEnglish() {
        return this.english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return this.chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }


    public Integer getKnowed() {
        return this.knowed;
    }

    public void setKnowed(Integer knowed) {
        this.knowed = knowed;
    }

    public void setKnowed() {
        this.knowed = 1;
    }

    public String getPhonetic() {
        return this.phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public Integer getNeverShow() {
        return this.neverShow;
    }

    public void setNeverShow(Integer neverShow) {
        this.neverShow = neverShow;

    }

    public void setNeverShowTo1() {
        this.neverShow = 1;
    }

    public Date getFirstLearnTime() {
        return this.firstLearnTime;
    }

    public void setFirstLearnTime(Date firstLearnTime) {
        this.firstLearnTime = firstLearnTime;
    }

    public Date getNeverShowTime() {
        return this.neverShowTime;
    }

    public void setNeverShowTime(Date neverShowTime) {
        this.neverShowTime = neverShowTime;
    }

    public Date getNeedLearnTime() {
        return this.needLearnTime;
    }

    public void setNeedLearnTime(Date needLearnTime) {
        this.needLearnTime = needLearnTime;
    }

    public Integer getHot() {
        return this.hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Integer getCollect() {
        return this.collect;
    }

    public void setCollect(Integer collect) {
        this.collect = collect;
    }

    public Integer getUnknownTime() {
        return this.unknownTime;
    }

    public void setUnknownTime(Integer unknownTime) {
        this.unknownTime = unknownTime;
    }

    public Integer getKnowTime() {
        return this.knowTime;
    }

    public void setKnowTime(Integer knowTime) {
        this.knowTime = knowTime;
    }

    public Date getLastLearnTime() {
        return this.lastLearnTime;
    }

    public void setLastLearnTime(Date lastLearnTime) {
        this.lastLearnTime = lastLearnTime;
    }

    public boolean getEasy() {
        return this.easy;
    }

    public void setEasy(boolean easy) {
        this.easy = easy;
    }
}
