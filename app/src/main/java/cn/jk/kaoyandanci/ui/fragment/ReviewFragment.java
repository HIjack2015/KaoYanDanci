package cn.jk.kaoyandanci.ui.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.greenrobot.greendao.query.WhereCondition;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jk.kaoyandanci.InitApplication;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.DaoSession;
import cn.jk.kaoyandanci.model.Word;
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
    MaterialCalendarView calendarView;
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
    @BindView(R.id.knowLyt)
    LinearLayout knowLyt;
    @BindView(R.id.unknownLyt)
    LinearLayout unknownLyt;
    @BindView(R.id.neverShowLyt)
    LinearLayout neverShowLyt;

    Date reviewDate = calendar.getTime();

    WhereCondition afterStart, beforeEnd, neverShow, shouldShow, learned, unLearned;

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
        //TODO 这里有一个bug.

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                calendar = new GregorianCalendar(calendarDay.getYear(), calendarDay.getMonth(), calendarDay.getDay());
                setCount();
            }
        });
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
                highLightMonth(calendarDay.getYear(), calendarDay.getMonth());
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
        refresh();
        return view;
    }

    public void refresh() {
        setCount();
        calendarView.setSelectedDate(Calendar.getInstance().getTime());
        highLightMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
    }


    public void setCount() {
        reviewDate = calendar.getTime();
        Integer knowCount = getDayKnowList().size();
        Integer unKnownCount = getDayUnknownList().size();
        Integer neverShowCount = getDayNeverShowList().size();
        unknownCountTxt.setText(unKnownCount.toString());
        knowCountTxt.setText(knowCount.toString());
        neverShowCountTxt.setText(neverShowCount.toString());
    }

    private List<Word> getDayKnowList() {
        setWhereCondition();
        return wordDao.queryBuilder().where(afterStart, beforeEnd, shouldShow, learned).list();
    }

    private List<Word> getDayUnknownList() {
        setWhereCondition();
        return wordDao.queryBuilder().where(afterStart, beforeEnd, shouldShow, unLearned).list();
    }

    private List<Word> getDayNeverShowList() {
        setWhereCondition();
        return wordDao.queryBuilder().where(afterStart, beforeEnd, neverShow).list();
    }

    private void setWhereCondition() {
        afterStart = WordDao.Properties.LastLearnTime.ge(DayUtil.getStartOfDay(reviewDate));
        beforeEnd = WordDao.Properties.LastLearnTime.le(DayUtil.getEndOfDay(reviewDate));
        shouldShow = WordDao.Properties.NeverShow.isNull();
        learned = WordDao.Properties.KnowTime.gt(0);
        unLearned = WordDao.Properties.KnowTime.isNull();
        neverShow = WordDao.Properties.NeverShow.gt(0);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @OnClick({R.id.knowLyt, R.id.unknownLyt, R.id.neverShowLyt})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(), LearnWordActivity.class);

        List<Word> wordList = null;
        String title = null;
        switch (view.getId()) {
            case R.id.knowLyt:
                wordList = getDayKnowList();
                title = "认识单词按天复习";
                break;
            case R.id.unknownLyt:
                wordList = getDayUnknownList();
                title = "不认识单词按天复习";
                break;
            case R.id.neverShowLyt:
                wordList = getDayNeverShowList();
                title = "已掌握单词复习";
                break;
        }

        intent.putExtra(Constant.WORD_LIST, (Serializable) wordList);
        intent.putExtra(Constant.TITLE, title);

        startActivity(intent);

    }
    @Override
    public void onResume() {
        super.onResume();

    }

    void highLightMonth(int year, int month) {
        Date firstDayOfMonth = DayUtil.getFirstDayOfMonth(year, month);
        Date lastDayOfMonth = DayUtil.getLastDayOfMonth(year, month);

        Set<CalendarDay> days = new HashSet<>();


        List<Word> words = wordDao.queryBuilder().where(WordDao.Properties.LastLearnTime.gt(firstDayOfMonth), WordDao.Properties.LastLearnTime.lt(lastDayOfMonth)).listLazy();
        for (Word word : words) {
            Date learnTime = word.getLastLearnTime();//TODO 先这样写,要是太慢的话就新建一个表.
            days.add(CalendarDay.from(learnTime));
        }

        calendarView.addDecorator(new CalendarViewDecorator(getActivity(), days));


    }

    class CalendarViewDecorator implements DayViewDecorator {
        Context mContext;
        Set<CalendarDay> mDays;

        public CalendarViewDecorator(Context context, Set<CalendarDay> days) {
            mContext = context;
            mDays = days;
        }


        @Override
        public boolean shouldDecorate(CalendarDay calendarDay) {
            return mDays.contains(calendarDay);
        }

        @Override
        public void decorate(DayViewFacade dayViewFacade) {
            dayViewFacade.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.blue_star));
        }
    }
}
