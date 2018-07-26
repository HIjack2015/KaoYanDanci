package cn.jk.kaoyandanci.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.greenrobot.greendao.query.QueryBuilder;

import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.Word;
import cn.jk.kaoyandanci.model.WordDao;
import cn.jk.kaoyandanci.ui.dialog.AddWordDialog;
import cn.jk.kaoyandanci.util.Config;

public class MyWordListActivity extends BaseWordListActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordType = "我的单词";
        QueryBuilder<Word> queryBuilder = wordDao.queryBuilder();
        words = queryBuilder.where(WordDao.Properties.Collect.eq(1)).list();
        showWord();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_my_word_list, menu);
        MenuItem showChineseChk = menu.findItem(R.id.showChineseChk);
        showChineseChk.setChecked(Config.getShowChinese());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.addWord:
                new AddWordDialog().show(getFragmentManager(), "addWordDialog");
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    int getContentViewId() {
        return R.layout.activity_my_word_list;
    }

    public void addWord(Word word) {
        words.add(0, word);
        showWord();

    }
}
