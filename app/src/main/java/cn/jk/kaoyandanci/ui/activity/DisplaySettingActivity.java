package cn.jk.kaoyandanci.ui.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.BottomNavIconTextMode;
import com.afollestad.aesthetic.NavigationViewMode;
import com.afollestad.materialdialogs.color.ColorChooserDialog;

import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.ui.preference.ColorPickerPreference;
import cn.jk.kaoyandanci.util.SPUtil;
import io.reactivex.functions.Consumer;

public class DisplaySettingActivity extends BaseActivity implements ColorChooserDialog.ColorCallback {
    SettingsFragment displaySettingFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_setting);

        if (savedInstanceState == null) {
            displaySettingFragment = new SettingsFragment();
            getFragmentManager().beginTransaction().replace(R.id.content_frames, displaySettingFragment).commit();
        } else {
            displaySettingFragment = (SettingsFragment) (getFragmentManager().findFragmentById(R.id.content_frames));

        }
        getSupportActionBar().setTitle("主题设置");

    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        boolean navColorOn = (boolean) SPUtil.get(context, getString(R.string.colored_nav_bar_on), true);
        boolean statusColorOn = (boolean) SPUtil.get(context, getString(R.string.colored_status_bar_on), true);
        int colorBarBlack = ContextCompat.getColor(context, R.color.bar_black);
        Aesthetic aesthetic = Aesthetic.get();

        switch (dialog.getTitle()) {
            case R.string.primary_color:
                aesthetic.colorPrimary(selectedColor)
                        .apply();
                displaySettingFragment.primaryColorPref.setColor(selectedColor);
                break;
            case R.string.accent_color:
                aesthetic.colorAccent(selectedColor)
                        .apply();
                displaySettingFragment.accentColorPref.setColor(selectedColor);
                break;
            case R.string.primary_text_color:
                aesthetic.textColorPrimary(selectedColor).apply();
                displaySettingFragment.textPrimaryColorPref.setColor(selectedColor);
                break;
            case R.string.secondary_text_color:
                aesthetic.textColorSecondary(selectedColor).apply();
                displaySettingFragment.textSecondColorPref.setColor(selectedColor);
                break;
        }
        if (navColorOn) {
            aesthetic.colorNavigationBarAuto().apply();
        } else {
            aesthetic.colorNavigationBar(colorBarBlack).apply();
        }
        if (statusColorOn) {
            aesthetic.colorStatusBarAuto().apply();
        } else {
            aesthetic.colorStatusBar(colorBarBlack).apply();
        }


    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

    }

    public static class SettingsFragment extends PreferenceFragment {
        ColorPickerPreference primaryColorPref, accentColorPref, textPrimaryColorPref, textSecondColorPref;
        DisplaySettingActivity context;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.display_setting);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            context = (DisplaySettingActivity) getActivity();


            primaryColorPref = (ColorPickerPreference) findPreference("primary_color_key");
            primaryColorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Aesthetic.get()
                            .colorPrimary()
                            .take(1)
                            .subscribe(new Consumer<Integer>() {
                                @Override
                                public void accept(@NonNull Integer color) throws Exception {
                                    new ColorChooserDialog.Builder(context, R.string.primary_color).backButton(R.string.back).cancelButton(R.string.cancel).doneButton(R.string.done).customButton(R.string.custom).presetsButton(R.string.back).preselect(color).show((FragmentActivity) getActivity());
                                }
                            });
                    return false;
                }
            });
            accentColorPref = (ColorPickerPreference) findPreference("accent_color_key");
            accentColorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Aesthetic.get()
                            .colorAccent()
                            .take(1)
                            .subscribe(new Consumer<Integer>() {
                                @Override
                                public void accept(@NonNull Integer color) throws Exception {
                                    new ColorChooserDialog.Builder(context, R.string.accent_color).backButton(R.string.back).cancelButton(R.string.cancel).doneButton(R.string.done).customButton(R.string.custom).presetsButton(R.string.back).preselect(color).show((FragmentActivity) getActivity());
                                }
                            });
                    return false;
                }
            });
            textPrimaryColorPref = (ColorPickerPreference) findPreference("text_primary_color_key");
            textPrimaryColorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Aesthetic.get()
                            .textColorPrimary()
                            .take(1)
                            .subscribe(new Consumer<Integer>() {
                                @Override
                                public void accept(@NonNull Integer color) throws Exception {
                                    new ColorChooserDialog.Builder(context, R.string.primary_text_color).backButton(R.string.back).cancelButton(R.string.cancel).doneButton(R.string.done).customButton(R.string.custom).presetsButton(R.string.back).preselect(color).show((FragmentActivity) getActivity());
                                }
                            });
                    return false;
                }
            });
            textSecondColorPref = (ColorPickerPreference) findPreference("text_second_color_key");
            textSecondColorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Aesthetic.get()
                            .textColorSecondary()
                            .take(1)
                            .subscribe(new Consumer<Integer>() {
                                @Override
                                public void accept(@NonNull Integer color) throws Exception {
                                    new ColorChooserDialog.Builder(context, R.string.secondary_text_color).backButton(R.string.back).cancelButton(R.string.cancel).doneButton(R.string.done).customButton(R.string.custom).presetsButton(R.string.back).preselect(color).show((FragmentActivity) getActivity());
                                }
                            });
                    return false;
                }
            });

            final int colorBarBlack = ContextCompat.getColor(context, R.color.bar_black);
            SwitchPreference colorStatusPreference = (SwitchPreference) findPreference(context.getString(R.string.colored_status_bar_on));
            colorStatusPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isOn = (boolean) newValue;
                    SPUtil.putAndApply(context, context.getString(R.string.colored_status_bar_on), isOn);
                    if (isOn) {
                        Aesthetic.get().colorStatusBarAuto().apply();
                    } else {

                        Aesthetic.get().colorStatusBar(colorBarBlack).apply();
                    }
                    return true;
                }
            });
            SwitchPreference colorNavPreference = (SwitchPreference) findPreference(context.getString(R.string.colored_nav_bar_on));
            colorNavPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isOn = (boolean) newValue;

                    SPUtil.putAndApply(context, context.getString(R.string.colored_nav_bar_on), isOn);
                    if (isOn) {
                        Aesthetic.get().colorNavigationBarAuto().apply();
                    } else {
                        Aesthetic.get().colorNavigationBar(colorBarBlack).apply();
                    }
                    return true;
                }
            });
            findPreference("restoreDefault").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Aesthetic.get()
                            .activityTheme(R.style.AppTheme_ActionBar)
                            .textColorPrimaryRes(R.color.colorPrimary)
                            .textColorSecondaryRes(R.color.text_color_secondary)
                            .colorPrimaryRes(R.color.colorPrimary)
                            .colorAccentRes(R.color.colorAccent)
                            .colorStatusBarAuto()
                            .colorNavigationBarAuto()
                            .navigationViewMode(NavigationViewMode.SELECTED_ACCENT)
                            .bottomNavigationIconTextMode(BottomNavIconTextMode.SELECTED_ACCENT)
                            .bottomNavigationBackgroundMode(BottomNavIconTextMode.BLACK_WHITE_AUTO)
                            .apply();
                    return false;
                }
            });
        }
    }
}
