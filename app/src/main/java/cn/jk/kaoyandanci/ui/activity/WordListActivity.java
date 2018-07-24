package cn.jk.kaoyandanci.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.Queries;
import cn.jk.kaoyandanci.model.Word;
import cn.jk.kaoyandanci.ui.adapter.WordListAdapter;
import cn.jk.kaoyandanci.util.Config;
import cn.jk.kaoyandanci.util.Constant;

public class WordListActivity extends BaseActivity {

    @BindView(R.id.wordRcy)
    RecyclerView wordRcy;

    boolean showChinese = Config.getShowChinese();
    boolean showEdt = false;

    ArrayList<Word> words;
    int currentPosition = 0;

    WordListAdapter wordListAdapter;
    LinearLayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        ButterKnife.bind(this);

        String label = getIntent().getStringExtra(Constant.WORD_LIST_LBL);
        boolean coreMode = Config.coreModeIsOn();
        boolean easyMode = Config.easyModeIsOn();

        getSupportActionBar().setTitle(label);
        Queries queries = Queries.getInstance(daoSession);
        String wordType = label.replaceAll("\\d", "");
        words = (ArrayList<Word>) queries.getList(wordType, coreMode, easyMode);

        showWord();
    }

    private void showWord() {
        if (wordListAdapter == null) {
            wordListAdapter = new WordListAdapter(words, this);
            wordRcy.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(context);
            wordRcy.setLayoutManager(layoutManager);
            wordRcy.setAdapter(wordListAdapter);
        }

        wordListAdapter.setShowEdt(showEdt);
        wordListAdapter.setShowChinese(showChinese);
        wordListAdapter.notifyDataSetChanged();
        layoutManager.scrollToPosition(currentPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_word_list, menu);
        MenuItem showChineseChk = menu.findItem(R.id.showChineseChk);
        showChineseChk.setChecked(Config.getShowChinese());
        return true;
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
    public void onPause() {
        wordRcy.stopScroll();  //这里有一个bug...就是在scroll的时候pause aesthetic就会崩溃.
        super.onPause();
    }

    public void neverShow(Word word) {
        if (word.getKnowTime() == null || word.getKnowTime() == 0) {
            word.setKnowTime(1);
        }
        word.setNeverShow(1);
        wordDao.update(word);
    }
}
