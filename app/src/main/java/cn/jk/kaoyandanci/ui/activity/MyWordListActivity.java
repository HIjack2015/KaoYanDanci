package cn.jk.kaoyandanci.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.Word;
import cn.jk.kaoyandanci.model.WordDao;
import cn.jk.kaoyandanci.ui.adapter.WordListAdapter;
import cn.jk.kaoyandanci.ui.dialog.AddWordDialog;
import cn.jk.kaoyandanci.util.Config;
import cn.jk.kaoyandanci.util.ToastUtil;

public class MyWordListActivity extends BaseActivity {
    @BindView(R.id.wordRcy)
    RecyclerView wordRcy;

    boolean showChinese = Config.getShowChinese();
    List<Word> words;
    int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_word_list);
        ButterKnife.bind(this);
        QueryBuilder<Word> queryBuilder = wordDao.queryBuilder();
        words = queryBuilder.where(WordDao.Properties.Collect.eq(1)).list();
        getSupportActionBar().setTitle("我的单词");
        if (words.size() == 0) {
            ToastUtil.showShort(context, "在学习单词时长按卡片英文可以收藏单词.");
        }
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

            case R.id.showChineseChk:
                LinearLayoutManager layoutManager = ((LinearLayoutManager) wordRcy.getLayoutManager());
                currentPosition = layoutManager.findFirstVisibleItemPosition();

                item.setChecked(!item.isChecked());
                showChinese = item.isChecked();
                Config.setShowChinese(showChinese);
                showWord();
                return true;
            case R.id.addWord:
                new AddWordDialog().show(getFragmentManager(), " ");
            default:
                return super.onOptionsItemSelected(item);
        }
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

    public void refresh() {
        QueryBuilder<Word> queryBuilder = wordDao.queryBuilder();
        words = queryBuilder.where(WordDao.Properties.Collect.eq(1)).list();
        showWord();
    }

    @Override
    public void onResume() {
        refresh();
        super.onResume();
    }
}
