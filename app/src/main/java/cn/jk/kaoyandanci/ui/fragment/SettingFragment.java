package cn.jk.kaoyandanci.ui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.afollestad.aesthetic.Aesthetic;

import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.ui.activity.AboutActivity;
import cn.jk.kaoyandanci.ui.activity.DisplaySettingActivity;
import cn.jk.kaoyandanci.ui.activity.MainActivity;
import cn.jk.kaoyandanci.ui.activity.MyWordListActivity;
import cn.jk.kaoyandanci.ui.dialog.ChoosePlanDialog;
import cn.jk.kaoyandanci.util.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends PreferenceFragment {


    public SettingFragment() {
        // Required empty public constructor
    }


    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        return inflater.inflate(R.layout.fragment_setting, container, false);
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.setting);
        SwitchPreference darkThemPreference = (SwitchPreference) findPreference(getActivity().getString(R.string.dark_theme_on));
        darkThemPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean darkThemeOn = (boolean) o;
                if (darkThemeOn) {
                    Aesthetic.get()
                            .activityTheme(R.style.AppThemeDark_ActionBar)
                            .isDark(true)
                            .textColorPrimaryRes(R.color.text_color_primary_dark)
                            .textColorSecondaryRes(R.color.text_color_secondary_dark)
                            .apply();
                } else {
                    Aesthetic.get()
                            .activityTheme(R.style.AppTheme_ActionBar)
                            .isDark(false)
                            .textColorPrimaryRes(R.color.text_color_primary)
                            .textColorSecondaryRes(R.color.text_color_secondary)
                            .apply();
                }
                return true;
            }
        });


        Preference displaySetPref = findPreference(Constant.DISPLAY_SETTING);
        displaySetPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), DisplaySettingActivity.class));
                return false;
            }
        });
        Preference planLearnPref = findPreference(getString(R.string.should_learn));
        planLearnPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new ChoosePlanDialog().show(getFragmentManager(), "choosePlan");

                return false;
            }
        });
        Preference aboutPref = findPreference(getString(R.string.about_app));
        aboutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
                return false;
            }
        });
        Preference myWordPref = findPreference(getString(R.string.my_word_list));
        myWordPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), MyWordListActivity.class));
                return false;
            }
        });
        Preference advanceSettingPref = findPreference(getString(R.string.advance_setting));
        advanceSettingPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ((MainActivity) getActivity()).startAdvancedSettingActivityWithPermissionCheck();
                return false;
            }
        });


    }


}
