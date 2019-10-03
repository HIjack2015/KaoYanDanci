package cn.jk.kaoyandanci.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.util.ToastUtil;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.versionLbl)
    TextView versionLbl;
    @BindView(R.id.commonQuestionLbl)
    TextView commonQuestionLbl;
    @BindView(R.id.rateLbl)
    TextView rateLbl;
    @BindView(R.id.shareLbl)
    TextView shareLbl;
    @BindView(R.id.donateLbl)
    TextView donateLbl;
    @BindView(R.id.feedbackLbl)
    TextView feedbackLbl;
    @BindView(R.id.suggestAppLbl)
    TextView suggestAppLbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("关于软件");
        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            ToastUtil.showShort(context, e.toString());
        }
        versionLbl.setText(versionName);
    }

    @OnClick(R.id.commonQuestionLbl)
    public void onCommonQuestionLblClicked() {
        startActivity(new Intent(context, CommonQuestionActivity.class));
    }
    @OnClick(R.id.suggestAppLbl)
    public void onsuggestAppLblclicked() {
        new MaterialDialog.Builder(this).content("背单词是考研单词的升级版,(可能)会长期维护,并且释义更全,单词书更多,欢迎下载(๑•́ ₃ •̀๑)").positiveText(R.string.open_broswer)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coolapk.com/apk/240082"));
                        startActivity(browserIntent);
                    }
                }).show();
    }

    @OnClick(R.id.rateLbl)
    public void onRateLblClicked() {
        try {

            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            ToastUtil.showShort(context, "没有找到应用市场.sorry");
        }
    }

    @OnClick(R.id.shareLbl)
    public void onShareLblClicked() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/*");
        sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_content));
        startActivity(sendIntent);
    }

    @OnClick(R.id.donateLbl)
    public void onDonateLblClicked() {
        startActivity(new Intent(context, DonateActivity.class));
    }

    @OnClick(R.id.feedbackLbl)
    public void onFeedbackLblClicked() {
        startActivity(new Intent(context, FeedbackActivity.class));
    }
}
