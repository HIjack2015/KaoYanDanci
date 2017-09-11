package cn.jk.kaoyandanci.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.CommonQuestion;
import cn.jk.kaoyandanci.ui.adapter.QuestionAdapter;
import cn.jk.kaoyandanci.util.AssetsUtil;

public class CommonQuestionActivity extends BaseActivity {

    @BindView(R.id.questionRcl)
    RecyclerView questionRcl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_question);

        List<CommonQuestion> commonQuestions = AssetsUtil.loadCardFromFile(this, "question.json");
        ButterKnife.bind(this);
        questionRcl.setHasFixedSize(true);

        questionRcl.setLayoutManager(new LinearLayoutManager(this));
        questionRcl.setAdapter(new QuestionAdapter(commonQuestions, this));
        getSupportActionBar().setTitle("常见问题");
    }
}
