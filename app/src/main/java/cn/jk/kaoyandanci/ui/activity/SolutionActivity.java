package cn.jk.kaoyandanci.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.CommonQuestion;
import cn.jk.kaoyandanci.util.Constant;

public class SolutionActivity extends BaseActivity {

    @BindView(R.id.questionTxt)
    TextView questionTxt;
    @BindView(R.id.solutionTxt)
    TextView solutionTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        ButterKnife.bind(this);
        CommonQuestion commonQuestion = (CommonQuestion) getIntent().getSerializableExtra(Constant.QUESTION);
        questionTxt.setText(commonQuestion.getQuestion());
        solutionTxt.setText("     " + commonQuestion.getSolution());
        getSupportActionBar().setTitle("问题解答");
    }
}
