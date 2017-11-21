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
    ArrayList<Word> words;
    int currentPosition = 0;

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
        WordListAdapter wordListAdapter = new WordListAdapter(words, this);
        wordListAdapter.setShowChinese(showChinese);
        wordRcy.setHasFixedSize(true);
        wordRcy.setLayoutManager(new LinearLayoutManager(context));
        wordRcy.setAdapter(wordListAdapter);
        wordListAdapter.notifyDataSetChanged();
        LinearLayoutManager layoutManager = ((LinearLayoutManager) wordRcy.getLayoutManager());
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
                LinearLayoutManager layoutManager = ((LinearLayoutManager) wordRcy.getLayoutManager());
                currentPosition = layoutManager.findFirstVisibleItemPosition();

                item.setChecked(!item.isChecked());
                showChinese = item.isChecked();
                Config.setShowChinese(showChinese);
                showWord();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
