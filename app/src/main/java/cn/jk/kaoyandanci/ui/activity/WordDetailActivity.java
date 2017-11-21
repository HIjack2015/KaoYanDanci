package cn.jk.kaoyandanci.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.Word;
import cn.jk.kaoyandanci.model.WordDao;
import cn.jk.kaoyandanci.util.ChineseCheck;
import cn.jk.kaoyandanci.util.Constant;
import cn.jk.kaoyandanci.util.MediaUtil;
import cn.jk.kaoyandanci.util.ToastUtil;

public class WordDetailActivity extends BaseActivity {

    @BindView(R.id.wordDetailBtn)
    TextView wordDetailBtn;
    @BindView(R.id.voiceBtn)
    Button voiceBtn;
    String english;
    @BindView(R.id.englishTxt)
    TextView englishTxt;
    @BindView(R.id.phoneticTxt)
    TextView phoneticTxt;
    @BindView(R.id.knownTimeTxt)
    TextView knownTimeTxt;
    @BindView(R.id.unknownTimeTxt)
    TextView unknownTimeTxt;
    @BindView(R.id.lastLearnTime)
    TextView lastLearnTime;
    @BindView(R.id.isNeverShowTxt)
    TextView isNeverShowTxt;
    @BindView(R.id.chineseTxt)
    TextView chineseTxt;
    @BindView(R.id.cancelGraspBtn)
    Button cancelGraspBtn;
    boolean isNeverShow;
    @BindView(R.id.coreImg)
    ImageView coreImg;
    @BindView(R.id.easyTxt)
    TextView easyTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        english = intent.getStringExtra(Constant.ENGLISH);
        if (english == null || english.equals("")) {
            Log.e("error", "怎么可能开始没有english传入");
        }

        final Word word = wordDao.queryBuilder().where(WordDao.Properties.English.eq(english)).build().list().get(0);
        setContentView(R.layout.activity_word_detail);
        ButterKnife.bind(this);


        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPronunciation();
            }
        });
        wordDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YoudaoWordActivity.class);
                intent.putExtra(Constant.ENGLISH, english);
                startActivity(intent);
            }
        });
        if (word.getHot() != null) {
            coreImg.setVisibility(View.VISIBLE);
        } else {
            coreImg.setVisibility(View.GONE);
        }

        englishTxt.setText(word.getEnglish());
        phoneticTxt.setText(word.getPhoneticFormat());
        isNeverShowTxt.setText(word.isNeverShow() ? "是" : "否");
        String knowTime = word.getKnowTime() == null ? "0" : word.getKnowTime().toString();
        String unknownTime = word.getUnknownTime() == null ? "0" : word.getUnknownTime().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String lastLearnTimeStr;
        if (word.getLastLearnTime() == null || word.getLastLearnTime().getTime() == 0) {
            lastLearnTimeStr = "不曾学过";
        } else {
            lastLearnTimeStr = simpleDateFormat.format(word.getLastLearnTime());
        }
        if (word.getEasy() == true) {
            easyTxt.setVisibility(View.VISIBLE);
        } else {
            easyTxt.setVisibility(View.INVISIBLE);
        }

        knownTimeTxt.setText(knowTime);
        unknownTimeTxt.setText(unknownTime);
        lastLearnTime.setText(lastLearnTimeStr);


        String chinese = word.getChinese();
        String regex = "[a-z]+\\.";
        Matcher matcher = Pattern.compile(regex).matcher(chinese);
        boolean isFirst = true;
        while (matcher.find()) {
            String showed = matcher.group();
            if (!isFirst) {
                //防止 出现a./vt.这种情况发生
                int index = chinese.indexOf(showed);
                String prefix = chinese.substring(0, index);
                if (ChineseCheck.containChinese(prefix)) {
                    chinese = chinese.replaceFirst(showed, "\n" + showed);
                }
            }
            isFirst = false;
        }
        chineseTxt.setText(chinese);
        isNeverShow = word.isNeverShow();
        if (isNeverShow) {
            cancelGraspBtn.setText("取消掌握");
        } else {
            cancelGraspBtn.setText("已经掌握");
        }

        cancelGraspBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNeverShow) {
                    cancelGraspBtn.setText("已经掌握");
                    word.setNeverShow(null);
                    ToastUtil.showShort(context, "成功取消掌握单词" + word.getEnglish());
                    wordDao.update(word);
                } else {
                    word.setNeverShow(1);
                    ToastUtil.showShort(context, "成功掌握单词" + word.getEnglish());
                    cancelGraspBtn.setText("取消掌握");
                    wordDao.update(word);
                }

                isNeverShow = !isNeverShow;
                isNeverShowTxt.setText(word.isNeverShow() ? "是" : "否");

            }
        });

        getSupportActionBar().setTitle("单词详情");
    }

    public void displayPronunciation() {
        String voiceUrl = Constant.shanbeiVoiceUrl + english + ".mp3";
        boolean netWorkOk = MediaUtil.display(voiceUrl, context);
        if (!netWorkOk) {
            ToastUtil.showShort(context, "单词发音需要连接网络.");
        }
    }

}
