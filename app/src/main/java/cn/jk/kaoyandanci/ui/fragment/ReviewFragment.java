package cn.jk.kaoyandanci.ui.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jk.kaoyandanci.InitApplication;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.DaoSession;
import cn.jk.kaoyandanci.model.WordDao;
import cn.jk.kaoyandanci.ui.activity.LearnWordActivity;
import cn.jk.kaoyandanci.util.Constant;
import cn.jk.kaoyandanci.util.DayUtil;
import cn.jk.kaoyandanci.util.SPUtil;
import cn.jk.kaoyandanci.util.ToastUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {

    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.startReviewBtn)
    Button startReviewBtn;
    Calendar calendar = Calendar.getInstance();
    DaoSession daoSession;
    WordDao wordDao;
    Context context;

    @BindView(R.id.knowCount)
    TextView knowCountTxt;

    @BindView(R.id.unknownCount)
    TextView unknownCountTxt;

    @BindView(R.id.neverShowCount)
    TextView neverShowCountTxt;


    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        ButterKnife.bind(this, view);

        daoSession = ((InitApplication) getActivity().getApplication()).getDaoSession();
        wordDao = daoSession.getWordDao();
        context = getActivity().getApplicationContext();


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar = new GregorianCalendar(year, month, dayOfMonth);
                setCount();
            }
        });


        startReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date selectDate = calendar.getTime();
                Intent intent = new Intent(getActivity(), LearnWordActivity.class);
                intent.putExtra(Constant.MODE, Constant.REVIEW_MODE);
                intent.putExtra(Constant.REVIEW_DATE, selectDate);
                startActivity(intent);
            }
        });
        startReviewBtn.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), LearnWordActivity.class);
                intent.putExtra(Constant.MODE, Constant.AUTO_REVIEW_MODE);
                startActivity(intent);
                return false;
            }
        });
        setCount();
        return view;
    }

    public void refresh() {
        setCount();
    }


    public void setCount() {
        Date reviewDate = calendar.getTime();
        WhereCondition afterStart = WordDao.Properties.LastLearnTime.ge(DayUtil.getStartOfDay(reviewDate));
        WhereCondition beforeEnd = WordDao.Properties.LastLearnTime.le(DayUtil.getEndOfDay(reviewDate));
        WhereCondition shouldShow = WordDao.Properties.NeverShow.isNull();
        WhereCondition learned = WordDao.Properties.KnowTime.gt(0);
        WhereCondition unLearned = WordDao.Properties.KnowTime.isNull();
        WhereCondition neverShow = WordDao.Properties.NeverShow.gt(0);

        Integer knowCount = wordDao.queryBuilder().where(afterStart, beforeEnd, shouldShow, learned).listLazy().size();
        Integer unKnownCount = wordDao.queryBuilder().where(afterStart, beforeEnd, shouldShow, unLearned).listLazy().size();
        Integer neverShowCount = wordDao.queryBuilder().where(afterStart, beforeEnd, neverShow).listLazy().size();
        unknownCountTxt.setText(unKnownCount.toString());
        knowCountTxt.setText(knowCount.toString());
        neverShowCountTxt.setText(neverShowCount.toString());
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            boolean haveShowTip = (boolean) SPUtil.get(context, Constant.SMART_REVIEW_TIP, false);
            if (!haveShowTip) {
                ToastUtil.showShort(context, "长按开始复习可以进入智能复习呦");
                SPUtil.putAndApply(context, Constant.SMART_REVIEW_TIP, true);
            }
        }

    }
}
