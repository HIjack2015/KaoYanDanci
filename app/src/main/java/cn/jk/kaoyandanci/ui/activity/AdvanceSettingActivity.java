package cn.jk.kaoyandanci.ui.activity;

import android.os.Bundle;

import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.ui.fragment.AdvanceSettingFragment;

public class AdvanceSettingActivity extends BaseActivity {

    private AdvanceSettingFragment fragment;
    public boolean visible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_setting);
        if (savedInstanceState == null) {
            fragment = new AdvanceSettingFragment();
            getFragmentManager().beginTransaction().replace(R.id.content_frames, fragment).commit();
        } else {
            fragment = (AdvanceSettingFragment) (getFragmentManager().findFragmentById(R.id.content_frames));

        }
        getSupportActionBar().setTitle("高级设置");
    }

    @Override
    public void onResume() {
        super.onResume();
        visible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        visible = false;
    }
}
