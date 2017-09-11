package cn.jk.kaoyandanci.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.util.Constant;

public class YoudaoWordActivity extends BaseActivity {

    @BindView(R.id.youdaoWebView)
    WebView youdaoWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youdao_word);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        String english = intent.getStringExtra(Constant.ENGLISH);
        if (english == null || english.equals("")) {
            Log.e("error", "怎么可能开始没有english传入");
        }
        WebSettings webSettings = youdaoWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        getSupportActionBar().setTitle("有道单词详情");
//        youdaoWebView.setWebViewClient(new WebViewClient() {
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                youdaoWebView.loadUrl("file:///android_asset/noConnectPage.html");
//
//            }
//        });
        youdaoWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        youdaoWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                String javaScript = "javascript:(function() {__closeBanner();})()";
                youdaoWebView.loadUrl(javaScript);
            }
        });
        youdaoWebView.loadUrl(Constant.youdaoWordPageUrl + english);

    }

}
