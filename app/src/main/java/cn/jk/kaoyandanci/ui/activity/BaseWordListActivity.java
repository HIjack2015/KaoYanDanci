package cn.jk.kaoyandanci.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.Word;
import cn.jk.kaoyandanci.model.WordState;
import cn.jk.kaoyandanci.ui.adapter.WordListAdapter;
import cn.jk.kaoyandanci.util.Config;

/**
 * <pre>
 *     author : jiakang
 *     e-mail : 1079153785@qq.com
 *     time   : 2018/07/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public abstract class BaseWordListActivity extends BaseActivity {
    @BindView(R.id.wordRcy)
    RecyclerView wordRcy;

    @BindView(R.id.empty_view)
    View emptyView;


    boolean showChinese = Config.getShowChinese();
    boolean showEdt = false;

    List<Word> words;
    int currentPosition = 0;

    WordListAdapter wordListAdapter;
    LinearLayoutManager layoutManager;

    String wordType = "单词列表";
    int wordCount = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
    }

    @Override
    public void onPause() {
        wordRcy.stopScroll();  //这里有一个bug...就是在scroll的时候pause aesthetic就会崩溃.
        super.onPause();
    }

    public void reverseNeverShow(Word word) {
        if (WordState.isNeverShow(word)) {
            word.setNeverShow(null);
            wordDao.update(word);
        } else {
            if (word.getKnowTime() == null || word.getKnowTime() == 0) {
                word.setKnowTime(1);
                word.setLastLearnTime(new Date());
            }
            word.setNeverShow(1);
            wordDao.update(word);
        }
        wordCount--;
        getSupportActionBar().setTitle(wordType + wordCount);
    }

    public void reverseCollect(Word word) {
        if (WordState.isCollect(word)) {
            word.setCollect(0);
        } else {
            word.setCollect(1);
        }
        wordDao.update(word);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.showChineseChk:

                currentPosition = layoutManager.findFirstVisibleItemPosition();

                item.setChecked(!item.isChecked());
                showChinese = item.isChecked();
                Config.setShowChinese(showChinese);
                showWord();

                return true;
            case R.id.showEditChk:
                item.setChecked(!item.isChecked());
                showEdt = item.isChecked();
                showWord();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_word_list, menu);
        MenuItem showChineseChk = menu.findItem(R.id.showChineseChk);
        showChineseChk.setChecked(Config.getShowChinese());
        return true;
    }

    public void showWord() {
        if (wordListAdapter == null) {
            wordListAdapter = new WordListAdapter(words, this);
            wordRcy.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(context);
            wordRcy.setLayoutManager(layoutManager);
            wordRcy.setAdapter(wordListAdapter);
        }

        if (words.size() != 0) {
            wordCount = words.size();
            getSupportActionBar().setTitle(wordType + wordCount);
        }
        if (words.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }

        wordListAdapter.setShowEdt(showEdt);
        wordListAdapter.setShowChinese(showChinese);
        wordListAdapter.notifyDataSetChanged();
        layoutManager.scrollToPosition(currentPosition);
    }

    abstract int getContentViewId();
}
