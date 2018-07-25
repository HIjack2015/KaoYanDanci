package cn.jk.kaoyandanci.ui.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jk.kaoyandanci.InitApplication;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.DaoSession;
import cn.jk.kaoyandanci.model.Queries;
import cn.jk.kaoyandanci.model.WordDao;
import cn.jk.kaoyandanci.ui.activity.LearnWordActivity;
import cn.jk.kaoyandanci.ui.activity.MainActivity;
import cn.jk.kaoyandanci.ui.activity.WordListActivity;
import cn.jk.kaoyandanci.util.Config;
import cn.jk.kaoyandanci.util.Constant;
import cn.jk.kaoyandanci.util.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.remainTimeTxt)
    TextView remainTimeTxt;


    @BindView(R.id.startLearnBtn)
    Button startLearnBtn;

    @BindView(R.id.progressRatePi)
    PieChart progressRatePi;

    DaoSession daoSession;
    WordDao wordDao;
    Context context;
    Timer timer = new Timer();
    MainActivity mainActivity;
    @BindView(R.id.countDownView)
    CardView countDownView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public void refresh() {
        drawPi();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {        //TODO 这个有啥用??
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (MainActivity.DATA_CHANGED) {
                drawPi();
                MainActivity.DATA_CHANGED = false;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();
        daoSession = ((InitApplication) getActivity().getApplication()).getDaoSession();
        wordDao = daoSession.getWordDao();
        context = getActivity().getApplicationContext();

        startLearnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LearnWordActivity.class);
                intent.putExtra(Constant.MODE, Constant.LEARN_MODE);
                startActivity(intent);
            }
        });

        timer.schedule(new ShowRemainTime(), 0, 1000);
        drawPi();
        countDownView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String[] encourageSentence = Constant.ENCOURAGE_SENTENCE;
                int choosePosition = new Random().nextInt(encourageSentence.length);
                String showSentence = encourageSentence[choosePosition];
                ToastUtil.showShort(context, showSentence);
                return false;
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void drawPi() {
        Queries queries = Queries.getInstance(daoSession);
        boolean isCoreMode = Config.coreModeIsOn();
        boolean isEasyMode = Config.easyModeIsOn();
        Integer neverShowCount = queries.getList(Constant.NEVER_SHOW, isCoreMode, isEasyMode).size();
        Integer knownWordCount = queries.getList(Constant.KNOWED, isCoreMode, isEasyMode).size();
        Integer unknownWordCount = queries.getList(Constant.UNKNOWN, isCoreMode, isEasyMode).size();
        Integer notLearnYetCount = queries.getList(Constant.NOT_LEARNED, isCoreMode, isEasyMode).size();

        // todayShouldLearnTxt.setText(String.valueOf(Config.getPlanShouldLearn()));

        progressRatePi.setCenterText(Config.getPieWord());

        List<PieEntry> entries = new ArrayList<>();
        final Integer[] counts = {neverShowCount, knownWordCount, unknownWordCount};
        if (notLearnYetCount > 500) {
            for (int i = 0; i < counts.length; i++) {
                if (counts[i] < 500) {
                    counts[i] += 500;
                }
            }
        }
        entries.add(new PieEntry(counts[0], Constant.NEVER_SHOW + neverShowCount));
        entries.add(new PieEntry(counts[1], Constant.KNOWED + knownWordCount));
        entries.add(new PieEntry(counts[2], Constant.UNKNOWN + unknownWordCount));
        entries.add(new PieEntry(notLearnYetCount, Constant.NOT_LEARNED + notLearnYetCount));

        PieDataSet set = new PieDataSet(entries, "总进度");
        set.setDrawValues(false);
        set.setValueTextSize(18);


        int grey4 = ContextCompat.getColor(context, R.color.grey400);
        int grey6 = ContextCompat.getColor(context, R.color.grey600);
        int grey8 = ContextCompat.getColor(context, R.color.grey800);
        int grey9 = ContextCompat.getColor(context, R.color.grey900);
        set.setColors(grey4, grey6, grey8, grey9);
        PieData data = new PieData(set);
        progressRatePi.setCenterTextSize(40);
        progressRatePi.getDescription().setEnabled(false);
        progressRatePi.getLegend().setEnabled(false);

        progressRatePi.setData(data);
        progressRatePi.invalidate();
        progressRatePi.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                e.getData();
                String label = ((PieEntry) e).getLabel();
                Intent intent = new Intent(context, WordListActivity.class);
                intent.putExtra(Constant.WORD_LIST_LBL, label);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void setRemainTime() {
        Date now = new Date();
        long diff = Config.getExamTime().getTime() - now.getTime();
        if (diff < 0) {
            Config.addExamDate();
            return;
        }


        Long diffSeconds = diff / 1000 % 60;
        Long diffMinutes = diff / (60 * 1000) % 60;
        Long diffHours = diff / (60 * 60 * 1000) % 24;
        Long diffDays = diff / (24 * 60 * 60 * 1000);


        String remainTime = diffDays + "天" + String.format(Locale.getDefault(), "%02d", diffHours) + "时"
                + String.format(Locale.getDefault(), "%02d", diffMinutes) + "分" + String.format(Locale.getDefault(), "%02d", diffSeconds) + "秒";
        remainTimeTxt.setText(remainTime);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    class ShowRemainTime extends TimerTask {
        public void run() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRemainTime();
                    }
                });
            }
        }
    }

}
