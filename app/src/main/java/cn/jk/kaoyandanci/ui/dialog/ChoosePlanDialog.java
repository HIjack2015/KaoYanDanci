package cn.jk.kaoyandanci.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import cn.jk.kaoyandanci.InitApplication;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.Queries;
import cn.jk.kaoyandanci.model.WordDao;
import cn.jk.kaoyandanci.ui.activity.MainActivity;
import cn.jk.kaoyandanci.util.Config;
import cn.jk.kaoyandanci.util.Constant;
import cn.jk.kaoyandanci.util.DayUtil;
import cn.jk.kaoyandanci.util.ToastUtil;

/**
 * Created by Administrator on 2017/7/10.
 */

public class ChoosePlanDialog extends DialogFragment {
    @BindView(R.id.learnPerDayTxt)
    EditText learnPerDayTxt;
    @BindView(R.id.needDayTxt)
    EditText needDayTxt;
    @BindView(R.id.finishTimeTxt)
    TextView finishTimeTxt;
    MainActivity context;
    WordDao wordDao;
    //未掌握单词数目.
    int unGraspCount =0;
    int learnPerDayRecord = -1;
    int needDayRecord = -1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = (MainActivity) getActivity();
        wordDao = context.getWordDao();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_plan_learn, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
        // Add action buttons
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Config.setPlanShouldLearn(learnPerDayTxt.getText().toString());
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        ButterKnife.bind(this, view);





        boolean isCoreMode = Config.coreModeIsOn();
        boolean isEasyMode = Config.easyModeIsOn();

        Queries queries = Queries.getInstance(((InitApplication) getActivity().getApplication()).getDaoSession());
        unGraspCount = queries.getList(Constant.NEED_LEARN, isCoreMode, isEasyMode).size();

        int learnPerDay = Config.getPlanShouldLearn();
        learnPerDayTxt.setText(String.valueOf(learnPerDay));
        return builder.create();
    }

    @OnTextChanged(R.id.learnPerDayTxt)
    void onLearnPerDayChange(CharSequence learnPerDayStr, int start, int count, int after) {

        if (learnPerDayStr == null || learnPerDayStr.toString().isEmpty()) {
            return;
        }
        int learnPerDay = Integer.valueOf(learnPerDayStr.toString());
        if (learnPerDay <= 0) {
            ToastUtil.showShort(context, "每天学习单词数目不能小于1");
            return;
        }
        if (learnPerDay == learnPerDayRecord) {
            return;
        }
        int needDay = unGraspCount / learnPerDay + 1;

        needDayRecord = needDay;
        learnPerDayRecord = learnPerDay;

        needDayTxt.setText(String.valueOf(needDay));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, needDay);
        finishTimeTxt.setText(DayUtil.getFormatDate(calendar));

    }

    @OnTextChanged(R.id.needDayTxt)
    void onNeedDayTxtChanged(CharSequence needDayStr, int start, int count, int after) {
        if (needDayStr == null || needDayStr.toString().isEmpty()) {
            return;
        }
        int needDay = Integer.valueOf(needDayStr.toString());
        if (needDay == needDayRecord) {
            return;
        }
        if (needDay < 0) {
            ToastUtil.showShort(context, "学习天数不能小于0");
            return;
        }

        int learnPerDay = unGraspCount / needDay + 1;

        needDayRecord = needDay;
        learnPerDayRecord = learnPerDay;

        learnPerDayTxt.setText(String.valueOf(learnPerDay));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, needDay);
        finishTimeTxt.setText(DayUtil.getFormatDate(calendar));
    }

}
