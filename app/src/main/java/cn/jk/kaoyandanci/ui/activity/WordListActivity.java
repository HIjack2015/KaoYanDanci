package cn.jk.kaoyandanci.ui.activity;

import android.os.Bundle;

import com.tencent.cos.xml.utils.StringUtils;

import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.Queries;
import cn.jk.kaoyandanci.util.Config;
import cn.jk.kaoyandanci.util.Constant;

public class WordListActivity extends BaseWordListActivity {








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String label = getIntent().getStringExtra(Constant.WORD_LIST_LBL);
        boolean coreMode = Config.coreModeIsOn();
        boolean easyMode = Config.easyModeIsOn();

        getSupportActionBar().setTitle(label);
        Queries queries = Queries.getInstance(daoSession);
        wordType = label.replaceAll("\\d", "");
        String wordCountStr = label.replaceAll(wordType, "");
        if (!StringUtils.isEmpty(wordCountStr)) {
            wordCount = Integer.valueOf(wordCountStr);
        }
        words =  queries.getList(wordType, coreMode, easyMode);

        showWord();
    }




    @Override
    int getContentViewId() {
        return R.layout.activity_word_list;
    }
}
