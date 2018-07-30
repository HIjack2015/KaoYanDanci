package cn.jk.kaoyandanci.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.util.Constant;
import cn.jk.kaoyandanci.util.SPUtil;

public class YoudaoWordActivity extends BaseActivity {

    @BindView(R.id.youdaoWebView)
    WebView mWebView;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    public static final String youdao = "youdao";
    public static final String ciba = "ciba";
    String word_url_type = "word_url_type";
    String english;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youdao_word);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        english = intent.getStringExtra(Constant.ENGLISH);
        if (english == null || english.equals("")) {
            Log.e("error", "怎么可能开始没有english传入");
        }
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        getSupportActionBar().setTitle("单词详情");
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        final String current_type = (String) SPUtil.get(context, word_url_type, youdao);


        loadWordDetail(current_type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_word_web_page, menu);
        MenuItem word_source_choose = menu.findItem(R.id.word_source_choose);
        word_source_choose.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showChooseSourceDialog();
                return false;
            }
        });
        return true;
    }

    private void showChooseSourceDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.please_select_word_web_type)
                .items(R.array.word_web_source_type)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String type = youdao;
                        switch (which) {
                            case 0://youdao
                                type = youdao;
                                break;
                            case 1:
                                type = ciba;
                                break;
                        }
                        SPUtil.putAndApply(context, word_url_type, type);
                        loadWordDetail(type);
                        return true;
                    }
                })
                .positiveText(R.string.confirm)
                .show();
    }

    void loadWordDetail(String type) {
        mWebView.stopLoading();
        WebViewClient webViewClient = new AppWebViewClients(mProgressBar, mWebView, type);
        String wordUrl = Constant.youdaoWordPageUrl + english;
        if (type.equals(ciba)) {
            wordUrl = Constant.cibaWordPageUrl + english;
        }
        mWebView.setWebViewClient(webViewClient);
        mWebView.loadUrl(wordUrl);
    }

    class AppWebViewClients extends WebViewClient {
        private ProgressBar progressBar;
        private WebView webView;
        String type;

        public AppWebViewClients(ProgressBar progressBar, WebView webView, final String type) {
            this.progressBar = progressBar;
            this.webView = webView;
            progressBar.setVisibility(View.VISIBLE);
            webView.setVisibility(View.INVISIBLE);
            this.type = type;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            String javaScript = "";
            if (type.equals(youdao)) {
                javaScript = "javascript:(function() {__closeBanner();})()";

            } else if (type.equals(ciba)) {
                javaScript = "javascript:(function() {$($(\"[src='//cdn.iciba.com/www/img/m/iciba-dialog/exit2.png']\")).click();$($(\"[src='//cdn.iciba.com/www/img/m/iciba-dialog/close.png']\")).click();})()";

            }
            view.loadUrl(javaScript);

        }


    }
}