package cn.jk.kaoyandanci.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;
import java.util.Map;

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
    public static final String kelinsi= "kelinsi";


    String word_url_type = "word_url_type";
    String english;

    Map<String, String> headers = new HashMap<String, String>();


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
        CookieManager.getInstance().setAcceptCookie(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
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
                            case 2:
                                type = kelinsi;
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    void loadWordDetail(String type) {
        mWebView.stopLoading();
        WebViewClient webViewClient = new AppWebViewClients(mProgressBar, mWebView, type);
        String wordUrl = Constant.youdaoWordPageUrl + english;
        if (type.equals(ciba)) {
            wordUrl = Constant.cibaWordPageUrl + english+"?flag=searchBack";
        } else  if (type.equals(kelinsi))  {
            wordUrl=Constant.kesilinPageUrl+english;
        }
        mWebView.setWebViewClient(webViewClient);
        mWebView.loadUrl(wordUrl,headers);

        mWebView.setWebContentsDebuggingEnabled(true);

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

            view.loadUrl(url,headers);
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
                javaScript="javascript:(function() { $(\"div.dic-follow\").hide() })()";
            } else if (type.equals(kelinsi)) {
                javaScript="javascript:(function() { $(\".cB-hook \").hide() })()";
            }
            view.loadUrl(javaScript);


        }


    }
}