package cn.jk.kaoyandanci.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        ButterKnife.bind(this);

        String label = getIntent().getStringExtra(Constant.WORD_LIST_LBL);
        boolean coreMode = Config.coreModeIsOn();
        getSupportActionBar().setTitle(label);
        Queries queries = Queries.getInstance(daoSession);
        String wordType = label.replaceAll("\\d", "");
        ArrayList<Word> words = (ArrayList<Word>) queries.getList(wordType, coreMode, false);

        WordListAdapter wordListAdapter = new WordListAdapter(words, this);
        wordRcy.setHasFixedSize(true);
        wordRcy.setLayoutManager(new LinearLayoutManager(context));
        wordRcy.setAdapter(wordListAdapter);
        wordListAdapter.notifyDataSetChanged();
    }
}
