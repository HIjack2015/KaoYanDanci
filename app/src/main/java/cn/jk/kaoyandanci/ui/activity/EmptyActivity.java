package cn.jk.kaoyandanci.ui.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * 启动一个空的activity然后关闭,为了给底部的view着色,后期再想办法,
 */
public class EmptyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
    }
}
